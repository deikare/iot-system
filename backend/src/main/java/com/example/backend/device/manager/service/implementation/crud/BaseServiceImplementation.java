package com.example.backend.device.manager.service.implementation.crud;

import com.example.backend.device.manager.controllers.exceptions.ControlSignalNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.ControlSignalResponseNotFoundException;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.ControlSignalResponse;
import com.example.backend.device.manager.service.Builder;
import com.example.backend.device.manager.service.interfaces.crud.BaseServiceInterface;
import com.example.backend.device.manager.model.interfaces.crud.BaseTypeInterface;
import com.example.backend.loggers.abstracts.ConfigLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Properties;

public class BaseServiceImplementation<
        B extends BaseTypeInterface<B>,
        E extends RuntimeException>
        implements BaseServiceInterface<B> {
    private final JpaRepository<B, Long> repository;
    private final Builder<E> objectNotFoundExceptionBuilder; //exception builder

    public BaseServiceImplementation(JpaRepository<B, Long> repository, Builder<E> objectNotFoundExceptionBuilder) {
        this.repository = repository;
        this.objectNotFoundExceptionBuilder = objectNotFoundExceptionBuilder;
    }


    @Override
    public B addObject(B t) {
        return repository.save(t);
    }

    @Override
    public List<B> getAllObjects() {
        return repository.findAll();
    }

    @Override
    public B findObjectById(Long id) throws E {
        return repository.findById(id).orElseThrow(() -> objectNotFoundExceptionBuilder.newObject(id));
    }

    @Override
    public B updateObjectById(Long id, B patch) throws E {
        B patchedObject = findObjectById(id);
        return updateObject(patchedObject, patch);
    }

    @Override
    public B updateObject(B patchedObject, B patch) {
        return patchedObject.update(patch);
    }

    @Override
    public void deleteObjectById(Long id) throws E {
        B objectToDelete = findObjectById(id);
        repository.delete(objectToDelete);
    }

    @Override
    public void deleteAllObjects() {
        repository.deleteAll();
    }
}
