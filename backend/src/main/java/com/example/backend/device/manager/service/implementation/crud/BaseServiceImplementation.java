package com.example.backend.device.manager.service.implementation.crud;

import com.example.backend.device.manager.service.Builder;
import com.example.backend.device.manager.service.interfaces.crud.BaseServiceInterface;
import com.example.backend.device.manager.model.interfaces.crud.BaseTypeInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

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
    public B updateObjectById(K id, B patch) throws E {
        return repository.findById(id)
                .map(object -> {
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
        List<B> result = new ArrayList<>();

        List<B> objectsToDelete = getAllObjects();

        for (B objectToDelete : objectsToDelete)
            result.add(objectToDelete.deepCopy());

        deleteAllObjects();
        return result;
    }

    private B deleteObjectAndReturnDeletedObject(B toDelete) {
        B deletedObjectCopy = toDelete.deepCopy();
        repository.delete(toDelete);
        return deletedObjectCopy;
    }
}
