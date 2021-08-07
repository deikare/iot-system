package com.example.backend.device.manager.model.interfaces.crud;

import java.util.Properties;

public interface BaseTypeInterface<B> {
    B update(Properties patch);
}
