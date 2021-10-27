package com.example.backend.data.controllers.representation.assemblers;

import com.example.backend.data.controllers.LogController;
import com.example.backend.data.controllers.representation.models.LogseriesRepresentationModel;
import com.example.backend.data.model.logseries.DeviceLogseries;
import com.example.backend.data.model.mappers.InfluxDeviceLogPojo;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LogseriesRepresentationAssembler extends RepresentationModelAssemblerSupport<DeviceLogseries<InfluxDeviceLogPojo>,
        LogseriesRepresentationModel> {


    public LogseriesRepresentationAssembler() {
        super(LogController.class, LogseriesRepresentationModel.class);
    }

    @NotNull
    @Override
    public LogseriesRepresentationModel toModel(@NotNull DeviceLogseries<InfluxDeviceLogPojo> entity) {
        return null;
    }

    @NotNull
    public LogseriesRepresentationModel toModelConsideringQueryParams(@NotNull DeviceLogseries<InfluxDeviceLogPojo> entity, Long limit,
                                                                              List<String> hubIds, List<String> deviceIds) {
        LogseriesRepresentationModel model = new LogseriesRepresentationModel(entity);

        model.add(linkTo(methodOn(LogController.class)
                        .all(Instant.ofEpochSecond(0), null, true, null, null,null))
                        .withRel("all-data"),
                linkTo(methodOn(LogController.class) //there is no need to make offset because logs are produced rarely
                        .all(entity.getStart().minus(2, ChronoUnit.HOURS), entity.getStart(),
                                true, limit, hubIds, deviceIds))
                        .withRel("prev"),
                linkTo(methodOn(LogController.class)
                        .all(entity.getEnd(), entity.getEnd().plus(2, ChronoUnit.HOURS),
                                false, limit, hubIds, deviceIds))
                        .withRel("next"));

        return model;
    }
}
