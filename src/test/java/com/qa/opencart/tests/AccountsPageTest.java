package com.qa.opencart.tests;

import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
/**normal/specific :precondition : user should be login
*global preconditions : launch the browser & enter the url
*/
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;

//this is called zig-zag pattern or page chaining model
//LOGIN IS MISSING SO ALL TC GET FAILED, now page chaining model come into the picture.
public class AccountsPageTest extends BaseTest {
	/**
	 * in accTest we are using login page reference avaiable in basetest(parent
	 * class) no need to write new AccountsPage(driver),becz this is return by Login
	 * method i.e we are refering it with accPage reference
	 */
	@BeforeClass
	public void accSetUp() {// whenever we create any page object ,we have to supply driver
//becz every page class have a const.. that is waiting for a driver.
		accPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
		// accPage = new AccountsPage(driver);
	}

	@Test
	public void accPageTitleTest() {
		Assert.assertEquals(accPage.getAccPageTitle(),AppConstants.ACCOUNTS_PAGE_TITLE);
	}

	@Test
	public void accPageURLTest() {
		Assert.assertTrue(accPage.getAccPageURL().contains(AppConstants.ACCOUNTS_PAGE_URL_FRACTION));
	}

	@Test
	public void isLogoutLinkExistTest() {
		Assert.assertTrue(accPage.isLogoutLinkExist());
	}

	@Test
	public void isSearchLinkExistTest() {
		Assert.assertTrue(accPage.isSearchFieldExist());
	}
	@Test
	public void accPageHeadersCountTest() {
		List<String> actAccPageHeadersList = accPage.getAccountsHeaders();
		System.out.println(actAccPageHeadersList);
		Assert.assertEquals(actAccPageHeadersList.size(), AppConstants.ACCOUNTS_PAGE_HEADERS_COUNT);
	}


	@Test
	public void accPageHeadersTest() {
		List<String> actAccPageHeadersList = accPage.getAccountsHeaders();
		System.out.println(actAccPageHeadersList);
        Assert.assertEquals(actAccPageHeadersList, AppConstants.ACCOUNTS_PAGE_HEADER_LIST);	
	//sort the actual list : collection.sort method
    //sort the expected list
    //compare 
	
	}
	//search scenario
//loginPage -- accountPage -- searchPage -- productInfoPage	--assertion : zig zig pattern /page chaining pattern
	@Test
	public void searchTest() {
	searchResultsPage = accPage.doSearch("MacBook");
	productInfoPage =searchResultsPage.selectProduct("MacBook Pro");
	String actProductHeader =productInfoPage.getProductHeaderName();
	Assert.assertEquals(actProductHeader, "MacBook Pro");
	}
// public void footerTest() {
//	List<String>accList = accPage.getFooter();
//	List<String> expectedList = Arrays.asList("About Us", "Site Map",
//			"Affiliate", "Newsletter");
//	softAssert.assertEquals(accList,expectedList);
// }
}
