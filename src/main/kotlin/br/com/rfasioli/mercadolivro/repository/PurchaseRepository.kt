package br.com.rfasioli.mercadolivro.repository

import br.com.rfasioli.mercadolivro.model.PurchaseModel
import org.springframework.data.jpa.repository.JpaRepository

interface PurchaseRepository : JpaRepository<PurchaseModel, Int>
