package br.com.rfasioli.mercadolivro.controller.response

import br.com.rfasioli.mercadolivro.enums.CustomerStatus

data class CustomerResponse (
    val id: Int? = null,
    val name: String,
    val email: String,
    val status: CustomerStatus
)