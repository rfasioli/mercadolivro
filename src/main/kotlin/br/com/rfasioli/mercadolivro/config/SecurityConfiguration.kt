package br.com.rfasioli.mercadolivro.config

import br.com.rfasioli.mercadolivro.repository.CustomerRepository
import br.com.rfasioli.mercadolivro.security.AuthenticationFilter
import br.com.rfasioli.mercadolivro.security.JwtUtil
import br.com.rfasioli.mercadolivro.service.UserDetailsCustomService
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val customerRepository: CustomerRepository,
    private val userDetails: UserDetailsCustomService,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val jwtUtil: JwtUtil
) : WebSecurityConfigurerAdapter() {

    private final val publicPostMatchers = arrayOf("/customers")

    override fun configure(http: HttpSecurity) {
        http.cors().and()
            .csrf().disable()

        http.authorizeRequests()
            .antMatchers(HttpMethod.POST, *publicPostMatchers).permitAll()
            .anyRequest().authenticated()

        http.addFilter(AuthenticationFilter(authenticationManager(), customerRepository, jwtUtil))

        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetails)
            .passwordEncoder(bCryptPasswordEncoder)
    }
}
