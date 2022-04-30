package br.com.rfasioli.mercadolivro.repository

import br.com.rfasioli.mercadolivro.model.CustomerModel
import org.springframework.data.repository.CrudRepository

interface CustomerRepository : CrudRepository<CustomerModel, Int> {
    fun findByNameContaining(name: String): Iterable<CustomerModel>
    fun existsByEmail(email: String): Boolean
}
