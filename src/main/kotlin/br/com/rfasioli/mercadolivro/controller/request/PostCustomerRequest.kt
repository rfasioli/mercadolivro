package br.com.rfasioli.mercadolivro.controller.request

import br.com.rfasioli.mercadolivro.validation.EmailAvailable
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class PostCustomerRequest(

    @field:NotEmpty(message = "Name should be filled")
    val name: String,

    @field:Email(message = "Email must be valid")
    @EmailAvailable
    val email: String,

    @field:NotEmpty(message = "Password required")
    val password: String
)
