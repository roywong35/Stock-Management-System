package com.example.stockmanagementsystem.service;

import com.example.stockmanagementsystem.model.Stock;
import java.util.List;

public interface StockService {
    List<Stock> getAllStock();
    void saveStock(Stock stock);
    Stock getStockById(long id);
    void deleteStockById(long id);
    int getNextStockId();
    void updateStockNum(Long stockId);
}