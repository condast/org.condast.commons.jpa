package org.condast.commons.persistence.service;

import java.lang.reflect.Constructor;
import java.util.Calendar;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import org.condast.commons.IUpdateable;

public abstract class AbstractEntityService<O extends Object>{

	public static final String S_SELECT_QUERY = "SELECT o FROM ";

	private IPersistenceService service;
	private EntityManager manager;

	private Class<O> clss;

	protected AbstractEntityService( Class<O> clss, IPersistenceService service) {
		super();
		this.service = service;
		this.clss = clss;
		manager = service.getManager();
	}

	protected IPersistenceService getService() {
		return service;
	}

	protected EntityManager getManager() {
		return manager;
	}

	/**
	 * Does not need open or close
	 * @param id
	 * @return
	 */
	public O find( long id ) {
		return manager.find( clss, id);
	}

	/**
	 * Get the Select all query. add additional parameters if required
	 * Creates SELECT o FROM <classname> o <querystr>
	 * @param id
	 * @return
	 */
	protected String createFindString( String querystr ) {
		return S_SELECT_QUERY + clss.getSimpleName() + " o " + querystr;
	}

	/**
	 * Get the Select all query. add additional parameters if required
	 * Creates SELECT o FROM <classname> o <querystr>
	 * @param id
	 * @return
	 */
	public List<O> findAll( String querystr ) {
		return query( createFindString(querystr));
	}

	/**
	 * Get the Select all query. add additional parameters if required
	 * Creates SELECT o FROM <classname> O
	 * @param id
	 * @return
	 */
	public List<O> findAll() {
		return findAll("");
	}

	/**
	 * Does not need open or close
	 * @param id
	 * @return
	 */
	public List<O> query( String querystr ) {
		TypedQuery<O> query = manager.createQuery( querystr, clss );
		return query.getResultList();
	}

	/**
	 * Does not need open or close
	 * @param id
	 * @return
	 */
	protected TypedQuery<O> getTypedQuery( String querystr ) {
		return manager.createQuery( querystr, clss );
	}

	/**
	 * Create a new object
	 * @param obj
	 */
	protected void create( O obj ) {
		if( obj instanceof IUpdateable) {
			IUpdateable updateable = (IUpdateable) obj;
			updateable.setCreateDate( Calendar.getInstance().getTime());
			updateable.setUpdateDate( Calendar.getInstance().getTime());
		}
		manager.persist(obj);
	}

	/**
	 * Create a new object
	 * @param obj
	 */
	public void update( O obj ) {
		if( obj instanceof IUpdateable) {
			IUpdateable updateable = (IUpdateable) obj;
			updateable.setUpdateDate( Calendar.getInstance().getTime());
		}
	}

	/**
	 * Remove the object with the given id.
	 * @param id
	 * @return true if the object existed prior to removal
	 */
	public boolean remove( long id ) {
		O obj = manager.find( clss, id);
		boolean retval = ( obj != null );
		if( obj != null )
			manager.remove(obj);
		return retval;
	}

	/**
	 * Remove all the given objects
	 * @param objects
	 */
	public void removeAll( O[] objects) {
		for( O obj: objects ) {
			manager.remove( obj );
		}
	}

	/**
	 * Remove all the given objects
	 * @param objects
	 */
	public void removeAll( long[] ids) {
		for( long id: ids ) {
			remove( id );
		}
	}

	@SuppressWarnings("unchecked")
	protected O createObject( Class<O> clss, String className){
		Class<O> builderClass;
		O obj = null;
		try {
			builderClass = (Class<O>) clss.getClassLoader().loadClass( className );
			Constructor<O> constructor = builderClass.getConstructor();
			obj = constructor.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
}