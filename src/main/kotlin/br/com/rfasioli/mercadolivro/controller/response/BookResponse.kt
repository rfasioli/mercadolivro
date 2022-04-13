package br.com.rfasioli.mercadolivro.controller.response

import java.math.BigDecimal
import br.com.rfasioli.mercadolivro.enums.BookStatus
import br.com.rfasioli.mercadolivro.model.CustomerModel

data class BookResponse (
    val id: Int? = null,
    val title: String,
    val price: BigDecimal,
    val customer: CustomerModel? = null,
    val status: BookStatus? = null
)
