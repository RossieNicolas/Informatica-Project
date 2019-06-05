package com.architec.crediti;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
public class SeleniumPortfolioTest {
    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;
    @Before
    public void setUp() {
        System.setProperty("webdriver.gecko.driver", "C:\\toegepaste informatica\\J2\\M4\\Project\\geckodriver.exe");
        driver = new FirefoxDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<String, Object>();
    }
    @After
    public void tearDown() {
        driver.quit();
    }
    //id= 53
    @Test
    public void portfolioLoad() {
        driver.get("http://vps092.ap.be/");
        driver.manage().window().setSize(new Dimension(900, 701));
        driver.findElement(By.linkText("Login")).click();
        driver.findElement(By.id("username")).sendKeys("nicolasstudent@gmail.com");
        driver.findElement(By.id("password")).sendKeys("testwachtwoord");
        driver.findElement(By.cssSelector(".btn")).click();
        driver.findElement(By.id("profile")).click();
        driver.findElement(By.linkText("Mijn portfolio")).click();
    }

    //id= 56
    @Test
    public void portfolioViewLecturersOrCoordinators() {
        driver.get("http://vps092.ap.be/");
        driver.manage().window().setSize(new Dimension(900, 705));
        driver.findElement(By.linkText("Login")).click();
        driver.findElement(By.id("username")).sendKeys("nicolas.rossie@gmail.com");
        driver.findElement(By.id("password")).sendKeys("testwachtwoord");
        driver.findElement(By.cssSelector(".btn")).click();
        driver.findElement(By.id("extras")).click();
        driver.findElement(By.linkText("Lijst Studenten")).click();
        driver.findElement(By.linkText("📃")).click();
    }
}
