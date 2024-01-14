package com.qa.opencart.listeners;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;

import org.testng.ITestContext;
//it is just a template we can copy it from anywhere
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.qa.opencart.factory.DriverFactory;

//in default method of interface after jdk 1.8 we can give the body
public class ExtentReportListener implements ITestListener {

	private static final String OUTPUT_FOLDER = "./reports/";// it is a folder name in while we want to save report.
	private static final String FILE_NAME = "TestExecutionReport.html";// it is the report file name.both name could be
																		// anything
	private static ExtentReports extent = init();
	public static ThreadLocal<ExtentTest> test = new ThreadLocal<ExtentTest>();
	private static ExtentReports extentReports;

	private static ExtentReports init() {// it is initialized method
		Path path = Paths.get(OUTPUT_FOLDER);
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		extentReports = new ExtentReports();
		ExtentSparkReporter reporter = new ExtentSparkReporter(OUTPUT_FOLDER + FILE_NAME);
		reporter.config().setReportName("Open Cart Automation Test Results");
		extentReports.attachReporter(reporter);
		extentReports.setSystemInfo("System", "Windows");
		extentReports.setSystemInfo("Author", "Pooja");
		extentReports.setSystemInfo("Build#", "1.1");
		extentReports.setSystemInfo("Team", "OpenCart QA Team");
		extentReports.setSystemInfo("Customer Name", "Naveen Automation Labs");
		extentReports.setSystemInfo("ENV NAME", System.getProperty("env"));
		return extentReports;
	}

	@Override
	public synchronized void onStart(ITestContext context) {
		System.out.println("Test Suite Started!");
	}

	@Override
	public synchronized void onFinish(ITestContext context) {
		System.out.println("Test Suite Is Ending!");
		extent.flush();
		test.remove();// remove all the test object from the memory
	}

	@Override
	public synchronized void onTestStart(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		String qualifiedName = result.getMethod().getQualifiedName();
		int last = qualifiedName.lastIndexOf(".");
		int mid = qualifiedName.substring(0, last).lastIndexOf(".");
		String className = qualifiedName.substring(mid + 1, last);

		System.out.println(methodName + " started!");
		ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName(),
				result.getMethod().getDescription());
		extentTest.assignCategory(result.getTestContext().getSuite().getName());
//category means from which class it is coming like : Login etc...
		extentTest.assignCategory(className);
		test.set(extentTest);
		test.get().getModel().setStartTime(getTime(result.getStartMillis()));
	}

	@Override
	public synchronized void onTestSuccess(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		System.out.println((methodName + " passed!"));
		test.get().pass("Test passed");
//test.get().pass(result.getThrowable(),MediaEntityBuilder.createScreenCaptureFromPath(DriverFactory.getScreenshot(methodName),methodName).build());
		test.get().getModel().setEndTime(getTime(result.getStartMillis()));
	}

	@Override
	public synchronized void onTestFailure(ITestResult result) {
		System.out.println((result.getMethod().getMethodName() + " failed!"));
		String methodName = result.getMethod().getMethodName();
		//test.get().fail("Test Failed!");
		test.get().fail(result.getThrowable(),MediaEntityBuilder.createScreenCaptureFromPath(DriverFactory.getScreenshot(methodName),methodName).build());
		//selenium will take the screenshot as we have to take the screenshot of browser it is interacted by selenium
		test.get().getModel().setEndTime(getTime(result.getStartMillis()));
	}

	@Override
	public synchronized void onTestSkipped(ITestResult result) {
		System.out.println((result.getMethod().getMethodName() + " skipped!"));
		String methodName = result.getMethod().getMethodName();
		test.get().skip("Test Skipped");
		//test.get().skip(result.getThrowable(),MediaEntityBuilder.createScreenCaptureFromPath(DriverFactory.getScreenshot(methodName),methodName).build());
		test.get().getModel().setEndTime(getTime(result.getStartMillis()));
	}

	@Override
	public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		System.out.println(("on Test Failed But Within Success Percentage for" + result.getMethod().getMethodName()));

	}

	private Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

}
