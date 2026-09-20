package org.condast.commons.jpa.authentication.ui.images;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.condast.commons.jpa.authentication.ui.Activator;
import org.condast.commons.ui.image.AbstractImages;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.ResourceManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

/**
 * @see: https://www.iconfinder.com/savlon
 * @author Condast
 *
 */
public class AuthenticationImages extends AbstractImages{

	private static final String S_BACKGROUND_IMAGE = "background.png";

	public enum Images{
		BACKGROUND,
		MENU;

		public static String getResource( Images image ){
			return getResource( image, false );
		}

		public static String getResource( Images image, boolean large ){
			StringBuffer buffer = new StringBuffer();
			switch( image ){
			case BACKGROUND:
				buffer.append( S_BACKGROUND_IMAGE );
				break;
			case MENU:
				buffer.append( image.name().toLowerCase());
				buffer.append( "-");
				buffer.append( large? "128": "32");
				buffer.append( ".png" );
				break;
			default:
				buffer.append( image.name().toLowerCase());
				buffer.append( "-32.png" );
				break;
			}
			return buffer.toString();
		}
	}

	private static AuthenticationImages images = new AuthenticationImages();

	private static Logger logger = Logger.getLogger( AuthenticationImages.class.getName() );

	private AuthenticationImages() {
		super( S_RESOURCES, Activator.BUNDLE_ID);
	}

	/**
	 * Get an instance of this map
	 * @return
	 */
	public static AuthenticationImages getInstance(){
		return images;
	}

	@Override
	public void initialise(){
		for( Images image: Images.values())
			setImage( Images.getResource(image) );
	}

	public Image getImage( Images image ){
		return super.getImageFromName( Images.getResource( image ));
	}

	/**
	 * Register the resource with the given name
	 * @param name
	 */
	public static void registerImage( Images image ){
		registerImage( image.name().toLowerCase(), Images.getResource(image));
	}

	/**
	 * Register the resource with the given name
	 * @param name
	 */
	public static void registerImage( String name, String file ){
		ResourceManager resourceManager = RWT.getResourceManager();
		if( !resourceManager.isRegistered( name ) ) {
			InputStream inputStream = AuthenticationImages.class.getClassLoader().getResourceAsStream( file );
			try {
				resourceManager.register( name, inputStream );
			} finally {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.log( Level.SEVERE, name + ": " + file );
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Get the image with the given name
	 * @param name
	 * @return
	 */
	public static String getImageString( Images image ){
		return Images.getResource(image);
	}

	/**
	 * Set the image for the given control
	 * @param widget
	 * @param name
	 */
	public static void setImage( Control widget, Images image ){
		widget.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		//registerImage( image );
		if( widget instanceof Label ){
			  Label label = (Label) widget;
			  String src = getImageString( image );
			  label.setText( "Hello<img width='24' height='24' src='" + src + "'/> there " );
			}
		if( widget instanceof Button ){
		  Button button = (Button) widget;
		  String src = getImageString( image );
		  button.setText( "<img width='24' height='24' src='" + src + "'/>" );
		}
	}

	/**
	 * Get the screen aver and size it to fit the parent
	 * @param parent
	 * @return
	 */
	public static Image getScreenSaver( Composite parent ){
		ImageData imageData = new ImageData( AuthenticationImages.class.getResourceAsStream( S_RESOURCES + Images.getResource( Images.BACKGROUND ) ));
		ImageData scaledData = imageData.scaledTo( parent.getBounds().width, parent.getBounds().height );
		return new Image(Display.getCurrent(), scaledData );
	}

}
