package br.com.rfasioli.mercadolivro.controller.response

import br.com.rfasioli.mercadolivro.enums.BookStatus
import br.com.rfasioli.mercadolivro.model.CustomerModel
import java.math.BigDecimal

data class BookResponse(
    val id: Int? = null,
    val title: String,
    val price: BigDecimal,
    val customer: CustomerModel? = null,
    val status: BookStatus? = null
)
