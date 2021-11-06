package com.example.demo.controller

import com.example.demo.model.AddressBook
import com.example.demo.servise.AddressBookService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/app")
class AppController(private val bookService: AddressBookService) {


    @PostMapping("/add")
    fun add(@RequestParam("name") name: String, @RequestParam("email") email: String): String {
        bookService.save(AddressBook(name, email))
        return "redirect:/app/list"
    }

    @GetMapping("/list")
    fun get(model: Model): String {
        model.addAttribute("books", bookService.findAll())
        return "book_list"
    }

    @GetMapping("/{id}/view")
    fun get(@PathVariable id: Int, model: Model): String {
        model.addAttribute("book", bookService.findById(id))
        return "book_view"
    }


    @GetMapping("/{id}/edit")
    fun getUpdate(
        @PathVariable("id") id: Int, model: Model
    ): String {
        val addressBook = bookService.findById(id)
        model.addAttribute("id", id)
        model.addAttribute("name",addressBook?.name);
        model.addAttribute("email",addressBook?.email);
        return "book_edit"
    }
    @PostMapping("/{id}/edit")
    fun postUpdate(
        @PathVariable("id") id: Int,
        @RequestParam("name") name: String,
        @RequestParam("email") email: String
    ): String {
        bookService.update(id, AddressBook(name, email))
        return "redirect:/app/list"
    }

    @PostMapping("/{id}/delete")
    fun delete(@PathVariable id: Int): String {
        bookService.deleteById(id)
        return "redirect:/app/list"
    }
}