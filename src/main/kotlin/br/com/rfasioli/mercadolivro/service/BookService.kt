package br.com.rfasioli.mercadolivro.service

import br.com.rfasioli.mercadolivro.enums.BookStatus
import br.com.rfasioli.mercadolivro.exception.BookNotFoundException
import br.com.rfasioli.mercadolivro.model.BookModel
import br.com.rfasioli.mercadolivro.model.CustomerModel
import br.com.rfasioli.mercadolivro.repository.BookRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BookService(
    val bookRepository: BookRepository,
) {

    fun create(book: BookModel): BookModel =
        bookRepository.save(book)

    fun getAll(title: String?, pageable: Pageable): Page<BookModel> =
        title?.let { bookRepository.findByTitleContaining(it, pageable) }
            ?: bookRepository.findAll(pageable)

    fun getById(id: Int): BookModel =
        bookRepository.findById(id).orElseThrow { BookNotFoundException(id) }

    fun getActives(pageable: Pageable): Page<BookModel> =
        bookRepository.findByStatus(BookStatus.ACTIVE, pageable)

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

    fun findByIds(bookIds: Set<Int>): Set<BookModel> =
        bookRepository.findAllById(bookIds.toList()).toSet()

}
