package com.example.stockmanagementsystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "t_unit_team1")
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unit_id")
    private Long unitId;

    @Column(name = "unit_name", nullable = false)
    private String unitName;

    @Column(name = "del_flg", nullable = false)
    private Integer delFlg = 0; // Default to 0

    // No-arguments constructor
    public Unit() {}

    // Constructor with unitName
    public Unit(String unitName) {
        this.unitName = unitName;
    }

    // All-arguments constructor
    public Unit(Long unitId, String unitName, Integer delFlg) {
        this.unitId = unitId;
        this.unitName = unitName;
        this.delFlg = delFlg;
    }

    // Getters and Setters
    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }
}
