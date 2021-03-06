package com.automationrhapsody.okhttp3;

import com.automationrhapsody.okhttp3.utils.JsonManager;
import com.automationrhapsody.okhttp3.utils.PersonServiceHttpClient;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import okhttp3.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PersonServiceTest {

    private PersonServiceHttpClient personServiceHttpClient = new PersonServiceHttpClient();

    private String personJson;

    @Before
    public void setUp() {
        personJson = JsonManager.createPerson("1234", "First Name", "Last Name", "Email");
    }

    @Test
    public void testAllOperations() throws IOException {
        Response persons = personServiceHttpClient.getAll();
        JSONArray jsonArray = JsonManager.getJsonArray(persons);
        assertThat(jsonArray.size(), is(4));
        for (int i = 0; i < 4; i++) {
            assertThat(JsonManager.getJsonObject(jsonArray, i).get("id"), is(i + 1L));
        }

        Response saveResult = personServiceHttpClient.save(personJson);
        assertThat(saveResult.body().string(), is("Added Person with id=1234"));

        Response actual = personServiceHttpClient.get(1234);
        JSONObject jsonObject = JsonManager.getJsonObject(actual);
        assertThat(jsonObject.get("id"), is(1234L));
        assertThat(jsonObject.get("firstName"), is("First Name"));
        assertThat(jsonObject.get("lastName"), is("Last Name"));
        assertThat(jsonObject.get("email"), is("Email"));

        persons = personServiceHttpClient.getAll();
        jsonArray = JsonManager.getJsonArray(persons);
        assertThat(jsonArray.size(), is(5));

        Response result = personServiceHttpClient.remove();
        assertThat(result.body().string(), is("Last person remove. Total count: 4"));

        persons = personServiceHttpClient.getAll();
        jsonArray = JsonManager.getJsonArray(persons);
        assertThat(jsonArray.size(), is(4));
    }
}
