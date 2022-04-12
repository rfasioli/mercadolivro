package br.com.rfasioli.mercadolivro.service

import br.com.rfasioli.mercadolivro.enums.BookStatus
import br.com.rfasioli.mercadolivro.model.BookModel
import br.com.rfasioli.mercadolivro.repository.BookRepository
import org.springframework.stereotype.Service

@Service
class BookService(
    val bookRepository: BookRepository
) {

    fun create(book: BookModel) =
        bookRepository.save(book)

    fun getAll(title: String?): List<BookModel> =
        title?.let { bookRepository.findByTitleContaining(it).toList() }
            ?: bookRepository.findAll().toList()

    fun getById(id: Int): BookModel =
        bookRepository.findById(id).get()

    fun getActives(): List<BookModel> =
        bookRepository.findByStatus(BookStatus.ATIVO)

    fun deleteById(id: Int) =
        bookRepository.findById(id).get()
            .also { it.status = BookStatus.CANCELADO }
            .let { bookRepository.save(it) }

    fun update(book: BookModel) =
        bookRepository.save(book)
}
