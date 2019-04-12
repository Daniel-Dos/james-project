/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 * *
 * http://www.apache.org/licenses/LICENSE-2.0                 *
 * *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.mailbox.cassandra;

import org.apache.james.backends.cassandra.CassandraCluster;
import org.apache.james.backends.cassandra.DockerCassandraRule;
import org.apache.james.mailbox.cassandra.mail.MailboxAggregateModule;
import org.apache.james.mailbox.events.InVMEventBus;
import org.apache.james.mailbox.events.delivery.InVmEventDelivery;
import org.apache.james.mailbox.store.AbstractCombinationManagerTest;
import org.apache.james.mailbox.store.CombinationManagerTestSystem;
import org.apache.james.mailbox.store.quota.NoQuotaManager;
import org.apache.james.metrics.api.NoopMetricFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

public class CassandraCombinationManagerTest extends AbstractCombinationManagerTest {

    @Rule public DockerCassandraRule cassandraServer = new DockerCassandraRule().allowRestart();

    private CassandraCluster cassandra;

    @Override
    @Before
    public void setUp() throws Exception {
        cassandra = CassandraCluster.create(MailboxAggregateModule.MODULE, cassandraServer.getHost());
        super.setUp();
    }

    @After
    public void tearDown() {
        cassandra.clearTables();
        cassandra.closeCluster();
    }
    
    @Override
    public CombinationManagerTestSystem createTestingData() {
        InVMEventBus eventBus = new InVMEventBus(new InVmEventDelivery(new NoopMetricFactory()));
        return CassandraCombinationManagerTestSystem.createTestingData(cassandra, new NoQuotaManager(), eventBus);
    }
    
}
