package org.condast.commons.persistence.core;

public interface IGraphDatabaseFactory<G extends Object> {

	public G createDatabase( String path );
	
	public void shutdown();
}
