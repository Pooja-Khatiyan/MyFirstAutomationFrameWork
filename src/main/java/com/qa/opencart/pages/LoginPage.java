package com.qa.opencart.pages;

//page object model are classic example of encapsulation
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.utils.ElementUtil;

import io.qameta.allure.Step;

public class LoginPage {
//all the by locator are private and all method are internally calling them in public layer: ENCAPSULATION
//PRIVATE Driver in public method : encapsulation 	
	// By Locators : Object Repository
	private WebDriver driver;// this driver is initialized due to const.. we don't get NPE
	private ElementUtil eleUtil;
	private By userName = By.id("input-email");
	private By password = By.id("input-password");
	private By loginBtn = By.xpath("//input[@value='Login']");
	private By forgotPwdLink = By.linkText("Forgotten Password");//to check make it fail
	private By logo = By.cssSelector("img[title='naveenopencart']");
	private By registerLink = By.linkText("Register");
	private By loginErrorMessg = By.xpath("//body//div[contains(@class,'alert')]");

	// Page Constructor : to initialize driver
	public LoginPage(WebDriver driver) {
		this.driver = driver;
		//eleUtil = new ElementUtil(driver);
		eleUtil = new ElementUtil(this.driver);//this is also fine we are passing the global driver here
	}

	// Page Actions/ Method : define the behavior of the specific feature
	// this is not a testng class we don't write any assertion here
@Step("getting login page title...")
	public String getLoginPageTitle() {
		String title = eleUtil.waitForTitleIs(AppConstants.LOGIN_PAGE_TITLE, AppConstants.SHORT_DEFAULT_WAIT);
		// String title = driver.getTitle();
		System.out.println("login page title: " + title);
		return title;
	}

@Step("getting login page url")
	public String getLoginPageURL() {
		String url = eleUtil.waitForUrlContains(AppConstants.LOGIN_PAGE_URL_FRACTION, AppConstants.SHORT_DEFAULT_WAIT);
		// String url = driver.getCurrentUrl();
		System.out.println("login page url: " + url);
		return url;
	}

@Step("checking forgot pwd link exist")
	public boolean isForgotPwdLinkExist() {
		return eleUtil.waitForVisibilityOfElement(forgotPwdLink, AppConstants.SHORT_DEFAULT_WAIT).isDisplayed();
		// return driver.findElement(forgotPwdLink).isDisplayed();
	}

@Step("checking logo exist")
	public boolean isLogoExist() {
		return eleUtil.waitForVisibilityOfElement(logo, AppConstants.SHORT_DEFAULT_WAIT).isDisplayed();
		// return driver.findElement(logo).isDisplayed();
	}

	@Step("username is : {0} and password {1}")
	public AccountsPage doLogin(String username, String pwd) {
		System.out.println("Creds are: " + username + ":" + pwd);
		eleUtil.waitForVisibilityOfElement(userName, AppConstants.MEDIUM_DEFAULT_WAIT).sendKeys(username);
		eleUtil.doSendKeys(password, pwd);
		eleUtil.doClick(loginBtn);
		return new AccountsPage(driver);//is this is eligible of GC as have no reference?no becz we connected it immediately with
		//in AccountPageTest with reference
/**now page chaining model come into the picture,after click on login button we lending on the
 * accounts page,if we click on something & we lend on  new page,it's the method responsibility
 * to return the next lending class object.
 *we are returning the next class object : return type is class name 		
 */	
//		driver.findElement(userName).sendKeys(username);
//		driver.findElement(password).sendKeys(pwd);
//		driver.findElement(loginBtn).click();
//		System.out.println("user is logged in");
//		return true;// for validation purpose: for time been
		// it will return true in either cases if logged in or not : bug : see later
//it doesn't contain any assertion
		}
	
	
	@Step("login with wrong username {0} and password {1}")
	public boolean doLoginWithWrongCredentials(String username, String pwd) {
		System.out.println("wrong Creds are: " + username + ":" + pwd);
		eleUtil.waitForVisibilityOfElement(userName, AppConstants.MEDIUM_DEFAULT_WAIT);
		eleUtil.doSendKeys(userName, username);
		eleUtil.doSendKeys(password, pwd);
		eleUtil.doClick(loginBtn);
		String errorMessg=eleUtil.doElementGetText(loginErrorMessg);
		System.out.println(errorMessg);
		if(errorMessg.contains(AppConstants.LOGIN_ERROR_MESSAGE)) {
			return true; 
			}
		return false;
		}
		
	
	
	
	/**
	 * it have locator only and only specific to page class and page behavior or we
	 * can say that it have locator & respective public layers
	 */
	
	@Step("navigating to register page")
	public RegisterPage navigateToRegisterPage() {
		eleUtil.waitForVisibilityOfElement(registerLink, AppConstants.MEDIUM_DEFAULT_WAIT).click();
		return new RegisterPage(driver);
	}
}
