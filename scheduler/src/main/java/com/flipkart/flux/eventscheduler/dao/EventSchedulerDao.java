/*
 * Copyright 2012-2016, the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.flipkart.flux.eventscheduler.dao;

import com.flipkart.flux.eventscheduler.model.ScheduledEvent;
import com.flipkart.flux.persistence.SessionFactoryContext;
import com.flipkart.flux.redriver.model.ScheduledMessage;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.util.List;

/**
 * <code>EventSchedulerDao</code> handles all Db interactions for {@link ScheduledEvent}(s)
 *
 * @author shyam.akirala
 */
@Singleton
public class EventSchedulerDao {

    private SessionFactoryContext sessionFactoryContext;

    @Inject
    public EventSchedulerDao(@Named("schedulerSessionFactoryContext") SessionFactoryContext sessionFactoryContext) {
        this.sessionFactoryContext = sessionFactoryContext;
    }

    @Transactional
    public void save(ScheduledEvent scheduledEvent) {
        currentSession().saveOrUpdate(scheduledEvent);
    }

    @Transactional
    public void delete(String correlationId, String eventName) {
        final Query deleteQuery = currentSession().createQuery("delete ScheduledEvent s where s.correlationId=:correlationId " +
                "and s.eventName=:eventName");
        deleteQuery.setString("correlationId", correlationId);
        deleteQuery.setString("eventName", eventName);
        deleteQuery.executeUpdate();
    }

    /**
     * Retrieves rows offset to offset+rowCount from ScheduledEvents table ordered by scheduledTime ascending.
     * @param offset
     * @param rowCount
     */
    @Transactional
    public List<ScheduledEvent> retrieveOldest(int offset, int rowCount) {
        return currentSession()
                .createCriteria(ScheduledEvent.class)
                .addOrder(Order.asc("scheduledTime"))
                .setFirstResult(offset)
                .setMaxResults(rowCount)
                .list();
    }

    /**
     * Provides the session which is bound to current thread.
     * @return Session
     */
    private Session currentSession() {
        return sessionFactoryContext.getSessionFactory().getCurrentSession();
    }

}
