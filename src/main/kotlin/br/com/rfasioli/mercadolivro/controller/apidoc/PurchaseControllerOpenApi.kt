package br.com.rfasioli.mercadolivro.controller.apidoc

import br.com.rfasioli.mercadolivro.controller.request.PostPurchaseRequest
import br.com.rfasioli.mercadolivro.model.PurchaseModel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "Purchase", description = "this API provide methods for operations with purchases")
interface PurchaseControllerOpenApi {

    @Operation(summary = "Create a purchase on Mercado Livro")
    fun createPurchase(request: PostPurchaseRequest)
}