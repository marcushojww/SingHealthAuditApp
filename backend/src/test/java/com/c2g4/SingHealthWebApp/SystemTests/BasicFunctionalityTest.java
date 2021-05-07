package com.c2g4.SingHealthWebApp.SystemTests;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class BasicFunctionalityTest {

	private final static String seleniumWebDriverPath = "external jars//chromedriver_89.exe";
	private final static String siteRootURL = "http://localhost:3000";

	private static WebDriver driver;

	private static TestUtilities testUtil;

	@BeforeAll
	public static void runBeforeAllTests() {
		System.setProperty("webdriver.chrome.driver", seleniumWebDriverPath);
		driver = new ChromeDriver();
		testUtil = new TestUtilities(driver, TestUtilities.TESTMODE);
		testUtil.timeOut = 5000;
		testUtil.timeSleep = 500;
		testUtil.siteRootURL = siteRootURL;
	}

	@AfterAll
	public static void runAfterAllTests() {
		driver.close();
	}

	@BeforeEach
	public void runBeforeEachTest() {
		driver.get(siteRootURL);
		testUtil.checkElementExists(By.id("username"));
	}

	@Test
	public void tenantLoginTest() {
		WebElement loginBox = driver.findElement(By.id("username"));
		WebElement passBox = driver.findElement(By.id("password"));
		Map.Entry<String, String> credentials = TestResources.getCredentials(TestResources.TENANT);
		loginBox.sendKeys(credentials.getKey());
		passBox.sendKeys(credentials.getValue());
		testUtil.comfortSleep();
		WebElement loginButton = driver.findElement(By.className("MuiButton-label"));
		loginButton.click();
		assertTrue(testUtil.checkElementExists(By.className("MuiTab-wrapper")));
		testUtil.comfortSleep();
	}

	@Test
	public void auditorLoginTest() {
		WebElement loginBox = driver.findElement(By.id("username"));
		WebElement passBox = driver.findElement(By.id("password"));
		Map.Entry<String, String> credentials = TestResources.getCredentials(TestResources.AUDITOR);
		loginBox.sendKeys(credentials.getKey());
		passBox.sendKeys(credentials.getValue());
		testUtil.comfortSleep();
		WebElement loginButton = driver.findElement(By.className("MuiButton-label"));
		loginButton.click();
		assertTrue(testUtil.checkElementExists(By.className("MuiTab-wrapper")));
		testUtil.comfortSleep();
	}

	@Test
	public void managerLoginTest() {
		WebElement loginBox = driver.findElement(By.id("username"));
		WebElement passBox = driver.findElement(By.id("password"));
		Map.Entry<String, String> credentials = TestResources.getCredentials(TestResources.MANAGER);
		loginBox.sendKeys(credentials.getKey());
		passBox.sendKeys(credentials.getValue());
		testUtil.comfortSleep();
		WebElement loginButton = driver.findElement(By.className("MuiButton-label"));
		loginButton.click();
		assertTrue(testUtil.checkElementExists(By.tagName("a")));
	}

	@Test
	public void badLoginTest() {
		WebElement loginBox = driver.findElement(By.id("username"));
		WebElement passBox = driver.findElement(By.id("password"));
		Map.Entry<String, String> credentials = TestResources.getCredentials(TestResources.RANDOM);
		loginBox.sendKeys(credentials.getKey());
		passBox.sendKeys(credentials.getValue());
		testUtil.comfortSleep();
		WebElement loginButton = driver.findElement(By.className("MuiButton-label"));
		loginButton.click();
		assertFalse(testUtil.checkElementExists(By.className("MuiTab-wrapper")));
		testUtil.comfortSleep();
	}

	@Test
	public void MuiTabsTest() {
		testUtil.auditUserLogin();
		List<WebElement> muiTabsInner = driver.findElements(By.className("MuiTab-wrapper"));
		List<WebElement> muiTabs = new ArrayList<>();
		for (WebElement webElement : muiTabsInner) {
			WebElement parent = (WebElement) ((JavascriptExecutor) driver)
					.executeScript("return arguments[0].parentNode;", webElement);
			muiTabs.add(parent);
		}

		String expectedString = "MuiButtonBase-root MuiTab-root Component-root-22 "
				+ "MuiTab-textColorInherit Mui-selected";
		for (WebElement webElement : muiTabs) {
			webElement.click();
			testUtil.comfortSleep();
			String str = webElement.getAttribute("class");
			assertTrue(expectedString.matches(str));
		}
	}

	@Test
	public void navBarMyAccountTest() {
		testUtil.auditUserLogin();
		WebElement myAccountAnchor = testUtil.tryFindLinkElementByLink("/account");
		myAccountAnchor.click();
		testUtil.comfortSleep();
		assertTrue(testUtil.tryFindLinkElementByLink("/edit_account") != null);
	}

	@Test
	public void navBarInstitutionsTest() {
		testUtil.auditUserLogin();
		WebElement myAccountAnchor = testUtil.tryFindLinkElementByLink("/institutions");
		myAccountAnchor.click();
		testUtil.comfortSleep();
		WebElement viewButton = driver.findElement(By.className("MuiButton-label"));
		assertTrue(viewButton.getText().matches("VIEW TENANTS"));
	}

	@Test
	public void navBarChatTest() {
		testUtil.auditUserLogin();
		WebElement myAccountAnchor = testUtil.tryFindLinkElementByLink("/chat");
		myAccountAnchor.click();
		testUtil.comfortSleep();
		WebElement getButton = driver.findElement(By.className("MuiButton-label"));
		assertTrue(getButton.getText().matches("GET ALL CHATS OF USER"));
	}

	@Test
	public void navBarHomeTest() {
		testUtil.auditUserLogin();
		WebElement myAccountAnchor = testUtil.tryFindLinkElementByLink("/home/a");
		myAccountAnchor.click();
		testUtil.comfortSleep();
		assertTrue(testUtil.checkElementExists(By.className("MuiTab-wrapper")));
	}

	@Test
	public void editAccountTest() {
		testUtil.auditUserLogin();
		WebElement myAccountAnchor = testUtil.tryFindLinkElementByLink("/account");
		myAccountAnchor.click();
		testUtil.comfortSleep();
		WebElement editAccountAnchor = testUtil.tryFindLinkElementByLink("/edit_account");
		editAccountAnchor.click();
		testUtil.comfortSleep();
		WebElement submitButton = driver.findElement(By.className("MuiButton-label"));
		System.out.println(submitButton.getText());
		assertTrue(submitButton.getText().matches("SUBMIT"));
		
	}
	
	

}
