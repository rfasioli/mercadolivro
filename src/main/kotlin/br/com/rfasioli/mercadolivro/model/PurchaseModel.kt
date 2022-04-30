package br.com.rfasioli.mercadolivro.model

import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity(name = "purchase")
@EntityListeners(AuditingEntityListener::class)
data class PurchaseModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "customer_id")
    val customer: CustomerModel,

    @ManyToMany
    @JoinTable(
        name = "purchase_book",
        joinColumns = [JoinColumn(name = "purchase_id")],
        inverseJoinColumns = [JoinColumn(name = "book_id")]
    )
    val books: List<BookModel>,

    @Column
    val nfe: String,

    @Column
    val price: BigDecimal,

    @Column(name = "created_at")
    @CreatedDate
    val createdAt: LocalDateTime,

    @Column(name = "updated_at")
    @LastModifiedDate
    val updatedAt: LocalDateTime
)
