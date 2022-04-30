package br.com.rfasioli.mercadolivro.service

import br.com.rfasioli.mercadolivro.enums.CustomerStatus
import br.com.rfasioli.mercadolivro.exception.CustomerNotFoundException
import br.com.rfasioli.mercadolivro.model.CustomerModel
import br.com.rfasioli.mercadolivro.repository.CustomerRepository
import org.springframework.stereotype.Service

@Service
class CustomerService(
    val customerRepository: CustomerRepository,
    val bookService: BookService
) {

    fun getAllCustomer(name: String?): List<CustomerModel> =
        name?.let { customerRepository.findByNameContaining(name).toList() }
            ?: customerRepository.findAll().toList()

    fun getCustomer(id: Int): CustomerModel =
        customerRepository.findById(id).orElseThrow { CustomerNotFoundException(id) }

    fun createCustomer(customer: CustomerModel) =
        customerRepository.save(customer)

    fun updateCustomer(customer: CustomerModel) =
        customer
            .takeIf { customerRepository.existsById(it.id!!) }
            ?.let { customerRepository.save(it) }
            ?: throw CustomerNotFoundException(customer.id!!)

    fun deleteCustomer(id: Int) {
        customerRepository.findById(id)
            .orElseThrow { CustomerNotFoundException(id) }
            .also { bookService.deleteByCustomer(it) }
            .also { it.status = CustomerStatus.INACTIVE }
            .let { updateCustomer(it) }
    }
}
