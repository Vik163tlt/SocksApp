package me.vik.socksstorageapp.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;

@Service
public interface FileService {

    void cleanFile();

    File getFile();

    Path createTempFile();
}
