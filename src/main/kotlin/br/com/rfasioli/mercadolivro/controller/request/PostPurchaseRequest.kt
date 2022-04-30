package br.com.rfasioli.mercadolivro.controller.request

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class PostPurchaseRequest(
    @field:NotNull
    @field:Positive
    val customerId: Int,

    @field:NotNull
    @field:NotEmpty
    val bookIds: Set<Int>
)
