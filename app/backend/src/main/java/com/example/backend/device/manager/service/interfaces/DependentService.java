package com.example.backend.device.manager.service.interfaces;

public interface DependentService<B, M> extends BaseService<B> {
    B addDependentAndBindItToMasterById(B dependent, Long masterId);
    B addDependentAndBindItToMaster(B dependent, M master);
}
