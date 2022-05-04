package br.com.rfasioli.mercadolivro.model

import javax.persistence.CollectionTable
import br.com.rfasioli.mercadolivro.enums.CustomerStatus
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import br.com.rfasioli.mercadolivro.enums.Profile

@Entity(name = "customer")
data class CustomerModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column
    var name: String,

    @Column
    var email: String,

    @Column
    @Enumerated(EnumType.STRING)
    var status: CustomerStatus,

    @Column
    var password: String,

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "customer_roles", joinColumns = [JoinColumn(name = "customerId")])
    @ElementCollection(targetClass = Profile::class, fetch = FetchType.EAGER)
    var roles: Set<Profile> = setOf()
)
