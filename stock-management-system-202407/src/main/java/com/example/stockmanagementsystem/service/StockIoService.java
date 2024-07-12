package com.example.stockmanagementsystem.service;

import com.example.stockmanagementsystem.model.StockIo;

import java.util.List;

public interface StockIoService {
    List<StockIo> getAllStockIo();
    void saveStockIo(StockIo stockIo);
    StockIo getStockIoById(long id);
    void deleteStockIoById(long id);
    int getNextIoId();
    boolean existsByStock_StockId(Long stockId);
    boolean matchesStockName(Long stockId, String stockName);
    boolean matchesStockUnit(Long stockId, Long unitId);
    List<StockIo> getStockIoByStockId(long stockId);
}