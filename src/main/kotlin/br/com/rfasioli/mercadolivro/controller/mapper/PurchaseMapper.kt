package br.com.rfasioli.mercadolivro.controller.mapper

import br.com.rfasioli.mercadolivro.controller.request.PostPurchaseRequest
import br.com.rfasioli.mercadolivro.model.PurchaseModel
import br.com.rfasioli.mercadolivro.service.BookService
import br.com.rfasioli.mercadolivro.service.CustomerService
import org.springframework.stereotype.Component

@Component
class PurchaseMapper(
    private val bookService: BookService,
    private val customerService: CustomerService
) {
    fun toModel(request: PostPurchaseRequest): PurchaseModel =
        bookService.findByIds(request.bookIds)
            .let {
                PurchaseModel(
                    customer = customerService.getCustomer(request.customerId),
                    books = it.toMutableSet(),
                    price = it.sumOf { book -> book.price }
                )
            }
}
