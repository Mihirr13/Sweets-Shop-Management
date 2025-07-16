package com.sweetshop.sweetshop.manager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sweetshop.sweetshop.model.Sweet;

@Service
public class SweetShopManager {

    private final Map<String, Sweet> sweetMap = new HashMap<>();

    public void addSweet(Sweet sweet) {
        if (sweet == null || sweet.getId() == null || sweet.getId().isBlank()) {
            throw new IllegalArgumentException("Sweet or Sweet ID cannot be null or blank.");
        }
        if (sweetMap.containsKey(sweet.getId())) {
            throw new IllegalArgumentException("Sweet with this ID already exists.");
        }
        sweetMap.put(sweet.getId(), sweet);
    }

    public void deleteSweet(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Sweet ID cannot be null or empty.");
        }
        sweetMap.remove(id);
    }

    public List<Sweet> getAllSweets() {
        return new ArrayList<>(sweetMap.values());
    }

    public Sweet getSweetById(String id) {
        return sweetMap.get(id);
    }

    public void updateSweet(String id, Sweet updatedSweet) {
        if (id == null || id.trim().isEmpty() || updatedSweet == null) {
            throw new IllegalArgumentException("Invalid ID or sweet.");
        }
        if (!sweetMap.containsKey(id)) {
            throw new IllegalArgumentException("Sweet not found.");
        }
        if (!id.equals(updatedSweet.getId())) {
            throw new IllegalArgumentException("Sweet ID mismatch.");
        }
        sweetMap.put(id, updatedSweet);
    }

    public List<Sweet> searchSweets(String keyword, String criteria, double minPrice, double maxPrice) {
        return sweetMap.values().stream()
                .filter(s -> {
                    boolean matches = true;

                    if (keyword != null && !keyword.trim().isEmpty()) {
                        if ("name".equalsIgnoreCase(criteria)) {
                            matches = s.getName().toLowerCase().contains(keyword.toLowerCase());
                        } else if ("category".equalsIgnoreCase(criteria)) {
                            matches = s.getCategory().equalsIgnoreCase(keyword);
                        }
                    }

                    if (minPrice > 0) matches &= s.getPrice() >= minPrice;
                    if (maxPrice > 0) matches &= s.getPrice() <= maxPrice;

                    return matches;
                })
                .collect(Collectors.toList());
    }

    public List<Sweet> sortSweets(String field, String direction) {
        Comparator<Sweet> comparator;

        switch (field.toLowerCase()) {
            case "name":
                comparator = Comparator.comparing(Sweet::getName, String.CASE_INSENSITIVE_ORDER);
                break;
            case "price":
                comparator = Comparator.comparingDouble(Sweet::getPrice);
                break;
            case "quantity":
                comparator = Comparator.comparingInt(Sweet::getQuantity);
                break;
            default:
                return getAllSweets(); // Invalid sort, return unsorted
        }

        if ("desc".equalsIgnoreCase(direction)) {
            comparator = comparator.reversed();
        }

        return sweetMap.values().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public void purchaseSweet(String id, int quantity) {
        Sweet sweet = sweetMap.get(id);
        if (sweet == null || quantity <= 0) {
            throw new IllegalArgumentException("Invalid sweet or quantity.");
        }
        if (sweet.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough stock.");
        }
        sweet.setQuantity(sweet.getQuantity() - quantity);
    }

    public void restockSweet(String id, int quantity) {
        Sweet sweet = sweetMap.get(id);
        if (sweet == null || quantity <= 0) {
            throw new IllegalArgumentException("Invalid sweet or quantity.");
        }
        sweet.setQuantity(sweet.getQuantity() + quantity);
    }
}
