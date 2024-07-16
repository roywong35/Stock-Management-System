package com.example.stockmanagementsystem.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.example.stockmanagementsystem.repository.StockIoRepository;
import com.example.stockmanagementsystem.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.stockmanagementsystem.model.Stock;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final StockIoRepository stockIoRepository;

    @Autowired
    public StockServiceImpl(StockRepository stockRepository, StockIoRepository stockIoRepository) {
        this.stockRepository = stockRepository;
        this.stockIoRepository = stockIoRepository;
    }

    @Override
    public List<Stock> getAllStock() {
        return stockRepository.findAllActiveStocks();
    }

    @Override
    public void saveStock(Stock stock) {
        this.stockRepository.save(stock);
    }

    @Override
    @Transactional(readOnly = true)
    public int getNextStockId() {
        Integer maxStockId = stockRepository.findMaxStockId();
        return maxStockId == null ? 1 : maxStockId + 1;
    }

    @Override
    public Stock getStockById(long id) {
        Optional<Stock> optional = stockRepository.findByIdAndNotDeleted(id);
        Stock stock = null;
        if (optional.isPresent()) {
            stock = optional.get();
        } else {
            throw new RuntimeException(" Employee not found for id :: " + id);
        }
        return stock;
    }

//    @Override
//    public Stock getStockById(long id) {
//        return stockRepository.findByIdAndNotDeleted(id)
//                .orElseThrow(() -> new RuntimeException("Stock not found for id :: " + id));
//    }

    @Transactional
    public void updateStockNum(Long stockId) {
        // Calculate sum of ioNum for the given stockId
        BigDecimal sumIoNum = stockIoRepository.sumIoNumByStockId(stockId);

        // Get the Stock entity
        Stock stock = stockRepository.findByIdAndNotDeleted(stockId).orElse(null);

        if (stock != null) {
            stock.setStockNum(sumIoNum != null ? sumIoNum : BigDecimal.ZERO);
            stockRepository.save(stock);
        } else {
            // Log or handle the case where stockId does not exist
            System.out.println("Stock with id " + stockId + " not found.");
            // You can optionally log this information or handle it in some other way
        }
    }


}