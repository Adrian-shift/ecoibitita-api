package com.ecoibitita.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApiApplicationTests {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	void testDatabaseConnection() {
		assertNotNull(dataSource, "DataSource não deve ser nulo");
		assertNotNull(jdbcTemplate, "JdbcTemplate não deve ser nulo");

		// Testa uma query simples para verificar conexão
		Integer result = jdbcTemplate.queryForObject(
			"SELECT 1",
			Integer.class
		);

		assertEquals(1, result, "Conexão com banco de dados falhou");
		System.out.println("✅ Conexão com PostgreSQL estabelecida com sucesso!");
	}

	@Test
	void testDatabaseTables() {
		// Verifica se as tabelas existem
		Integer tableCount = jdbcTemplate.queryForObject(
			"SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public'",
			Integer.class
		);
		
		System.out.println("✅ Tabelas encontradas no banco: " + tableCount);
		assertTrue(tableCount > 0, "Banco deve ter tabelas");
	}

}
