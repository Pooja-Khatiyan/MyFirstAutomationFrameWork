package com.qa.opencart.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.utils.ElementUtil;

public class AccountsPage {
	private WebDriver driver;
	private ElementUtil eleUtil;
	private By logoutLink = By.linkText("Logout");
	private By search = By.name("search");
	private By searchIcon = By.cssSelector("div#search button");// button/i[@class='fa fa-search']
	private By accHeaders = By.cssSelector("div#content > h2");// div[@id='content']//h2 :xpath
	private By footerLink = By.xpath("//footer//a");
	// page const..
	public AccountsPage(WebDriver driver) {
		this.driver = driver;
		eleUtil = new ElementUtil(this.driver);
	}

	// page actions:

	public String getAccPageTitle() {
		String title = eleUtil.waitForTitleIs(AppConstants.ACCOUNTS_PAGE_TITLE, AppConstants.SHORT_DEFAULT_WAIT);
		// String title = driver.getTitle();
		System.out.println("acc page title: " + title);
		return title;
	}

	public String getAccPageURL() {
		String url = eleUtil.waitForUrlContains(AppConstants.ACCOUNTS_PAGE_URL_FRACTION,
				AppConstants.SHORT_DEFAULT_WAIT);
		// String url = driver.getCurrentUrl();
		System.out.println("Acc page url: " + url);
		return url;
	}

	public boolean isLogoutLinkExist() {
		return eleUtil.waitForVisibilityOfElement(logoutLink, AppConstants.SHORT_DEFAULT_WAIT).isDisplayed();
	}

	public void logout() {
		if (isLogoutLinkExist()) {
			eleUtil.doClick(logoutLink);
		}
	}

	public boolean isSearchFieldExist() {
		return eleUtil.waitForVisibilityOfElement(search, AppConstants.SHORT_DEFAULT_WAIT).isDisplayed();
	}

	public List<String> getAccountsHeaders() {
		List<WebElement> headersList = eleUtil.waitForVisibilityOfElements(accHeaders,
				AppConstants.MEDIUM_DEFAULT_WAIT);
		List<String> headersValList = new ArrayList<String>();
		for (WebElement e : headersList) {
			String text = e.getText();
			headersValList.add(text);
		}
		return headersValList;

	}

	// Search behavior
	public SearchResultsPage doSearch(String searchKey) {
		eleUtil.waitForVisibilityOfElement(search, AppConstants.MEDIUM_DEFAULT_WAIT).clear();//at time of using dataprovider
		//we need this line as our test get failed
		eleUtil.waitForVisibilityOfElement(search, AppConstants.MEDIUM_DEFAULT_WAIT).sendKeys(searchKey);
		eleUtil.doClick(searchIcon);
		return new SearchResultsPage(driver);// in order to create this method we should have this class : what kind of
												// development
		// we are doing here : this is called test driven development.
	}
//	public List<String> getFooter() {
//		List<WebElement>footerList =eleUtil.getElements(footerLink);
//		List<String> footersList= new ArrayList<String>();
//		for(WebElement e : footerList) {
//		String list =e.getText();
//		System.out.println("header list: " + list);
//		footersList.add(list);
//		}
//		return footersList;
//	}
}
