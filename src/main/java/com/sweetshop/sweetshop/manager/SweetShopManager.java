// src/main/java/com/sweetshop/manager/SweetShopManager.java
package com.sweetshop.sweetshop.manager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sweetshop.sweetshop.model.Sweet;

public class SweetShopManager {
    private Map<String, Sweet> sweets; // Using a Map for efficient ID-based access

    public SweetShopManager() {
        this.sweets = new HashMap<>();
    }

    /**
     * Adds a new sweet to the shop.
     *
     * @param sweet The sweet object to add.
     * @throws IllegalArgumentException if the sweet is null or a sweet with the
     *                                  same ID already exists.
     */
    public void addSweet(Sweet sweet) {
        if (sweet == null) {
            throw new IllegalArgumentException("Sweet cannot be null.");
        }
        if (sweets.containsKey(sweet.getId())) {
            throw new IllegalArgumentException("Sweet with ID " + sweet.getId() + " already exists.");
        }
        sweets.put(sweet.getId(), sweet);
    }

    /**
     * Retrieves a sweet by its ID.
     *
     * @param id The ID of the sweet.
     * @return The Sweet object if found, otherwise null.
     */
    public Sweet getSweetById(String id) {
        return sweets.get(id);
    }

    /**
     * Returns an unmodifiable list of all sweets currently in the shop.
     *
     * @return A list of all sweets.
     */
    public List<Sweet> getAllSweets() {
        return new ArrayList<>(sweets.values());
    }

    // --- Other operations will be added here following TDD ---

    // ... inside SweetShopManager class ...

    /**
     * Deletes a sweet from the shop by its ID.
     *
     * @param id The ID of the sweet to delete.
     * @throws IllegalArgumentException if the ID is null or empty.
     */
    public void deleteSweet(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Sweet ID cannot be null or empty for deletion.");
        }
        sweets.remove(id);
    }

    // ... rest of the SweetShopManager class ...
    // ... inside SweetShopManager class ...

    /**
     * Updates the details of an existing sweet.
     * The ID of the sweet to update and the ID in the updatedSweet object must
     * match.
     *
     * @param id           The ID of the sweet to update.
     * @param updatedSweet The sweet object containing the new details.
     * @throws IllegalArgumentException if the ID is null/empty, updatedSweet is
     *                                  null,
     *                                  the sweet to update does not exist, or the
     *                                  IDs don't match.
     */
    public void updateSweet(String id, Sweet updatedSweet) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Sweet ID cannot be null or empty for update.");
        }
        if (updatedSweet == null) {
            throw new IllegalArgumentException("Updated sweet details cannot be null.");
        }
        if (!sweets.containsKey(id)) {
            throw new IllegalArgumentException("Sweet with ID " + id + " does not exist.");
        }
        if (!id.equals(updatedSweet.getId())) {
            throw new IllegalArgumentException("ID mismatch: provided ID (" + id
                    + ") does not match updated sweet's ID (" + updatedSweet.getId() + ").");
        }

        Sweet existingSweet = sweets.get(id);
        existingSweet.setName(updatedSweet.getName());
        existingSweet.setCategory(updatedSweet.getCategory());
        existingSweet.setPrice(updatedSweet.getPrice());
        existingSweet.setQuantity(updatedSweet.getQuantity());
        // No need to put it back into the map if we updated the reference directly.
        // sweets.put(id, updatedSweet); // This would replace the object, which is also
        // fine.
    }

    // ... rest of the SweetShopManager class ...

    // ... inside SweetShopManager class ...

    /**
     * Searches for sweets based on provided criteria.
     *
     * @param searchTerm The term to search for in name or category.
     *                   Case-insensitive, partial match for name.
     *                   Can be null or empty if not searching by name/category.
     * @param searchBy   "name", "category", or null/empty if not searching by
     *                   name/category.
     * @param minPrice   Minimum price for the range. Set to 0 or less to ignore.
     * @param maxPrice   Maximum price for the range. Set to 0 or less to ignore.
     * @return A list of sweets matching the criteria.
     */
    public List<Sweet> searchSweets(String searchTerm, String searchBy, double minPrice, double maxPrice) {
        List<Sweet> results = new ArrayList<>(sweets.values());

        // Normalize searchTerm and searchBy for easier comparison
        final String lowerCaseSearchTerm = (searchTerm != null) ? searchTerm.trim().toLowerCase() : null;
        final String lowerCaseSearchBy = (searchBy != null) ? searchBy.trim().toLowerCase() : null;

        // Determine if specific text search is active
        final boolean isTextSearchActive = lowerCaseSearchTerm != null && !lowerCaseSearchTerm.isEmpty();
        final boolean isNameSearch = isTextSearchActive && "name".equals(lowerCaseSearchBy);
        final boolean isCategorySearch = isTextSearchActive && "category".equals(lowerCaseSearchBy);

        // Determine if price search is active
        final boolean isPriceSearchActive = minPrice > 0 || maxPrice > 0;

        // If no search criteria are provided, return all sweets
        if (!isTextSearchActive && !isPriceSearchActive) {
            return results;
        }

        return results.stream()
                .filter(sweet -> {
                    boolean textMatches = true; // Assume true if no text search is active or conditions met

                    if (isNameSearch) {
                        textMatches = sweet.getName().toLowerCase().contains(lowerCaseSearchTerm);
                    } else if (isCategorySearch) {
                        textMatches = sweet.getCategory().toLowerCase().equals(lowerCaseSearchTerm);
                    }
                    // If isTextSearchActive is true but neither isNameSearch nor isCategorySearch
                    // are true (e.g., searchBy is "invalid" or null), then textMatches remains
                    // true,
                    // effectively ignoring the searchTerm for text search, which is consistent
                    // with your default case for `return results` when no criteria.
                    // This behavior is handled by the `if (!isTextSearchActive)` check above.

                    boolean priceMatches = true; // Assume true if no price search is active
                    if (isPriceSearchActive) {
                        if (minPrice > 0 && sweet.getPrice() < minPrice) {
                            priceMatches = false;
                        }
                        if (maxPrice > 0 && sweet.getPrice() > maxPrice) {
                            priceMatches = false;
                        }
                    }

                    // A sweet matches if both its text criteria (if any) AND its price criteria (if
                    // any) are met.
                    return textMatches && priceMatches;
                })
                .collect(Collectors.toList());
    }

    public List<Sweet> sortSweets(String sortBy, String order) {
        List<Sweet> sortedList = new ArrayList<>(sweets.values());

        Comparator<Sweet> comparator = null;

        switch (sortBy.toLowerCase()) {
            case "name":
                comparator = Comparator.comparing(Sweet::getName);
                break;
            case "price":
                comparator = Comparator.comparingDouble(Sweet::getPrice);
                break;
            case "quantity":
                comparator = Comparator.comparingInt(Sweet::getQuantity);
                break;
            default:
                // If invalid sortBy, return unsorted list.
                return sortedList;
        }

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        sortedList.sort(comparator);
        return sortedList;
    }

    // ... rest of the SweetShopManager class ...
    // ... inside SweetShopManager class ...

    /**
     * Processes a purchase of sweets, decreasing the quantity in stock.
     *
     * @param id       The ID of the sweet to purchase.
     * @param quantity The quantity to purchase.
     * @throws IllegalArgumentException if the sweet does not exist,
     *                                  quantity is invalid (<= 0), or not enough
     *                                  stock is available.
     */
    public void purchaseSweet(String id, int quantity) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Sweet ID cannot be null or empty for purchase.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Purchase quantity must be positive.");
        }

        Sweet sweet = sweets.get(id);
        if (sweet == null) {
            throw new IllegalArgumentException("Sweet with ID " + id + " not found.");
        }

        if (sweet.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough stock for " + sweet.getName() +
                    ". Available: " + sweet.getQuantity() + ", Requested: " + quantity);
        }

        sweet.setQuantity(sweet.getQuantity() - quantity);
    }

    // ... rest of the SweetShopManager class ...
    // ... inside SweetShopManager class ...

    /**
     * Restocks a sweet, increasing its quantity in stock.
     *
     * @param id       The ID of the sweet to restock.
     * @param quantity The quantity to add to stock.
     * @throws IllegalArgumentException if the sweet does not exist or quantity is
     *                                  invalid (<= 0).
     */
    public void restockSweet(String id, int quantity) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Sweet ID cannot be null or empty for restock.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Restock quantity must be positive.");
        }

        Sweet sweet = sweets.get(id);
        if (sweet == null) {
            throw new IllegalArgumentException("Sweet with ID " + id + " not found.");
        }

        sweet.setQuantity(sweet.getQuantity() + quantity);
    }

    // ... rest of the SweetShopManager class ...
}