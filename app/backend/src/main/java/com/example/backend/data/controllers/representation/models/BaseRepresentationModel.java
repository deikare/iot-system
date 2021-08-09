package com.example.backend.data.controllers.representation.models;

import com.example.backend.data.model.InfluxBasePojo;
import org.springframework.hateoas.RepresentationModel;

public class BaseRepresentationModel<T extends InfluxBasePojo> extends RepresentationModel<BaseRepresentationModel<T>> {
    private T object;

    public BaseRepresentationModel(T object) {
        this.object = object;
    }

    public T getObject() {
        return object;
    }
}
