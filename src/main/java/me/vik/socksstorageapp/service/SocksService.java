package me.vik.socksstorageapp.service;

import me.vik.socksstorageapp.dto.SockDto;
import me.vik.socksstorageapp.model.Color;
import me.vik.socksstorageapp.model.Size;
import me.vik.socksstorageapp.model.Socks;
import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Service;

@Service
public interface SocksService {

    void addFormatters(FormatterRegistry registry);

    void addSocks(SockDto sockRequest);

    void issueSocks(SockDto sockRequest);

    void removeDefectiveSocks(SockDto sockRequest);

    int getSocksQuantity(Color color, Size size, Integer cottonMin, Integer cottonMax);

    void validateRequest(SockDto sockRequest);

    Socks mapToSocks(SockDto sockRequest);
}
