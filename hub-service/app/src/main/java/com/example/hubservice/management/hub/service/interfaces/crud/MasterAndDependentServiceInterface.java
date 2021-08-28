package com.example.hubservice.management.hub.service.interfaces.crud;

public interface MasterAndDependentServiceInterface<B, D, M, K, K_M> extends MasterServiceInterface<B, D, K>, DependentServiceInterface<B, M, K, K_M> {
}
