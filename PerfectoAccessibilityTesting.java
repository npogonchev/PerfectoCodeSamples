import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResult;
import com.perfecto.reportium.test.result.TestResultFactory;

public class PerfectoAccessibilityTesting {
	
	public static void main(String[] args) throws Exception {

		AppiumDriver driver;
	   	ReportiumClient reportiumClient;
		
		String cloudName = <CLOUD_NAME>;
		
		XCUITestOptions xcuiTestOptions = new XCUITestOptions();
		xcuiTestOptions.setPlatformName("ios");
		xcuiTestOptions.setAutomationName("Appium");
		xcuiTestOptions.setBundleId("io.perfecto.expense.tracker");
		
		Map<String, Object> perfectoOptions = new HashMap<>();
		perfectoOptions.put("appiumVersion", "1.22.3");
		perfectoOptions.put("automationVersion", "3.59.0");
		perfectoOptions.put("securityToken", <SECURITY_TOKEN>);
		perfectoOptions.put("deviceName", <DEVICE_NAME>);
		perfectoOptions.put("scriptName", "Perfecto Accessibility Testing");
		perfectoOptions.put("javascriptEnabled", true);
		xcuiTestOptions.setCapability("perfecto:options", perfectoOptions);
		
		TestResult testResult = TestResultFactory.createSuccess();
		
		try {
			
		driver = new IOSDriver(new URL("https://" + cloudName  + ".perfectomobile.com/nexperience/perfectomobile/wd/hub"), xcuiTestOptions);
			
		//driver = new AndroidDriver(new URL("https://" + cloudName  + ".perfectomobile.com/nexperience/perfectomobile/wd/hub"), capabilities);
		
        	driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        
       		PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
        	.withWebDriver(driver)
        	.build();
        
        	reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);
        
		reportiumClient.testStart("Perfecto Accessibility Testing", new TestContext("", "")); //Starts the reportium testd

		driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
		
		System.out.println("INFO: Opening Expense Tracker App");
		
		Map<String, Object> params = new HashMap<>();
		  
		params.put("identifier", "io.perfecto.expense.tracker");
		driver.executeScript("mobile:application:open", params);
		
		Thread.sleep(3000);		
		
		System.out.println("INFO: Running Accessibility Check");
		
		params.clear();
		params.put("tag", "accessibility-testing");
		driver.executeScript("mobile:checkAccessibility:audit", params); 
		
		Thread.sleep(3000);	
		
		System.out.println("INFO: Finishing test execution");

		reportiumClient.testStop(TestResultFactory.createSuccess());
		
		System.out.println("INFO: Test Ended Successfully");

		}
		catch (Exception e) {
			
			System.out.println("ERROR: Test Failed!");
			e.printStackTrace();
			testResult = TestResultFactory.createFailure(e.toString());
			reportiumClient.testStop(testResult);
			
		}
		
		driver.quit();
		
	}

}
