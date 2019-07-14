package com.apthletic.codingchallenge.entities;

import com.apthletic.codingchallenge.config.JacksonConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;

import static com.apthletic.codingchallenge.TestUtils.resourceToString;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public abstract class BaseModelTest<T> {

    private final Class<T> clazz;
    private final String sampleResource;
    private final ObjectMapper mapper = new JacksonConfig().objectMapper(Jackson2ObjectMapperBuilder.json());

    public BaseModelTest(Class<T> clazz, String sampleResource) {
        this.clazz = clazz;
        this.sampleResource = sampleResource;
    }

    @Test
    public void testDeserializationAndThenSerializationIsSame() throws IOException {
        String json = resourceToString(sampleResource);

        //Plain json to a json tree
        JsonNode jsonFromFile = mapper.readTree(json);

        //Plain json to our POJO
        T event = mapper.readValue(json, clazz);

        //POJO back to json tree -> It should match the one read directly from file
        assertThat(jsonFromFile, is(equalTo(mapper.valueToTree(event))));
    }

    @Test
    public void testCanonical() throws IOException {
        //Given
        String json = resourceToString(sampleResource);
        T event1 = mapper.readValue(json, clazz);
        T event2 = mapper.readValue(json, clazz);
        T event3 = randomEvent();
        //Then: Test equals and hashcode contract
        assertEquals(event1, event1);
        assertNotEquals(event1, null);

        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());

        assertNotEquals(event1, event3);
        assertNotEquals(event2, event3);

        assertNotEquals(event1.hashCode(), event3.hashCode());
        assertNotEquals(event2.hashCode(), event3.hashCode());
    }

    @Test
    public void testToStringShouldPrintId() {
        assertThat(randomEvent().toString(), is(containsString("id=")));
    }

    protected abstract T randomEvent();

}
