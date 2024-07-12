package com.example.stockmanagementsystem.service;

import java.util.List;
import java.util.Optional;

import com.example.stockmanagementsystem.repository.StockIoRepository;
import com.example.stockmanagementsystem.repository.StockRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.stockmanagementsystem.model.StockIo;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockIoServiceImpl implements StockIoService {

    private final StockIoRepository stockIoRepository;
    private final StockRepository stockRepository;

    @Autowired
    public StockIoServiceImpl(StockIoRepository stockIoRepository, StockRepository stockRepository) {
        this.stockIoRepository = stockIoRepository;
        this.stockRepository = stockRepository;
    }

    @Override
    public List<StockIo> getAllStockIo() {
        return stockIoRepository.findAllActiveStockIo();
    }

    @Override
    public void saveStockIo(StockIo stockIo) {
        this.stockIoRepository.save(stockIo);
    }

    @Override
    public StockIo getStockIoById(long id) {
        Optional<StockIo> optional = stockIoRepository.findById(id);
        return optional.orElse(null); // Returns null if StockIo is not found
    }

    @Override
    @Transactional(readOnly = true)
    public int getNextIoId() {
        Integer maxIoId = stockIoRepository.findMaxIoId();
        return maxIoId == null ? 1 : maxIoId + 1;
    }

    @Override
    public void deleteStockIoById(long id) {
        Optional<StockIo> optionalStockIo = stockIoRepository.findById(id);
        if (optionalStockIo.isPresent()) {
            StockIo stockIo = optionalStockIo.get();
            stockIo.setDelFlg(1);
            stockIoRepository.save(stockIo);
        } else {
            throw new EntityNotFoundException("StockIo not found with id: " + id);
        }
    }

    @Override
    public boolean existsByStock_StockId(Long stockId) {
        return stockRepository.existsByStock_StockId(stockId);
    }

    @Override
    public boolean matchesStockName(Long stockId, String stockName) {
        return stockRepository.existsByStockIdAndStockName(stockId, stockName);
    }

    @Override
    public boolean matchesStockUnit(Long stockId, Long unitId) {
        return stockRepository.existsByStockIdAndUnitId(stockId, unitId);
    }

    @Override
    public List<StockIo> getStockIoByStockId(long stockId) {
        return stockIoRepository.findActiveStockIoByStockId(stockId);
    }


}