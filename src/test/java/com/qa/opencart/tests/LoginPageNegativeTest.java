package com.qa.opencart.tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;

public class LoginPageNegativeTest extends BaseTest {

	@DataProvider
	public Object[][] incorrectLoginTestData() {
		return new Object[][] {
			{ "autmation@gmaill.com", "123445667" }, 
			{ "test@@gmail", "12344" },
		     { "selenium", "test" },
			 { "@#%$&$##", "#%@#@%" }, 
			{ "@gmail", "@tyr67" } 
				};
	}
	
	@Test(dataProvider="incorrectLoginTestData")
	public void loginWithWrongCredentialsTest(String userName, String password) {
		Assert.assertTrue(loginPage.doLoginWithWrongCredentials(userName,password));	
			}

}
