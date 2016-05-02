Java implementation of [Microsoft Azure Cloud Platform](http://www.infoq.com/articles/Grid-Azure-David-Pallmann).

Features:
  * Exposed [DSL](http://en.wikipedia.org/wiki/Domain-specific_language) style api.
  * Using [Google Guice](http://code.google.com/p/google-guice/) as internal component container.
  * Polling or event driven job loading.
  * Pluginable Memory storage or Mysql Storage.
  * Pluginable VmQueueEndpoint or MinaQueueEndpoint.
  * Completely task tracking support with flexible criteria.

Example code:

```

       EnterpriseSide es = JAzure.createEnterprise(new Module() {
            public void configure(Console console) {
                console.configProject(ProjectConfiguration.named("RateExample").retryTimes(2))
                       .storeTaskIn(new Mysql5TaskStorage(txManager, dataSource))
                       .connectBy(new MinaQueueStorageEndpoint(new InetSocketAddress("localhost", 11111)));
  
                 
                console.addPollingJobConfig(new ExampleJobConfig())
                       .loadAt(new ExampleLoader())
                       .aggregateWith(new ExampleAggregator());
            }
        });

        es.start();



```