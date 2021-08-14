package com.example.hubservice.device.manager.service.interfaces.crud;

public interface MasterAndDependentServiceInterface<B, D, M> extends MasterServiceInterface<B, D>, DependentServiceInterface<B, M> {
}
