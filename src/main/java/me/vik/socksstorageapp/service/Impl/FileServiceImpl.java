package me.vik.socksstorageapp.service.Impl;

import me.vik.socksstorageapp.exception.WritingFileException;
import me.vik.socksstorageapp.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FileServiceImpl implements FileService {
    @Value("${pathToSocksJson}")
    private String socksJsonPath;
    @Value("${nameOfSockStorageJson}")
    private String socksJsonName;

    public boolean saveFile(String json) {
        try {
            cleanFile();
            Files.writeString(Path.of(socksJsonPath, socksJsonName), json);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String readFile() {
        String json;
        try {
            json = Files.readString(Path.of(socksJsonPath, socksJsonName));
            return json;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void cleanFile() {
        try {
            Files.deleteIfExists(Path.of(socksJsonPath, socksJsonName));
            Files.createFile(Path.of(socksJsonPath, socksJsonName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public File getFile() {
        return new File(socksJsonPath + "/" + socksJsonName);
    }

    @Override
    public Path createTempFile() {
        try {
            Path path = Files.createTempFile(Path.of(socksJsonPath),"tempFile",""+ LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));
            return path;
        } catch (IOException e) {
            throw new WritingFileException("Ошибка записи файла");
        }
    }
}