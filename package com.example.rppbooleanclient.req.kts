package com.example.rppbooleanclient.request

import com.example.rppbooleanclient.constant.Route
import com.example.rppbooleanclient.controller.AccountController
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.mockito.InjectMocks
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.mockito.Mock
import com.example.rppbooleanclient.service.AccountService
import com.example.rppbooleanclient.response.AccountResponse
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc


@SpringBootTest
@AutoConfigureMockMvc
internal class AccountRequestTest{


 private lateinit var mockMvc: MockMvc


    private lateinit var  account: AccountRequest

    @InjectMocks
    private lateinit var accountController: AccountController

    @Mock
    private lateinit var accountService: AccountService


    private lateinit var accountResponse: AccountResponse

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
        account = AccountRequest("rappi","nueve","xxx",88,99)
    }

    @Test
    fun addDataTestIsOk() {
        var completeRoute=Route.ROUTE_BASE+Route.ACOUNT;
        accountResponse= AccountResponse(true,"Account saved successfully","10",null,null,10,null);
        Mockito.`when`(accountService.sendAccountData(account)).thenReturn(accountResponse);
        var mvcResult=mockMvc.perform(
                        MockMvcRequestBuilders.post(completeRoute)
                                .headers(HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON })
                                .content(jacksonObjectMapper().writeValueAsString(account))
                )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("true"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Account saved successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.account_confirmation_id").value("10"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        val content= mvcResult.response.getContentAsString();
        println(content);
        Mockito.verify(accountService).sendAccountData(account);
    }
}
