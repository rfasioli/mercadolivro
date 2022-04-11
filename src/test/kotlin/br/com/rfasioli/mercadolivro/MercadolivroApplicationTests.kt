package br.com.rfasioli.mercadolivro

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext

@SpringBootTest
class MercadolivroApplicationTests(
    val context: ApplicationContext
) {

    @Test
    fun contextLoads() {
        assertThat(context).isNotNull
    }
}
