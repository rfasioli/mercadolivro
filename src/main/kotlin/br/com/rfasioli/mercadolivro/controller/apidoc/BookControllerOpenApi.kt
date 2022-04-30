package br.com.rfasioli.mercadolivro.controller.apidoc

import br.com.rfasioli.mercadolivro.controller.request.PostBookRequest
import br.com.rfasioli.mercadolivro.controller.request.PutBookRequest
import br.com.rfasioli.mercadolivro.controller.response.BookResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

@Tag(name = "Book", description = "this API provide methods for operations with Books")
interface BookControllerOpenApi {

    @Operation(summary = "Create new book")
    fun create(request: PostBookRequest): BookResponse

    @Operation(summary = "Find all new books, also all book containing text on title")
    fun getAll(title: String?, pageable: Pageable): Page<BookResponse>

    @Operation(summary = "Find all books in active status")
    fun getActives(pageable: Pageable): Page<BookResponse>

    @Operation(summary = "Find a book by id")
    fun getById(id: Int): BookResponse

    @Operation(summary = "Delete a book by id")
    fun deleteById(id: Int)

    @Operation(summary = "Update book data by id")
    fun updateById(id: Int, book: PutBookRequest): BookResponse
}
