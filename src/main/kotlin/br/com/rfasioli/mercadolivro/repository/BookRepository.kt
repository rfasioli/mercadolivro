package br.com.rfasioli.mercadolivro.repository

import br.com.rfasioli.mercadolivro.enums.BookStatus
import br.com.rfasioli.mercadolivro.model.BookModel
import br.com.rfasioli.mercadolivro.model.CustomerModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository : JpaRepository<BookModel, Int> {
    fun findByTitleContaining(title: String, pageable: Pageable): Page<BookModel>
    fun findByStatus(ativo: BookStatus, pageable: Pageable): Page<BookModel>
    fun findByCustomer(customer: CustomerModel): Iterable<BookModel>
    fun findByIdInAndStatus(ids: List<Int>, status: BookStatus): Iterable<BookModel>
}
