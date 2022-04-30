package br.com.rfasioli.mercadolivro.service

import br.com.rfasioli.mercadolivro.enums.CustomerStatus
import br.com.rfasioli.mercadolivro.exception.CustomerNotFoundException
import br.com.rfasioli.mercadolivro.model.CustomerModel
import br.com.rfasioli.mercadolivro.repository.CustomerRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CustomerService(
    val customerRepository: CustomerRepository,
    val bookService: BookService
) {

    fun getAllCustomer(name: String?, pageable: Pageable): Page<CustomerModel> =
        name?.let { customerRepository.findByNameContaining(name, pageable) }
            ?: customerRepository.findAll(pageable)

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

    fun emailAvailable(email: String): Boolean =
        !customerRepository.existsByEmail(email)
}
