package com.example.hubservice.management.hub.service.interfaces.crud;

public interface DependentServiceInterface<B, M, K, K_M> extends BaseServiceInterface<B, K> {
    B addDependentAndBindItToMasterById(B dependent, K_M masterId);
    B addDependentAndBindItToMaster(B dependent, M master);
}
