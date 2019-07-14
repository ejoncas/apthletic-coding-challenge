package com.apthletic.codingchallenge.web;

import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;


@RunWith(DataProviderRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseWebTest {

    @ClassRule
    public static final SpringClassRule SCR = new SpringClassRule();
    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private MockMvc mockMvc;


    protected MockMvc getMockMvc() {
        return mockMvc;
    }
}
