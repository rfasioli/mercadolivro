package br.com.rfasioli.mercadolivro.controller

import br.com.rfasioli.mercadolivro.controller.apidoc.PurchaseControllerOpenApi
import br.com.rfasioli.mercadolivro.controller.mapper.PurchaseMapper
import br.com.rfasioli.mercadolivro.controller.request.PostPurchaseRequest
import br.com.rfasioli.mercadolivro.service.PurchaseService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("purchases")
class PurchaseController(
    private val purchaseService: PurchaseService,
    private val purchaseMapper: PurchaseMapper
): PurchaseControllerOpenApi {

    @PostMapping
    override fun createPurchase(
        @RequestBody request: PostPurchaseRequest
    ) {
        purchaseMapper.toModel(request)
            .let { purchaseService.create(it) }
    }
}