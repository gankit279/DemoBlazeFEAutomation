	package com.blaze.tests;

import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.blaze.utils.CommonUtilities;
import com.blaze.utils.ConfigSetUp;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {
	
	public WebDriver driver = null;
	public ExtentSparkReporter sparkReporter = null;
	public ExtentReports extent = null;
	public ExtentTest test = null;
	public Logger logger;
	
	@BeforeTest(alwaysRun = true)
	@Parameters({"deviceType", "platformName", "platformVersion", "deviceName", "browser"})
	public void initSetUp(String deviceType, String platformName, String platformVersion, String deviceName, String browser) {
		initDriver(deviceType, platformName, platformVersion, deviceName, browser);
		initTestReport();
		initLogger();
	}
	
	public void initLogger() {
		logger = Logger.getLogger("DemoBlazeFrontEndAutomation");
		PropertyConfigurator.configure("log4j.properties");
		logger.setLevel(Level.DEBUG);
	}
	
	public void initDriver(String deviceType, String platformName, String platformVersion, String deviceName, String browser) {
		if(browser.equalsIgnoreCase(ConfigSetUp.Browser.Chrome.toString())) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		} else if(browser.equalsIgnoreCase(ConfigSetUp.Browser.Firefox.toString())) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		}
	}
	
	public void initTestReport() {
		sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir")+"/test-reports/report.html");
		sparkReporter.config().setDocumentTitle("Automation Report");
		sparkReporter.config().setReportName("Functional Test Report");
		sparkReporter.config().setTheme(Theme.DARK);
		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
		extent.setSystemInfo("Hostname", "demoblaze.com");
		extent.setSystemInfo("OS", "Windows 10");
		extent.setSystemInfo("QA'ed", "gankit279");
		extent.setSystemInfo("Browser", "Chrome");
	}
	
	@BeforeMethod
	public void addTestResult(Method method) {
		test = extent.createTest(method.getName());
	}
	
	@AfterMethod
	public void logResultInReport(ITestResult result) throws IOException {
		if(result.getStatus() == ITestResult.FAILURE) {
			test.log(Status.FAIL, "Test case failed is "+result.getName());
			test.log(Status.FAIL, "Test case failed is "+result.getThrowable()); 
			String screenshotPath = CommonUtilities.getScreenshot(driver, result.getName());
			test.addScreenCaptureFromPath(screenshotPath);
		}else if(result.getStatus() == ITestResult.SKIP) {
			test.log(Status.SKIP, "Test case skipped is "+result.getName());
		}else if(result.getStatus() == ITestResult.SUCCESS) {
			test.log(Status.PASS, "Test case passed is "+result.getName());
		}
	}
	
	@AfterTest
	public void tearDown() {
		driver.close();
		driver.quit();
		extent.flush();
	}

}
