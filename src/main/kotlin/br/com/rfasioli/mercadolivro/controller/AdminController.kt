package br.com.rfasioli.mercadolivro.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("admin")
class AdminController {

    companion object {
        private const val MESSAGE = "This is a Report. Only admin can see it!"
    }

    @GetMapping("/report")
    fun getReport(): String = MESSAGE
}
