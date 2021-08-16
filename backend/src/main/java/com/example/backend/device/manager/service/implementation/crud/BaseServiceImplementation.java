package com.example.backend.device.manager.service.implementation.crud;

import com.example.backend.device.manager.service.Builder;
import com.example.backend.device.manager.service.interfaces.crud.BaseServiceInterface;
import com.example.backend.device.manager.model.interfaces.crud.BaseTypeInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

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
    public B addObject(B t) { //needs to enable to set id of object
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
        return repository.findById(id)
                .map(object -> {
                    object.update(patch);
                    return repository.save(object);
                })
                .orElseThrow(() -> objectNotFoundExceptionBuilder.newObject(id));
    }

    @Override
    public void deleteObjectById(Long id) throws E {
        findObjectById(id);
        repository.deleteById(id);
    }

    @Override
    public void deleteAllObjects() {
        repository.deleteAll();
    }
}
