package com.c2g4.SingHealthWebApp.SystemTests;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class RobustnessTest {

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

	
	//<><><> Monkeys <><><>
	@Test
	public void MonkeyButtonTest() {
		testUtil.auditUserLogin();
		List<WebElement> elements = driver.findElements(By.tagName("button"));
		
		int safety = 10;
		//int safety = 1000;
		while(safety > 0) {
			WebElement element = testUtil.getRandomElement(elements);
			int numFailed = 0;
			while(numFailed < elements.size()) {
				try {
					element.click();
					elements = driver.findElements(By.tagName("button"));
					break;
				}catch(Exception e) {
					numFailed++;
				}
			}
			if(numFailed == elements.size()) {
				driver.navigate().back();
				if(testUtil.checkElementExists(By.id("username"))) {
					testUtil.auditUserLogin();
				}
				elements = driver.findElements(By.tagName("button"));
			}
			safety--;
		}
	}
	
	@Test
	public void MonkeyURLTest() {
		testUtil.auditUserLogin();
		int safety = 10;
		//int safety = 1000;
		List<URL> urls = new ArrayList<>();
		while(safety > 0) {
			urls = getLinks(urls);
			if(urls.isEmpty()) {
				driver.navigate().back();
			}else {
				goSomewhere(urls);
			}
			safety--;
		}
	}
	
	//<><><> URL Hacking <><><>
	@Test
	public void bypassLoginTest() {
		List<String> urls = new ArrayList<String>() {{
			add("http://localhost:3000/home/a");
			add("http://localhost:3000/chat");
			add("http://localhost:3000/institutions");
			add("http://localhost:3000/account");
			add("http://localhost:3000/edit_account");
			add("http://localhost:3000/edit_password");
			add("http://localhost:3000/institution/KKH");
			add("http://localhost:3000/tenant/1005");
			add("http://localhost:3000/tenant/report/170");
			add("http://localhost:3000/tenant/fbChecklist/1005");
		}};
		
		for(String url:urls) {
			driver.get(url);
			assertFalse(testUtil.checkElementExists(By.className("MuiTab-wrapper"),1));
		}
		
	}
	
	
	//<><><> Et tu, Brute? <><><>
	@Test
	public void brutePasswordTest() {
		WebElement loginBox = driver.findElement(By.id("username"));
		WebElement passBox = driver.findElement(By.id("password"));
		int safety = 10;
		while(safety > 0) {
			testUtil.waitForId("username");
			Map.Entry<String, String> credentials = TestResources.getCredentials(TestResources.AUDITOR);
			testUtil.clear(loginBox);
			loginBox.sendKeys(credentials.getKey());
			credentials = TestResources.getCredentials(TestResources.RANDOM);
			testUtil.clear(passBox);
			passBox.sendKeys(credentials.getValue());
			testUtil.comfortSleep();
			WebElement loginButton = driver.findElement(By.className("MuiButton-label"));
			loginButton.click();
			assertFalse(testUtil.checkElementExists(By.className("MuiTab-wrapper"),1));
			testUtil.comfortSleep();
			safety--;
		}
	}
	
	@Test
	public void bruteLoginTest() {
		WebElement loginBox = driver.findElement(By.id("username"));
		WebElement passBox = driver.findElement(By.id("password"));
		int safety = 10;
		while(safety > 0) {
			testUtil.waitForId("username");
			Map.Entry<String, String> credentials = TestResources.getCredentials(TestResources.RANDOM);
			testUtil.clear(loginBox);
			loginBox.sendKeys(credentials.getKey());
			testUtil.clear(passBox);
			passBox.sendKeys(credentials.getValue());
			testUtil.comfortSleep();
			WebElement loginButton = driver.findElement(By.className("MuiButton-label"));
			loginButton.click();
			assertFalse(testUtil.checkElementExists(By.className("MuiTab-wrapper"),1));
			testUtil.comfortSleep();
			safety--;
		}
	}
	
	@Test
	public void SQLInjectionsTest() {
		String fileName = "src\\test\\java\\com\\c2g4\\SingHealthWebApp\\SystemTests\\SQL_Injection_Payloads.txt";
		File file = new File(fileName);
		Scanner fileScanner = null;
		try {
			fileScanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int safety = 10;
		while(fileScanner.hasNextLine() && safety > 0) {
			WebElement loginBox = driver.findElement(By.id("username"));
			WebElement passBox = driver.findElement(By.id("password"));
			String line = fileScanner.nextLine();
			testUtil.waitForId("username");
			testUtil.clear(loginBox);
			loginBox.sendKeys(line);
			testUtil.clear(passBox);
			passBox.sendKeys(line);
			testUtil.comfortSleep();
			WebElement loginButton = driver.findElement(By.className("MuiButton-label"));
			loginButton.click();
			assertFalse(testUtil.checkElementExists(By.className("MuiTab-wrapper"),1));
			testUtil.comfortSleep();
			safety--;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//Utilities
	private void goSomewhere(List<URL> urls) {
		driver.navigate().to(testUtil.getRandomElement(urls));
	}
	
	private List<URL> getLinks(List<URL> urls) {
		List<WebElement> elements = driver.findElements(By.tagName("a"));
		urls = new ArrayList<>();
		Set<String> strurls = new HashSet<>();
		for(WebElement element:elements) {
			String strURL = element.getAttribute("href");
			if (strURL != null && !strurls.contains(strURL)) {
				strurls.add(strURL);
				try {
					urls.add(new URL(strURL));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return urls;
	}
	
	

}
