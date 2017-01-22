import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.support.ui.*;

import io.appium.java_client.android.*;
import io.appium.java_client.remote.*;

public class ChatTest {

	Scanner sc = new Scanner(System.in);
	AndroidDriver driver;
	private String testemail = "test";
	private String testpass = "test";
	
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
		
		WebDriverWait wait = new WebDriverWait(driver, 30); 
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.sheikbro.onlinechat:id/editEmail")));
		
		
		WebElement emailTextField = driver.findElementById("com.sheikbro.onlinechat:id/editEmail") ;
		
		emailTextField.sendKeys(testemail);
		
		WebElement passwordTextField = driver.findElementById("com.sheikbro.onlinechat:id/editPassword");
		passwordTextField.sendKeys(testpass);
		
		WebElement clickOnChat = driver.findElementById("com.sheikbro.onlinechat:id/signin");
		clickOnChat.click();
		
		WebElement clickOnIcon = driver.findElementById("com.sheikbro.onlinechat:id/chatt");
		clickOnIcon.click();
		
		WebElement ClickOnText = driver.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.ListView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]");
		ClickOnText.click();
		
		String input = sc.nextLine();
		ClickOnText.sendKeys(input);
		
		
	}
	
	@After
	public void close() throws Exception {
		driver.quit();
		
	}
	

}
