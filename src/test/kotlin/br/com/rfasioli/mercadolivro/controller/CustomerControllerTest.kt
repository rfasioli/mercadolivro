package br.com.rfasioli.mercadolivro.controller

import br.com.rfasioli.mercadolivro.controller.request.PostCustomerRequest
import br.com.rfasioli.mercadolivro.controller.request.PutCustomerRequest
import br.com.rfasioli.mercadolivro.enums.CustomerStatus
import br.com.rfasioli.mercadolivro.mock.buildCustomer
import br.com.rfasioli.mercadolivro.model.CustomerModel
import br.com.rfasioli.mercadolivro.repository.CustomerRepository
import br.com.rfasioli.mercadolivro.security.UserCustomDetails
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.random.Random

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration
@WithMockUser
class CustomerControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() =
        customerRepository.deleteAll()

    @AfterEach
    fun tearDown() =
        customerRepository.deleteAll()

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `should return all customers`() {
        val expectedCustomers = buildCustomers()
            .map { customerRepository.save(it) }

        mockMvc.perform(get("/customers"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.totalItems").value(expectedCustomers.size))
            .andExpect(jsonPath("$.itens[0].id").value(expectedCustomers[0].id))
            .andExpect(jsonPath("$.itens[1].id").value(expectedCustomers[1].id))
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `should filter customers by name`() {
        val expectedCustomers = buildCustomers()
            .map { customerRepository.save(it) }

        mockMvc.perform(get("/customers?name=Ada"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.totalItems").value(1))
            .andExpect(jsonPath("$.itens[0].id").value(expectedCustomers[0].id))
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `should create customer`() {
        val email = "gil${Random.nextInt()}@email"
        val request = PostCustomerRequest("Givânilda", email, "123456")

        mockMvc.perform(
            post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)

        val customers = customerRepository.findByEmail(email)

        assertThat(customers)
            .isNotNull
            .hasFieldOrPropertyWithValue("name", request.name)
    }

    @Test
    fun `should get user by id when authenticated user has the same id  `() {
        val expectedCustomers = buildCustomers()
            .map { customerRepository.save(it) }

        val sampleCustomer = expectedCustomers[0]

        mockMvc.perform(get("/customers/${sampleCustomer.id}").with(user(UserCustomDetails(sampleCustomer))))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(sampleCustomer.id))
            .andExpect(jsonPath("$.name").value(sampleCustomer.name))
            .andExpect(jsonPath("$.email").value(sampleCustomer.email))
    }

    @Test
    fun `should deny access for user with different id`() {
        val expectedCustomers = buildCustomers()
            .map { customerRepository.save(it) }

        val sampleCustomer = expectedCustomers[0]

        mockMvc.perform(get("/customers/0").with(user(UserCustomDetails(sampleCustomer))))
            .andExpect(status().isForbidden)
            .andExpect(jsonPath("$.code").value("AccessDeniedException"))
            .andExpect(jsonPath("$.message").value("Access is denied"))
            .andExpect(jsonPath("$.errors").doesNotExist())
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `should get user by id when user has admin role `() {
        val expectedCustomers = buildCustomers()
            .map { customerRepository.save(it) }

        val sampleCustomer = expectedCustomers[0]

        mockMvc.perform(get("/customers/${sampleCustomer.id}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(sampleCustomer.id))
            .andExpect(jsonPath("$.name").value(sampleCustomer.name))
            .andExpect(jsonPath("$.email").value(sampleCustomer.email))
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `should update customer`() {
        val customer = customerRepository.save(buildCustomer())
        val request = PutCustomerRequest("Givânilda", "gil${Random.nextInt()}@email")

        mockMvc.perform(
            put("/customers/${customer.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isAccepted)

        val updatedCustomer = customerRepository.findById(customer.id!!)

        assertThat(updatedCustomer)
            .isNotEmpty
            .get()
            .hasFieldOrPropertyWithValue("name", request.name)
            .hasFieldOrPropertyWithValue("email", request.email)
            .usingRecursiveComparison()
            .ignoringFields("name", "email", "roles")
            .isEqualTo(customer)
    }

    @ParameterizedTest
    @CsvSource(
        "'',email@email.com,123456",
        "thename,'',123456",
        "thename,email@email.com,''",
        "thename,invalid_email,123456"
    )
    @WithMockUser(roles = ["ADMIN"])
    fun `should throw error when create customer with invalid data`(
        name: String,
        email: String,
        password: String
    ) {
        val request = PostCustomerRequest(name, email, password)

        mockMvc.perform(
            post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.code").value("MethodArgumentNotValidException"))
            .andExpect(jsonPath("$.message").value("Invalid data on request body"))
            .andExpect(jsonPath("$.errors").isArray)
    }

    @ParameterizedTest
    @CsvSource(
        "'',email@email.com",
        "thename,invalid_email"
    )
    @WithMockUser(roles = ["ADMIN"])
    fun `should throw error when update customer with invalid data`(
        name: String,
        email: String
    ) {
        val request = PutCustomerRequest(name, email)

        mockMvc.perform(
            put("/customers/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.code").value("MethodArgumentNotValidException"))
            .andExpect(jsonPath("$.message").value("Invalid data on request body"))
            .andExpect(jsonPath("$.errors").isArray)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `should throw error when update not found customer`() {
        val customerId = Random.nextInt()
        val request = PutCustomerRequest("name", "email@email.com")

        mockMvc.perform(
            put("/customers/$customerId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.code").value("CustomerNotFoundException"))
            .andExpect(jsonPath("$.message").value("Customer [$customerId] Not Found"))
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `should delete customer`() {
        val customer = customerRepository.save(buildCustomer())

        mockMvc.perform(delete("/customers/${customer.id}"))
            .andExpect(status().isAccepted)

        val inactiveCustomer = customerRepository.findById(customer.id!!)

        assertThat(inactiveCustomer)
            .isNotNull.get()
            .hasFieldOrPropertyWithValue("status", CustomerStatus.INACTIVE)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `should throw error when delete not found customer`() {
        val customerId = Random.nextInt()
        mockMvc.perform(delete("/customers/$customerId"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.code").value("CustomerNotFoundException"))
            .andExpect(jsonPath("$.message").value("Customer [$customerId] Not Found"))
    }

    private fun buildCustomers(): List<CustomerModel> {
        return listOf(
            buildCustomer(name = "Adailton"),
            buildCustomer(name = "Peterson")
        )
    }
}
