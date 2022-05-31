package br.com.rfasioli.mercadolivro.controller

import br.com.rfasioli.mercadolivro.controller.request.PostCustomerRequest
import br.com.rfasioli.mercadolivro.mock.buildCustomer
import br.com.rfasioli.mercadolivro.model.CustomerModel
import br.com.rfasioli.mercadolivro.repository.CustomerRepository
import br.com.rfasioli.mercadolivro.security.UserCustomDetails
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
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
    fun setup() {
        customerRepository.deleteAll()
    }

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

    private fun buildCustomers(): List<CustomerModel> {
        return listOf(
            buildCustomer(name = "Adailton"),
            buildCustomer(name = "Peterson")
        )
    }
}
