package br.com.rfasioli.mercadolivro.mock

import br.com.rfasioli.mercadolivro.enums.CustomerStatus
import br.com.rfasioli.mercadolivro.enums.Role
import br.com.rfasioli.mercadolivro.model.CustomerModel
import java.util.UUID

fun buildCustomer(
    id: Int? = null,
    name: String = "Customer Name",
    email: String = "${UUID.randomUUID()}@email.com",
    password: String = UUID.randomUUID().toString(),
    status: CustomerStatus = CustomerStatus.ACTIVE,
    roles: Set<Role> = setOf(Role.CUSTOMER)
): CustomerModel =
    CustomerModel(
        id = id,
        name = name,
        email = email,
        status = status,
        password = password,
        roles = roles
    )
