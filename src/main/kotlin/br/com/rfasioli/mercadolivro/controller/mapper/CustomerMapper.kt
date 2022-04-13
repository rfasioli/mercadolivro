package br.com.rfasioli.mercadolivro.controller.mapper

import br.com.rfasioli.mercadolivro.controller.request.PostCustomerRequest
import br.com.rfasioli.mercadolivro.controller.request.PutCustomerRequest
import br.com.rfasioli.mercadolivro.controller.response.CustomerResponse
import br.com.rfasioli.mercadolivro.enums.CustomerStatus
import br.com.rfasioli.mercadolivro.model.CustomerModel

fun PostCustomerRequest.toModel(id: Int? = null): CustomerModel =
    CustomerModel(
        id,
        name,
        email,
        CustomerStatus.ACTIVE
    )

fun PutCustomerRequest.toModel(previous: CustomerModel): CustomerModel =
    CustomerModel(
        previous.id,
        name = this.name ?: previous.name,
        email = this.email ?: previous.email,
        previous.status
    )

fun CustomerModel.toCustomerResponse(): CustomerResponse =
    CustomerResponse(
        id,
        name,
        email,
        status
    )
