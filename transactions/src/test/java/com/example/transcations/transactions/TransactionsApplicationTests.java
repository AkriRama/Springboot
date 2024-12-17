package com.example.transcations.transactions;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.transcations.transactions.repository.ProductRepository;
import com.example.transcations.transactions.repository.RoleRepository;
import com.example.transcations.transactions.repository.UserRepository;

@SpringBootTest
class TransactionsApplicationTests {
	private RoleRepository roleRepository;
	private UserRepository userRepository;

	@Autowired
	public TransactionsApplicationTests(RoleRepository roleRepository, UserRepository userRepository) {
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
	}

	@Autowired
	private ProductRepository productRepository;
	// private ProductRepository productRepository;

	// @Autowired
	// public TransactionsApplicationTests(ProductRepository productRepository) {
	// this.productRepository = productRepository;
	// }

	@Test
	void contextLoads() {
		// Arrange
		int total = 9;
		// Act
		int actual = productRepository.findAll().size();

		// Assert
		assertEquals(total, actual);
	}

	@Test
	void checkSizeOfProductRepository() {
		// Arrange
		int total = 9;
		// Act
		int actual = productRepository.findAll().size();

		// Assert
		assertEquals(total, actual);
	}

	@Test
	void checkNullOfProductRepository() {
		// Arrange
		boolean isEmpty = true;
		// Act
		boolean actual = productRepository.findAll().isEmpty();
		// Assert
		assertEquals(isEmpty, actual);
		// assertTrue(productRepository.findAll().isEmpty());
	}

	@Test
	void checkItemInProductRepositoryByID() {
		// Arrange
		String name = "Item 1";
		// Act
		// String actual = productRepository.findAll().get(0).getName();
		String actual = productRepository.findById(1).get().getName();

		// Assert
		assertEquals(name, actual);
		// assertTrue(productRepository.findAll().isEmpty());
	}

	@Test
	void checkItemInProductRepository() {
		// Arrange
		String name = "Item 12";
		int stock = 112;
		Float price = 120000f;
		// Act
		String actual = productRepository.findAll().get(0).getName();
		int actual1 = productRepository.findAll().get(0).getStock();
		Float actual2 = productRepository.findAll().get(0).getPrice();
		// Item 1
		// 10000.00
		// 11
		// Assert
		assertAll(
				"Group of Assert All",
				() -> assertEquals(name, actual),
				() -> assertEquals(stock, actual1),
				() -> assertEquals(price, actual2));
	}

	@Test
	void checkItemInProductRepository2() {
		// Arrange
		String[] name = { "Item 1", "Item 2" };

		// Act
		List<String> actual = Arrays.asList(productRepository.findAll().iterator().next().getName());
		String[] actual3 = Arrays.copyOf(actual.toArray(), actual.size(), String[].class);
		// String[] actual2 = actual.toArray(new String[actual.size()]);
		// Assert
		assertArrayEquals(name, actual3);
	}

	@Test
	void checkCountOfRole() {
		// Arrange
		long expetedCount = 3;

		// Act
		long actualCount = roleRepository.count();
		// Assert
		assertEquals(expetedCount, actualCount);
	}

	@Test
	void checkNameofRole() {
		// Arrange

		// Act
		long actualCount = roleRepository.count();
		// Assert
		assertEquals("admin", roleRepository.findById(1).get().getName());
	}

	@Test
	void checkNameofUser() {
		// Arrange

		// Act
		// Assert
		// assertEquals("Fahri", userRepository.findById(1).get().getName());
	}

	@Test
	void checkNameofUser2() {
		// Arrange

		// Act
		// Assert
		// assertEquals("Fahri", userRepository.get(1).getName());
	}

	@Test
	void checkNameofUserDTO() {
		// Arrange

		// Act
		// Assert
		// assertEquals("Fahri", userRepository.getUsingDTO(1).getName());
	}

	@Test
	void checkNameofUserDTOID() {
		// Arrange
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		// Act
		// Assert
		assertEquals("admin", userRepository.getUsingDTO("fahri@gmail.com").getRole());
		// assertTrue(encoder.matches("toor",
		// userRepository.getUsingDTO("fahri@gmail.com").getPassword()));
	}

}
