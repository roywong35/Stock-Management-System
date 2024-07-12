package com.example.stockmanagementsystem.repository;

import com.example.stockmanagementsystem.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("SELECT MAX(s.stockId) FROM Stock s")
    Integer findMaxStockId();

    @Query("SELECT s FROM Stock s WHERE s.delFlg = 0")
    List<Stock> findAllActiveStocks();

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Stock s WHERE s.stockId = :stockId AND s.delFlg = 0")
    boolean existsByStock_StockId(Long stockId);
    // existsById is used for primary key only
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Stock s WHERE s.stockId = :stockId AND s.stockName = :stockName AND s.delFlg = 0")
    boolean existsByStockIdAndStockName(@Param("stockId") Long stockId, @Param("stockName") String stockName);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Stock s WHERE s.stockId = :stockId AND s.unit.unitId = :unitId AND s.delFlg = 0")
    boolean existsByStockIdAndUnitId(@Param("stockId") Long stockId, @Param("unitId") Long unitId);


}