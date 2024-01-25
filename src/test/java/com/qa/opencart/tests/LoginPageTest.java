package com.qa.opencart.tests;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//execution will start from here
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.listeners.TestAllureListener;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
//at class level Epic,Story,Feature from jira we can define
@Epic("Epic 100: Design Open Cart Login Page")
@Story("User Story: Login Page Feature")
@Feature("F50: Feature LoginPage")
@Listeners(TestAllureListener.class)

public class LoginPageTest extends BaseTest  {
//this class is child of BaseTest so inherit all it's property
//at class level we can define: description,severity
	
private static final Logger log = LogManager.getLogger(LoginPageTest.class);	
	
	@Description("Login Page Title Test...")
	@Severity(SeverityLevel.MINOR)
	@Test(priority = 1)
	public void loginPageTitleTest() {
String actTitle =loginPage.getLoginPageTitle();
log.info("actual login page title: " + actTitle);
	Assert.assertEquals(actTitle, AppConstants.LOGIN_PAGE_TITLE);
	}
	@Description("Login Page URL Test...")
	@Severity(SeverityLevel.NORMAL)
 @Test(priority = 2)
 public void loginPageURLTest() {
String actURL =	 loginPage.getLoginPageURL();
Assert.assertTrue(actURL.contains(AppConstants.LOGIN_PAGE_URL_FRACTION));
 }
 
	@Description("Verifying forgot pwd Link Test...")
	@Severity(SeverityLevel.CRITICAL)
 @Test(priority = 3)
 public void forgotPwdLinkExistTest() {
Assert.assertTrue(loginPage.isForgotPwdLinkExist());
 }
 
	@Description("Verifying App logo Exist Test...")
	@Severity(SeverityLevel.CRITICAL)
@Test(priority = 4)
public void appLogoExistTest() {
Assert.assertTrue(loginPage.isLogoExist());
 }
	
	
	@Description("Verifying user is able to login with correct credentials...")
	@Severity(SeverityLevel.BLOCKER)
@Test(priority = 5)
public void loginTest() {
accPage=loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
Assert.assertTrue(accPage.isLogoutLinkExist());
//we can verify login through logout link or that page title or url
//Assert.assertTrue(loginPage.doLogin("poojakhatiyan021@gmail.com", "test@12345"));
}
/**
 * doesn't contain any driver code or selenium code
 * testng classes are responsible only &only for test :u can call specific method from page class and assert them.
 * 
 */
//without assertion we can not write our TC : while execution there is a problem as it may run loginTest 1st a/c to
//alphabetic order to solve this we will give priority


 
 
}
 