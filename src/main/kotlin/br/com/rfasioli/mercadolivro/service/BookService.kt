package br.com.rfasioli.mercadolivro.service

import br.com.rfasioli.mercadolivro.model.BookModel
import br.com.rfasioli.mercadolivro.repository.BookRepository
import org.springframework.stereotype.Service

@Service
class BookService(
    val bookRepository: BookRepository
) {

    fun create(book: BookModel) =
        bookRepository.save(book)
}
