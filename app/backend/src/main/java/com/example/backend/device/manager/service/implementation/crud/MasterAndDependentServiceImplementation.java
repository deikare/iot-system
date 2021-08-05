package com.example.backend.device.manager.service.implementation.crud;

import com.example.backend.device.manager.model.interfaces.DependentTypeInterface;
import com.example.backend.device.manager.model.interfaces.MasterAndDependentTypeInterface;
import com.example.backend.device.manager.model.interfaces.MasterTypeInterface;
import com.example.backend.device.manager.service.Builder;
import com.example.backend.device.manager.service.interfaces.crud.MasterAndDependentServiceInterface;
import org.springframework.data.jpa.repository.JpaRepository;

public class MasterAndDependentServiceImplementation <B extends MasterAndDependentTypeInterface<B, D, M>, D extends DependentTypeInterface<D, B>, M extends MasterTypeInterface<M, B>, R extends JpaRepository<B, Long>, R_M extends JpaRepository<M, Long>, E extends RuntimeException, E_M extends RuntimeException> extends BaseServiceImplementation<B, R, E> implements MasterAndDependentServiceInterface<B, D, M> {
    private final MasterServiceImplementation<B, D, R, E> masterServiceImplementation;
    private final DependentServiceImplementation<B, M, R, R_M, E, E_M> dependentServiceImplementation;

    public MasterAndDependentServiceImplementation(R repository, Builder<E> builder, MasterServiceImplementation<B, D, R, E> masterServiceImplementation, DependentServiceImplementation<B, M, R, R_M, E, E_M> dependentServiceImplementation) {
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
