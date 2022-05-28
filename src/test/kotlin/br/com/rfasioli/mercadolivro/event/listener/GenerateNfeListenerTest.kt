package br.com.rfasioli.mercadolivro.event.listener

import br.com.rfasioli.mercadolivro.UnitTest
import br.com.rfasioli.mercadolivro.event.PurchaseEvent
import br.com.rfasioli.mercadolivro.mock.buildPurchase
import br.com.rfasioli.mercadolivro.model.PurchaseModel
import br.com.rfasioli.mercadolivro.service.PurchaseService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.UUID

class GenerateNfeListenerTest: UnitTest {

    @MockK
    private lateinit var purchaseService: PurchaseService

    @InjectMockKs
    private lateinit var generateNfeListener: GenerateNfeListener

    private val purchaseModelSlot = slot<PurchaseModel>()

    @Test
    fun `should generate nfe`() {
        val purchase = buildPurchase()
        val fakeNfe = UUID.randomUUID()
        val purchaseWithNfe = purchase.copy(nfe = fakeNfe.toString())

        mockkStatic(UUID::class)

        every { UUID.randomUUID() } returns fakeNfe
        every { purchaseService.update(purchaseWithNfe) } just runs

        generateNfeListener.listen(PurchaseEvent(this, purchase))

        verify(exactly = 1) { purchaseService.update(capture(purchaseModelSlot)) }

        assertEquals(purchaseWithNfe, purchaseModelSlot.captured)
    }
}
