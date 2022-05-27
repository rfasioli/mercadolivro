package br.com.rfasioli.mercadolivro.service

import br.com.rfasioli.mercadolivro.UnitTest
import br.com.rfasioli.mercadolivro.enums.CustomerStatus
import br.com.rfasioli.mercadolivro.enums.Role
import br.com.rfasioli.mercadolivro.model.CustomerModel
import br.com.rfasioli.mercadolivro.repository.CustomerRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.UUID

class CustomerServiceTest : UnitTest {

    @MockK
    private lateinit var customerRepository: CustomerRepository

    @MockK
    lateinit var bookService: BookService

    @MockK
    private lateinit var bcrypt: BCryptPasswordEncoder

    @InjectMockKs
    private lateinit var customerService: CustomerService

    @Test
    fun `should return all customers`() {

        val pageable = PageRequest.of(0, 10)
        val fakeCustomers = listOf(buildCustomer(), buildCustomer())

        every { customerRepository.findAll(pageable) } returns
            PageImpl(fakeCustomers, pageable, fakeCustomers.size.toLong())

        val customers = customerService.getAllCustomer(null, pageable)

        assertEquals(fakeCustomers, customers.content)
        verify(exactly = 1) { customerRepository.findAll(pageable) }
        verify(exactly = 0) { customerRepository.findByNameContaining(any(), any()) }
    }

    @Test
    fun `should return all customers containing name part`() {

        val pageable = PageRequest.of(0, 10)
        val fakeCustomers = listOf(buildCustomer(), buildCustomer())
        val name = "abc"

        every { customerRepository.findByNameContaining(any(), pageable) } returns
            PageImpl(fakeCustomers, pageable, fakeCustomers.size.toLong())

        val customers = customerService.getAllCustomer(name, pageable)

        assertEquals(fakeCustomers, customers.content)
        verify(exactly = 0) { customerRepository.findAll(pageable) }
        verify(exactly = 1) { customerRepository.findByNameContaining(name, pageable) }
    }

    fun buildCustomer(
        id: Int? = null,
        name: String = "Customer Name",
        email: String = "${UUID.randomUUID()}@email.com",
        password: String = "password"
    ): CustomerModel =
        CustomerModel(
            id = id,
            name = name,
            email = email,
            status = CustomerStatus.ACTIVE,
            password = password,
            roles = setOf(Role.CUSTOMER)
        )
}
