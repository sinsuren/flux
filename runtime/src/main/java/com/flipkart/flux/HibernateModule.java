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

package com.flipkart.flux;

import com.flipkart.flux.dao.*;
import com.flipkart.flux.dao.iface.*;
import com.flipkart.flux.util.TransactionInterceptor;
import com.flipkart.flux.util.Transactional;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.matcher.Matchers;

/**
 * @author shyam.akirala
 */
public class HibernateModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AuditDAO.class).to(AuditDAOImpl.class);
        bind(EventsDAO.class).to(EventsDAOImpl.class);
        bind(StateMachinesDAO.class).to(StateMachinesDAOImpl.class);
        bind(StatesDAO.class).to(StatesDAOImpl.class);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class), new TransactionInterceptor());
    }

}