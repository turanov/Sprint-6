package com.example.demo.repository

import com.example.demo.model.AddressBook
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class AddressBookRepository {

    private var inc: Int = 1
    private val books = ConcurrentHashMap<Int, AddressBook>()


    init {
        books[inc++] = AddressBook("islam", "email@mail.ru")
    }

    fun save(book: AddressBook): AddressBook {
        books[inc++] = book
        return book
    }

    fun findById(id: Int): AddressBook? {
        println("Id = $id")
        for ((k, v) in books) {
            if (k == id)
                return v
        }
        return null
    }

    fun findAll(): ConcurrentHashMap<Int, AddressBook> = books;

    fun update(id: Int, book: AddressBook): AddressBook? {
        for ((k, v) in books) {
            if (k == id) {
                books[k] = book
                return books[k]
            }
        }
        return null
    }

    fun deleteById(id: Int) {
        books.remove(id)
    }
}