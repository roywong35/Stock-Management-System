package com.example.stockmanagementsystem.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_stock_io_team1")
public class StockIo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "io_id")
    private int ioId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stock_id", referencedColumnName = "stock_id")
    private Stock stock;

    @Column(name = "io_type", nullable = false)
    private int ioType;

    @Column(name = "io_num", nullable = false, precision = 10, scale = 1)
    private BigDecimal ioNum;

    @Column(name = "io_datetime", nullable = false)
    private LocalDateTime ioDatetime;

    @Column(name = "remarks", length = 200)
    private String remarks;

    @Column(name = "del_flg", nullable = false)
    private int delFlg;

    public StockIo() {
    }

    public StockIo(int ioId, Stock stock, int ioType, BigDecimal ioNum, LocalDateTime ioDatetime, String remarks, int delFlg) {
        this.ioId = ioId;
        this.stock = stock;
        this.ioType = ioType;
        this.ioNum = ioNum;
        this.ioDatetime = ioDatetime;
        this.remarks = remarks;
        this.delFlg = delFlg;
    }

    public int getIoId() {
        return ioId;
    }

    public void setIoId(int ioId) {
        this.ioId = ioId;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public int getIoType() {
        return ioType;
    }

    public void setIoType(int ioType) {
        this.ioType = ioType;
    }

    public BigDecimal getIoNum() {
        return ioNum;
    }

    public void setIoNum(BigDecimal ioNum) {
        this.ioNum = ioNum;
    }

    public LocalDateTime getIoDatetime() {
        return ioDatetime;
    }

    public void setIoDatetime(LocalDateTime ioDatetime) {
        this.ioDatetime = ioDatetime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(int delFlg) {
        this.delFlg = delFlg;
    }
}
