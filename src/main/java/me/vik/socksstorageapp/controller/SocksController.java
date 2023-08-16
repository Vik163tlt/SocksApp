package me.vik.socksstorageapp.controller;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import me.vik.socksstorageapp.dto.SockDto;
import me.vik.socksstorageapp.exception.InsufficientQuantityException;
import me.vik.socksstorageapp.exception.InvalidSockRequestException;
import me.vik.socksstorageapp.model.Color;
import me.vik.socksstorageapp.model.Size;
import me.vik.socksstorageapp.service.Impl.FileServiceImpl;
import me.vik.socksstorageapp.service.SocksService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
@RequestMapping("/socks")
public class SocksController {

    private final FileServiceImpl fileService;

    private final SocksService socksService;

    public SocksController(FileServiceImpl fileService, SocksService socksService) {
        this.fileService = fileService;
        this.socksService = socksService;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleInvalidException(InvalidSockRequestException wrongSocksException) {
        return ResponseEntity.badRequest().body(wrongSocksException.getMessage());
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleInvalidException(InsufficientQuantityException insufficientQuantityException) {
        return ResponseEntity.badRequest().body(insufficientQuantityException.getMessage());
    }

    @PostMapping
    public void addSocks(@RequestBody SockDto sockRequest) {
        socksService.addSocks(sockRequest);
    }

    @PutMapping
    public void issueSocks(@RequestBody SockDto sockRequest) {
        socksService.issueSocks(sockRequest);
    }

    @GetMapping
    @JsonValue
    public int getSocksCount(@RequestParam(required = false, name = "Цвет") Color color,
                             @RequestParam(required = false, name = "Размер") Size size,
                             @RequestParam(required = false, name = "Минимальный процент хлопка") Integer cottonMin,
                             @RequestParam(required = false, name = "Максимальный процент хлопка") Integer cottonMax) {
        return socksService.getSocksQuantity(color, size, cottonMin, cottonMax);
    }

    @DeleteMapping
    public void removeDefectiveSocks(@RequestBody SockDto sockRequest) {
        socksService.issueSocks(sockRequest);
    }

    @GetMapping("/export")
    @Operation(summary = "Скачать список")
    public ResponseEntity<InputStreamResource> downloadFile() throws FileNotFoundException {
        File file = fileService.getFile();
        InputStreamResource resource;
        if (file.exists()) {
            try {
                resource = new InputStreamResource(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                throw new FileNotFoundException("Файл на сервере не найден");
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"RecipesLog.json\"")
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузить список")
    public ResponseEntity<Void> loadFile() {
        File file = fileService.getFile();
        return fileService.uploadFile((MultipartFile) file);
    }
}
