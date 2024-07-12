package com.example.stockmanagementsystem.service;

import java.util.List;
import java.util.Optional;

import com.example.stockmanagementsystem.model.Unit;
import com.example.stockmanagementsystem.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnitServiceImpl implements UnitService {

    private final UnitRepository unitRepository;

    @Autowired
    public UnitServiceImpl(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    @Override
    public List<Unit> findAllUnit() {
        return unitRepository.findAll();
    }

    @Override
    public String getUnitNameById(Long unitId) {
        return unitRepository.findById(unitId)
                .map(Unit::getUnitName)
                .orElse("Unknown Unit");
    }
    @Override
    public Optional<Long> getUnitIdByName(String unitName) {
        return unitRepository.findIdByUnitName(unitName);
    }
}