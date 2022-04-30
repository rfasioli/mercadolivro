package br.com.rfasioli.mercadolivro.repository

import java.util.*
import br.com.rfasioli.mercadolivro.enums.CustomerStatus
import br.com.rfasioli.mercadolivro.model.CustomerModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository : JpaRepository<CustomerModel, Int> {
    fun findByNameContaining(name: String, pageable: Pageable): Page<CustomerModel>
    fun existsByEmail(email: String): Boolean
    fun findByIdAndStatus(id: Int, status: CustomerStatus): Optional<CustomerModel>
}
