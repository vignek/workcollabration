import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;

public class MyAccountTest {

	AndroidDriver driver;
	private String testemail = "test";
	private String testpass = "test";
	private String existingEmail;
	private String existingUName;
	
	Scanner sc = new Scanner(System.in);
	
	
	@Before
	public void setup() throws MalformedURLException {
		
		File appDir = new File ("src");
		File app = new File(appDir,"WorkCollaboration.apk");
		
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability(MobileCapabilityType.PLATFORM_NAME,MobilePlatform.ANDROID);
		
		
		cap.setCapability(MobileCapabilityType.DEVICE_NAME, "Android device");
		
		cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "100");
		cap.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
		
		driver=new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"),cap );
	}
	
	@Test
	public void test() {
		
		// Launch and Login	
		WebDriverWait wait = new WebDriverWait(driver, 30); 
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sheikbro.onlinechat:id/editEmail")));
		
		WebElement emailTextField = driver.findElementById("com.sheikbro.onlinechat:id/editEmail") ;
		emailTextField.sendKeys(testemail);
		
		WebElement passwordTextField = driver.findElementById("com.sheikbro.onlinechat:id/editPassword");
		passwordTextField.sendKeys(testpass);
		
		WebElement emailLoginButton = driver.findElementById("com.sheikbro.onlinechat:id/signin");
		emailLoginButton.click();
		
		// Click to Add Contact
		WebElement MyAccount = driver.findElementById("com.sheikbro.onlinechat:id/myacc");
		MyAccount.click();
		
		// Get user name and verify with the login user name
		String email = driver.findElementById("com.sheikbro.onlinechat:id/emailname").getAttribute("text");
		assertEquals(testemail,email);
	}


}
