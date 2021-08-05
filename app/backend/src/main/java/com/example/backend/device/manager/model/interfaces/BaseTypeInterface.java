package com.example.backend.device.manager.model.interfaces;

import java.util.List;
import java.util.Properties;

public interface BaseTypeInterface<B> {
    B update(Properties patch);
}
