package br.com.rfasioli.mercadolivro.controller.apidoc

import br.com.rfasioli.mercadolivro.controller.request.PostCustomerRequest
import br.com.rfasioli.mercadolivro.controller.request.PutCustomerRequest
import br.com.rfasioli.mercadolivro.controller.response.CustomerResponse
import br.com.rfasioli.mercadolivro.controller.response.PageResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable

@Tag(name = "Customer", description = "this API provide methods for operations with customers")
interface CustomerControllerOpenApi {

    @Operation(summary = "Find all customers, also find all containing a string on name")
    fun getAllCustomer(name: String?, pageable: Pageable): PageResponse<CustomerResponse>

    @Operation(summary = "Find a customer by id")
    fun getCustomer(id: Int): CustomerResponse

    @Operation(summary = "Create new customer")
    fun createCustomer(customer: PostCustomerRequest): CustomerResponse

    @Operation(summary = "Update existing customer")
    fun updateCustomer(id: Int, customer: PutCustomerRequest): CustomerResponse

    @Operation(summary = "Delete a customer by id")
    fun deleteCustomer(id: Int)
}
