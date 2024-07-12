package com.example.stockmanagementsystem.service;


import com.example.stockmanagementsystem.model.Unit;

import java.util.List;
import java.util.Optional;

public interface UnitService{
    List<Unit> findAllUnit();
    String getUnitNameById(Long unitId);
    Optional<Long> getUnitIdByName(String unitName);
}