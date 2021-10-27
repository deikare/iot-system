package com.example.backend.device.manager.service.implementation.crud;

import com.example.backend.device.manager.controllers.exceptions.EntityNotModifiedException;
import com.example.backend.device.manager.model.interfaces.crud.BaseTypeInterface;
import com.example.backend.device.manager.service.Builder;
import com.example.backend.device.manager.service.interfaces.crud.BaseServiceInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
public class BaseServiceImplementation<
        B extends BaseTypeInterface<B>,
        K,
        E extends RuntimeException>
        implements BaseServiceInterface<B, K> {

    private final JpaRepository<B, K> repository;
    private final Builder<E, K> objectNotFoundExceptionBuilder; //exception builder

    public BaseServiceImplementation(JpaRepository<B, K> repository, Builder<E, K> objectNotFoundExceptionBuilder) {
        this.repository = repository;
        this.objectNotFoundExceptionBuilder = objectNotFoundExceptionBuilder;
    }

    @Override
    public B addObject(B t) { //needs to enable to set id of object
        return repository.save(t);
    }

    @Override
    public List<B> getAllObjects() {
        return repository.findAll();
    }

    @Override
    public B findObjectById(K id) throws E {
        return repository.findById(id).orElseThrow(() -> objectNotFoundExceptionBuilder.newObject(id));
    }

    @Override
    public B updateObjectById(K id, B patch) throws E, EntityNotModifiedException {
        return repository.findById(id)
                .map(object -> {
                    if (object.equals(patch))
                        throw new EntityNotModifiedException();
                    object.update(patch);
                    return repository.save(object);
                })
                .orElseThrow(() -> objectNotFoundExceptionBuilder.newObject(id));
    }

    @Override
    public void deleteObjectById(K id) throws E {
        findObjectById(id);
        repository.deleteById(id);
    }

    @Override
    public B deleteObjectByIdAndReturnDeletedObject(K id) throws E {
        B toDelete = findObjectById(id);
        return deleteObjectAndReturnDeletedObject(toDelete);
    }

    @Override
    public void deleteAllObjects() {
        repository.deleteAll();
    }

    @Override
    public List<B> deleteAllObjectsAndReturnThemListed() {
        List<B> objectsToDelete = getAllObjects();
        deleteAllObjects();
        return objectsToDelete;
    }

    private B deleteObjectAndReturnDeletedObject(B toDelete) {
        repository.delete(toDelete);
        return toDelete;
    }
}
