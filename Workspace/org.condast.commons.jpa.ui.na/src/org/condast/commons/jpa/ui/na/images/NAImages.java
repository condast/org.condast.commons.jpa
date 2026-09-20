/*******************************************************************************
 * Copyright (c) 2016 Condast and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Condast                - EetMee
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.condast.commons.jpa.ui.na.images;

import org.condast.commons.jpa.ui.na.Activator;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;
import org.condast.commons.ui.image.AbstractImages;
import org.eclipse.swt.graphics.Image;

public class NAImages extends AbstractImages {

	public enum Images{
			ADDRESS,
			SEARCH_ADDRESS;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static boolean isValid( String name ) {
			if( StringUtils.isEmpty(name))
				return false;
			for( Images img: values()) {
				if( img.name().equals(name))
					return true;
			}
			return false;
		}
		
		/**
		 * A filename by this method is going to be fetched out of the
		 * root of the resources directory.
		 * The extensions are now only used as subdir of the resources dir
		 * ...and of course as extension of the filename.
		 * @param image
		 * @return str the filename
		 */
		public static String getFileName( Images image ){
			String str = null;
			switch( image ){
			default:
				str = StringStyler.xmlStyleString( image.name()) + "-32";
				break;
			}
			str += ".png";
			return str;
		}

		/**
		 * The filenames by this method are going to be fetched out of a
		 * subdir of the resources directory.
		 * The extensions are now only used as subdir of the resources dir and ...
		 * ...as extension of the filename.
		 * @param image enumimages like Images.ADD, DELETE and so on
		 * @param imageSize the enumsize of the image like ImageSize.TINY
		 * @return str the subdir with filename
		 */
		public static String getFileName( Images image, ImageSize imageSize ){
			//get location, filename and size from super
			String str = ImageSize.getLocation( image.name(), imageSize );
			return str;
		}
	}

	private static NAImages images = new NAImages();

	private NAImages() {
		super( S_RESOURCES, Activator.BUNDLE_ID );
	}

	/**
	 * Get an instance of this map
	 * @return
	 */
	public static NAImages getInstance(){
		return images;
	}

	@Override
	public void initialise() {
		for( Images img: Images.values() ) {
			for( ImageSize imsi : ImageSize.values() ) {
				setImage( Images.getFileName( img, imsi ) );
			}
		}
	}

	/**
	 * Get the image
	 * @param descr the descriptor like Images.ADD, DELETE and so on
	 * @return image
	 */
	public static Image getImage( Images descr ){
		Image image = images.getImageFromName( Images.getFileName( descr ) );
		return image;
	}

	/**
	 * @param descr the descriptor like Images.ADD, DELETE and so on
	 * @param imageSize, a size like Abstractimages.ImageSizes.SMALL, NORMAL, LARGE, TILE
	 * @return the image in the wanted size
	 */
	public static Image getImage( Images descr, ImageSize imageSize ){
		Image image = images.getImageFromName( Images.getFileName( descr, imageSize ) );
		return image;
	}

	/**
	 * @param descr the descriptor of the image
	 * @param size a size like 16, 24, 32, 48, 64, 128, any other value is changed in 32
	 * @return the image in the wanted size
	 */
	public static Image getImage( Images descr, int size ){
		if( size==16 || size==24 || size==32 || size==48 || size==64 || size==128 ) {
			//the size is ok
		}
		else {
			size = 32;//standard size
		}
		ImageSize imageSize = AbstractImages.ImageSize.getImageSize( size );
		Image image = images.getImageFromName( Images.getFileName( descr, imageSize ) );
		return image;
	}

}