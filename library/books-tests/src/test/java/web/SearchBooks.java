package web;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class SearchBooks {

    private WebDriver driver;
    private WebDriverWait wait;

    private static final String BOOKS_URL = "https://demoqa.com/books";
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    // --------------------
    // Hooks -- they should me moved to separate file
    // --------------------
    @Before
    public void setUp() {
        // we suppose only Chrome
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);

        wait = new WebDriverWait(driver, TIMEOUT);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // --------------------
    // Given / When / Then steps
    // --------------------

    @When("the user navigates to the catalogue of books")
    public void userNavigatesToTheCatalogueOfBooks() {
        driver.get(BOOKS_URL);
    }

    @When("user types {string} in the search input")
    public void userTypesInTheSearchInput(String text) {
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchBox")));
        searchBox.clear();
        searchBox.sendKeys(text);
    }

    @Then("the books with title {string} are displayed")
    public void theBooksWithTitleAreDisplayed(String text) {
        List<String> booksList = getBooksList();

        String title = text.toLowerCase();
        for (String element : booksList) {
            assertTrue(element.toLowerCase().contains(title),
                    "Row '" + element + "' does not contain string '" + text + "'");
        }
    }

    // --------------------
    // This methods will be moved a separate file as well
    // --------------------

    private List<String> getBooksList() {
        List<WebElement> rows = driver.findElements(By.cssSelector(".rt-tbody .rt-tr-group"));
        List<String> result = new ArrayList<>();
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.cssSelector(".rt-td"));
            if (cells.isEmpty())
                continue;
            String title = "";
            if (cells.size() >= 2)
                title = cells.get(1).getText().trim();
            if (!title.isEmpty()) {
                result.add(title);
            }
        }
        return result;
    }

}
