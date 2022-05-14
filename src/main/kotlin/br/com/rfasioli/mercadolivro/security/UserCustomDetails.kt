package br.com.rfasioli.mercadolivro.security

import br.com.rfasioli.mercadolivro.enums.CustomerStatus
import br.com.rfasioli.mercadolivro.model.CustomerModel
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserCustomDetails(
    private val customerModel: CustomerModel
): UserDetails {
    val id = customerModel.id

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        customerModel.roles
            .map { SimpleGrantedAuthority(it.description) }
            .toMutableList()

    override fun getPassword(): String = customerModel.password
    override fun getUsername(): String = customerModel.id.toString()
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = CustomerStatus.ACTIVE == customerModel.status
}
