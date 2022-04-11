package br.com.rfasioli.mercadolivro.service

import br.com.rfasioli.mercadolivro.model.CustomerModel
import br.com.rfasioli.mercadolivro.repository.CustomerRepository
import org.springframework.stereotype.Service

@Service
class CustomerService(
    val customerRepository: CustomerRepository
) {

    fun getAllCustomer(name: String?): List<CustomerModel> =
        (name?.let { customerRepository.findByNameContaining(name) }
            ?: customerRepository.findAll()).toList()

    fun getCustomer(id: Int): CustomerModel =
        customerRepository.findById(id).get()

    fun createCustomer(customer: CustomerModel) =
        customerRepository.save(customer)

    fun updateCustomer(customer: CustomerModel) =
        customer
            .takeIf { customerRepository.existsById(it.id!!) }
            ?.let { customerRepository.save(it) }
            ?: throw RuntimeException("Customer Not Found!")

    fun deleteCustomer(id: Int) =
        id
            .takeIf { customerRepository.existsById(id) }
            ?.let { customerRepository.deleteById(it) }
            ?: throw RuntimeException("Customer Not Found!")
}
