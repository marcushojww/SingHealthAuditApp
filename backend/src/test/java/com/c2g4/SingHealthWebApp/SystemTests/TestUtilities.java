package com.c2g4.SingHealthWebApp.SystemTests;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestUtilities {
	
	public boolean comfortSleep = false;
	public WebDriver driver = null;
	public int timeOut = 2000;
	public int timeSleep = 100;
	public String siteRootURL = "http://localhost:3000";
	public Random random = new Random();
	
	public static final int TESTMODE = 0;
	public static final int DEBUGMODE = 1;
	
	public TestUtilities(WebDriver driver, int mode) {
		this.driver = driver;
		if(mode == DEBUGMODE) {
			comfortSleep = true;
		}
	}
	
	//UTILITIES
	//Because selenium's clear doesn't always work
	public void clear(WebElement element) {
		element.sendKeys(Keys.CONTROL + "a");
		element.sendKeys(Keys.BACK_SPACE);
	}
	
	public void waitForId(String username) {
		waitForId(username, timeOut);
	}
	
	public void waitForId(String username, int timeout) {
        try {
        	WebDriverWait wait = new WebDriverWait(driver, timeout);
        	wait.until(ExpectedConditions.elementToBeClickable(By.id(username)));
        }catch(Exception noSuchELementException) {
        	System.out.println("Something went wrong.");
        }
	}
	
	public <T> T getRandomElement(List<T> list) {
		return list.get(random.nextInt(list.size()));
	}
	
	public void auditUserLogin() {
		WebElement loginBox = driver.findElement(By.id("username"));
		WebElement passBox = driver.findElement(By.id("password"));
		Map.Entry<String, String> credentials = TestResources.getCredentials(TestResources.AUDITOR);
		loginBox.sendKeys(credentials.getKey());
		passBox.sendKeys(credentials.getValue());
		this.comfortSleep();
		WebElement loginButton = driver.findElement(By.className("MuiButton-label"));
		loginButton.click();
		assertTrue(this.checkElementExists(By.className("MuiTab-wrapper")));
		this.comfortSleep();
	}
	
	public WebElement tryFindLinkElementByLink(String link, int duration) {
		for(int i = 0; i < duration; i = i + 500) {
			List<WebElement> webElements = driver.findElements(By.tagName("a"));
			for(WebElement webElement:webElements) {
				if(webElement.getAttribute("href").matches(siteRootURL + link)) {
					return webElement;
				}
			}
		}
		return null;
	}
	
	public WebElement tryFindLinkElementByLink(String link) {
		return tryFindLinkElementByLink(link, timeOut);
	}
	
	public boolean checkElementExists(By by, int duration) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, duration);
			//wait.until(ExpectedConditions.elementToBeClickable(by));
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean checkElementExists(By by) {
		return checkElementExists(by, timeOut/1000);
	}

	
	public void comfortSleep(int duration){
		if(comfortSleep) {
			try {
				Thread.sleep(duration);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void comfortSleep(){
		comfortSleep(timeSleep);
	}
}
