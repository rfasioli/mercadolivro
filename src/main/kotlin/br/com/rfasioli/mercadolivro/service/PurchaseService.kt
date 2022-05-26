package br.com.rfasioli.mercadolivro.service

import br.com.rfasioli.mercadolivro.security.event.PurchaseEvent
import br.com.rfasioli.mercadolivro.model.PurchaseModel
import br.com.rfasioli.mercadolivro.repository.PurchaseRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class PurchaseService(
    private val purchaseRepository: PurchaseRepository,
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    fun create(purchaseModel: PurchaseModel): PurchaseModel =
        purchaseRepository.save(purchaseModel)
            .also {
                applicationEventPublisher.publishEvent(PurchaseEvent(this, it))
            }

    fun update(purchaseModel: PurchaseModel) {
        purchaseRepository.save(purchaseModel)
    }
}
