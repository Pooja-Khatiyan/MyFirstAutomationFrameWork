package com.qa.opencart.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {
	private int count = 0;
	private static int maxTry = 3;

	@Override
	public boolean retry(ITestResult iTestResult) {
		if (!iTestResult.isSuccess()) {//check if test not succeed
			if (count < maxTry) {//check if maxTry count is reached
				count++; //increase the maxTry count by 1
				iTestResult.setStatus(ITestResult.FAILURE);//mark test as failed
				return true;//Tells TestNg to re-run the test
			} else {
				iTestResult.setStatus(ITestResult.FAILURE);//if maxCount reached,test marked as failed
			}
		} else {
			iTestResult.setStatus(ITestResult.SUCCESS);//if test passes,TestNG marks it as passed
		}
		return false;

	}

}
