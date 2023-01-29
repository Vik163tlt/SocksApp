package me.vik.socksstorageapp.service;

import me.vik.socksstorageapp.dto.SockRequest;
import me.vik.socksstorageapp.model.Color;
import me.vik.socksstorageapp.model.Size;
import me.vik.socksstorageapp.model.Socks;
import org.springframework.stereotype.Service;

@Service
public interface SocksService {

    void addSocks(SockRequest sockRequest);

    void issueSocks(SockRequest sockRequest);

    void removeDefectiveSocks(SockRequest sockRequest);

    int getSocksQuantity(Color color, Size size, Integer cottonMin, Integer cottonMax);

    void validateRequest(SockRequest sockRequest);

    Socks mapToSocks(SockRequest sockRequest);
}
