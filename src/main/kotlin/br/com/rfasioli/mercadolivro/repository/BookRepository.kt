package br.com.rfasioli.mercadolivro.repository

import br.com.rfasioli.mercadolivro.enums.BookStatus
import br.com.rfasioli.mercadolivro.model.BookModel
import org.springframework.data.repository.CrudRepository

interface BookRepository : CrudRepository<BookModel, Int> {
    fun findByTitleContaining(title: String): Iterable<BookModel>
    fun findByStatus(ativo: BookStatus): List<BookModel>
}
