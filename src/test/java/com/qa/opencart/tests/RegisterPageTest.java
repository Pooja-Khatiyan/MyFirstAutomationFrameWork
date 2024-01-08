package com.qa.opencart.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.utils.ExcelUtil;

public class RegisterPageTest extends BaseTest {
//precondition : user should be on registeration page

	@BeforeClass
	public void regSetUp() {
		registerPage = loginPage.navigateToRegisterPage();// precondition
	}

//email id should be uniqe every time ,to advoid manually we can create a method for that

	public String getRandomEmailID() {
		return "testautomation" + System.currentTimeMillis() + "@opencart.com";
		// return "testautomation" + UUID.randomUUID()+"@opencart.com";
	}

//what is the need of creating 10 different user if application is working with 2 it will work with n also as all locator 
//remain same &registration flow remain same.this is called a data testing approach :then there is no need to unnecessary filling
//the data in your database.we are not doing any performance testing here 
//DATA TESTING APPROACH: it is not possible to check for each & every data , we pick imp data and check for that 
//:any online application ; RUN = TOTAL ROWS ; PARAMETER = NUMBER OF COLUMN

	           //********DATA PROVIDER : TESTNG************//
	//	@DataProvider
//	public Object[][] getUserRegData() {
//		return new Object[][] {
//			    { "tom", "automation", "7608734307", "admins@123", "yes" },
//				{ "ciya", "automation", "7608034807", "admins@123", "yes" },
//				{ "shelly", "automation", "7698034307", "test@123", "yes" },
//				{ "priya", "automation", "7008030300", "selenium@123", "no" }, 
//				};
//	         }
	  
	          //**********DATA PROVIDER : EXCEL SHEET***********//
	
	@DataProvider
	public Object[][] getUserRegTestExcelData() {
Object regData[][]=	ExcelUtil.getTestData(AppConstants.REGISTER_DATA_SHEET_NAME);
	return regData;
	}
	
//here important thing is to maintain unique email id ,other things can be duplicate,no need to maintain data sheet here
	//@Test(dataProvider = "getUserRegData")
	@Test(dataProvider = "getUserRegTestExcelData")
//there is no link between title name coming from excel sheet &parameter we are passing but sequence should be the same.
//title name & supplied parameter name could be anything
	public void userRegTest(String firstName, String lastName, String teleNumber, String pwd, String subs) {
		boolean isRegDone = registerPage.userRegisteration(firstName, lastName, getRandomEmailID(), teleNumber, pwd,
				subs);
		Assert.assertTrue(isRegDone);
	}

}
