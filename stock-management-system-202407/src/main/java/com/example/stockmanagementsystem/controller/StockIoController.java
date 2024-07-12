package com.example.stockmanagementsystem.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.stockmanagementsystem.model.Stock;
import com.example.stockmanagementsystem.model.StockIo;
import com.example.stockmanagementsystem.model.Unit;
import com.example.stockmanagementsystem.service.StockIoService;
import com.example.stockmanagementsystem.service.StockService;
import com.example.stockmanagementsystem.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class StockIoController {

    private final StockIoService stockIoService;
    private final UnitService unitService;
    private final StockService stockService;

    @Autowired
    public StockIoController(StockIoService stockIoService, UnitService unitService, StockService stockService) {
        this.stockIoService = stockIoService;
        this.unitService = unitService;
        this.stockService = stockService;
    }

    // Display list of stocks
    @GetMapping("/stockio")
    public String getAllStockIo(Model model) {
        List<StockIo> stockIos = stockIoService.getAllStockIo();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<Map<String, Object>> stockIoData = stockIos.stream().map(stockIo -> {
            Map<String, Object> data = new HashMap<>();
            data.put("ioId", stockIo.getIoId());
            data.put("stock", stockIo.getStock());
            data.put("ioType", stockIo.getIoType());
            data.put("ioNum", stockIo.getIoNum());
            data.put("formattedIoDatetime", stockIo.getIoDatetime().format(formatter));
            data.put("remarks", stockIo.getRemarks());
            data.put("delFlg", stockIo.getDelFlg());
            return data;
        }).collect(Collectors.toList());

        model.addAttribute("stockIoData", stockIoData);
        return "stockio"; // Assuming "stockio" is your stock page template
    }

    @GetMapping("/showEditStockIoForm")
    public String showEditStockIoForm(
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "stockName", required = false) String stockName,
            @RequestParam(name = "unit", required = false) String unit,
            @RequestParam(name = "stockNum", required = false) BigDecimal stockNum,
            Model model) {

        StockIo stockIo = new StockIo();

        if (id != null) {
            try {
                Stock stock = stockService.getStockById(id);
                if (stock == null) {
                    throw new RuntimeException("Stock not found for id: " + id);
                }
                stockIo.setStock(stock);
            } catch (RuntimeException e) {
                throw new RuntimeException("Stock not found for id: " + id);
            }
        } else {
            Stock stock = new Stock();
            if (stockName != null) {
                stock.setStockName(stockName);
            }
            if (unit != null) {
                stock.setUnit(new Unit(unit)); // Assuming you have a constructor in Unit that takes a String
            }
            if (stockNum != null) {
                stock.setStockNum(stockNum); // Assuming stockNum is a field in the Stock entity
            }
            stockIo.setStock(stock);
        }

        // Set the next available ioId dynamically
        int nextIoId = stockIoService.getNextIoId();
        stockIo.setIoId(nextIoId);

        model.addAttribute("stockIo", stockIo);
        return "edit_stockIo";
    }


    // Method to save stock details
    @PostMapping("/saveStockIo")
    public ResponseEntity<String> saveStockIo(@ModelAttribute StockIo stockIo) {
        // Set the current date and time to ioDatetime
        stockIo.setIoDatetime(LocalDateTime.now());

        // Check if stock is null (should not be null due to hidden input)
        Stock stock = stockIo.getStock();
        if (stock != null) {
            System.out.println("Stock ID: " + stock.getStockId()); // Debug print to check stockId
        }

        // Save the StockIo object
        stockIoService.saveStockIo(stockIo);
        stockService.updateStockNum(stock != null ? stock.getStockId() : null);

        // Return a ResponseEntity with a success message or status code
        return ResponseEntity.ok("StockIo saved successfully");
    }




    @DeleteMapping("/deleteStockIo/{stockIoid}")
    public ResponseEntity<String> deleteStockIo(@PathVariable(value = "stockIoid") long stockIoid) {
        try {
            StockIo stockIo = stockIoService.getStockIoById(stockIoid);
            if (stockIo != null) {
                stockIo.setDelFlg(1);
                stockIoService.saveStockIo(stockIo);

                // Update the stock number after marking StockIo as deleted
                Stock stock = stockIo.getStock();
                stockService.updateStockNum(stock.getStockId());

                return ResponseEntity.ok("StockIo marked as deleted successfully.");
            } else {
                return ResponseEntity.status(404).body("StockIo not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error marking StockIo as deleted.");
        }
    }

    @GetMapping("/checkStockId/{stockId}")
    public ResponseEntity<Boolean> checkStockDetails(@PathVariable Long stockId) {
        boolean exists = stockIoService.existsByStock_StockId(stockId);

        if (exists) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/checkStockName/{stockId}/{stockName}")
    public ResponseEntity<Boolean> checkStockName(@PathVariable Long stockId, @PathVariable String stockName) {
        boolean matches = stockIoService.matchesStockName(stockId, stockName);
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/checkStockIdAndUnit/{stockId}/{unitId}")
    public ResponseEntity<Boolean> checkStockUnit(@PathVariable Long stockId, @PathVariable Long unitId) {
        boolean matches = stockIoService.matchesStockUnit(stockId, unitId);
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/getUnitIdByName/{unitName}")
    public ResponseEntity<Long> getUnitIdByName(@PathVariable String unitName) {
        Optional<Long> unitId = unitService.getUnitIdByName(unitName);
        return unitId.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


}
