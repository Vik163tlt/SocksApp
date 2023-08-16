package me.vik.socksstorageapp.service.Impl;

import me.vik.socksstorageapp.dto.SockDto;
import me.vik.socksstorageapp.exception.InsufficientQuantityException;
import me.vik.socksstorageapp.exception.InvalidSockRequestException;
import me.vik.socksstorageapp.model.Color;
import me.vik.socksstorageapp.model.Size;
import me.vik.socksstorageapp.model.Socks;
import me.vik.socksstorageapp.service.SocksService;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@Configuration
public class SocksServiceImpl implements SocksService  {

    private final FileServiceImpl fileService;

    private Map<Socks, Integer> socksStorage = new HashMap<>();

    public SocksServiceImpl(FileServiceImpl fileService) {
        this.fileService = fileService;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        ApplicationConversionService.configure(registry);
    }

    @Override
    public void addSocks(SockDto sockRequest) {

        Socks socks = mapToSocks(sockRequest);
        if (socksStorage.containsKey(socks)) {
            socksStorage.put(socks, socksStorage.get(socks) + sockRequest.getQuantity());
        } else {
            socksStorage.put(socks, sockRequest.getQuantity());
        }
    }

    @Override
    public int getSocksQuantity(Color color, Size size, Integer cottonMin, Integer cottonMax) {
        int total = 0;
        for (Map.Entry<Socks, Integer> entry : socksStorage.entrySet()) {
            if (color != null && !entry.getKey().getColor().equals(color)) {
                continue;
            }
            if (size != null && entry.getKey().getSize().equals(size)) {
                continue;
            }
            if (cottonMin != null && entry.getKey().getCottonPart() < cottonMin) {
                continue;
            }
            if (cottonMax != null && entry.getKey().getCottonPart() > cottonMax) {
                continue;
            }
            total += entry.getValue();
        }
        return total;
    }

    @Override
    public void removeDefectiveSocks(SockDto sockRequest) {
        decreaseSocks(sockRequest);
    }

    @Override
    public void issueSocks(SockDto sockRequest) {
        decreaseSocks(sockRequest);
    }

    private void decreaseSocks (SockDto sockRequest) {
        validateRequest(sockRequest);
        Socks socks = mapToSocks(sockRequest);
        int socksQuantity = socksStorage.getOrDefault(socks, 0);
        if (socksQuantity >= sockRequest.getQuantity()) {
            socksStorage.put(socks, socksQuantity - sockRequest.getQuantity());
        } else {
            throw new InsufficientQuantityException("Запрашиваемое количество носков больше, чем есть на складе!");
        }
    }

    @Override
    public Socks mapToSocks(SockDto sockRequest) {
        return new Socks(
                sockRequest.getColor(),
                sockRequest.getSize(),
                sockRequest.getCottonPart());
    }

    @Override
    public void validateRequest(SockDto sockRequest) {
        if (sockRequest.getColor() == null || sockRequest.getSize() == null) {
            throw new InvalidSockRequestException("Все поля должны быть заполнены");
        }
        if (sockRequest.getQuantity() > 0) {
            throw new InvalidSockRequestException("Количество должно быть больше нуля");
        }
        if (sockRequest.getCottonPart() < 0 || sockRequest.getCottonPart() > 100) {
            throw new InvalidSockRequestException("Процент хлопка должен быть от нуля до ста");
        }
    }
}