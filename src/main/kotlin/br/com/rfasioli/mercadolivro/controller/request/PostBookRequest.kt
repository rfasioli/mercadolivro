package br.com.rfasioli.mercadolivro.controller.request

import com.fasterxml.jackson.annotation.JsonAlias
import java.math.BigDecimal

data class PostBookRequest(
    val title: String,

    val price: BigDecimal,

    @JsonAlias("customer_id")
    var customerId: Int
)
