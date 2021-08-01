package com.example.backend.device.manager.service.implementation;

import com.example.backend.device.manager.service.Builder;
import com.example.backend.device.manager.service.interfaces.DependentService;
import com.example.backend.device.manager.service.interfaces.DependentTypeInterface;
import com.example.backend.device.manager.service.interfaces.MasterTypeInterface;
import org.springframework.data.jpa.repository.JpaRepository;

public class DependentServiceImplementation<B extends Object & DependentTypeInterface<B, M>, M extends Object & MasterTypeInterface<M, B>, R extends JpaRepository<B, Long>, R_M extends JpaRepository<M, Long>, E extends RuntimeException, E_M extends RuntimeException> extends BaseServiceImplementation<B, R, E> implements DependentService<B, M> {

    private final MasterServiceImplementation<M, B, R_M, E_M> masterServiceImplementation;

    public DependentServiceImplementation(R repository, Builder<E> builder, MasterServiceImplementation<M, B, R_M, E_M> masterServiceImplementation) {
        super(repository, builder);
        this.masterServiceImplementation = masterServiceImplementation;
    }

    @Override
    public B addDependentAndBindItToMasterById(B dependent, Long masterId) throws E {
        M master = masterServiceImplementation.findObjectById(masterId);
        return addDependentAndBindItToMaster(dependent, master);
    }

    @Override
    public B addDependentAndBindItToMaster(B dependent, M master) {
        B addedDependent= addObject(dependent);
        masterServiceImplementation.addDependentToListInObject(master, dependent);
        return addedDependent;
    }
}

