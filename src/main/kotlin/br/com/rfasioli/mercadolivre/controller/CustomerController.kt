package br.com.rfasioli.mercadolivre.controller

import br.com.rfasioli.mercadolivre.controller.mapper.mergeWithModel
import br.com.rfasioli.mercadolivre.controller.mapper.toModel
import br.com.rfasioli.mercadolivre.controller.request.PostCustomerRequest
import br.com.rfasioli.mercadolivre.controller.request.PutCustomerRequest
import br.com.rfasioli.mercadolivre.model.CustomerModel
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

@RestController
@RequestMapping("customers")
class CustomerController {

    val customers = mutableListOf<CustomerModel>()

    @GetMapping
    fun getAllCustomer(
        @RequestParam name: String?
    ): List<CustomerModel> =
        name?.let { customers.filter { it.name.contains(name, true) } }
            ?: customers

    @GetMapping("/{id}")
    fun getCustomer(
        @PathVariable id: String
    ): CustomerModel =
        customers.first { it.id == id }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createCustomer(@RequestBody customer: PostCustomerRequest) =
        customers.add(customer.toModel(((customers.size) + 1).toString()))

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateCustomer(
        @PathVariable id: String,
        @RequestBody customer: PutCustomerRequest
    ) =
        customers.filter { it.id == id }
            .first()
            .let { customer.mergeWithModel(it) }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomer(@PathVariable id: String) =
        customers.removeIf { it.id == id }
}
