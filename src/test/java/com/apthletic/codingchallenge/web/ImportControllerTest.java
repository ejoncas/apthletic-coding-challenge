package com.apthletic.codingchallenge.web;

import com.apthletic.codingchallenge.TestUtils;
import com.tngtech.java.junit.dataprovider.DataProvider;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ImportControllerTest extends BaseWebTest {

    @DataProvider({
            "afl, sample_afl_import.json, Imported 9 events",
            "ironman, sample_ironman_import.json, Imported 7 events",
    })
    @Test
    public void shouldImportAndReturnDefaultMessage(String eventType, String sampleFile, String expectedResponse) throws Exception {
        getMockMvc().perform(
                post("/v1/import/" + eventType)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.resourceToString(sampleFile))
        ).andExpect(status().isCreated())
                .andExpect(content().string(containsString(expectedResponse)));
    }

    @Test
    public void shouldReturn400OnInvalidEventType() throws Exception {
        getMockMvc().perform(
                post("/v1/import/myNewEventTypeNotSupportedYet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]")
        ).andExpect(status().isBadRequest());
    }

}