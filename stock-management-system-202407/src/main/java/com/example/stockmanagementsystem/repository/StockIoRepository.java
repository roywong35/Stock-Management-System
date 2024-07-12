package com.example.stockmanagementsystem.repository;

import com.example.stockmanagementsystem.model.StockIo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface StockIoRepository extends JpaRepository<StockIo, Long> {

    @Query("SELECT COALESCE(MAX(io.ioId), 0) FROM StockIo io")
    Integer findMaxIoId();

    @Query("SELECT s FROM StockIo s WHERE s.delFlg = 0")
    List<StockIo> findAllActiveStockIo();


    @Query("SELECT SUM(CASE " +
            "WHEN io.ioType = 1 THEN io.ioNum " +
            "WHEN io.ioType = 2 THEN -io.ioNum " +
            "WHEN io.ioType = 3 THEN io.ioNum " +
            "WHEN io.ioType = 4 THEN -io.ioNum " +
            "WHEN io.ioType = 5 THEN -io.ioNum " +
            "WHEN io.ioType = 6 THEN io.ioNum " +
            "ELSE 0 END) " +
            "FROM StockIo io WHERE io.stock.id = :stockId AND io.delFlg = 0")
    BigDecimal sumIoNumByStockId(@Param("stockId") Long stockId);

    @Query("SELECT s FROM StockIo s WHERE s.delFlg = 0 AND s.stock.id = :stockId")
    List<StockIo> findActiveStockIoByStockId(@Param("stockId") long stockId);
}
