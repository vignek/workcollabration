import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.support.ui.*;

import io.appium.java_client.android.*;
import io.appium.java_client.remote.*;

public class AddContactNew {
	
	
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
		WebElement addContact;
		addContact = driver.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]");
		addContact.click();
		
		// Add Email and Name  
		WebElement newEmail;
		newEmail = driver.findElementById("com.sheikbro.onlinechat:id/editEmail");
		System.out.println("Enter the new user mail address");
		existingEmail = sc.nextLine();
		newEmail.sendKeys(existingEmail);
		
		WebElement newName;
		newName = driver.findElementById("com.sheikbro.onlinechat:id/editName");
		System.out.println("Enter the new user's name");
		existingUName = sc.nextLine();
		newName.sendKeys(existingUName);
		
		// Click to Add user if new
		WebElement sendInvite = driver.findElementById("com.sheikbro.onlinechat:id/addButton"); 
		sendInvite.click();
		
		// Click Yes send Invite
		
		WebElement popUpYes = driver.findElementById("android:id/button1");
		WebDriverWait waitOne = new WebDriverWait(driver, 30); 
		waitOne.until(ExpectedConditions.presenceOfElementLocated(By.id("android:id/button1")));
		popUpYes.click();		
	}
	
	@After
	public void close() throws Exception {
		driver.quit();
		
	}

}
