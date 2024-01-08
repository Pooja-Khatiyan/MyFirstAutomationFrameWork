package com.qa.opencart.pages;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.utils.ElementUtil;

public class ProductInfoPage {
	private WebDriver driver;
	private ElementUtil eleUtil;
	private By productHeader = By.cssSelector("div#content h1");
	private By productImages = By.cssSelector("ul.thumbnails img");
	private By productMetaData = By.xpath("(//div[@class='btn-group']//following-sibling::ul)[2]//li");
	private By productPriceData = By.xpath("(//div[@id='content']//ul[@class='list-unstyled'])[2]/li");
	
	//private Map<String, String> productMap = new HashMap<String, String>();// HashMap is a class & Map is an interface:topcasting
	//HashMap doesn't maintain any order
	//private Map<String, String> productMap = new LinkedHashMap<String, String>();//LinkedHashMap maintains order
	private Map<String, String> productMap = new TreeMap<String, String>();//maintain in alphabetic/sorted order
	// Page const...
	public ProductInfoPage(WebDriver driver) {
		this.driver = driver;
		eleUtil = new ElementUtil(this.driver);
	}

	public String getProductHeaderName() {
		String productHeaderVal = eleUtil.doElementGetText(productHeader);
		System.out.println("product header: " + productHeaderVal);
		return productHeaderVal;
	}

	public int getProductImagesCount() {
		int imagesCount = eleUtil.waitForVisibilityOfElements(productImages, AppConstants.MEDIUM_DEFAULT_WAIT).size();
		System.out.println("Product " + getProductHeaderName() + "images count :" + imagesCount);
		return imagesCount;
	}

	private void getProductMetaData() {
		List<WebElement> metaDataList = eleUtil.waitForVisibilityOfElements(productMetaData,
				AppConstants.MEDIUM_DEFAULT_WAIT);
//we have to write for loop 1st to iterate this data
		for (WebElement e : metaDataList) {
			String metaData = e.getText();
			String metaKey = metaData.split(":")[0].trim();
			String metaVal = metaData.split(":")[1].trim();
			productMap.put(metaKey, metaVal);
		}
	}

	private void getProductPriceData() {//if we want to give access to user we can use public modifier same as in above
		List<WebElement> metaPriceList = eleUtil.waitForVisibilityOfElements(productPriceData,
				AppConstants.MEDIUM_DEFAULT_WAIT);
		String productPrice = metaPriceList.get(0).getText();
		String productExTaxPrice = metaPriceList.get(1).getText().split(":")[1].trim();
// Ex Tax: $2,000.00 ,we are taking 1st position value,ignoring 0th position value
		productMap.put("price", productPrice);
		productMap.put("extaxprice", productExTaxPrice);
	}

	public Map<String, String> getProductDetails() {
		productMap.put("productname", getProductHeaderName());
		getProductMetaData();
		getProductPriceData();
		System.out.println(productMap);//store value randomly
		return productMap;
	}
	
	
	
}
