package org.condast.commons.persistence.core;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpSession;

public abstract class AbstractSessionPersistence<S extends Object> implements ISessionStoreFactory<HttpSession,S>{

	public static final int DEFAULT_HOURS = 4;

	private String id;

	private Map<HttpSession, S> sessions;

	private ScheduledExecutorService executor;

	protected AbstractSessionPersistence( String id ) {
		this( id, DEFAULT_HOURS, TimeUnit.HOURS );
	}

	protected AbstractSessionPersistence( String id, int amount, TimeUnit tu) {
		sessions = new HashMap<>();
		this.id = id;
		executor = Executors.newScheduledThreadPool(5);
		executor.scheduleAtFixedRate(() -> onScheduledCleanup(), amount, amount, tu );
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public S createSessionStore(HttpSession session) {
			S result=  null;
		if( !sessions.containsKey(session)) {
			result = createPersistence(session);
			sessions.put(session, result);
			session.setMaxInactiveInterval( (int) Duration.ofDays(1).getSeconds() );
		}else {
			result = sessions.get(session);
		}
		return result;
	}

	@Override
	public S getSessionStore(HttpSession session) {
		return sessions.get(session);
	}

	protected abstract S createPersistence( HttpSession session );

	protected int getSessionCount() {
		return sessions.size();
	}

	protected Map<HttpSession, S> getSessions() {
		return sessions;
	}

	protected boolean isMaxTimeInactive( HttpSession session) {
		return ( System.currentTimeMillis() - session.getLastAccessedTime()) > session.getMaxInactiveInterval();
	}

	protected void updateSession( HttpSession session) {
		if( isMaxTimeInactive(session) )
			sessions.remove(session);
	}

	private void onScheduledCleanup() {
		for( HttpSession session: sessions.keySet()) {
			updateSession(session);
		}
	}
}
