package com.soap.onebrowser;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.google.common.io.Files;

public class Main {


	private InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

	public static boolean isStringWebsite(String s) {
		try {
			if (s.substring(0, 4).equals("http")) {
				return true;
			}
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		
		System.setProperty("webdriver.chrome.driver", "C:\\bin\\chromedriver_win32\\chromedriver.exe");
		String clipboardData = "";

		try {
			Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboardData = (String) c.getData(DataFlavor.stringFlavor);
		} catch (Exception e) {
		}

		String url = isStringWebsite(clipboardData) ? clipboardData : "https://www.google.com";

		Main main = new Main();
		
		ChromeOptions options = new ChromeOptions();
		File extensionFile = new File(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getAbsolutePath() + "/extension_3_11_4_0.crx");
		extensionFile.deleteOnExit();
		try {
			Files.write(main.getFileFromResourceAsStream("Extensions/extension_3_11_4_0.crx").readAllBytes(), extensionFile);
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		
		
		
		options.addExtensions(extensionFile);

		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, options);

		ChromeDriver driver = new ChromeDriver(desiredCapabilities);
		driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
		driver.get(url);

		Object[] handles;
		
		handles = driver.getWindowHandles().toArray();
		driver.switchTo().window((String) handles[1]);
		driver.close();
		while (true) {
			handles = driver.getWindowHandles().toArray();
			
			while (handles.length > 1) {
				
				try {
					driver.switchTo().window((String) handles[1]);
					driver.close();
				} catch (NoSuchWindowException e) {
					System.out.println("NoSuchWindowException");
				} catch (Exception e) {
					e.printStackTrace();
				}
				driver.switchTo().window((String) handles[0]);
				handles = driver.getWindowHandles().toArray();
			}

			try {
				driver.getTitle();

			} catch (Exception e) {
				driver.quit();
				return;
			}
		}

	}

}
