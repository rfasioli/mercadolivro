package br.com.rfasioli.mercadolivro.controller

import br.com.rfasioli.mercadolivro.enums.Role
import br.com.rfasioli.mercadolivro.mock.buildCustomer
import br.com.rfasioli.mercadolivro.repository.CustomerRepository
import br.com.rfasioli.mercadolivro.security.UserCustomDetails
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

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
    fun `should return all customers`() {
        val adminCustomer = buildCustomer(roles = setOf(Role.CUSTOMER, Role.ADMIN))
        val expectedCustomers = listOf(buildCustomer(), buildCustomer(), adminCustomer)
            .map { customerRepository.save(it) }

        mockMvc.perform(get("/customers").with(user(UserCustomDetails(adminCustomer))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalItems").value(expectedCustomers.size))
            .andExpect(jsonPath("$.itens[0].id").value(expectedCustomers[0].id))
            .andExpect(jsonPath("$.itens[1].id").value(expectedCustomers[1].id))
            .andExpect(jsonPath("$.itens[2].id").value(expectedCustomers[2].id))
    }

}
