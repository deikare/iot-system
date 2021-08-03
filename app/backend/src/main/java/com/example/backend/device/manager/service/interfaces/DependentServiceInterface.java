package com.example.backend.device.manager.service.interfaces;

//TODO possible warning - both dependent and master interfaces impose add method - perhaps both don't need to extend base interface
public interface DependentServiceInterface<B, M> extends BaseServiceInterface<B> {
    B addDependentAndBindItToMasterById(B dependent, Long masterId);
    B addDependentAndBindItToMaster(B dependent, M master);
}
