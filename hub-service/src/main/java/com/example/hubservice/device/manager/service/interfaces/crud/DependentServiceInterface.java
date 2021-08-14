package com.example.hubservice.device.manager.service.interfaces.crud;

public interface DependentServiceInterface<B, M> extends BaseServiceInterface<B> {
    B addDependentAndBindItToMasterById(B dependent, Long masterId);
    B addDependentAndBindItToMaster(B dependent, M master);
}
