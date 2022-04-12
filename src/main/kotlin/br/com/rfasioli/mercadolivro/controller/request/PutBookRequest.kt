package br.com.rfasioli.mercadolivro.controller.request

import java.math.BigDecimal

data class PutBookRequest(
    val title: String?,
    val price: BigDecimal?
)
