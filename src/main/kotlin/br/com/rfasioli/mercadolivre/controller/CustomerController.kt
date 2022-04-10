package br.com.rfasioli.mercadolivre.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CustomerController {

    @GetMapping
    fun helloWorld(): String = "Hello World!"
}