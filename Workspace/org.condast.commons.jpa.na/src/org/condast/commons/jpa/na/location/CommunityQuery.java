package org.condast.commons.jpa.na.location;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.condast.commons.Utils;
import org.condast.commons.data.latlng.LatLng;
import org.condast.commons.jpa.na.community.CommunityResource;
import org.condast.commons.jpa.na.community.ICommunityQuery;
import org.condast.commons.jpa.na.community.CommunityResource.Resources;
import org.condast.commons.jpa.na.model.ICommunity;
import org.condast.commons.project.ProjectFolderUtils;
import org.condast.commons.strings.StringUtils;

public class CommunityQuery implements ICommunityQuery {

	public static final String S_REGEX = "[,;]";
	public static final String CHAR_SET = StandardCharsets.ISO_8859_1.name();

	public static final String S_PC6HNR = "pc6hnr";
	public static final String S_DBF = "dbf";
	public static final String S_GWB_DB = "0801_gwb.csv";

	public static final String S_RESOURCES = "/resources/";
	public static final String S_YEAR = "2017";
	public static final String S_FILE_MUNICIPALITY = "gemeentenaam";
	public static final String S_FILE_NEIGHBOURHOOD = "wijknaam";
	public static final String S_FILE_LOCALITY = "buurtnaam";
	public static final String S_UNKNOWN = "Unknown";

	private InputStream inp;

	private Collection<ICommunityQueryListener> listeners;

	private static Logger logger = Logger.getLogger( CommunityQuery.class.getName() );

	public CommunityQuery( File file ) throws FileNotFoundException {
		this( new FileInputStream( file ));
	}

	public CommunityQuery( Class<?> clss, String resource ) {
		this( clss.getResourceAsStream( resource ));
	}

	public CommunityQuery( InputStream inp ) {
		super();
		this.inp = inp;
		listeners = new ArrayList<>();
	}

	@Override
	public void prepare( String start, String end ) {
		Scanner scanner = new Scanner( inp, CHAR_SET);
		try {
			String line;
			if( !scanner.hasNextLine())
				return;
			scanner.nextLine();//skip the header
			while( scanner.hasNextLine()) {
				line = scanner.nextLine();
				if( StringUtils.isEmpty( line ))
					continue;
				String[] split = line.split("[;]");
				if(!StringUtils.isEmpty(start) && start.compareToIgnoreCase( split[0] )>0)
						continue;
				if( !StringUtils.isEmpty(end) && end.compareToIgnoreCase( split[0] )<0)
					return;
				String locality = getDetails(Resources.LOCALITY, split);
				String neighbourhood = getDetails(Resources.NEIGHBOURHOOD, split);
				String municipality = getDetails(Resources.MUNICIPALITY, split);
				notifyListeners( new CommunityQueryEvent(this, split[0], Integer.parseInt(split[1]),
						locality, neighbourhood, municipality ));
			}
		}
		finally {
			scanner.close();
		}
	}

	@Override
	public void addlistener( ICommunityQueryListener listener ) {
		this.listeners.add(listener);
	}

	@Override
	public void removelistener( ICommunityQueryListener listener ) {
		this.listeners.remove(listener);
	}

	protected void notifyListeners( CommunityQueryEvent event ) {
		for(ICommunityQueryListener listener: listeners)
			listener.notifyPostCodeScanned(event);
	}

	protected static String getDetails( CommunityResource.Resources resource, String[] split) {
		return getDetails(resource, Integer.parseInt( split[ resource.getIndex()]) );
	}

	protected static String getDetails( CommunityResource.Resources resource, int code) {
		if( code <=0 )
			return S_UNKNOWN;
		String config = ProjectFolderUtils.getDefaultConfigDir();
		File file = new File( config, getFile( resource ));
		if(!file.exists()) {
			logger.info("File not found: " + config + ": " + file.exists());
			return null;
		}
		Scanner scanner = null;
		try {
			scanner = new Scanner( file, CHAR_SET );
			byte[] line = scanner.nextLine().getBytes();
			DBFFormat format = new DBFFormat(line );
			line = scanner.nextLine().getBytes();
			String name = format.get(line, code );
			if( !StringUtils.isEmpty( name))
				return name;
		}
		catch( Exception ex ) {
			logger.info("Exception in file: " + file.getAbsolutePath());
			ex.printStackTrace();
		}
		finally {
			if( scanner != null )
				scanner.close();
		}
		return S_UNKNOWN;
	}

	protected static String getFile( CommunityResource.Resources resource ) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(CommunityQuery.S_RESOURCES);
		switch( resource ) {
		case POSTCODE:
			buffer.append(CommunityQuery.S_PC6HNR);
			buffer.append(CommunityQuery.S_YEAR);
			buffer.append(CommunityQuery.S_GWB_DB);
			return buffer.toString();
		case MUNICIPALITY:
			buffer.append(CommunityQuery.S_FILE_MUNICIPALITY);
			break;
		case NEIGHBOURHOOD:
			buffer.append(CommunityQuery.S_FILE_NEIGHBOURHOOD);
			break;
		case LOCALITY:
			buffer.append(CommunityQuery.S_FILE_LOCALITY);
			break;
		default:
			break;
		}
		buffer.append(CommunityQuery.S_YEAR);
		buffer.append(".");
		buffer.append( CommunityQuery.S_DBF);
		return buffer.toString();
	}

	public static int getNumberFromAddress( String address ) {
		String str = address.replaceAll("[\\D]", "");
		return Integer.parseInt( str );
	}

	public static ICommunityQuery getDefaultQuery() throws FileNotFoundException {
		String config = ProjectFolderUtils.getDefaultConfigDir();
		File file = new File( config, getFile( CommunityResource.Resources.POSTCODE));
		logger.info("Expecting file at: " + config + ": " + file.exists());
		return new CommunityQuery( file );
	}

	public static Collection<CommunityResource> getPostcodes( String places ) throws FileNotFoundException {
		String config = ProjectFolderUtils.getDefaultConfigDir();
		String[] split = places.toLowerCase().trim().split(S_REGEX);
		File file = new File( config, getFile( CommunityResource.Resources.POSTCODE ));
		return getPostcodes( new FileInputStream( file ), split, null, null);
	}

	protected static Map<Integer, CommunityResource> getLocationIds( CommunityResource.Resources resource, Collection<String> places) {
		Map<Integer, CommunityResource> results = new HashMap<>();
		if( Utils.assertNull(places))
			return results;
		String config = ProjectFolderUtils.getDefaultConfigDir();
		File file = new File( config, getFile( resource ));
		if(!file.exists())
			return null;
		Scanner scanner = null;
		try {
			scanner = new Scanner( file, CHAR_SET );
			byte[] line = scanner.nextLine().getBytes();
			DBFFormat format = new DBFFormat(line );
			line = scanner.nextLine().getBytes();
			results.putAll( format.get(resource, line, places ));
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		finally {
			if( scanner != null )
				scanner.close();
		}
		return results;
	}

	/**
	 * Get the postcodes between the given start and end range
	 * @param inp
	 * @param places
	 * @param start
	 * @param end
	 * @return
	 * @throws FileNotFoundException
	 */
	protected static Collection<CommunityResource> getPostcodes( InputStream inp, String[] places, String start, String end ) throws FileNotFoundException {
		Collection<String> input = new ArrayList<>();
		for( String str: places)
			input.add( str.toLowerCase().trim());
		Map<Integer, CommunityResource> codes = new HashMap<>();
		codes.putAll( getLocationIds(CommunityResource.Resources.MUNICIPALITY, input));
		codes.putAll( getLocationIds(CommunityResource.Resources.NEIGHBOURHOOD, input));
		codes.putAll( getLocationIds(CommunityResource.Resources.LOCALITY, input));
		Collection<CommunityResource> results = new TreeSet<>();
		Scanner scanner = new Scanner( inp, CHAR_SET );
		try {
			scanner.nextLine();//skip the header
			while( scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if( StringUtils.isEmpty( line ))
					continue;
				String[] split = line.split("[;]");
				if((!StringUtils.isEmpty(start) && start.compareToIgnoreCase( split[0] )< 0) ||
						( !StringUtils.isEmpty(end) && end.compareToIgnoreCase( split[0] )>0))
					return results;
				CommunityData data = new CommunityData( split );
				Iterator<Map.Entry<Integer, CommunityResource>> iterator = codes.entrySet().iterator();
				while( iterator.hasNext() ) {
					Map.Entry<Integer, CommunityResource> entry = iterator.next();
					CommunityResource cr = entry.getValue();
					if( data.contains(entry.getKey(), cr.getResource())) {
						cr.addPostcode(split[0]);
						results.add( cr );
					}
				}
			}
		}
		finally {
			scanner.close();
		}
		return results;
	}

	@SuppressWarnings("unused")
	private static class DBFFormat{
		private static final int DEFAULT_NUMBER_BREAK = 9;
		private static final int DEFAULT_LINE_BREAK = 75;


		private byte vendor;
		private Date update;
		private int recordSize;
		private int lineSize, size;

		private Map<Integer, String> records;

		public DBFFormat( byte[] line ) {
			this.vendor = line[0];
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, line[1] );
			calendar.set(Calendar.MONTH, line[2] );
			calendar.set(Calendar.DAY_OF_MONTH, line[3] );
			update = calendar.getTime();
			recordSize = line[4] + line[5]*256 + line[6]*(256^2) + line[7]*(256^3);
			lineSize = line[10] + line[11]*256;
			size = line[8] + line[9]*256;
			records = new HashMap<>();
		}

		public void parse( byte[] line ) {
			int index = 0;
			byte[] record;
			int code;
			String name;
			for( int i=0; i<recordSize; i++ ) {
				record = Arrays.copyOfRange(line, index, index+lineSize);
				byte[] numbers = Arrays.copyOfRange(record, 0, DEFAULT_NUMBER_BREAK);
				code = Integer.parseInt( CommunityQuery.toString(numbers));
				name = CommunityQuery.toString( Arrays.copyOfRange(record, DEFAULT_NUMBER_BREAK, record.length-1));
				records.put(code, name);
				index += lineSize;
			}
		}

		/**
		 * Get the code and name of municipalities, neigbourhoods and localities.
		 * These files are organised as one line with header data, and one line with all
		 * the codes and names.
		 * @param line
		 * @param find
		 * @return
		 */
		public String get( byte[] line, int find ) {
			if( records.containsKey(find))
				return records.get(find);
			int index = 0;
			byte[] record;
			int code;
			String name;
			for( int i=0; i<recordSize; i++ ) {
				record = Arrays.copyOfRange(line, index, index+lineSize);
				byte[] numbers = Arrays.copyOfRange(record, 0, DEFAULT_NUMBER_BREAK);
				code = Integer.parseInt( CommunityQuery.toString(numbers));
				name = CommunityQuery.toString( Arrays.copyOfRange(record, DEFAULT_NUMBER_BREAK, record.length-1));
				index += lineSize;
				if( code == find )
					return name;
				records.put(code, name);
			}
			return null;
		}

		public Map<Integer,CommunityResource> get( CommunityResource.Resources resource, byte[] line, Collection<String> places ) {
			Map<Integer, CommunityResource> results = new HashMap<>();
			if( Utils.assertNull(places))
				return results;

			int index = 0;
			byte[] record;
			int code;
			String name;
			for( int i=0; i<recordSize; i++ ) {
				record = Arrays.copyOfRange(line, index, index+lineSize);
				byte[] numbers = Arrays.copyOfRange(record, 0, DEFAULT_NUMBER_BREAK);
				code = Integer.parseInt( CommunityQuery.toString(numbers));
				name = CommunityQuery.toString( Arrays.copyOfRange(record, DEFAULT_NUMBER_BREAK, record.length-1));
				String[] split = name.toLowerCase().trim().split(" ");
				for( String str: split )
					if( places.contains(str.trim()))
						results.put(code, new CommunityResource( resource, code, name ));
				index += lineSize;
			}
			return results;
		}
	}

	public static final String toString( byte[] arr) {
		StringBuilder buffer = new StringBuilder();
		for( byte bt: arr) {
			buffer.append((char)bt);
		}
		String str = buffer.toString().trim();
		return str;
	}

	private static class CommunityData{
		@SuppressWarnings("unused")//Used by Json
		int range;
		String neighbourhood;
		String municipality;
		String locality;

		public CommunityData( String[] split) {
			this( split[ CommunityResource.Resources.HOUSE_NUMBER.getIndex()],
					split[ CommunityResource.Resources.NEIGHBOURHOOD.getIndex()],
					split[ CommunityResource.Resources.MUNICIPALITY.getIndex()],
					split[ CommunityResource.Resources.LOCALITY.getIndex()]);
		}

		private CommunityData( String range, String neighbourhood, String municipality, String locality) {
			super();
			this.range = Integer.valueOf(range);
			this.neighbourhood = neighbourhood;
			this.municipality = municipality;
			this.locality = locality;
		}

		public boolean contains( Integer key, CommunityResource.Resources resource) {
			boolean result = false;
			switch( resource) {
			case MUNICIPALITY:
				result = key == Integer.parseInt(municipality);
				break;
			case NEIGHBOURHOOD:
				result = key == Integer.parseInt(neighbourhood);
				break;
			case LOCALITY:
				result = key == Integer.parseInt(locality);
				break;
			default:
				break;
			}
			return result;
		}
	}

	@Override
	public ICommunity locationQuery(String postcode, String houseNumber, LatLng location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void complete(ICommunity community, String houseNumber) {
		// TODO Auto-generated method stub

	}
}