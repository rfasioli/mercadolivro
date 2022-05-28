package br.com.rfasioli.mercadolivro.mock

import br.com.rfasioli.mercadolivro.model.BookModel
import br.com.rfasioli.mercadolivro.model.CustomerModel
import br.com.rfasioli.mercadolivro.model.PurchaseModel
import java.math.BigDecimal
import java.util.UUID
import kotlin.random.Random

fun buildPurchase(
    id: Int? = null,
    customer: CustomerModel = buildCustomer(id = Random.nextInt()),
    books: MutableSet<BookModel> = mutableSetOf(),
    nfe: String? = UUID.randomUUID().toString(),
    price: BigDecimal = BigDecimal.TEN,
) = PurchaseModel(
    id = id,
    customer = customer,
    books = books,
    nfe = nfe,
    price = price
)
