package com.diamante.orderingsystem.integration;

import com.diamante.orderingsystem.OrderingSystemApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderingSystemApplication.class)
@ActiveProfiles("test")
public class CustomerControllerIntegrationTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getAllCustomers_returnsAllCustomers() throws Exception {
        mockMvc.perform(get("/customer"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Tom"))
                .andExpect(jsonPath("$[0].lastName").value("Green"))
                .andExpect(jsonPath("$[1].firstName").value("George"))
                .andExpect(jsonPath("$[1].lastName").value("Jefferson"))
                .andExpect(jsonPath("$[2].firstName").value("Frank"))
                .andExpect(jsonPath("$[2].lastName").value("Wright"));
    }
}
