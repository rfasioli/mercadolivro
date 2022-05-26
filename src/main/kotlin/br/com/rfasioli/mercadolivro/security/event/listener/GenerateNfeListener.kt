package br.com.rfasioli.mercadolivro.security.event.listener

import br.com.rfasioli.mercadolivro.security.event.PurchaseEvent
import br.com.rfasioli.mercadolivro.service.PurchaseService
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class GenerateNfeListener(
    private val purchaseService: PurchaseService
) {

    @Async
    @EventListener
    fun listen(purchaseEvent: PurchaseEvent) {
        UUID.randomUUID().toString()
            .let { purchaseEvent.purchaseModel.copy(nfe = it) }
            .also { purchaseService.update(it) }
    }
}
