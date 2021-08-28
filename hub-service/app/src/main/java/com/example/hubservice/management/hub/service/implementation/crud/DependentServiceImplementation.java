package com.example.hubservice.management.hub.service.implementation.crud;

import com.example.hubservice.management.hub.model.crud.DependentTypeInterface;
import com.example.hubservice.management.hub.model.crud.MasterTypeInterface;
import com.example.hubservice.management.hub.service.Builder;
import com.example.hubservice.management.hub.service.interfaces.crud.DependentServiceInterface;
import org.springframework.data.jpa.repository.JpaRepository;

public class DependentServiceImplementation<
        B extends DependentTypeInterface<B, M>,
        M extends MasterTypeInterface<M, B>,
        K,
        K_M,
        E extends RuntimeException,
        E_M extends RuntimeException>
        extends BaseServiceImplementation<B, K, E>
        implements DependentServiceInterface<B, M, K, K_M> {

    private final MasterServiceImplementation<M, B, K_M, E_M> masterServiceImplementation;

    public DependentServiceImplementation(JpaRepository<B, K> repository, Builder<E, K> builder, MasterServiceImplementation<M, B, K_M, E_M> masterServiceImplementation) {
        super(repository, builder);
        this.masterServiceImplementation = masterServiceImplementation;
    }

    @Override
    public B addDependentAndBindItToMasterById(B dependent, K_M masterId) throws E {
        M master = masterServiceImplementation.findObjectById(masterId);
        return addDependentAndBindItToMaster(dependent, master);
    }

    @Override
    public B addDependentAndBindItToMaster(B dependent, M master) {
        B addedDependent = addObject(dependent);
        masterServiceImplementation.addDependentToListInObject(master, dependent);
        return addedDependent;
    }
}

