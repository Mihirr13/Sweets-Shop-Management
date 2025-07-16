package com.sweetshop.sweetshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sweetshop.sweetshop.manager.SweetShopManager;
import com.sweetshop.sweetshop.model.Sweet;

@RestController
@RequestMapping("/api/sweets")
public class SweetController {

    @Autowired
    private SweetShopManager manager;

    // Add a new sweet
    @PostMapping
    public void addSweet(@RequestBody Sweet sweet) {
        manager.addSweet(sweet);
    }

    // Delete a sweet by ID
    @DeleteMapping("/{id}")
    public void deleteSweet(@PathVariable String id) {
        manager.deleteSweet(id);
    }

    // View all sweets
    @GetMapping
    public List<Sweet> getAllSweets() {
        return manager.getAllSweets();
    }

    // Search sweets by name/category or price range
    @GetMapping("/search")
    public List<Sweet> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String criteria,
            @RequestParam(required = false, defaultValue = "0") double minPrice,
            @RequestParam(required = false, defaultValue = "0") double maxPrice
    ) {
        return manager.searchSweets(keyword, criteria, minPrice, maxPrice);
    }

    // Sort sweets
    @GetMapping("/sort")
    public List<Sweet> sort(
            @RequestParam(defaultValue = "name") String by,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return manager.sortSweets(by, direction);
    }

    // Purchase sweets
    @PostMapping("/{id}/purchase")
    public void purchaseSweet(@PathVariable String id, @RequestParam int quantity) {
        manager.purchaseSweet(id, quantity);
    }

    // Restock sweets
    @PostMapping("/{id}/restock")
    public void restockSweet(@PathVariable String id, @RequestParam int quantity) {
        manager.restockSweet(id, quantity);
    }
}
