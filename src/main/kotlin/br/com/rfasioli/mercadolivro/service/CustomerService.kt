package br.com.rfasioli.mercadolivro.service

import br.com.rfasioli.mercadolivro.enums.CustomerStatus
import br.com.rfasioli.mercadolivro.enums.Role
import br.com.rfasioli.mercadolivro.exception.CustomerNotActiveException
import br.com.rfasioli.mercadolivro.exception.CustomerNotFoundException
import br.com.rfasioli.mercadolivro.model.CustomerModel
import br.com.rfasioli.mercadolivro.repository.CustomerRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
    private val bookService: BookService,
    private val bcrypt: BCryptPasswordEncoder
) {

    fun getAllCustomer(name: String?, pageable: Pageable): Page<CustomerModel> =
        name?.let { customerRepository.findByNameContaining(name, pageable) }
            ?: customerRepository.findAll(pageable)

    fun getCustomer(id: Int): CustomerModel =
        customerRepository.findById(id)
            .orElseThrow { CustomerNotFoundException(id) }

    fun getActiveCustomer(id: Int): CustomerModel =
        customerRepository.findById(id)
            .orElseThrow { CustomerNotFoundException(id) }
            .takeIf { it.status == CustomerStatus.ACTIVE }
            ?: throw CustomerNotActiveException(id)

    fun createCustomer(customer: CustomerModel) =
        customer
            .copy(
                roles = setOf(Role.CUSTOMER),
                password = bcrypt.encode(customer.password)
            )
            .let { customerRepository.save(it) }

    fun updateCustomer(customer: CustomerModel) =
        customer
            .takeIf { customerRepository.existsById(it.id!!) }
            ?.let { customerRepository.save(it) }
            ?: throw CustomerNotFoundException(customer.id!!)

    fun deleteCustomer(id: Int) {
        this.getCustomer(id)
            .also { bookService.deleteByCustomer(it) }
            .also { it.status = CustomerStatus.INACTIVE }
            .let { customerRepository.save(it) }
    }

    fun emailAvailable(email: String): Boolean =
        !customerRepository.existsByEmail(email)
}
