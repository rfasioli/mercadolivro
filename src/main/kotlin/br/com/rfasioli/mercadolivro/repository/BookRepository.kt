package br.com.rfasioli.mercadolivro.repository

import br.com.rfasioli.mercadolivro.model.BookModel
import org.springframework.data.repository.CrudRepository

interface BookRepository : CrudRepository<BookModel, Int>
