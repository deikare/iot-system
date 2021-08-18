package com.example.backend.device.manager.service.interfaces.crud;

public interface MasterAndDependentServiceInterface<B, D, M, K, K_M> extends MasterServiceInterface<B, D, K>, DependentServiceInterface<B, M, K, K_M> {
}
