package com.example.demo

import com.example.demo.model.AddressBook
import com.example.demo.servise.AddressBookService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.util.concurrent.ConcurrentHashMap


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiTest {


    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var service: AddressBookService

    private lateinit var url: String

    private val headers = HttpHeaders()

    @BeforeEach
    fun setUp() {
        url = "http://localhost:$port"
    }

    @Test
    fun testView() {
        val resp: ResponseEntity<AddressBook> = restTemplate.exchange(
            "${url}/api/1/view",
            HttpMethod.GET, HttpEntity<AddressBook>(null, headers),
            AddressBook::class.java
        )
        println(resp)
        Assertions.assertEquals("islam", resp.body!!.name)
    }

    @Test
    fun testAdd() {
        val resp = restTemplate.exchange(
            "${url}/api/add", HttpMethod.POST,
            HttpEntity<AddressBook>(AddressBook("islam", "email@mail.ru"), headers),
            AddressBook::class.java
        )
        Assertions.assertEquals("islam", service.findById(2)!!.name)
        Assertions.assertEquals("email@mail.ru", service.findById(2)!!.email)
    }

    @Test
    fun testList() {
        val resp = restTemplate.exchange("${url}/api/list", HttpMethod.GET,
            HttpEntity(null, headers),
            object : ParameterizedTypeReference<Map<String, AddressBook>>() {})
        Assertions.assertEquals("islam", resp.body!!["1"]!!.name)
    }


    @Test
    fun testDelete() {
        Assertions.assertTrue(service.findAll().size == 2)
        restTemplate.exchange(
            "${url}/api/1/delete",
            HttpMethod.DELETE, HttpEntity(null, headers),
            AddressBook::class.java, 1
        )
        Assertions.assertTrue(service.findAll().size == 1)
    }
}