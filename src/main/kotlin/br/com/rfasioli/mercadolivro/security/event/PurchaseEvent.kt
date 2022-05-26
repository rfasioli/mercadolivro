package br.com.rfasioli.mercadolivro.security.event

import br.com.rfasioli.mercadolivro.model.PurchaseModel
import org.springframework.context.ApplicationEvent

class PurchaseEvent(
    source: Any,
    val purchaseModel: PurchaseModel
) : ApplicationEvent(source)
