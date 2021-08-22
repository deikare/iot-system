package com.example.backend.device.manager.service.implementation.discovery;

import com.example.backend.data.model.mappers.InfluxHubLogPojo;
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
import java.util.List;

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

        start = Instant.now(); //change next start to current one

        List<InfluxHubLogPojo> queryResults = queryService.query(query, InfluxHubLogPojo.class);

        consumeQueryResults(queryResults);
    }

    private void consumeQueryResults(List<InfluxHubLogPojo> queryResults) {
        for (InfluxHubLogPojo queryResult : queryResults) {
            consumeQueryResult(queryResult);
        }
    }

    private void consumeQueryResult(InfluxHubLogPojo queryResult) {
        switch (queryResult.getValue()) {
            case CREATED -> {
                Hub createdHub = crudServiceImplementation.addObject(new Hub(queryResult.getHubId(), queryResult.getName()));
                logger.info("Discovered creation of hub: " + createdHub);
            }
            case RESTARTED -> {
                String hubId = queryResult.getHubId();
                try {
                    Hub restartedHub = crudServiceImplementation.findObjectById(hubId);
                    logger.info("Discovered restart of hub: " + restartedHub);
                } catch (HubNotFoundException e) {
                    Hub createdHub = crudServiceImplementation.addObject(new Hub(hubId, queryResult.getName()));
                    hubSender.postPersist(createdHub);

                    logger.info("Restarted hub{id:" + hubId + "} not found in SQL, created restarted Hub: " + createdHub);
                }
            }
            case STOPPED -> {
                String hubId = queryResult.getHubId();
                try {
                    Hub hub = crudServiceImplementation.findObjectById(hubId);
                    logger.info("Discovered stopped of hub: " + hub);
                } catch (HubNotFoundException e) {
                    Hub createdHub = crudServiceImplementation.addObject(new Hub(hubId, queryResult.getName()));
                    logger.info("Stopped hub{id:" + hubId + "} not found in SQL, created stopped Hub: " + createdHub);
                }
            }
            case DELETED -> {
                String hubId = queryResult.getHubId();
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
        final String range = "start: " + start.toString();
        final String measurement = "hubLog";
        final String field = "value";
        final String sort = "columns: [\"_time\"], desc: true";

        return queryService.produceQuery(bucket, range, measurement, field, "", "", "", "", "", "");
    }
}
