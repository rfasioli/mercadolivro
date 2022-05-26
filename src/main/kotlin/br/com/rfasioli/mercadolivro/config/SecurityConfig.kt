package br.com.rfasioli.mercadolivro.config

import br.com.rfasioli.mercadolivro.enums.Role
import br.com.rfasioli.mercadolivro.repository.CustomerRepository
import br.com.rfasioli.mercadolivro.security.AuthenticationFilter
import br.com.rfasioli.mercadolivro.security.AuthorizationFilter
import br.com.rfasioli.mercadolivro.security.CustomAuthenticationEntrypoint
import br.com.rfasioli.mercadolivro.security.JwtUtil
import br.com.rfasioli.mercadolivro.service.UserDetailsCustomService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val customerRepository: CustomerRepository,
    private val userDetails: UserDetailsCustomService,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val customEntrypoint: CustomAuthenticationEntrypoint
) : WebSecurityConfigurerAdapter() {

    private final val publicGetMatchers = arrayOf("/books")
    private final val publicPostMatchers = arrayOf("/customers")
    private final val adminMatchers = arrayOf("/admin/**")

    private final var swaggerMatchers = arrayOf(
        "/v3/api-docs.yaml",
        "/v3/api-docs",
        "/swagger-ui.html",
        "/v3/api-docs/swagger-config",
        "/swagger-ui/index.html"
    )

    override fun configure(http: HttpSecurity) {
        http.cors().and()
            .csrf().disable()

        http.authorizeRequests()
            .antMatchers(HttpMethod.GET, *publicGetMatchers).permitAll()
            .antMatchers(HttpMethod.POST, *publicPostMatchers).permitAll()
            .antMatchers(*adminMatchers).hasAuthority(Role.ADMIN.description)
            .anyRequest().authenticated()

        http.addFilter(AuthenticationFilter(authenticationManager(), customerRepository, jwtUtil))
            .addFilter(AuthorizationFilter(authenticationManager(), userDetails, jwtUtil))

        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http.exceptionHandling().authenticationEntryPoint(customEntrypoint)
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetails)
            .passwordEncoder(bCryptPasswordEncoder)
    }

    override fun configure(web: WebSecurity) {
        web.ignoring()
            .antMatchers(*swaggerMatchers)
    }

    @Bean
    fun corsConfig(): CorsFilter {
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOriginPattern("*")
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }
}
