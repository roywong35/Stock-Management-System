package com.example.stockmanagementsystem.repository;

import com.example.stockmanagementsystem.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long>{

    @Query("SELECT u.unitId FROM Unit u WHERE u.unitName = :unitName")
    Optional<Long> findIdByUnitName(@Param("unitName") String unitName);
}