package com.example.demo.servise

import com.example.demo.model.AddressBook
import com.example.demo.repository.AddressBookRepository
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class AddressBookService(private val repository: AddressBookRepository) {
    fun save(student: AddressBook): AddressBook {
        return repository.save(student)
    }

    fun findById(id: Int): AddressBook? {
        return repository.findById(id)
    }

    fun findAll(): ConcurrentHashMap<Int, AddressBook> = repository.findAll();

    fun deleteById(id: Int) {
        repository.deleteById(id)
    }

    fun update(id: Int, student: AddressBook) :AddressBook?{
       return repository.update(id, student)
    }


}