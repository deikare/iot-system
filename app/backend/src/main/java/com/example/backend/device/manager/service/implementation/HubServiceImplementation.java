package com.example.backend.device.manager.service.implementation;

import com.example.backend.device.manager.controllers.exceptions.hub.HubNotFoundException;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.repositories.HubRepository;
import com.example.backend.device.manager.service.Builder;
import com.example.backend.device.manager.service.interfaces.BaseService;
import com.example.backend.device.manager.service.interfaces.BaseTypeInterface;
import com.example.backend.device.manager.service.interfaces.HubService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class HubServiceImplementation implements HubService {
    private final HubRepository hubRepository;

    public HubServiceImplementation(HubRepository hubRepository) {
        this.hubRepository = hubRepository;
    }

    @Override
    public Hub addHub(Hub hub) {
        return hubRepository.save(hub);
    }

    @Override
    public Page<Hub> getAllHubs(Pageable pageable) {
        return hubRepository.findAll(pageable);
    }

    @Override
    public Page<Hub> getAllHubsByNameContaining(String name, Pageable pageable) {
        return hubRepository.findAllByNameContaining(name, pageable);
    }

    @Override
    public Hub findHubById(Long id) throws HubNotFoundException {
        return hubRepository.findById(id).orElseThrow(() -> new HubNotFoundException(id));
    }

    @Override
    public Hub updateHubNameById(Long id, String name) throws HubNotFoundException {
        Hub hub = findHubById(id);
        hub.setName(name);
        return hub;
    }

    @Override
    public void deleteHubById(Long id) {
        hubRepository.deleteById(id);
    }

    @Override
    public Hub addDeviceToDeviceListInHubByHubId(Long hubId, Device device) throws HubNotFoundException{
        Hub hub = findHubById(hubId);
        return addDeviceToDeviceListInHub(hub, device);
    }

    @Override
    public Hub addDeviceToDeviceListInHub(Hub hub, Device device) {
        hub.getDevices().add(device);
        return hub;
    }

    @Override
    public boolean deleteDeviceFromDeviceListInHubByHubId(Long hubId, Device device) throws HubNotFoundException{
        Hub hub = findHubById(hubId);
        return hub.removeDeviceFromDeviceList(device);
    }

    @Override
    public boolean deleteDeviceFromDeviceListInHub(Hub hub, Device device) {
        return hub.removeDeviceFromDeviceList(device);
    }

    @Override
    public void deleteAllHubs() {
        hubRepository.deleteAll();
    }
}
/*
// T = this class, S = dependent, R - repo, E - T not found exception
public class xyz<B extends Object & BaseTypeInterface<B, D>, D, R extends JpaRepository<B, Long>, E extends RuntimeException> implements BaseService<B, D> {
    private final R repository;
    private final Builder<E> objectNotFoundExceptionBuilder; //exception builder

    public BaseServiceImplementation(R repository, Builder<E> builder) {
        this.repository = repository;
        this.objectNotFoundExceptionBuilder = builder;
    }

    @Override
    public B addObject(B t) {
        return repository.save(t);
    }

    @Override
    public Page<B> getAllObjects(Pageable pageable) { //TODO still needs custom query interface, dunno how to do it yet
        return null;
    }

    @Override
    public B findObjectById(Long id) throws E {
        return repository.findById(id).orElseThrow(() -> objectNotFoundExceptionBuilder.newObject(id));
    }

    @Override
    public B updateObjectById(Long id, B patch) throws E {
        B patchedObject = findObjectById(id);
        return updateObject(patchedObject, patch);
    }

    @Override
    public B updateObject(B patchedObject, B patch) {
        return patchedObject.update(patch);
    }

    @Override
    public void deleteObjectById(Long id) throws E {
        B objectToDelete = findObjectById(id);
        repository.delete(objectToDelete);
    }

    @Override
    public B addDependentToListInObjectById(Long objectId, D dependent) throws E {
        B object = findObjectById(objectId);
        return addDependentToListInObject(object, dependent);
    }

    @Override
    public B addDependentToListInObject(B object, D dependent) {
        return object.addDependentToDependentsList(dependent);
    }

    @Override
    public boolean deleteDependentFromListInObjectById(Long objectId, D dependent) throws E {
        B object = findObjectById(objectId);
        return deleteDependentFromListInObject(object, dependent);
    }

    @Override
    public boolean deleteDependentFromListInObject(B object, D dependent) {
        return object.removeDependentFromDependentsList(dependent);
    }

    @Override
    public void deleteAllObjects() {
        repository.deleteAll();
    }
}*/
