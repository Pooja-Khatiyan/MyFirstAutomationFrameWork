package com.qa.opencart.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;//this class help to get the properties 
//import java.util.logging.FileHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;//for FileHandler
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.qa.opencart.exception.FrameworkException;

//code for driver initialization
public class DriverFactory {
	WebDriver driver;
	Properties prop;
	OptionsManager optionsManager;
	public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<WebDriver>();

	private static final Logger log = LogManager.getLogger(DriverFactory.class);
	public static String highlight = null;

	public WebDriver initializeDriver(Properties prop) {
		String browserName = prop.getProperty("browser");
		// String browserName = System.getProperty("browser");
		//System.out.println("browser name is: " + browserName);
        log.info("browser name is: " + browserName);
		
		highlight = prop.getProperty("highlight");
		optionsManager = new OptionsManager(prop);

		switch (browserName.toLowerCase().trim()) {
		case "chrome":
		log.info("Running it on chrome browser...");	
			if (Boolean.parseBoolean(prop.getProperty("remote"))) {
				// run it on grid:
				log.info("Running it on remote machine");
				initRemoteDriver(browserName);
			} else {
				// run it on local:
				// driver = new ChromeDriver(optionsManager.getChromeOptions());
				log.info("Running it on local");
				tlDriver.set(new ChromeDriver(optionsManager.getChromeOptions()));
			}
			break;
			
		case "firefox":
			if (Boolean.parseBoolean(prop.getProperty("remote"))) {
				// run it on grid:
				initRemoteDriver(browserName);
			} else {
			// driver = new FirefoxDriver(optionsManager.getFirefoxOptions());
			tlDriver.set(new FirefoxDriver(optionsManager.getFirefoxOptions()));
			}
			break;
			
		case "edge":
			if (Boolean.parseBoolean(prop.getProperty("remote"))) {
				// run it on grid:
				initRemoteDriver(browserName);
			} else {
			// driver = new EdgeDriver(optionsManager.getEdgeOptions());
			tlDriver.set(new EdgeDriver(optionsManager.getEdgeOptions()));// every thread will get their own EdgeDriver copy
			}													
			break;
			
		case "safari":
			// driver = new SafariDriver();
			tlDriver.set(new SafariDriver());
			break;

		default:
			//System.out.println("please pass the right browser name... " + browserName);
			log.warn("please pass the right browser name... " + browserName);
			throw new FrameworkException("No Browser Found...");
		}
//		driver.manage().deleteAllCookies(); with normal driver
//		driver.manage().window().maximize();
//		driver.get(prop.getProperty("url"));
//		return driver;

		getDriver().manage().deleteAllCookies(); // with ThreadLocal
		getDriver().manage().window().maximize();
		getDriver().get(prop.getProperty("url"));
		return getDriver();

	}
	/**
	 * run tests on grid 
	 * @param browserName
	 */

	private void initRemoteDriver(String browserName) {
		//System.out.println("Running tests on GRID with browser: " + browserName);
		log.info("Running tests on GRID with browser: " + browserName);
		try {
			switch (browserName.toLowerCase().trim()) {
			case "chrome":
		tlDriver.set(new RemoteWebDriver(new URL(prop.getProperty("huburl")), optionsManager.getChromeOptions()));
				break;
			case "firefox":
		tlDriver.set(new RemoteWebDriver(new URL(prop.getProperty("huburl")), optionsManager.getFirefoxOptions()));
				break;
			case "edge":
		tlDriver.set(new RemoteWebDriver(new URL(prop.getProperty("huburl")), optionsManager.getEdgeOptions()));
				break;

			default:
				//System.out.println("wrong brsowser info... can not run on grid remote machine...");
				log.warn("wrong brsowser info... can not run on grid remote machine...");
				break;
			}
		} catch (MalformedURLException e) {

		}
	}

	public static WebDriver getDriver() {// it will local copy of the driver
		return tlDriver.get();
	}

/**
 *this method helps to initialized /to read the config.properties,selenium can not connect with the config.property,so we have to
 * write a java code.for this we maintain a properties reference here properties is a class which  already there in java
 */ 
	public Properties initializedProp() {
		// mvn clean install -Denv="qa"
		FileInputStream ip = null;
		prop = new Properties(); // object of properties
		String envName = System.getProperty("env");
		//System.out.println("environment name is: " + envName);
		log.info("environment name is: " + envName);
		
		try {
			if (envName == null) {
				log.warn("your env is null... hence running test on QA env...");
				//System.out.println("your env is null... hence running test on QA env...");
				ip = new FileInputStream("./src/test/resources/config/config.qa.properties");
			
			} else {
				switch (envName.toLowerCase().trim()) {
				case "qa":
					ip = new FileInputStream("./src/test/resources/config/config.qa.properties");
					break;
				case "dev":
					ip = new FileInputStream("./src/test/resources/config/config.dev.properties");
					break;
				case "stage":
					ip = new FileInputStream("./src/test/resources/config/config.stage.properties");
					break;
				case "uat":
					ip = new FileInputStream("./src/test/resources/config/config.uat.properties");
					break;
				case "prod":
					ip = new FileInputStream("./src/test/resources/config/config.properties");
					break;

				default:
					log.error("wrong env name : " + envName);
					//System.out.println("please pass the right env name... " + envName);
					throw new FrameworkException("Wrong Environment Name: " + envName);
				}
			}
		} catch (FileNotFoundException e) {

		}
		try {
			prop.load(ip);
			log.info(prop);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		try {
//			FileInputStream ip = new FileInputStream("./src/test/resources/config/config.properties");
//			// to make connect with the config we use this class of java . means from the root of the current project

//			prop.load(ip);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return prop;
	}

	/**
	 * take screenshot
	 * 
	 * @param methodName
	 * @return
	 */

	public static String getScreenshot(String methodName) {// here we have to pass webelement
		// to take the screenshot of only specific element haing issue ,draw boarder by
		// js
		File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);// screenshot is taken in the
																						// form of file or file object

		String path = System.getProperty("user.dir") + "/screenshot/" + methodName + "_" + System.currentTimeMillis()
				+ ".png";
		File destination = new File(path);// to supply file object we have create a desination path
//		try {
//		Path srcPath = srcFile.toPath();
//        Path destPath = destination.toPath();
//        Files.copy(srcPath, destPath, StandardCopyOption.REPLACE_EXISTING);
//    } catch (IOException e) {

		try {
			FileHandler.copy(srcFile, destination);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return path;
//takesScreenshot : interface from selenium ;here we are typecasting driver into takesScreenshot, entire thing become TakesScreenshot ,type of screenshot could be anything here we go for FILE
//our task is to supply source file to the path(we have to move this screenshoot to our customized path) : both are mismatch one is file object and another is String 
//i.e we are creating one more destination file object which is pointing to this path		
//interview question: how we will get our project directory through => System.getProperty("user.dir") i.e POMSeries
//name is already predefine user.dir i.e current directory. we are appending with time to avoid image replacement with 
//previously saved image : to get unique name every time
		// System.out.println("user directory: " + System.getProperty("user.dir"));
		// System.out.println("screenshot path: " + path);

	}
}
