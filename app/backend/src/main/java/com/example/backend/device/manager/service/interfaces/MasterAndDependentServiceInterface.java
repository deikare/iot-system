package com.example.backend.device.manager.service.interfaces;

public interface MasterAndDependentServiceInterface<B, D, M> extends MasterServiceInterface<B, D>, DependentServiceInterface<B, M> {
}
