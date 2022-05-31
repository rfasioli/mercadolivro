package br.com.rfasioli.mercadolivro.repository

import br.com.rfasioli.mercadolivro.IntegratedTest
import br.com.rfasioli.mercadolivro.mock.buildCustomer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import java.util.UUID

class CustomerRepositoryTest : IntegratedTest {
    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @BeforeEach
    fun setup() {
        customerRepository.deleteAll()
    }

    @Test
    fun `should return name containing`() {
        val expectedNames = arrayOf("Marcos", "Matheus")
        val unexpectedNames = arrayOf("Alex")

        val expectedCustomers = expectedNames.map { customerRepository.save(buildCustomer(name = it)) }
        unexpectedNames.forEach { customerRepository.save(buildCustomer(name = it)) }

        val customers = customerRepository.findByNameContaining("Ma", Pageable.unpaged())

        assertEquals(expectedCustomers, customers.content)
    }

    @Nested
    inner class ExistsByEmail {
        @Test
        fun `should return true when email exists`() {
            val expectedEmail = "${UUID.randomUUID()}@email.com"
            listOf(buildCustomer(email = expectedEmail), buildCustomer()).forEach { customerRepository.save(it) }
            assertTrue(customerRepository.existsByEmail(expectedEmail))
        }

        @Test
        fun `should return false when email not exists`() {
            val expectedEmail = "${UUID.randomUUID()}@email.com"
            listOf(buildCustomer(), buildCustomer()).forEach { customerRepository.save(it) }
            assertFalse(customerRepository.existsByEmail(expectedEmail))
        }
    }

    @Nested
    inner class FindByEmail {
        @Test
        fun `sould return customer when found by email`() {
            val expectedEmail = "${UUID.randomUUID()}@email.com"
            val expectedCustomer = buildCustomer(email = expectedEmail)

            listOf(expectedCustomer, buildCustomer())
                .forEach { customerRepository.save(it) }

            val customer = customerRepository.findByEmail(expectedEmail)

            assertThat(customer)
                .isNotNull
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedCustomer)
        }

        @Test
        fun `sould return empty when not found by email`() {
            val expectedEmail = "${UUID.randomUUID()}@email.com"
            listOf(buildCustomer(), buildCustomer()).forEach { customerRepository.save(it) }
            assertNull(customerRepository.findByEmail(expectedEmail))
        }
    }
}
