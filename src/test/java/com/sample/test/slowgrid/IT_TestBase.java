package com.sample.test.slowgrid;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;

import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.TextFieldElement;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;

import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.MenuBarElement;
import com.vaadin.testbench.elements.WindowElement;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Base class for all our tests, allowing us to change the applicable driver,
 * test URL or other configurations in one place.
 */
public class IT_TestBase extends TestBenchTestCase {
    public static final String baseUrl = "http://localhost:8080/";

    @BeforeClass
    public static void setupClass() {
    	WebDriverManager.chromedriver().setup();
    }
    
    @Before
    public void setUp() throws Exception {
  	
        // Create a new Selenium driver - it is automatically extended to work
        // with TestBench
        driver = new ChromeDriver();
        setDriver(driver);

        // Open the test application URL with the ?restartApplication URL
        // parameter to ensure Vaadin provides us with a fresh UI instance.
        getDriver().get(baseUrl + "?restartApplication");
        // Set a fixed view port of 1024x768 pixels for comparison

        // If you deploy using WTP in Eclipse, this will fail. You should
        // update baseUrl to point to where the app is deployed.
        String pageSource = getDriver().getPageSource();
        String errorMsg = "Application is not available at " + baseUrl + ". Server not started?";
        assertFalse(errorMsg, pageSource.contains("HTTP Status 404")
        		|| pageSource.contains("can't establish a connection to the server"));
    }

	@Test
	public void comboTimeout() {
		ComboBoxElement combo = $(ComboBoxElement.class).first();
		combo.selectByText("trojo fici");
		combo.sendKeys(Keys.ENTER);


		long start = System.currentTimeMillis();
		combo.waitForVaadin();
		long end = System.currentTimeMillis();
		assertTrue(end-start<10000);


		TextFieldElement text = $(TextFieldElement.class).first();

		start = System.currentTimeMillis();
		text.setValue("auto");
		text.waitForVaadin();
		end = System.currentTimeMillis();
		assertTrue("Lasted: " + (end-start)/1000 + " seconds", end-start<10000);
		text.setValue("lala");
	}

	@Test
	public void comboTimeoutBinder() {
		getDriver().get(baseUrl + "?withBinder=true");
		ComboBoxElement combo = $(ComboBoxElement.class).first();
		combo.selectByText("trojo fici");
		combo.sendKeys(Keys.ENTER);


		long start = System.currentTimeMillis();
		combo.waitForVaadin();
		long end = System.currentTimeMillis();
		assertTrue(end-start<10000);


		TextFieldElement text = $(TextFieldElement.class).first();

		start = System.currentTimeMillis();
		text.setValue("auto");
		text.waitForVaadin();
		end = System.currentTimeMillis();
		assertTrue("Lasted: " + (end-start)/1000 + " seconds", end-start<10000);
		text.setValue("lala");
	}

    @After
    public void tearDown() throws Exception {

        // Calling quit() on the driver closes the test browser.
        // When called like this, the browser is immediately closed on _any_
        // error. If you wish to take a screenshot of the browser at the time
        // the error occurred, you'll need to add the ScreenshotOnFailureRule
        // to your test and remove this call to quit().
    	getDriver().quit();
    }
    
    private MenuBarElement getMenuBar() {
    	return $(MenuBarElement.class).first();
    }
    
    private MenuBarElement getMenuBarInDialog() {
    	return $$(WindowElement.class).caption("DialogWithMenu").$$(MenuBarElement.class).first();
    }
    
    private ButtonElement getDialogButton() {
    	return $(ButtonElement.class).caption("Open Dialog").first();
    }
    
}