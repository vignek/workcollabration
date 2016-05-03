import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Test;
import org.openqa.selenium.remote.*;
import io.appium.java_client.android.*;
import io.appium.java_client.remote.*;

public class LaunchApp {

	AndroidDriver driver;
	
	@Test
	public void Launch() throws MalformedURLException {
		
		File appDir = new File ("src");
		File app = new File(appDir,"WorkCollaboration.apk");
		
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability(MobileCapabilityType.PLATFORM_NAME,MobilePlatform.ANDROID);
		
		
		cap.setCapability(MobileCapabilityType.DEVICE_NAME, "Android device");
		
		cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "100");
		cap.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
		
		driver=new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"),cap );
	}

}
