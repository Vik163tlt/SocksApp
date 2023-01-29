package me.vik.socksstorageapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import me.vik.socksstorageapp.dto.SockRequest;
import me.vik.socksstorageapp.exception.InsufficientQuantityException;
import me.vik.socksstorageapp.exception.InvalidSockRequestException;
import me.vik.socksstorageapp.exception.WritingFileException;
import me.vik.socksstorageapp.model.Color;
import me.vik.socksstorageapp.model.Size;
import me.vik.socksstorageapp.service.Impl.FileServiceImpl;
import me.vik.socksstorageapp.service.SocksService;
import org.apache.commons.io.IOUtils;
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

    @ExceptionHandler(InvalidSockRequestException.class)
    public ResponseEntity<String> handleInvalidException(InvalidSockRequestException wrongSocksException) {
        return ResponseEntity.badRequest().body(wrongSocksException.getMessage());
    }

    @ExceptionHandler(InsufficientQuantityException.class)
    public ResponseEntity<String> handleInvalidException(InsufficientQuantityException insufficientQuantityException) {
        return ResponseEntity.badRequest().body(insufficientQuantityException.getMessage());
    }

    @PostMapping
    public void addSocks(@RequestBody SockRequest sockRequest) {
        socksService.addSocks(sockRequest);
    }

    @PutMapping
    public void issueSocks(@RequestBody SockRequest sockRequest) {
        socksService.issueSocks(sockRequest);
    }

    @GetMapping
    public int getSocksCount(@RequestParam(required = false, name = "Цвет") Color color,
                             @RequestParam(required = false, name = "Размер") Size size,
                             @RequestParam(required = false, name = "Минимальный процент хлопка") Integer cottonMin,
                             @RequestParam(required = false, name = "Максимальный процент хлопка") Integer cottonMax) {
        return socksService.getSocksQuantity(color, size, cottonMin, cottonMax);
    }

    @DeleteMapping
    public void removeDefectiveSocks(@RequestBody SockRequest sockRequest) {
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
    public ResponseEntity<Void> uploadFile(@RequestParam MultipartFile file) {
        fileService.cleanFile();
        try (FileOutputStream fos = new FileOutputStream(fileService.getFile())) {
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
