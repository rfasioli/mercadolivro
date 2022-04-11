package br.com.rfasioli.mercadolivro.controller.mapper

import br.com.rfasioli.mercadolivro.controller.request.PostCustomerRequest
import br.com.rfasioli.mercadolivro.controller.request.PutCustomerRequest
import br.com.rfasioli.mercadolivro.model.CustomerModel

fun PostCustomerRequest.toModel(id: String): CustomerModel =
    CustomerModel(
        id,
        name,
        email
    )

fun PutCustomerRequest.mergeWithModel(model: CustomerModel): CustomerModel {
    model.name = name
    model.email = email
    return model
}
