package com.qa.opencart.utils;

import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class JavaScriptUtil {
	private WebDriver driver;// this driver is initialized with the help of constructor
	private JavascriptExecutor js;

	public JavaScriptUtil(WebDriver driver) {// constructor to initialize driver
		this.driver = driver;
		js = (JavascriptExecutor) this.driver;
	}

	public String getTitleByJS() {
		return js.executeScript("return document.title").toString();
	}

	public String getURLByJS() {
		return js.executeScript("return document.URL").toString();
	}

	public void generateJSAlert(String msg) {
		// it is not returning anything:undefined;just generating an alert
		js.executeScript("alert('" + msg + "')");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.switchTo().alert().accept();

	}

	public void generateJConfirm(String msg) {
		js.executeScript("confirm('" + msg + "')");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.switchTo().alert().accept();

	}

	public void generateJSPrompt(String msg, String value) {
		js.executeScript("prompt('" + msg + "')");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Alert alert = driver.switchTo().alert();
		alert.sendKeys(value);
		alert.accept();
	}

	public void goBackWithJS() {
		js.executeScript("history.go(-1)");
	}

	public void goForwardWithJS() {
		js.executeScript("history.go(1)");
	}

	public void pageRefreshWithJS() {
		js.executeScript("history.go(0)");
	}

	public String getPageInnerTextWithJS() {
		return js.executeScript("return document.documentElement.innerText").toString();
		// writing return in script is important else it will just execute the script on
		// browser
	}
////for horizontal scrolling
//	public void scrollPageRight(int x, int y) {
//		js.executeScript("window.scrollBy(x,y)");
//	}	
	
//for vertical scrolling
	public void scrollPageDown() {
		js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
	}

	public void scrollPageUp() {
		js.executeScript("window.scrollTo(document.body.scrollHeight,0)");
	}

	public void scrollPageDown(String height) {
		js.executeScript("window.scrollTo(0,'" + height + "')");
	}

	public void scrollPageUp(String height) {
		js.executeScript("window.scrollTo('" + height + "',0)");
	}

	public void scrollPageMidDown() {
		js.executeScript("window.scrollTo(0,document.body.scrollHeight/2)");
	}

	public void scrollPageMidUp() {
		js.executeScript("window.scrollTo(document.body.scrollHeight/2,0)");
	}

	/**
	 * use to scroll down till particular element
	 * 
	 * @param element
	 */
	public void scrollIntoView(WebElement element) {
		js.executeScript("arguments[0].scrollIntoView", element);
	}

	/**
	 * different resolution testing example:"document.body.style.zoom ='400.0%' "
	 * 
	 * @param zoomPercentage
	 */
	public void zoomChromeEdgeSafari(String zoomPercentage) {
		String zoom = "document.body.style.zoom = '" + zoomPercentage + "%'";
		js.executeScript(zoom);
	}

	/**
	 * example:"document.body.style.MozTransform ='scale(0.5)'; " //1.0 means 100%
	 * 
	 * @param zoomPercentage
	 */
	public void zoomFirefox(String zoomPercentage) {
		String zoom = "document.body.style.MozTransform = 'scale(" + zoomPercentage + ")'";
		js.executeScript(zoom);
	}

	public void drawBorder(WebElement element) {
		// color could be any thing,to make it more dense we can increase the px
		js.executeScript("arguments[0].style.border='3px solid red'", element);
	}

	public void flash(WebElement element) {
		String backgroungColor = element.getCssValue("backgroundColor");
		for (int i = 0; i < 10; i++) {
			changeColor("rgb(0,200,0)", element);
			changeColor(backgroungColor, element);
		}
	}

	private void changeColor(String color, WebElement element) {
		js.executeScript("arguments[0].style.backgroundColor= '" + color + "'", element);
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
	/**
	 * if normal .click()is not working/action class .click()is not working then we can go with jsclick or
	 * Javascript executor
	 *it is not stimulating the user action we have to avoid this,it is not recommended
	 *normal user uses the normal click or move to element then click =action click 
	 * @param element
	 */
public void clickElementByJS(WebElement element) {
	js.executeScript("arguments[0].click();", element);
}	
	/**
	 * only with id option is there ,with other attribute we can't able to send keys
	 * @param id
	 * @param value
	 */
public void sendKeysUsingWithId(String id,String value) {
	js.executeScript("document.getElementById('"+id+"').value='"+value+"'");
}	
	
public void mandField(String script) {
	String mand_text = js.executeScript(script).toString();
	System.out.println(mand_text);
	if (mand_text.contains("*")) {
		System.out.println("mandatory field");
	}
	
}
}
