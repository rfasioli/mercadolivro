package br.com.rfasioli.mercadolivro.service.mapper

import br.com.rfasioli.mercadolivro.model.CustomerModel

fun CustomerModel.merge(model: CustomerModel): CustomerModel {
    val merged = this
    merged.name = model.name
    merged.email = model.email
    return merged
}
