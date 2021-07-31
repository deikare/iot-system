package com.example.backend.device.manager.service.implementation;

import com.example.backend.device.manager.service.Builder;
import com.example.backend.device.manager.service.interfaces.BaseService;
import com.example.backend.device.manager.service.interfaces.BaseTypeInterface;
import com.example.backend.device.manager.service.interfaces.MasterService;
import com.example.backend.device.manager.service.interfaces.MasterTypeInterface;
import org.springframework.data.jpa.repository.JpaRepository;


public class MasterServiceImplementation<B extends Object & MasterTypeInterface<B, D>, D, R extends JpaRepository<B, Long>, E extends RuntimeException> extends BaseServiceImplementation<B, R, E> implements MasterService<B, D> {
    public MasterServiceImplementation(R repository, Builder<E> builder) {
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
