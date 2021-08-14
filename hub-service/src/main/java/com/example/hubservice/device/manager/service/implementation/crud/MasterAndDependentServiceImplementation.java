package com.example.hubservice.device.manager.service.implementation.crud;

import com.example.backend.device.manager.model.interfaces.crud.DependentTypeInterface;
import com.example.backend.device.manager.model.interfaces.crud.MasterAndDependentTypeInterface;
import com.example.backend.device.manager.model.interfaces.crud.MasterTypeInterface;
import com.example.backend.device.manager.service.Builder;
import com.example.backend.device.manager.service.interfaces.crud.MasterAndDependentServiceInterface;
import org.springframework.data.jpa.repository.JpaRepository;

public class MasterAndDependentServiceImplementation <B extends MasterAndDependentTypeInterface<B, D, M>,
        D extends DependentTypeInterface<D, B>,
        M extends MasterTypeInterface<M, B>,
        E extends RuntimeException,
        E_M extends RuntimeException>
        extends BaseServiceImplementation<B, E>
        implements MasterAndDependentServiceInterface<B, D, M> {
    private final MasterServiceImplementation<B, D, E> masterServiceImplementation;
    private final DependentServiceImplementation<B, M, E, E_M> dependentServiceImplementation;

    public MasterAndDependentServiceImplementation(JpaRepository<B, Long> repository, Builder<E> builder, MasterServiceImplementation<B, D, E> masterServiceImplementation, DependentServiceImplementation<B, M, E, E_M> dependentServiceImplementation) {
        super(repository, builder);
        this.masterServiceImplementation = masterServiceImplementation;
        this.dependentServiceImplementation = dependentServiceImplementation;
    }

    @Override
    public B addDependentAndBindItToMasterById(B dependent, Long masterId) throws E_M {
        return dependentServiceImplementation.addDependentAndBindItToMasterById(dependent, masterId);
    }

    @Override
    public B addDependentAndBindItToMaster(B dependent, M master) {
        return dependentServiceImplementation.addDependentAndBindItToMaster(dependent, master);
    }

    @Override
    public B addDependentToListInObjectById(Long objectId, D dependent) throws E {
        return masterServiceImplementation.addDependentToListInObjectById(objectId, dependent);
    }

    @Override
    public B addDependentToListInObject(B object, D dependent) {
        return masterServiceImplementation.addDependentToListInObject(object, dependent);
    }

    @Override
    public boolean deleteDependentFromListInObjectById(Long objectId, D dependent) throws E {
        return masterServiceImplementation.deleteDependentFromListInObjectById(objectId, dependent);
    }

    @Override
    public boolean deleteDependentFromListInObject(B object, D dependent) {
        return masterServiceImplementation.deleteDependentFromListInObject(object, dependent);
    }
}
