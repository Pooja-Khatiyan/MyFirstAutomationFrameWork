package com.qa.opencart.base;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.qa.opencart.factory.DriverFactory;
import com.qa.opencart.pages.AccountsPage;
import com.qa.opencart.pages.LoginPage;
import com.qa.opencart.pages.ProductInfoPage;
import com.qa.opencart.pages.RegisterPage;
import com.qa.opencart.pages.SearchResultsPage;

import io.qameta.allure.Step;
//we are maintaining LoginPage reference here so that it's child  class can access that
/**
 * all page reference should be maintain here
 */
public class BaseTest {
protected WebDriver driver;
protected Properties prop;
DriverFactory df; // Reference variable of this class
protected LoginPage loginPage;//this class reference to use it's method //right now it is pointing to null
//protected : directly access through child class/in different package child class is there give access to only them
protected AccountsPage accPage;
protected SearchResultsPage searchResultsPage;
protected ProductInfoPage productInfoPage;
protected RegisterPage registerPage;
protected SoftAssert softAssert;

@Step("initializing the browser")
@Parameters({"browser" , "browserversion"})
@BeforeTest
public void setUp(String browserName ,String browserversion) {//this browserName is coming from xml file
	df = new DriverFactory();
	prop = df.initializedProp();
	if(browserName!=null) { //very important check to give preferance to xml file
		prop.setProperty("browser", browserName);
		prop.setProperty("browserversion", browserversion);
	}
	driver = df.initializeDriver(prop);
	loginPage = new LoginPage(driver);//initializing driver
    softAssert = new SoftAssert();
}

@Step("closing the connection")
@AfterTest
public void tearDown() {
	driver.quit();
}

}
