package br.com.rfasioli.mercadolivro.service

import br.com.rfasioli.mercadolivro.model.CustomerModel
import br.com.rfasioli.mercadolivro.service.mapper.merge
import org.springframework.stereotype.Service

@Service
class CustomerService {

    val customers = mutableListOf<CustomerModel>()

    fun getAllCustomer(name: String?): List<CustomerModel> =
        name?.let { customers.filter { it.name.contains(name, true) } }
            ?: customers

    fun getCustomer(id: String): CustomerModel =
        customers.first { it.id == id }

    fun createCustomer(customer: CustomerModel) =
        customer
            .also { it.id = (customers.size + 1).toString() }
            .run { customers.add(this) }

    fun updateCustomer(customer: CustomerModel) =
        customers.filter { it.id == customer.id }
            .map { it.merge(customer) }

    fun deleteCustomer(id: String) =
        customers.removeIf { it.id == id }
}
