package me.vik.socksstorageapp.service.Impl;

import me.vik.socksstorageapp.dto.SockDto;
import me.vik.socksstorageapp.exception.WritingFileException;
import me.vik.socksstorageapp.model.Socks;
import me.vik.socksstorageapp.service.FileService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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

    private SockDto mapToDto(Socks socks, int quantity) {
        SockDto sockDto = new SockDto();
        sockDto.setColor(socks.getColor());
        sockDto.setSize(socks.getSize());
        sockDto.setCottonPart(socks.getCottonPart());
        sockDto.setQuantity(quantity);
        return sockDto;
    }

    public ResponseEntity<Void> uploadFile(@RequestParam MultipartFile file) {
        cleanFile();
        try (FileOutputStream fos = new FileOutputStream(getFile())) {
            IOUtils.copy(file.getInputStream(), fos);
            return ResponseEntity.ok().build();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IOException e) {
            throw new WritingFileException("Неверный формат файла для записи, попробуйте снова");
        }
    }

}