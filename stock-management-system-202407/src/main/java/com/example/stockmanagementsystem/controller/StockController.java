package com.example.stockmanagementsystem.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.stockmanagementsystem.model.Stock;
import com.example.stockmanagementsystem.model.StockIo;
import com.example.stockmanagementsystem.model.Unit;
import com.example.stockmanagementsystem.service.StockIoService;
import com.example.stockmanagementsystem.service.StockService;
import com.example.stockmanagementsystem.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class StockController {

    private final StockService stockService;
    private final UnitService unitService;
    private final StockIoService stockIoService;

    @Autowired
    public StockController(StockService stockService, UnitService unitService, StockIoService stockIoService) {
        this.stockService = stockService;
        this.unitService = unitService;
        this.stockIoService = stockIoService;
    }

    @GetMapping("/")
    public String redirectToStock() {
        return "redirect:/stock";
    }

    // Display list of stocks
    @GetMapping("/stock")
    public String getAllStock(Model model) {
        List<Stock> stocks = stockService.getAllStock();
        List<Unit> units = unitService.findAllUnit();
        Map<Long, String> unitMap = units.stream()
                .collect(Collectors.toMap(Unit::getUnitId, Unit::getUnitName));

        // Update stock numbers before displaying
        for (Stock stock : stocks) {
            stockService.updateStockNum(stock.getStockId());
        }

        model.addAttribute("stocks", stocks);
        model.addAttribute("unitMap", unitMap);
        return "stock"; // Assuming "stock" is your stock page template
    }

    // Method to show the edit stock form
    @GetMapping("/showEditStockForm")
    public String showEditStockForm(@RequestParam(value = "id", required = false) Long id, Model model) {
        Stock stock = null;
        if (id != null) {
            stock = stockService.getStockById(id);
            if (stock == null) {
                throw new RuntimeException("Stock with id " + id + " not found.");
            }
        } else {
            stock = new Stock(); // Create a new Stock object for the form
            // Set the next available stockId dynamically
            long nextStockId = stockService.getNextStockId();
            stock.setStockId(nextStockId);
        }
        model.addAttribute("stock", stock);
        return "edit_stock"; // Name of your Thymeleaf template
    }


    // Method to save stock details
    @PostMapping("/saveStock")
    public ResponseEntity<String> saveStock(@ModelAttribute("stock") Stock stock) {
        try {
            stockService.saveStock(stock);
            stockService.updateStockNum(stock.getStockId());
            return ResponseEntity.ok("在庫が正常に保存されました");
        } catch (DataIntegrityViolationException e) {
            // Handle unique constraint violation and format the message
            String unitName = unitService.getUnitNameById(stock.getUnit().getUnitId());
            String errorMessage = "重複エラー: 在庫名 '"
                    + stock.getStockName() + "' と単位 '"
                    + unitName + "' は既に存在します。";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
        } catch (Exception e) {
            // Handle other exceptions
            String errorMessage = "在庫の保存エラー: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }


    @DeleteMapping("/deleteStock/{stockId}")
    public ResponseEntity<String> deleteStock(@PathVariable(value = "stockId") long stockId) {
        try {
            // Check if there are any StockIo entries referencing this Stock
            List<StockIo> relatedStockIoList = stockIoService.getStockIoByStockId(stockId);

            if (!relatedStockIoList.isEmpty()) {
                return ResponseEntity.status(400).body("在庫を削除できません。入出庫情報に参照されています。");
            }

            // Proceed with deleting the Stock by setting delFlg
            Stock stock = stockService.getStockById(stockId);

            if (stock != null) {
                stock.setDelFlg(1);
                stockService.saveStock(stock); // Assuming you have a save method in your service
                return ResponseEntity.ok("在庫が正常に削除されました。");
            } else {
                return ResponseEntity.status(404).body("在庫が見つかりません。");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("在庫を削除中にエラーが発生しました。");
        }
    }


}
