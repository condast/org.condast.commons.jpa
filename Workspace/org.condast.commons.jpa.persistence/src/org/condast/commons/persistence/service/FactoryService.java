package org.condast.commons.persistence.service;

import java.util.logging.Logger;

/**
 * This factory service binds a factory
 *
 * @author keesp
 *
 */
public class FactoryService<F extends Object> implements AutoCloseable {

	private String name;
	private F factory;

	private static Logger logger = Logger.getLogger( FactoryService.class.getName() );

	protected FactoryService(String name){
		this.name = name;
	}

	protected F getFactory() {
		return factory;
	}

	/**
	 * Returns true if the service is open
	 * @return
	 */
	protected boolean isOpen(){
		return ( this.factory != null );
	}

	protected synchronized void bindEMF( F emf) {
		this.factory = emf;
		logger.info("FACTORY BOUND FOR : " + this.name );
	}

	protected synchronized void unbindEMF( F emf) {
		if( !emf.equals( factory ) )
			return;
		logger.info("FACTORY UNBOUNDED FOR : " + this.name );
	}

	@Override
	public void close() throws Exception {
		unbindEMF( this.factory );
	}
}