package br.com.rfasioli.mercadolivro.controller

import br.com.rfasioli.mercadolivro.controller.mapper.toModel
import br.com.rfasioli.mercadolivro.controller.request.PostBookRequest
import br.com.rfasioli.mercadolivro.controller.request.PutBookRequest
import br.com.rfasioli.mercadolivro.model.BookModel
import br.com.rfasioli.mercadolivro.service.BookService
import br.com.rfasioli.mercadolivro.service.CustomerService
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

@RestController
@RequestMapping("books")
class BookController(
    val bookService: BookService,
    val customerService: CustomerService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: PostBookRequest) =
        request.customerId
            .let { customerService.getCustomer(it) }
            .let { request.toModel(it) }
            .let { bookService.create(it) }

    @GetMapping
    fun getAll(@RequestParam title: String?): List<BookModel> =
        bookService.getAll(title)

    @GetMapping("/active")
    fun getActives(): List<BookModel> =
        bookService.getActives()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int): BookModel =
        bookService.getById(id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id: Int): BookModel =
        bookService.deleteById(id)

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun getById(
        @PathVariable id: Int,
        @RequestBody book: PutBookRequest
    ) =
        bookService.getById(id)
            .let { bookService.update(book.toModel(it)) }
}
