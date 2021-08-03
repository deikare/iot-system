package com.example.backend.device.manager.service.implementation;

import com.example.backend.device.manager.service.Builder;
import com.example.backend.device.manager.service.interfaces.BaseServiceInterface;
import com.example.backend.device.manager.service.interfaces.BaseTypeInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Properties;

// T = this class, S = dependent, R - repo, E - T not found exception
public class BaseServiceImplementation<
        B extends BaseTypeInterface<B>,
        R extends JpaRepository<B, Long>,
        E extends RuntimeException>
        implements BaseServiceInterface<B> {
    private final R repository;
    private final Builder<E> objectNotFoundExceptionBuilder; //exception builder

    public BaseServiceImplementation(R repository, Builder<E> builder) {
        this.repository = repository;
        this.objectNotFoundExceptionBuilder = builder;
    }

    @Override
    public B addObject(B t) {
        return repository.save(t);
    }

    @Override
    public Page<B> getAllObjects(Pageable pageable) { //TODO still needs custom query interface, dunno how to do it yet
        return null;
    }

    @Override
    public B findObjectById(Long id) throws E {
        return repository.findById(id).orElseThrow(() -> objectNotFoundExceptionBuilder.newObject(id));
    }

    @Override
    public B updateObjectById(Long id, Properties patch) throws E {
        B patchedObject = findObjectById(id);
        return updateObject(patchedObject, patch);
    }

    @Override
    public B updateObject(B patchedObject, Properties patch) {
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
