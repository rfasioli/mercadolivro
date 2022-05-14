package br.com.rfasioli.mercadolivro.service

import br.com.rfasioli.mercadolivro.exception.AuthenticationException
import br.com.rfasioli.mercadolivro.repository.CustomerRepository
import br.com.rfasioli.mercadolivro.security.UserCustomDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsCustomService(
    private val customerRepository: CustomerRepository
) : UserDetailsService {
    override fun loadUserByUsername(id: String): UserDetails =
        customerRepository.findById(id.toInt())
            .orElseThrow { AuthenticationException("User not found") }
            .let { UserCustomDetails(it) }
}
