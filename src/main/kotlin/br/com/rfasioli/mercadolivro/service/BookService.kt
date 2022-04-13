package br.com.rfasioli.mercadolivro.service

import br.com.rfasioli.mercadolivro.enums.BookStatus
import br.com.rfasioli.mercadolivro.model.BookModel
import br.com.rfasioli.mercadolivro.model.CustomerModel
import br.com.rfasioli.mercadolivro.repository.BookRepository
import org.springframework.stereotype.Service

@Service
class BookService(
    val bookRepository: BookRepository,
) {

    fun create(book: BookModel): BookModel =
        bookRepository.save(book)

    fun getAll(title: String?): List<BookModel> =
        title?.let { bookRepository.findByTitleContaining(it).toList() }
            ?: bookRepository.findAll().toList()

    fun getById(id: Int): BookModel =
        bookRepository.findById(id).get()

    fun getActives(): List<BookModel> =
        bookRepository.findByStatus(BookStatus.ACTIVE)

    fun update(book: BookModel): BookModel =
        bookRepository.save(book)

    fun deleteById(id: Int) {
        bookRepository.findById(id).get()
            .also { it.status = BookStatus.CANCELED }
            .let { bookRepository.save(it) }
    }

    fun deleteByCustomer(customer: CustomerModel) {
        bookRepository.findByCustomer(customer)
            .map { changeStatusToDeleted(it) }
            .let { bookRepository.saveAll(it) }
    }

    private fun changeStatusToDeleted(book: BookModel): BookModel =
        book.also { it.status = BookStatus.DELETED }
}
