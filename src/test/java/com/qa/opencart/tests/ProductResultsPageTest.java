package com.qa.opencart.tests;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.utils.ExcelUtil;

public class ProductResultsPageTest extends BaseTest {
//precondition : login

	@BeforeClass
	public void productInfoSetUp() {
		accPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));

	}
//Hard Coded Data: hard coded data means you are using directly either in your page class or test class or directly in your library
//we can say this is hard coded but in our dataProvider
//if we use other 3rd party resource like excel sheet/test file etc to maintaine below data it increase 3rd party dependency,
//sheet can be deleted,corrupted or updated by someone in model framework we won't use it ,foe easier maintinance we 
//maintain data in script itself
//excelsheet create proble in automation if multipe person are using same sheet for automation and some are editing,deleting?
	@DataProvider
	public Object[][] getSearchData() {
		return new Object[][] {
			{"MacBook", "MacBook Pro", 4},
			{"MacBook", "MacBook Air", 4},
			{"iMac", "iMac", 3},
			{"Samsung", "Samsung SyncMaster 941BW", 1}
	   };
	}
	
	@DataProvider
	public Object[][] getSearchExcelTestData(){
		return ExcelUtil.getTestData(AppConstants.PRODUCT_DATA_SHEET_NAME);
	}
	
   //@Test(dataProvider = "getSearchData")
	@Test(dataProvider = "getSearchExcelTestData")
	public void productImagesTest(String searchKey, String productName, String imageCount) {
//if we maintain 3 columns we have to supply 3 parameters in the method
//this method will run 3 times on bases of given data (as we maintained 3 rows in dataProvider)
		searchResultsPage = accPage.doSearch(searchKey);
		productInfoPage = searchResultsPage.selectProduct(productName);
		//Assert.assertEquals(productInfoPage.getProductImagesCount(), imageCount);
		Assert.assertEquals(String.valueOf(productInfoPage.getProductImagesCount()),imageCount);
//java to java :String.valueOf ;External Source to java: toString method
	}

//for assertion(having key value format data)HashMap is better option over List in that we have to maintain loop 
//if a specific assertion got fail it doesn't go to the next assertion instead it terminate the test/test is marked as fail
//that's it is better to have one test one assertion, we should not write hard(multiple) assertion for a single test
//so testng provide an option of soft assertion : SoftAssert is a class in testng
//Assert class is the hard assert all the methods inside it is static as we are using them by there class name
//in case of soft assert if any specific assert fail it go to the next one,it go to all assert irrespective of test pass or fail
//in softAsset it is compulsory to write assertAll
        
 //when we are verifying specific thing we can use hard assertion but when we have multiple checks for same functionality
 // we have to go with Soft Assertion
	
	@Test
	public void productsInfoTest() {//without data provider
		searchResultsPage = accPage.doSearch("MacBook");
		productInfoPage = searchResultsPage.selectProduct("MacBook Pro");
		Map<String, String> productDetailsMap = productInfoPage.getProductDetails();
		softAssert.assertEquals(productDetailsMap.get("Brand"), "Apple");
		softAssert.assertEquals(productDetailsMap.get("Availability"), "In Stock");
		softAssert.assertEquals(productDetailsMap.get( "Product Code"),  "Product 18");
		softAssert.assertEquals(productDetailsMap.get("Reward Points"), "800");
		softAssert.assertEquals(productDetailsMap.get("price"), "$2,000.00");
		softAssert.assertEquals(productDetailsMap.get("extaxprice"), "$2,000.00");
		//Assert.assertEquals(productDetailsMap.get("extaxprice"), "$2,000.00");//deliberately left hard assert
        softAssert.assertAll();
        }
	
	
}
