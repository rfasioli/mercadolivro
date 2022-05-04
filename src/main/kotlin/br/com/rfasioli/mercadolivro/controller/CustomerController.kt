package br.com.rfasioli.mercadolivro.controller

import br.com.rfasioli.mercadolivro.controller.apidoc.CustomerControllerOpenApi
import br.com.rfasioli.mercadolivro.controller.mapper.toCustomerResponse
import br.com.rfasioli.mercadolivro.controller.mapper.toModel
import br.com.rfasioli.mercadolivro.controller.request.PostCustomerRequest
import br.com.rfasioli.mercadolivro.controller.request.PutCustomerRequest
import br.com.rfasioli.mercadolivro.controller.response.CustomerResponse
import br.com.rfasioli.mercadolivro.service.CustomerService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("customers")
class CustomerController(
    val customerService: CustomerService
) : CustomerControllerOpenApi {

    @GetMapping
    override fun getAllCustomer(@RequestParam name: String?, pageable: Pageable): Page<CustomerResponse> =
        customerService.getAllCustomer(name, pageable)
            .map { it.toCustomerResponse() }

    @GetMapping("/{id}")
    override fun getCustomer(@PathVariable id: Int): CustomerResponse =
        customerService.getCustomer(id)
            .toCustomerResponse()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    override fun createCustomer(@RequestBody @Valid customer: PostCustomerRequest): CustomerResponse =
        customerService.createCustomer(customer.toModel())
            .toCustomerResponse()

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    override fun updateCustomer(
        @PathVariable id: Int,
        @Valid @RequestBody customer: PutCustomerRequest
    ): CustomerResponse =
        customerService.getCustomer(id)
            .let { customerService.updateCustomer(customer.toModel(it)) }
            .toCustomerResponse()

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    override fun deleteCustomer(@PathVariable id: Int) =
        customerService.deleteCustomer(id)
}
