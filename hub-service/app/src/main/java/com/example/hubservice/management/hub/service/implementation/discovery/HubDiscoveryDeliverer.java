package com.example.hubservice.management.hub.service.implementation.discovery;

import org.springframework.stereotype.Component;

@Component
public class HubDiscoveryDeliverer {
 /*   private final Logger logger = LoggerFactory.getLogger(HubDiscoveryDeliverer.class);

    private Instant start = Instant.ofEpochSecond(0);

    private final InfluxQueryService queryService;

    private final MasterServiceImplementation<Hub, Device, String, HubNotFoundException> crudServiceImplementation;
    private final EntityCrudSenderService<String, Hub> hubSender;


    public HubDiscoveryDeliverer(InfluxQueryService queryService, MasterServiceImplementation<Hub, Device, String, HubNotFoundException> crudServiceImplementation, EntityCrudSenderService<String, Hub> hubSender) {
        this.queryService = queryService;
        this.crudServiceImplementation = crudServiceImplementation;
        this.hubSender = hubSender;
    }

    @Scheduled(fixedRate = 10000)
    private void discoverServices() {
        String query = getQueryForHubDiscovery();

        start = Instant.now(); //change next start to current one

        List<InfluxHubLogPojo> queryResults = queryService.query(query, null, InfluxHubLogPojo.class);

        consumeQueryResults(queryResults);
    }

    private void consumeQueryResults(List<InfluxHubLogPojo> queryResults) {
        for (InfluxHubLogPojo queryResult : queryResults) {
            consumeQueryResult(queryResult);
        }
    }

    private void consumeQueryResult(InfluxHubLogPojo queryResult) {
        String hubId = queryResult.getHubId();
        switch (queryResult.getValue()) {
            case CREATED -> {
                try {
                    Hub hub = crudServiceImplementation.findObjectById(hubId);

                    hub.setStatus(InfluxHubStatusValue.CREATED);
                    hub = crudServiceImplementation.updateObjectById(hubId, hub);

                    logger.info("Discovered hub :" + hub + "already found in SQL");
                } catch (HubNotFoundException e) {
                    Hub createdHub = crudServiceImplementation.addObject(new Hub(queryResult.getHubId(), queryResult.getName(), InfluxHubStatusValue.CREATED));
                    logger.info("Discovered creation of hub: " + createdHub);
                }
            }
            case RESTARTED -> {
                try {
                    Hub hub = crudServiceImplementation.findObjectById(hubId);

                    hub.setStatus(InfluxHubStatusValue.RESTARTED);
                    hub = crudServiceImplementation.updateObjectById(hubId, hub);

                    logger.info("Discovered restart of hub: " + hub);
                } catch (HubNotFoundException e) {
                    Hub createdHub = crudServiceImplementation.addObject(new Hub(hubId, queryResult.getName(), InfluxHubStatusValue.RESTARTED));
                    hubSender.postPersist(createdHub);

                    logger.info("Restarted hub{id:" + hubId + "} not found in SQL, created restarted Hub: " + createdHub);
                }
            }
            case STOPPED -> {
                try {
                    Hub hub = crudServiceImplementation.findObjectById(hubId);

                    hub.setStatus(InfluxHubStatusValue.STOPPED);
                    hub = crudServiceImplementation.updateObjectById(hubId, hub);

                    logger.info("Discovered stopped of hub: " + hub);
                } catch (HubNotFoundException e) {
                    Hub createdHub = crudServiceImplementation.addObject(new Hub(hubId, queryResult.getName(), InfluxHubStatusValue.STOPPED));
                    logger.info("Stopped hub{id:" + hubId + "} not found in SQL, created stopped Hub: " + createdHub);
                }
            }
            case DELETED -> {
                try {
                    crudServiceImplementation.deleteObjectById(hubId);
                    logger.info("Discovered delete of hub{" + hubId + "}");
                } catch (HubNotFoundException e) {
                    logger.info("Deleted hub{id:" + hubId + "} not found in SQL");
                }
            }
        }
    }

    private String getQueryForHubDiscovery() {
        final String bucket = "hubs";
        final String measurement = "hubLog";
        final String field = "value";

        //cast last parameter, so java knows which implementation of produceQuery has to use
        return queryService.produceQuery(bucket, measurement, field, start, null, false, null, null , (String) null);
    }*/
}
