package br.com.rfasioli.mercadolivro.event

import br.com.rfasioli.mercadolivro.model.PurchaseModel
import org.springframework.context.ApplicationEvent

class PurchaseEvent(
    source: Any,
    val purchaseModel: PurchaseModel
): ApplicationEvent(source)