package com.example.backend.device.manager.service.implementation.crud;

import com.example.backend.device.manager.service.Builder;
import com.example.backend.device.manager.model.interfaces.crud.DependentTypeInterface;
import com.example.backend.device.manager.service.interfaces.crud.MasterServiceInterface;
import com.example.backend.device.manager.model.interfaces.crud.MasterTypeInterface;
import org.springframework.data.jpa.repository.JpaRepository;

public class MasterServiceImplementation<B extends MasterTypeInterface<B, D>,
        D extends DependentTypeInterface<D, B>,
        E extends RuntimeException>
        extends BaseServiceImplementation<B, E>
        implements MasterServiceInterface<B, D> {

    public MasterServiceImplementation(JpaRepository<B, Long> repository, Builder<E> builder) {
        super(repository, builder);
    }

    @Override
    public B addDependentToListInObjectById(Long objectId, D dependent) {
        B object = findObjectById(objectId);
        return addDependentToListInObject(object, dependent);
    }

    @Override
    public B addDependentToListInObject(B object, D dependent) {
        return object.addDependentToDependentsList(dependent);
    }

    @Override
    public boolean deleteDependentFromListInObjectById(Long objectId, D dependent) {
        B object = findObjectById(objectId);
        return deleteDependentFromListInObject(object, dependent);
    }

    @Override
    public boolean deleteDependentFromListInObject(B object, D dependent) {
        return object.removeDependentFromDependentsList(dependent);
    }
}
