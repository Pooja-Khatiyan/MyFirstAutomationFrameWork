package com.qa.opencart.factory;

import java.util.Properties;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariOptions;

public class OptionsManager {
//created to manage headless ,incognito or remote mode
//we don't need driver here we need prop created in driver factory to read the property.prop file is making connection
//with config.properties
//everything we reading from config.properties file is String
	private Properties prop;
	private ChromeOptions co;
	private FirefoxOptions fo;
	private EdgeOptions eo;

	// safari don't support incognito + headless
	public OptionsManager(Properties prop) {
		this.prop = prop;
	}

	public ChromeOptions getChromeOptions() {
		co = new ChromeOptions();
		// to convert String into boolean
		if (Boolean.parseBoolean(prop.getProperty("headless").trim()))co.addArguments("--headless");
		// if there is single line we can also write if statement this way
		if (Boolean.parseBoolean(prop.getProperty("incognito").trim()))co.addArguments("--incognito");
		return co;
	}

	public EdgeOptions getEdgeOptions() {
		eo = new EdgeOptions();
		if (Boolean.parseBoolean(prop.getProperty("headless").trim()))eo.addArguments("--headless");
		if (Boolean.parseBoolean(prop.getProperty("incognito").trim()))eo.addArguments("--inPrivate");
		return eo;
	}

	public FirefoxOptions getFirefoxOptions() {
		fo = new FirefoxOptions();
		if (Boolean.parseBoolean(prop.getProperty("headless").trim()))fo.addArguments("--headless");
		if (Boolean.parseBoolean(prop.getProperty("incognito").trim()))fo.addArguments("--incognito");
		return fo;
	}

}
