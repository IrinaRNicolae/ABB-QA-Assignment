package api;

import java.util.*;

import io.restassured.response.Response;
import static io.restassured.RestAssured.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ApiTestBooks {

    private static final String BOOKS_ENDPOINT = "https://demoqa.com/BookStore/v1/Books";
    private static final int EXPECTED_BOOK_COUNT = 8;

    public static Response getResponse() {
        var response = given().when().get(BOOKS_ENDPOINT).andReturn();
        return response;
    }

    public static void assertResponseNotNull(Response response) {
        assertNotNull(response, "Response must not be null");
    }

    public static void assertStatus200(Response response) {
        assertResponseNotNull(response);
        assertEquals(200, response.getStatusCode(), "Expected HTTP 200");
    }

    public static void assertBooksCount(Response response) {
        List<Map<String, Object>> books = response.jsonPath().getList("books");
        assertNotNull(books, "Response doesn't contain a 'books' array");
        assertEquals(EXPECTED_BOOK_COUNT, books.size(), "Expected " + EXPECTED_BOOK_COUNT + " books");
    }

    public static void assertBooksHaveTitleAndAuthor(Response response) {
        List<Map<String, Object>> books = getBooksList(response);
        assertNotNull(books, "Response doesn't contain a 'books' array");

        for (int i = 0; i < books.size(); i++) {
            Map<String, Object> book = books.get(i);
            String title = getStringFieldIgnoreCase(book, "title");
            String author = getStringFieldIgnoreCase(book, "author");

            assertNotNull(title, "Book " + i + " is missing a title field");
            assertFalse(title.trim().isEmpty(), "Book " + i + " has an empty title");

            assertNotNull(author, "Book " + i + " is missing an author field");
            assertFalse(author.trim().isEmpty(), "Book #" + i + " has an empty author");
        }
    }

    private static List<Map<String, Object>> getBooksList(Response response) {

        List<Map<String, Object>> books = response.jsonPath().getList("books");
        return books;
    }

    private static String getStringFieldIgnoreCase(Map<String, Object> map, String fieldName) {
        if (map == null)
            return null;
        for (Object key : map.keySet()) {
            if (key == null)
                continue;
            if (key.toString().equalsIgnoreCase(fieldName)) {
                Object val = map.get(key);
                return val == null ? null : val.toString();
            }
        }
        return null;
    }

    @Test
    @DisplayName("Books API - status validation")
    public void validateBooksStatus() {
        Response resp = getResponse();
        assertStatus200(resp);
    }

    @Test
    @DisplayName("Books API - count validation")
    public void validateBooksCount() {
        Response resp = getResponse();
        assertBooksCount(resp);
    }

    @Test
    @DisplayName("Books API - title & author fields validation")
    public void validateTitleAndAuthor_only() {
        Response resp = getResponse();
        assertBooksHaveTitleAndAuthor(resp);
    }
}
