package br.com.rfasioli.mercadolivre.controller.mapper

import br.com.rfasioli.mercadolivre.controller.request.PostCustomerRequest
import br.com.rfasioli.mercadolivre.controller.request.PutCustomerRequest
import br.com.rfasioli.mercadolivre.model.CustomerModel

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
