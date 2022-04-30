package br.com.rfasioli.mercadolivro.controller

import br.com.rfasioli.mercadolivro.controller.mapper.toBookResponse
import br.com.rfasioli.mercadolivro.controller.mapper.toModel
import br.com.rfasioli.mercadolivro.controller.request.PostBookRequest
import br.com.rfasioli.mercadolivro.controller.request.PutBookRequest
import br.com.rfasioli.mercadolivro.controller.response.BookResponse
import br.com.rfasioli.mercadolivro.service.BookService
import br.com.rfasioli.mercadolivro.service.CustomerService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("books")
class BookController(
    val bookService: BookService,
    val customerService: CustomerService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid request: PostBookRequest): BookResponse =
        request.customerId
            .let { customerService.getCustomer(it) }
            .let { request.toModel(it) }
            .let { bookService.create(it) }
            .toBookResponse()

    @GetMapping
    fun getAll(
        @RequestParam title: String?,
        @PageableDefault(page = 0, size = 10) pageable: Pageable
    ): Page<BookResponse> =
        bookService.getAll(title, pageable)
            .map { it.toBookResponse() }

    @GetMapping("/active")
    fun getActives(
        @PageableDefault(page = 0, size = 10) pageable: Pageable
    ): Page<BookResponse> =
        bookService.getActives(pageable)
            .map { it.toBookResponse() }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int): BookResponse =
        bookService.getById(id)
            .toBookResponse()

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id: Int) =
        bookService.deleteById(id)

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateById(
        @PathVariable id: Int,
        @Valid @RequestBody book: PutBookRequest
    ): BookResponse =
        bookService.getById(id)
            .let { bookService.update(book.toModel(it)) }
            .toBookResponse()
}
