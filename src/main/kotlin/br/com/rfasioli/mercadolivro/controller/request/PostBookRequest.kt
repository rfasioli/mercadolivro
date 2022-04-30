package br.com.rfasioli.mercadolivro.controller.request

import com.fasterxml.jackson.annotation.JsonAlias
import java.math.BigDecimal
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

data class PostBookRequest(

    @field:NotEmpty(message = "Title should be filled")
    val title: String,

    @field:Positive(message = "Price should be positive")
    val price: BigDecimal,

    @JsonAlias("customer_id")
    @field:PositiveOrZero(message = "Customer ID should be positive or Zero")
    var customerId: Int
)
