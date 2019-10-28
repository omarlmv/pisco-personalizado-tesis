/*package com.plazapoints.saas.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.session.ExpiringSession;
import org.springframework.session.events.SessionCreatedEvent;
import org.springframework.session.events.SessionDeletedEvent;
import org.springframework.session.events.SessionExpiredEvent;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryEvictedListener;
import com.hazelcast.map.listener.EntryRemovedListener;

public class SessionListenerCustom  implements EntryAddedListener<String, ExpiringSession>,
EntryEvictedListener<String, ExpiringSession>,
EntryRemovedListener<String, ExpiringSession> 
{
	private static Logger logger = LoggerFactory.getLogger(SessionListenerCustom.class);
	
	private ApplicationEventPublisher eventPublisher;
	
	public SessionListenerCustom(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@Override
	public void entryRemoved(EntryEvent<String, ExpiringSession> event) {
		logger.info("Session removed: " + event);
		eventPublisher.publishEvent(new SessionDeletedEvent(this, event.getOldValue()));
	}

	@Override
	public void entryEvicted(EntryEvent<String, ExpiringSession> event) {
		logger.info("Session removed: " + event);
		eventPublisher.publishEvent(new SessionExpiredEvent(this, event.getOldValue()));
		
	}

	@Override
	public void entryAdded(EntryEvent<String, ExpiringSession> event) {
		logger.info("Session added: " + event);
		eventPublisher.publishEvent(new SessionCreatedEvent(this, event.getValue()));
		
	}	
}
*/