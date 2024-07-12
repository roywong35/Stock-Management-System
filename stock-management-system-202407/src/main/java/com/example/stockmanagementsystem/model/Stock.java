package com.example.stockmanagementsystem.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "t_stock_team1",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"stock_name", "unit_id"})})
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private Long stockId;

    @Column(name = "stock_name", nullable = false)
    private String stockName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unit_id", referencedColumnName = "unit_id", nullable = false)
    private Unit unit;

    @Column(name = "stock_num", nullable = false, precision = 10, scale = 1)
    private BigDecimal stockNum = BigDecimal.ZERO;

    @Column(name = "remarks", length = 200)
    private String remarks;

    @Column(name = "del_flg", nullable = false)
    private Integer delFlg = 0; // Default to 0

    // Constructors, getters, and setters

    public Stock() {
    }

    public Stock(Long stockId, String stockName, Unit unit, BigDecimal stockNum, String remarks, Integer delFlg) {
        this.stockId = stockId;
        this.stockName = stockName;
        this.unit = unit;
        this.stockNum = stockNum;
        this.remarks = remarks;
        this.delFlg = delFlg;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public BigDecimal getStockNum() {
        return stockNum;
    }

    public void setStockNum(BigDecimal stockNum) {
        this.stockNum = stockNum;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }
}
