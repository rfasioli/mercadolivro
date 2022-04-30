package br.com.rfasioli.mercadolivro.service

import br.com.rfasioli.mercadolivro.model.PurchaseModel
import br.com.rfasioli.mercadolivro.repository.PurchaseRepository
import org.springframework.stereotype.Service

@Service
class PurchaseService(
    private val purchaseRepository: PurchaseRepository
) {

    fun create(purchaseModel: PurchaseModel): PurchaseModel =
        purchaseRepository.save(purchaseModel)
}