package org.condast.commons.jpa.persistence.core;

public interface IGraphDatabaseFactory<G extends Object> {

	public G createDatabase( String path );
	
	public void shutdown();
}
