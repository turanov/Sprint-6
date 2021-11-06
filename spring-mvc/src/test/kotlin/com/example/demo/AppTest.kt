package com.example.demo

import com.example.demo.model.AddressBook
import com.example.demo.repository.AddressBookRepository
import com.example.demo.servise.AddressBookService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.concurrent.ConcurrentHashMap


const val TEST_BOOK_ID = 1

@SpringBootTest
@AutoConfigureMockMvc
class AppTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var service: AddressBookService

    @BeforeEach
    fun setUp() {
        val book = AddressBook("islam", "email@mail.ru")
        val map = ConcurrentHashMap<Int, AddressBook>()
        map[1] = book
        given(this.service.findAll()).willReturn(map)
        given(this.service.findById(TEST_BOOK_ID)).willReturn(AddressBook("islam", "email@mail.ru"))
    }

    @Test
    fun testView() {
        mockMvc.perform(get("/app/{bookId}/view", TEST_BOOK_ID))
            .andExpect(status().isOk)
            .andExpect(view().name("book_view"))
            .andExpect(model().attributeExists("book"))
    }

    @Test
    fun testAdd() {
        mockMvc.perform(
            post("/app/add")
                .param("name", "islam")
                .param("email", "email@mail.ru")
        )
            .andExpect(status().is3xxRedirection)
            .andExpect(view().name("redirect:/app/list"))
    }

    @Test
    fun testUpdate() {
        mockMvc.perform(get("/app/{bookId}/edit", TEST_BOOK_ID))
            .andExpect(status().isOk)
            .andExpect(model().attributeExists("id"))
            .andExpect(model().attributeExists("name"))
            .andExpect(model().attributeExists("email"))
            .andExpect(view().name("book_edit"))
    }

    @Test
    fun testDelete() {
        mockMvc.perform(post("/app/{bookId}/delete", TEST_BOOK_ID))
            .andExpect(status().is3xxRedirection)
            .andExpect(view().name("redirect:/app/list"))

    }

    @Test
    fun testList() {
        mockMvc.perform(get("/app/list"))
            .andExpect(status().isOk)
            .andExpect(view().name("book_list"))

    }
}