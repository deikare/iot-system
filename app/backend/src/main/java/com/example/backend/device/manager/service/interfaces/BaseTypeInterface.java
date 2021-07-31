package com.example.backend.device.manager.service.interfaces;

import java.util.List;
import java.util.Properties;

public interface BaseTypeInterface<B> {
    B update(Properties patch);
}
