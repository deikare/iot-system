package com.example.backend.device.manager.service.implementation.discovery;

import com.example.backend.data.model.mappers.InfluxHubLogPojo;
import com.example.backend.data.model.mappers.InfluxHubStatusValue;
import com.example.backend.data.service.InfluxQueryService;
import com.example.backend.device.manager.controllers.exceptions.HubNotFoundException;
import com.example.backend.device.manager.kafka.services.senders.EntityCrudSenderService;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.service.implementation.crud.MasterServiceImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

//TODO add offset
@Component
public class HubDiscoveryService {
    private final Logger logger = LoggerFactory.getLogger(HubDiscoveryService.class);

    private Instant start = Instant.ofEpochSecond(0);

    private final InfluxQueryService queryService;

    private final MasterServiceImplementation<Hub, Device, String, HubNotFoundException> crudServiceImplementation;
    private final EntityCrudSenderService<String, Hub> hubSender;


    public HubDiscoveryService(InfluxQueryService queryService, MasterServiceImplementation<Hub, Device, String, HubNotFoundException> crudServiceImplementation, EntityCrudSenderService<String, Hub> hubSender) {
        this.queryService = queryService;
        this.crudServiceImplementation = crudServiceImplementation;
        this.hubSender = hubSender;
    }

    @Scheduled(fixedRate = 10000)
    private void discoverServices() {
        String query = getQueryForHubDiscovery();

        start = Instant.now().minus(20L, ChronoUnit.SECONDS); //change next start to current one with offset

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
            case STARTED -> {
                try {
                    Hub hub = crudServiceImplementation.findObjectById(hubId);

                    hub.setStatus(InfluxHubStatusValue.STARTED);
                    hub = crudServiceImplementation.updateObjectById(hubId, hub);

                    logger.info("Started hub :" + hub + "already found in SQL");
                } catch (HubNotFoundException e) {
                    Hub createdHub = crudServiceImplementation.addObject(new Hub(queryResult.getHubId(), queryResult.getName(), InfluxHubStatusValue.STARTED));
                    logger.info("Discovered creation of hub: " + createdHub);
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
    }
}
