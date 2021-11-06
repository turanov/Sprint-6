package com.example.demo.controller

import com.example.demo.model.AddressBook
import com.example.demo.servise.AddressBookService
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.WebUtils.getCookie
import java.net.URI
import java.util.concurrent.ConcurrentHashMap

@RestController
@RequestMapping("/api")
class ApiController(private val bookService: AddressBookService) {


    @PostMapping("/add")
    fun add(@RequestBody book: AddressBook) =
        bookService.save(book)
            .let { ResponseEntity.created(URI("/book/${it.name}")).build<Unit>() }


    @GetMapping("/list")
    fun getAll(): ResponseEntity<ConcurrentHashMap<Int, AddressBook>> = ResponseEntity.ok(bookService.findAll())


    @GetMapping("/{id}/view")
    fun get(@PathVariable("id") id: Int) =
        bookService.findById(id)?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()

    @PutMapping("/{id}/edit")
    fun update(@PathVariable("id") id: Int, @RequestBody book: AddressBook)  {bookService.update(id, book).let {
        ResponseEntity.noContent().build<Unit>()
        }
    }

    @DeleteMapping("{id}/delete")
    fun delete(@PathVariable id: Int): ResponseEntity<Unit> {
        bookService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}