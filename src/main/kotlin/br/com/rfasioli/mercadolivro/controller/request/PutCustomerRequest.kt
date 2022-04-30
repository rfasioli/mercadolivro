package br.com.rfasioli.mercadolivro.controller.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class PutCustomerRequest(

    @field:NotEmpty(message = "Name should be filled")
    val name: String?,

    @field:Email(message = "Email must be valid")
    val email: String?
)
