package br.com.rfasioli.mercadolivro.service

import br.com.rfasioli.mercadolivro.UnitTest
import br.com.rfasioli.mercadolivro.enums.CustomerStatus
import br.com.rfasioli.mercadolivro.exception.CustomerNotActiveException
import br.com.rfasioli.mercadolivro.exception.CustomerNotFoundException
import br.com.rfasioli.mercadolivro.mock.buildCustomer
import br.com.rfasioli.mercadolivro.model.CustomerModel
import br.com.rfasioli.mercadolivro.repository.CustomerRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.Optional
import java.util.UUID
import kotlin.random.Random

class CustomerServiceTest : UnitTest {

    @MockK
    private lateinit var customerRepository: CustomerRepository

    @MockK
    lateinit var bookService: BookService

    @MockK
    private lateinit var bcrypt: BCryptPasswordEncoder

    @InjectMockKs
    @SpyK
    private lateinit var customerService: CustomerService

    @Test
    fun `should return all customers`() {

        val pageable = PageRequest.of(0, 10)
        val fakeCustomers = listOf(CustomerModel.buildCustomer(), CustomerModel.buildCustomer())

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
        val fakeCustomers = listOf(CustomerModel.buildCustomer(), CustomerModel.buildCustomer())
        val name = "abc"

        every { customerRepository.findByNameContaining(any(), pageable) } returns
            PageImpl(fakeCustomers, pageable, fakeCustomers.size.toLong())

        val customers = customerService.getAllCustomer(name, pageable)

        assertEquals(fakeCustomers, customers.content)
        verify(exactly = 0) { customerRepository.findAll(pageable) }
        verify(exactly = 1) { customerRepository.findByNameContaining(name, pageable) }
    }

    @Test
    fun `should create customer with encrypted password`() {
        val fakeCustomer = CustomerModel.buildCustomer()
        val initialPassword = fakeCustomer.password
        val fakePassword = UUID.randomUUID().toString()
        val fakeCustomerEncrypted = fakeCustomer.copy(password = fakePassword)

        every { customerRepository.save(fakeCustomerEncrypted) } returns fakeCustomer
        every { bcrypt.encode(initialPassword) } returns fakePassword

        customerService.createCustomer(fakeCustomer)

        verify(exactly = 1) { customerRepository.save(fakeCustomerEncrypted) }
        verify(exactly = 1) { bcrypt.encode(initialPassword) }
    }

    @Test
    fun `shouldReturnCustomerById`() {
        val customerId = Random.nextInt()
        val fakeCustomer = CustomerModel.buildCustomer(customerId)

        every { customerRepository.findById(customerId) } returns Optional.of(fakeCustomer)

        val customer = customerService.getCustomer(customerId)

        assertEquals(fakeCustomer, customer)
        verify(exactly = 1) { customerRepository.findById(customerId) }
    }

    @Test
    fun `should throw error when customer not found`() {
        val customerId = Random.nextInt()

        every { customerRepository.findById(customerId) } returns Optional.empty()

        assertThrows<CustomerNotFoundException> { customerService.getCustomer(customerId) }

        verify(exactly = 1) { customerRepository.findById(customerId) }
    }

    @Test
    fun `should return customer by id when status active`() {
        val customerId = Random.nextInt()
        val fakeCustomer = CustomerModel.buildCustomer(id = customerId, status = CustomerStatus.ACTIVE)

        every { customerRepository.findById(customerId) } returns Optional.of(fakeCustomer)

        val customer = customerService.getActiveCustomer(customerId)

        assertEquals(fakeCustomer, customer)
        verify(exactly = 1) { customerRepository.findById(customerId) }
    }

    @ParameterizedTest
    @EnumSource(value = CustomerStatus::class, names = ["ACTIVE"], mode = EnumSource.Mode.EXCLUDE)
    fun `should throw not active exception when customer in diferent status`(
        status: CustomerStatus
    ) {
        val customerId = Random.nextInt()
        val fakeCustomer = CustomerModel.buildCustomer(id = customerId, status = status)

        every { customerRepository.findById(customerId) } returns Optional.of(fakeCustomer)

        assertThrows<CustomerNotActiveException> { customerService.getActiveCustomer(customerId) }

        verify(exactly = 1) { customerRepository.findById(customerId) }
    }

    @Test
    fun `should throw exception when customer not found on get active customer`() {
        val customerId = Random.nextInt()

        every { customerRepository.findById(customerId) } returns Optional.empty()

        assertThrows<CustomerNotFoundException> { customerService.getActiveCustomer(customerId) }

        verify(exactly = 1) { customerRepository.findById(customerId) }
    }

    @Test
    fun `should update customer `() {
        val customerId = Random.nextInt()
        val fakeCustomer = CustomerModel.buildCustomer(customerId)

        every { customerRepository.existsById(customerId) } returns true
        every { customerRepository.save(fakeCustomer) } returns fakeCustomer

        customerService.updateCustomer(fakeCustomer)

        verify(exactly = 1) { customerRepository.existsById(customerId) }
        verify(exactly = 1) { customerRepository.save(fakeCustomer) }
    }

    @Test
    fun `should throw not found exception when update invalid customer `() {
        val customerId = Random.nextInt()
        val fakeCustomer = CustomerModel.buildCustomer(customerId)

        every { customerRepository.existsById(customerId) } returns false

        assertThrows<CustomerNotFoundException> { customerService.updateCustomer(fakeCustomer) }

        verify(exactly = 1) { customerRepository.existsById(customerId) }
        verify(exactly = 0) { customerRepository.save(any()) }
    }

    @Test
    fun `should change customer status do inactive when delete customer`() {
        val customerId = Random.nextInt()
        val fakeCustomer = CustomerModel.buildCustomer(customerId)
        val expectedCustomer = fakeCustomer.copy(status = CustomerStatus.INACTIVE)

        every { customerService.getCustomer(customerId) } returns fakeCustomer
        every { bookService.deleteByCustomer(fakeCustomer) } just runs
        every { customerRepository.save(expectedCustomer) } returns expectedCustomer

        customerService.deleteCustomer(customerId)

        verify(exactly = 1) { customerService.getCustomer(customerId) }
        verify(exactly = 1) { bookService.deleteByCustomer(fakeCustomer) }
        verify(exactly = 1) { customerRepository.save(expectedCustomer) }
    }

    @Test
    fun `should throw not found exception when delete customer`() {
        val customerId = Random.nextInt()

        every { customerService.getCustomer(customerId) } throws CustomerNotFoundException(customerId)

        assertThrows<CustomerNotFoundException> { customerService.deleteCustomer(customerId) }

        verify(exactly = 1) { customerService.getCustomer(customerId) }
        verify(exactly = 0) { bookService.deleteByCustomer(any()) }
        verify(exactly = 0) { customerRepository.save(any()) }
    }

    @ParameterizedTest
    @CsvSource(*["true", "false"])
    fun `should verify if email is available`(
        exists: Boolean
    ) {
        val fakeEmail = UUID.randomUUID().toString()
        val expected = !exists

        every { customerRepository.existsByEmail(fakeEmail) } returns exists

        val result = customerService.emailAvailable(fakeEmail)

        verify(exactly = 1) { customerRepository.existsByEmail(fakeEmail) }
        assertEquals(expected, result)
    }
}
