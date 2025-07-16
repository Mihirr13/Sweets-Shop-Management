// src/test/java/com/sweetshop/manager/SweetShopManagerTest.java
package com.sweetshop.sweetshop;

import com.sweetshop.sweetshop.manager.SweetShopManager;
import com.sweetshop.sweetshop.model.Sweet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class SweetShopManagerTest {

	private SweetShopManager manager;

	@BeforeEach
	void setUp() {
		manager = new SweetShopManager();
	}

	@Test
	@DisplayName("Should add a new sweet to the shop")
	void shouldAddSweet() {
		Sweet kajuKatli = new Sweet("1001", "Kaju Katli", "Nut-Based", 50.0, 20);
		manager.addSweet(kajuKatli);
		assertEquals(1, manager.getAllSweets().size());
		assertTrue(manager.getAllSweets().contains(kajuKatli));
	}

	@Test
	@DisplayName("Should not add a sweet with a duplicate ID")
	void shouldNotAddDuplicateSweetId() {
		Sweet kajuKatli1 = new Sweet("1001", "Kaju Katli", "Nut-Based", 50.0, 20);
		Sweet kajuKatli2 = new Sweet("1001", "Kaju Katli (Dup)", "Nut-Based", 55.0, 10);

		manager.addSweet(kajuKatli1);
		assertThrows(IllegalArgumentException.class, () -> manager.addSweet(kajuKatli2));
		assertEquals(1, manager.getAllSweets().size()); // Ensure no duplicate was added
	}

	@Test
	@DisplayName("Should throw IllegalArgumentException if sweet is null when adding")
	void shouldThrowExceptionWhenAddingNullSweet() {
		assertThrows(IllegalArgumentException.class, () -> manager.addSweet(null));
	}

	@Test
	@DisplayName("Should add multiple sweets correctly")
	void shouldAddMultipleSweets() {
		manager.addSweet(new Sweet("1001", "Kaju Katli", "Nut-Based", 50.0, 20));
		manager.addSweet(new Sweet("1002", "Gulab Jamun", "Milk-Based", 10.0, 50));
		assertEquals(2, manager.getAllSweets().size());
	}

	// ... inside SweetShopManagerTest class ...

	@Test
	@DisplayName("Should delete an existing sweet from the shop")
	void shouldDeleteSweet() {
		Sweet kajuKatli = new Sweet("1001", "Kaju Katli", "Nut-Based", 50.0, 20);
		manager.addSweet(kajuKatli);
		assertEquals(1, manager.getAllSweets().size());

		manager.deleteSweet("1001");
		assertEquals(0, manager.getAllSweets().size());
		assertNull(manager.getSweetById("1001"));
	}

	@Test
	@DisplayName("Should do nothing if trying to delete a non-existent sweet")
	void shouldDoNothingWhenDeletingNonExistentSweet() {
		Sweet kajuKatli = new Sweet("1001", "Kaju Katli", "Nut-Based", 50.0, 20);
		manager.addSweet(kajuKatli);
		assertEquals(1, manager.getAllSweets().size());

		manager.deleteSweet("9999"); // Non-existent ID
		assertEquals(1, manager.getAllSweets().size()); // Size should remain unchanged
	}

	@Test
	@DisplayName("Should throw IllegalArgumentException if ID is null or empty when deleting")
	void shouldThrowExceptionWhenDeletingWithNullOrEmptyId() {
		manager.addSweet(new Sweet("1001", "Kaju Katli", "Nut-Based", 50.0, 20));
		assertThrows(IllegalArgumentException.class, () -> manager.deleteSweet(null));
		assertThrows(IllegalArgumentException.class, () -> manager.deleteSweet(""));
		assertThrows(IllegalArgumentException.class, () -> manager.deleteSweet("   "));
		assertEquals(1, manager.getAllSweets().size()); // Ensure sweet is still there
	}

	// ... inside SweetShopManagerTest class ...

	@Test
	@DisplayName("Should update an existing sweet's details")
	void shouldUpdateSweetDetails() {
		Sweet kajuKatli = new Sweet("1001", "Kaju Katli", "Nut-Based", 50.0, 20);
		manager.addSweet(kajuKatli);

		Sweet updatedSweet = new Sweet("1001", "Kaju Katli Premium", "Nut-Based", 60.0, 25);
		manager.updateSweet("1001", updatedSweet);

		Sweet retrievedSweet = manager.getSweetById("1001");
		assertNotNull(retrievedSweet);
		assertEquals("Kaju Katli Premium", retrievedSweet.getName());
		assertEquals(60.0, retrievedSweet.getPrice());
		assertEquals(25, retrievedSweet.getQuantity());
	}

	@Test
	@DisplayName("Should throw IllegalArgumentException when updating a non-existent sweet")
	void shouldThrowExceptionWhenUpdatingNonExistentSweet() {
		Sweet updatedSweet = new Sweet("9999", "New Sweet", "Category", 100.0, 10);
		assertThrows(IllegalArgumentException.class, () -> manager.updateSweet("9999", updatedSweet));
	}

	@Test
	@DisplayName("Should throw IllegalArgumentException if ID is null or empty when updating")
	void shouldThrowExceptionWhenUpdatingWithNullOrEmptyId() {
		Sweet kajuKatli = new Sweet("1001", "Kaju Katli", "Nut-Based", 50.0, 20);
		manager.addSweet(kajuKatli);
		Sweet updatedSweet = new Sweet("1001", "Kaju Katli Premium", "Nut-Based", 60.0, 25);

		assertThrows(IllegalArgumentException.class, () -> manager.updateSweet(null, updatedSweet));
		assertThrows(IllegalArgumentException.class, () -> manager.updateSweet("", updatedSweet));
		assertThrows(IllegalArgumentException.class, () -> manager.updateSweet("   ", updatedSweet));
	}

	@Test
	@DisplayName("Should throw IllegalArgumentException if updated sweet object is null when updating")
	void shouldThrowExceptionWhenUpdatingWithNullSweetObject() {
		Sweet kajuKatli = new Sweet("1001", "Kaju Katli", "Nut-Based", 50.0, 20);
		manager.addSweet(kajuKatli);

		assertThrows(IllegalArgumentException.class, () -> manager.updateSweet("1001", null));
	}

	@Test
	@DisplayName("Should throw IllegalArgumentException if updated sweet ID does not match original ID")
	void shouldThrowExceptionIfUpdatedSweetIdMismatch() {
		Sweet kajuKatli = new Sweet("1001", "Kaju Katli", "Nut-Based", 50.0, 20);
		manager.addSweet(kajuKatli);
		Sweet updatedSweetWithDiffId = new Sweet("1002", "Kaju Katli Premium", "Nut-Based", 60.0, 25);

		assertThrows(IllegalArgumentException.class, () -> manager.updateSweet("1001", updatedSweetWithDiffId));
	}

	@Test
	@DisplayName("Should search sweets by name (case-insensitive, partial match)")
	void shouldSearchSweetsByName() {
		manager.addSweet(new Sweet("1001", "Kaju Katli", "Nut-Based", 50.0, 20));
		manager.addSweet(new Sweet("1002", "Gulab Jamun", "Milk-Based", 10.0, 50));
		manager.addSweet(new Sweet("1003", "Rasgulla", "Milk-Based", 12.0, 30));

		List<Sweet> results = manager.searchSweets("katli", "name", 0, 0); // Price range 0,0 means not searching by
																			// price
		assertEquals(1, results.size());
		assertEquals("Kaju Katli", results.get(0).getName());

		results = manager.searchSweets("jamun", "name", 0, 0);
		assertEquals(1, results.size());
		assertEquals("Gulab Jamun", results.get(0).getName());

		results = manager.searchSweets("kaju", "name", 0, 0);
		assertEquals(1, results.size());
		assertEquals("Kaju Katli", results.get(0).getName());
	}

	@Test
	@DisplayName("Should search sweets by category (case-insensitive)")
	void shouldSearchSweetsByCategory() {
		manager.addSweet(new Sweet("1001", "Kaju Katli", "Nut-Based", 50.0, 20));
		manager.addSweet(new Sweet("1002", "Gulab Jamun", "Milk-Based", 10.0, 50));
		manager.addSweet(new Sweet("1003", "Rasgulla", "Milk-Based", 12.0, 30));

		List<Sweet> results = manager.searchSweets("milk-based", "category", 0, 0);
		assertEquals(2, results.size());
		assertTrue(results.stream().anyMatch(s -> s.getName().equals("Gulab Jamun")));
		assertTrue(results.stream().anyMatch(s -> s.getName().equals("Rasgulla")));
	}

	@Test
	@DisplayName("Should search sweets by price range")
	void shouldSearchSweetsByPriceRange() {
		manager.addSweet(new Sweet("1001", "Kaju Katli", "Nut-Based", 50.0, 20));
		manager.addSweet(new Sweet("1002", "Gulab Jamun", "Milk-Based", 10.0, 50));
		manager.addSweet(new Sweet("1003", "Rasgulla", "Milk-Based", 12.0, 30));
		manager.addSweet(new Sweet("1004", "Mysore Pak", "Ghee-Based", 80.0, 10));

		List<Sweet> results = manager.searchSweets("", "", 10.0, 30.0); // Search by price only
		assertEquals(2, results.size());
		assertTrue(results.stream().anyMatch(s -> s.getName().equals("Gulab Jamun")));
		assertTrue(results.stream().anyMatch(s -> s.getName().equals("Rasgulla")));

		results = manager.searchSweets("", "", 50.0, 100.0);
		assertEquals(2, results.size());
		assertTrue(results.stream().anyMatch(s -> s.getName().equals("Kaju Katli")));
		assertTrue(results.stream().anyMatch(s -> s.getName().equals("Mysore Pak")));
	}

	@Test
	@DisplayName("Should search by name and price range combined")
	void shouldSearchSweetsByNameAndPriceRange() {
		manager.addSweet(new Sweet("1001", "Kaju Katli", "Nut-Based", 50.0, 20));
		manager.addSweet(new Sweet("1002", "Gulab Jamun", "Milk-Based", 10.0, 50));
		manager.addSweet(new Sweet("1003", "Rasgulla", "Milk-Based", 12.0, 30));
		manager.addSweet(new Sweet("1004", "Kaju Barfi", "Nut-Based", 45.0, 15));

		List<Sweet> results = manager.searchSweets("kaju", "name", 40.0, 50.0);
		assertEquals(2, results.size());
		assertTrue(results.stream().anyMatch(s -> s.getName().equals("Kaju Katli")));
		assertTrue(results.stream().anyMatch(s -> s.getName().equals("Kaju Barfi")));
	}

	@Test
	@DisplayName("Should return empty list if no search criteria given and no sweets")
	void shouldReturnEmptyListIfNoCriteriaAndNoSweets() {
		List<Sweet> results = manager.searchSweets("", "", 0, 0);
		assertTrue(results.isEmpty());
	}

	@Test
	@DisplayName("Should return all sweets if no search criteria given")
	void shouldReturnAllSweetsIfNoCriteria() {
		manager.addSweet(new Sweet("1001", "Kaju Katli", "Nut-Based", 50.0, 20));
		manager.addSweet(new Sweet("1002", "Gulab Jamun", "Milk-Based", 10.0, 50));
		List<Sweet> results = manager.searchSweets("", "", 0, 0);
		assertEquals(2, results.size());
	}

	@Test
	@DisplayName("Should handle empty search term for name/category gracefully")
	void shouldHandleEmptySearchTerm() {
		manager.addSweet(new Sweet("1001", "Kaju Katli", "Nut-Based", 50.0, 20));
		List<Sweet> results = manager.searchSweets("", "name", 0, 0);
		assertEquals(1, results.size());
		results = manager.searchSweets(null, "category", 0, 0);
		assertEquals(1, results.size());
	}

	// ... inside SweetShopManagerTest class ...

	@Test
	@DisplayName("Should sort sweets by name ascending")
	void shouldSortSweetsByNameAscending() {
		manager.addSweet(new Sweet("1002", "Gulab Jamun", "Milk-Based", 10.0, 50));
		manager.addSweet(new Sweet("1001", "Kaju Katli", "Nut-Based", 50.0, 20));
		manager.addSweet(new Sweet("1003", "Rasgulla", "Milk-Based", 12.0, 30));

		List<Sweet> sortedSweets = manager.sortSweets("name", "asc");
		assertEquals("Gulab Jamun", sortedSweets.get(0).getName());
		assertEquals("Kaju Katli", sortedSweets.get(1).getName());
		assertEquals("Rasgulla", sortedSweets.get(2).getName());
	}

	@Test
	@DisplayName("Should sort sweets by name descending")
	void shouldSortSweetsByNameDescending() {
		manager.addSweet(new Sweet("1002", "Gulab Jamun", "Milk-Based", 10.0, 50));
		manager.addSweet(new Sweet("1001", "Kaju Katli", "Nut-Based", 50.0, 20));
		manager.addSweet(new Sweet("1003", "Rasgulla", "Milk-Based", 12.0, 30));

		List<Sweet> sortedSweets = manager.sortSweets("name", "desc");
		assertEquals("Rasgulla", sortedSweets.get(0).getName());
		assertEquals("Kaju Katli", sortedSweets.get(1).getName());
		assertEquals("Gulab Jamun", sortedSweets.get(2).getName());
	}

	@Test
	@DisplayName("Should sort sweets by price ascending")
	void shouldSortSweetsByPriceAscending() {
		manager.addSweet(new Sweet("1001", "Kaju Katli", "Nut-Based", 50.0, 20));
		manager.addSweet(new Sweet("1002", "Gulab Jamun", "Milk-Based", 10.0, 50));
		manager.addSweet(new Sweet("1003", "Rasgulla", "Milk-Based", 12.0, 30));

		List<Sweet> sortedSweets = manager.sortSweets("price", "asc");
		assertEquals(10.0, sortedSweets.get(0).getPrice());
		assertEquals(12.0, sortedSweets.get(1).getPrice());
		assertEquals(50.0, sortedSweets.get(2).getPrice());
	}

	@Test
	@DisplayName("Should sort sweets by quantity descending")
	void shouldSortSweetsByQuantityDescending() {
		manager.addSweet(new Sweet("1001", "Kaju Katli", "Nut-Based", 50.0, 20));
		manager.addSweet(new Sweet("1002", "Gulab Jamun", "Milk-Based", 10.0, 50));
		manager.addSweet(new Sweet("1003", "Rasgulla", "Milk-Based", 12.0, 30));

		List<Sweet> sortedSweets = manager.sortSweets("quantity", "desc");
		assertEquals(50, sortedSweets.get(0).getQuantity());
		assertEquals(30, sortedSweets.get(1).getQuantity());
		assertEquals(20, sortedSweets.get(2).getQuantity());
	}

	@Test
	@DisplayName("Should return unsorted list if sort criteria is invalid")
	void shouldReturnUnsortedIfInvalidCriteria() {
		Sweet s1 = new Sweet("1001", "Kaju Katli", "Nut-Based", 50.0, 20);
		Sweet s2 = new Sweet("1002", "Gulab Jamun", "Milk-Based", 10.0, 50);
		manager.addSweet(s1);
		manager.addSweet(s2);

		List<Sweet> results = manager.sortSweets("invalid", "asc");
	
		List<Sweet> expectedUnsorted = List.of(s1, s2); 
		
		assertEquals(2, results.size());
		assertTrue(results.contains(s1));
		assertTrue(results.contains(s2));
	}

	@Test
	@DisplayName("Should decrease sweet quantity upon purchase")
	void shouldDecreaseQuantityOnPurchase() {
		Sweet kajuKatli = new Sweet("1001", "Kaju Katli", "Nut-Based", 50.0, 20);
		manager.addSweet(kajuKatli);

		manager.purchaseSweet("1001", 5);
		assertEquals(15, manager.getSweetById("1001").getQuantity());
	}

	@Test
	@DisplayName("Should throw IllegalArgumentException if not enough stock for purchase")
	void shouldThrowExceptionIfNotEnoughStockForPurchase() {
		Sweet kajuKatli = new Sweet("1001", "Kaju Katli", "Nut-Based", 50.0, 20);
		manager.addSweet(kajuKatli);

		assertThrows(IllegalArgumentException.class, () -> manager.purchaseSweet("1001", 25));
		assertEquals(20, manager.getSweetById("1001").getQuantity()); // Quantity should remain unchanged
	}

	@Test
	@DisplayName("Should throw IllegalArgumentException if purchasing non-existent sweet")
	void shouldThrowExceptionWhenPurchasingNonExistentSweet() {
		assertThrows(IllegalArgumentException.class, () -> manager.purchaseSweet("9999", 1));
	}

	@Test
	@DisplayName("Should throw IllegalArgumentException if purchase quantity is invalid")
	void shouldThrowExceptionIfPurchaseQuantityInvalid() {
		Sweet kajuKatli = new Sweet("1001", "Kaju Katli", "Nut-Based", 50.0, 20);
		manager.addSweet(kajuKatli);

		assertThrows(IllegalArgumentException.class, () -> manager.purchaseSweet("1001", 0));
		assertThrows(IllegalArgumentException.class, () -> manager.purchaseSweet("1001", -5));
		assertEquals(20, manager.getSweetById("1001").getQuantity()); // Quantity should remain unchanged
	}

	@Test
	@DisplayName("Should increase sweet quantity upon restock")
	void shouldIncreaseQuantityOnRestock() {
		Sweet kajuKatli = new Sweet("1001", "Kaju Katli", "Nut-Based", 50.0, 20);
		manager.addSweet(kajuKatli);

		manager.restockSweet("1001", 10);
		assertEquals(30, manager.getSweetById("1001").getQuantity());
	}

	@Test
	@DisplayName("Should throw IllegalArgumentException if restocking non-existent sweet")
	void shouldThrowExceptionWhenRestockingNonExistentSweet() {
		assertThrows(IllegalArgumentException.class, () -> manager.restockSweet("9999", 10));
	}

	@Test
	@DisplayName("Should throw IllegalArgumentException if restock quantity is invalid")
	void shouldThrowExceptionIfRestockQuantityInvalid() {
		Sweet kajuKatli = new Sweet("1001", "Kaju Katli", "Nut-Based", 50.0, 20);
		manager.addSweet(kajuKatli);

		assertThrows(IllegalArgumentException.class, () -> manager.restockSweet("1001", 0));
		assertThrows(IllegalArgumentException.class, () -> manager.restockSweet("1001", -5));
		assertEquals(20, manager.getSweetById("1001").getQuantity()); 
	}
}