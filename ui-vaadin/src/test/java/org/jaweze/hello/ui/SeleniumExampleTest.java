package org.jaweze.hello.ui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(properties = "server.port=8080", webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SeleniumExampleTest {


    private WebDriver webDriver;

    @Before
    public void setupClass() {
        webDriver =  new FirefoxDriver();
    }

    @After
    public void teardownClass() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    @Test
    public void loginShouldRedirectToMainView() throws Exception {
        webDriver.get("http://localhost:8080");

        WebElement usernameField = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("username-text-field")));

        usernameField.sendKeys("admin");
        webDriver.findElement(By.id("password-text-field")).sendKeys("p");
        webDriver.findElement(By.id("login-button")).click();

        WebElement logoutButton = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("logout-button")));

        assertThat(logoutButton).isNotNull();
    }

}