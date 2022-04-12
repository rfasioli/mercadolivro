package br.com.rfasioli.mercadolivro.controller.mapper

import br.com.rfasioli.mercadolivro.controller.request.PostBookRequest
import br.com.rfasioli.mercadolivro.controller.request.PutBookRequest
import br.com.rfasioli.mercadolivro.enums.BookStatus
import br.com.rfasioli.mercadolivro.model.BookModel
import br.com.rfasioli.mercadolivro.model.CustomerModel

fun PostBookRequest.toModel(customer: CustomerModel): BookModel =
    BookModel(
        title = this.title,
        price = this.price,
        status = BookStatus.ATIVO,
        customer = customer
    )

fun PutBookRequest.toModel(previous: BookModel): BookModel =
    BookModel(
        previous.id,
        title = this.title ?: previous.title,
        price = this.price ?: previous.price,
        status = previous.status,
        customer = previous.customer
    )
