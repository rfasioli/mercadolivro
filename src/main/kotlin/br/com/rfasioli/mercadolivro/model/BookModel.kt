package br.com.rfasioli.mercadolivro.model

import br.com.rfasioli.mercadolivro.enums.BookStatus
import br.com.rfasioli.mercadolivro.exception.InvalidBookStatusChangeException
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity(name = "book")
data class BookModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column
    var title: String,

    @Column
    var price: BigDecimal,

    @ManyToOne
    @JoinColumn(name = "customer_id")
    var customer: CustomerModel? = null

) {

    companion object;

    constructor(
        id: Int? = null,
        title: String,
        price: BigDecimal,
        customer: CustomerModel? = null,
        status: BookStatus? = null
    ) : this(id, title, price, customer) {
        this.status = status
    }

    @Column
    @Enumerated(EnumType.STRING)
    var status: BookStatus? = null
        set(value) {
            if (field == BookStatus.DELETED || field == BookStatus.CANCELED) {
                throw InvalidBookStatusChangeException()
            }
            field = value
        }
}
