package com.qa.opencart.utils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qa.opencart.exception.FrameworkException;
import com.qa.opencart.factory.DriverFactory;
import com.qa.opencart.pages.LoginPage;

import io.qameta.allure.Step;

public class ElementUtil {
	private WebDriver driver;
	private JavaScriptUtil jsUtil;

	private static final Logger log = LogManager.getLogger(ElementUtil.class);
//constructor is created to call driver
	public ElementUtil(WebDriver driver) {
		this.driver = driver;
		jsUtil = new JavaScriptUtil(driver);
	}
	
	private void isHighlight(WebElement element) {
		if (Boolean.parseBoolean(DriverFactory.highlight)) {// to convert String to boolean
			jsUtil.flash(element);
		//	jsUtil.drawBorder(element); if we want to draw border we can call it
		}
	}

	
	public By getBy(String locatorType, String locatorValue) {
		By by = null;
		switch (locatorType.toLowerCase().trim()) {
		case "id":
			by = By.id(locatorValue);
			break;
		case "name":
			by = By.name(locatorValue);
			break;
		case "class":
			by = By.className(locatorValue);
			break;
		case "xpath":
			by = By.xpath(locatorValue);
			break;
		case "css":
			by = By.cssSelector(locatorValue);
			break;
		case "linktext":
			by = By.linkText(locatorValue);
			break;
		case "partiallinktext":
			by = By.partialLinkText(locatorValue);
			break;
		case "tag":
			by = By.tagName(locatorValue);
			break;
		default:
			System.out.println("wrong locator type is passed: " + locatorType);
			throw new FrameworkException("WRONG LOCATOR TYPE");
		}
		return by;
	}
	
	
	
	private void logLocator(By locator) {
		log.info("locator : " + locator);
	}

	private void logLocator(By locator, String value) {
		log.info("locator : " + locator + "-----value----" + value);
	}
	
	
	//locatorType="id"; locatorValue="input-email",value="pooja@gmail.com"
		public void doSendKeys(String locatorType, String locatorValue, String value) {
			getElement(locatorType, locatorValue).sendKeys(value);
		}

		@Step("entering value {1} to element : {0}")
		public void doSendKeys(By locator, String value) {
			logLocator(locator, value);
			getElement(locator).sendKeys(value);
		}

		

		@Step("clicking on element : {0}")
		public void doClick(By locator) {
			logLocator(locator);
			getElement(locator).click();
		}

		public void doClick(String locatorType, String locatorValue) {
			getElement(locatorType, locatorValue).click();
		}

		public String doElementGetText(By locator) {
			return getElement(locator).getText();
		}

		public String doElementGetText(String locatorType, String locatorValue) {
			return getElement(locatorType, locatorValue).getText();
	}
	
	public WebElement getElement(By locator) {
		logLocator(locator);
		WebElement element = driver.findElement(locator);
		isHighlight(element);
		return element;
	}

	public WebElement getElement(String locatorType, String locatorValue) {
		WebElement element = driver.findElement(getBy(locatorType, locatorValue));
		isHighlight(element);
		return element;
	}


	public String doGetElementAttribute(By locator, String attrName) {
		return getElement(locator).getAttribute(attrName);
	}

	// WAF :capture the text of all the page links & return List<String>
	public List<String> getElementsTextList(By locator) {
		List<WebElement> eleList = getElements(locator);
		List<String> eleTextList = new ArrayList<String>();// pc=0{}
		for (WebElement e : eleList) {
			String text = e.getText();
			if (text.length() != 0) {
				eleTextList.add(text);
			}
		}
		return eleTextList;
	}

	// WAF : capture specific attribute from the List

	public List<String> getElementsAttributeList(By locator, String attrName) {
		List<WebElement> eleList = getElements(locator);
		List<String> eleAttrList = new ArrayList<String>();// pc=0{}
		for (WebElement e : eleList) {
			String attrValue = e.getAttribute(attrName);
			eleAttrList.add(attrValue);
		}
		return eleAttrList;
	}

	public int getElementsCount(By locator) {
		return getElements(locator).size();
	}

	public List<WebElement> getElements(By locator) {
		return driver.findElements(locator);
	}

	public boolean checkSingleElementPresent(By locator) {
		return driver.findElements(locator).size() == 1 ? true : false;
	}

	public boolean checkMultipleElementPresent(By locator) {
		return driver.findElements(locator).size() > 0 ? true : false;
		// return driver.findElements(locator).size() >=1 ? true : false;
	}

	public boolean checkMultipleElementPresent(By locator, int totalElements) {
		return driver.findElements(locator).size() == totalElements ? true : false;
		// return driver.findElements(locator).size() >=1 ? true : false;
	}

	public void search(By searchField, By suggestions, String searchKey, String suggName) throws InterruptedException {
		doSendKeys(searchField, searchKey);

		Thread.sleep(3000);
		List<WebElement> suggList = getElements(suggestions);
		System.out.println(suggList.size());
		for (WebElement e : suggList) {
			String text = e.getText();
			System.out.println(text);
			if (text.contains(suggName)) {
				e.click();
				break;
			}

		}

	}

	public void clickOnElement(By locator, String eleText) {
		List<WebElement> eleList = getElements(locator);
		System.out.println(eleList.size());
		for (WebElement e : eleList) {
			String text = e.getText();
			System.out.println(text);
			if (text.contains(eleText)) {
				e.click();
				break;
			}
		}
	}

	// ******************SELECT DROP DOWN UTIL*********************//
//creating wrapper top of select

	private Select createSelect(By locator) {
		Select select = new Select(getElement(locator));
		return select;
	}// private to avoid unnecessary select creation

	public void doSelectDropDownByIndex(By locator, int index) {
		createSelect(locator).selectByIndex(index);
	}

	public void DoSelectDropDownByVisible(By locator, String text) {
		createSelect(locator).selectByVisibleText(text);
	}

	public void DoSelectDropDownByValue(By locator, String text) {
		createSelect(locator).selectByVisibleText(text);
	}

	/**
	 * in this generic method we don't handle size as in few case header is included
	 * and in some cases header is excluded so we do it in caller method
	 * 
	 */
	public int getDropDownOptionCount(By locator) {
		return createSelect(locator).getOptions().size();
	}

	public List<String> getDropDownOptions(By locator) {
		List<WebElement> optionsList = createSelect(locator).getOptions();
		List<String> optionsTextList = new ArrayList<String>();
		for (WebElement e : optionsList) {
			String text = e.getText();
			// to exclude we can add a line of code
			// if text is country don;t add it:programmatically we can add
			optionsTextList.add(text);
		}
		return optionsTextList;
	}

	public void selectDropDownOption(By locator, String dropDownValue) {
		List<WebElement> optionsList = createSelect(locator).getOptions();
		System.out.println(optionsList.size());
		for (WebElement e : optionsList) {
			String text = e.getText();
			System.out.println(text);
			if (text.equals(dropDownValue)) {
				e.click();
				break;
			}
		}

	}

	// without Select class
	public void selectDropDownValue(By locator, String value) {
		List<WebElement> optionList = getElements(locator);
		for (WebElement e : optionList) {
			/**
			 * this code will work problem with "if(e.getText().equals(value))" this code is
			 * that it will not helping much in debugging
			 */
			String text = e.getText();
			if (text.equals(value)) {
				// if(e.getText().equals(value)) {
				e.click();
				break;
			}
		}
	}

	public boolean isDropDownMultiple(By locator) {
		return createSelect(locator).isMultiple() ? true : false;
	}

	/**
	 * this method is used to select the values from the drop down.It can select 1:
	 * single selection 2: multiple selection 3: all selection : please pass "select
	 * all" as a value to select all the value
	 * 
	 * @param locator
	 * @param values
	 */
	public void selectDropDownMultipleValues(By locator, By optionLocator, String... values) {

		// to pass multiple value we use spread operator
		// in java ... these three dots are called var arguments
		// int.. values : it will give int array/[]
		/**
		 * 1>why we always call getElement method,why we are not passing by locator
		 * directly becz select class waiting for WebElement not by locator 2>why don't
		 * we supply by WebElement directly: if we want to supply webElement than use
		 * have to create the WebElement unnecessary,which will hit server unnecessary
		 * here i have just maintain the By locator,let the method decide when to call
		 * the server and when to create the WebElement
		 */
		if (isDropDownMultiple(locator)) {
			if (values[0].equalsIgnoreCase("select all")) {
				List<WebElement> optionList = getElements(optionLocator);
				for (WebElement e : optionList) {
					e.click();
				}

			} else {
				for (String value : values) {

					createSelect(locator).selectByVisibleText(value);
				}
			}
		}
	}

//*************ACTION UTIL******************//
	public void doActionsSendKeys(By locator, String value) {
		Actions act = new Actions(driver);
		act.sendKeys(getElement(locator), value).perform();
	}

	public void doActionsClick(By locator) {
		Actions act = new Actions(driver);
		act.click(getElement(locator)).perform();
	}

	public void ContextClick(By locator, By optionLocator, String req) {
		Actions act = new Actions(driver);
		act.contextClick(getElement(locator)).perform();
		List<WebElement> optionList = getElements(optionLocator);
		System.out.println(optionList.size());
		for (WebElement e : optionList) {
			String text = e.getText();
			System.out.println(text);
			if (text.equals(req)) {
				e.click();
				break;
			}
		}
	}

	public void doActionsSendKeysWithPause(By locator, String value) {
		Actions act = new Actions(driver);
		char val[] = value.toCharArray();
		for (char c : val) {
			act.sendKeys(getElement(locator), String.valueOf(c)).pause(500).build().perform();
			// keep it pause(500) don't pass as a parameter if someone pass it as 5000 ,it
			// become very slow
		}
	}

	public void twoLevelMenuHandle(By parentMenuLocator, By ChildMenuLocator) {
		Actions act = new Actions(driver);
		act.moveToElement(getElement(parentMenuLocator)).perform();
		doClick(ChildMenuLocator);
	}

	public void fourLevelMenuHandle(By parentMenuLocator, By firstChildMenuLocator, By secondChildLocator,
			By thirdChildMenuLocator) throws InterruptedException {
//4 menu item	
		doClick(parentMenuLocator);
		Actions act = new Actions(driver);
		Thread.sleep(1000);
		act.moveToElement(getElement(firstChildMenuLocator)).build().perform();
		Thread.sleep(1000);
		act.moveToElement(getElement(secondChildLocator)).build().perform();
		Thread.sleep(1000);
		doClick(thirdChildMenuLocator);
	}

	

	
	// ************************************WAIT UTILS******************************//

	/**
	 * whenever we open any page DOM will come 1st An expectation for checking that an element is present on the DOM of a page.
	 *  This does not necessarily mean that the element is visible on the page.
	 * @param locator
	 * @param timeout
	 * @return
	 */
	public WebElement waitForPresenceOfElement(By locator, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		isHighlight(element);
		return element;
	}

	/**
	 * whenever we open any page DOM will come 1st An expectation for checking that
	 * an element is present on the DOM of a page. This does not necessarily mean
	 * that the element is visible on the page.
	 * 
	 * @param locator
	 * @param timeout
	 * @param intervalTime
	 * @return
	 */
	public WebElement waitForPresenceOfElement(By locator, int timeout, int intervalTime) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout), Duration.ofSeconds(intervalTime));
		WebElement element =  wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		isHighlight(element);
		return element;
	}

	/**
	 * An expectation for checking that there is at least one element present on a
	 * web page.
	 * 
	 * @param locator
	 * @param timeout
	 * @return
	 */

	public List<WebElement> waitForPresenceOfElements(By locator, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
	
	}

	/**
	 * An expectation for checking that an element is present on the DOM of a page
	 * and visible. Visibility means that the element is not only displayed but also
	 * has a height and width that is greater than 0.
	 * 
	 * @param locator
	 * @param timeout
	 * @return This method is better from the user point of view as it have
	 *         visibility
	 */

	@Step("waiting for element : {0} with timeout {1}")
	public WebElement waitForVisibilityOfElement(By locator, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		isHighlight(element);
		return element;
	}

	/**
	 * An expectation for checking that an element is present on the DOM of a page
	 * and visible. Visibility means that the element is not only displayed but also
	 * has a height and width that is greater than 0.
	 * 
	 * @param locator
	 * @param timeout
	 * @param intervalTime
	 * @return
	 */

	public WebElement waitForVisibilityOfElement(By locator, int timeout, int intervalTime) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout), Duration.ofSeconds(intervalTime));
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		isHighlight(element);
		return element;
	}

	public void doClickWithWait(By locator, int timeout) {
		waitForVisibilityOfElement(locator, timeout).click();
	}

	public void doSendKeysWithWait(By locator, int timeout, String value) {
		waitForVisibilityOfElement(locator, timeout).sendKeys(value);

	}

	/**
	 * An expectation for checking that all elements present on the web page that
	 * match the locator are visible. Visibility means that the elements are not
	 * only displayed but also have a height and width that is greater than 0.
	 * 
	 * @param locator
	 * @param timeout
	 * @return List<WebElement>
	 */

	public List<WebElement> waitForVisibilityOfElements(By locator, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	}

	/**
	 * check if supplied part of title is present or not
	 * 
	 * @param titleFraction
	 * @param timeOut
	 * @return
	 */
	public String waitForTitleContains(String titleFraction, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		try {
			if (wait.until(ExpectedConditions.titleContains(titleFraction))) {
				return driver.getTitle();
			}
		} catch (TimeoutException e) {
			System.out.println(titleFraction + ": Title value is not present...");
			e.printStackTrace();
		}
		return null;// return driver.getTitle();
	}

	/**
	 * check for exact title
	 * 
	 * @param title
	 * @param timeOut
	 * @return
	 */
	@Step("waiting for the page title : {0} and timeout: {1}") // to print parameter
	public String waitForTitleIs(String title, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		try {
			if (wait.until(ExpectedConditions.titleIs(title))) {
				return driver.getTitle();
			}
		} catch (TimeoutException e) {
			System.out.println(title + ": Title value is not present...");
			e.printStackTrace();
		}
		return null;
	}

	@Step("waiting for url : {0} and timeout{1}")
	public String waitForUrlContains(String urlFraction, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		try {
			if (wait.until(ExpectedConditions.urlContains(urlFraction))) {
				return driver.getCurrentUrl();
			}
		} catch (TimeoutException e) {
			System.out.println(urlFraction + ": Title value is not present...");
			e.printStackTrace();
		}
		return null;
	}

	public String waitForUrlToBe(String url, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		try {
			if (wait.until(ExpectedConditions.urlToBe(url))) {
				return driver.getCurrentUrl();
			}
		} catch (TimeoutException e) {
			System.out.println(url + ": Title value is not present...");
			e.printStackTrace();
		}
		return null;
	}

//****************************ALERT UTIL*****************************************//

	public Alert waitForJSAlert(int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		return wait.until(ExpectedConditions.alertIsPresent());
	}

	public void acceptJSAlert(int timeout) {
		waitForJSAlert(timeout).accept();
	}

	public void dismissJSAlert(int timeout) {
		waitForJSAlert(timeout).dismiss();
	}

	public String getJSAlertText(int timeout) {
		return waitForJSAlert(timeout).getText();
	}

	public void enterValueOnJSAlert(int timeout, String value) {
		waitForJSAlert(timeout).sendKeys(value);

	}

	// ****************************FRAME UTIL*****************************************//

	public void waitForFrameByLocator(By frameLocator, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
	}

	public void waitForFrameByIndex(int frameIndex, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameIndex));
	}

	public void waitForFrameByIDOrName(String IDOrName, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(IDOrName));
	}

	public void waitForFrameByElement(WebElement frameElement, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
	}

	// ****************************WINDOW UTIL*****************************************//

	public boolean checkNewWindowExist(int timeOut, int expectedNumberOfWindows) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		try {
			if (wait.until(ExpectedConditions.numberOfWindowsToBe(expectedNumberOfWindows))) {
				return true;
			}
		} catch (TimeoutException e) {
			System.out.println("number of windows are not same...");
		}
		return false;
	}

	/**
	 * An expectation for checking an element is visible and enabled such that you
	 * can click it.
	 * 
	 * @param locator
	 * @param timeOut
	 */
	public void clickElementWhenReady(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		try {
			wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
		} catch (TimeoutException e) {
			System.out.println("element is not clickable or enable...");
		}
	}

	public WebElement waitForElementWithFluentWait(By locator, int timeOut, int intervalTime) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(timeOut))
				.pollingEvery(Duration.ofSeconds(intervalTime))
				.withMessage("-- time out is done... element is not found...").ignoring(NoSuchElementException.class)
				.ignoring(ElementNotInteractableException.class);// also can add

		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		isHighlight(element);
		return element;
	}

	// waiting for the frame while didn't get any exception
	public void waitForFrameWithFluentWait(String frameIROrName, int timeOut, int intervalTime) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(timeOut))
				.pollingEvery(Duration.ofSeconds(intervalTime))
				.withMessage("-- time out is done...frame is not found...").ignoring(NoSuchFrameException.class);

		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameIROrName));
	}

	public Alert waitForJSAlertWithFluentWait(int timeOut, int intervalTime) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(timeOut))
				.pollingEvery(Duration.ofSeconds(intervalTime))
				.withMessage("-- time out is done...Alert is not appeared...").ignoring(NoSuchFrameException.class);

		return wait.until(ExpectedConditions.alertIsPresent());
	}

//**********************************CUSTOM WAIT***************************************//	
//when WebdriverWait/FluentWait nothing is working we can go with this
	public WebElement retryingElement(By locator, int timeOut) {
//here timeOut is related to number of attempt
		WebElement element = null;
		int attempts = 0;
//as number of iteration is not fixed we will use while loop
		while (attempts < timeOut) {
			try {
				element = getElement(locator);
				System.out.println("element is found...." + locator + " in attempt " + attempts);
				break;
			} catch (NoSuchElementException e) {
				System.out.println("element is not found...." + locator + " in attempt " + attempts);
				try {
					Thread.sleep(500);// default polling time = 500 ms
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

			}

			attempts++;
		}

		if (element == null) {
			System.out.println("element is not found....tried for " + timeOut + " times " + " with the interval of "
					+ 500 + " milli seconds ");
			throw new FrameworkException("No Such Element");
		}

		isHighlight(element);
		return element;
	}

	public WebElement retryingElement(By locator, int timeOut, int intervalTime) {
//intervalTime is in milli seconds multiple of 1000 is have to supply
		WebElement element = null;
		int attempts = 0;

		while (attempts < timeOut) {
			try {
				element = getElement(locator);
				System.out.println("element is found...." + locator + " in attempt " + attempts);
				break;
			} catch (NoSuchElementException e) {
				System.out.println("element is not found...." + locator + " in attempt " + attempts);
				try {
					Thread.sleep(intervalTime);// custom polling time
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

			}

			attempts++;
		}

		if (element == null) {
			System.out.println("element is not found....tried for " + timeOut + " times " + " with the interval of "
					+ 500 + " milli seconds ");
			throw new FrameworkException("No Such Element");
		}

		isHighlight(element);
		return element;
	}

	public boolean isPageLoaded(int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		String flag = wait.until(ExpectedConditions.jsReturnsValue("return document.readyState == 'complete'"))
				.toString();
		return Boolean.parseBoolean(flag);
	}

}
