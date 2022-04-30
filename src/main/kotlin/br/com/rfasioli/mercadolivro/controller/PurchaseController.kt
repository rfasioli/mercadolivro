package br.com.rfasioli.mercadolivro.controller

import br.com.rfasioli.mercadolivro.controller.apidoc.PurchaseControllerOpenApi
import br.com.rfasioli.mercadolivro.controller.request.PostPurchaseRequest
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("purchases")
class PurchaseController: PurchaseControllerOpenApi {

    override fun createPurchase(
        @RequestBody request: PostPurchaseRequest
    ) {

    }
}