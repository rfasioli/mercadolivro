package br.com.rfasioli.mercadolivro.controller.mapper

import br.com.rfasioli.mercadolivro.controller.request.PostCustomerRequest
import br.com.rfasioli.mercadolivro.controller.request.PutCustomerRequest
import br.com.rfasioli.mercadolivro.model.CustomerModel

fun PostCustomerRequest.toModel(id: Int? = null): CustomerModel =
    CustomerModel(
        id,
        name,
        email
    )

fun PutCustomerRequest.toModel(id: Int): CustomerModel =
    CustomerModel(
        id,
        name,
        email
    )
