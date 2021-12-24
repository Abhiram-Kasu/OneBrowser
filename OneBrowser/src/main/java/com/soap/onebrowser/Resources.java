package com.soap.onebrowser;

public class Resources {
	private String chromeDriverPath = getClass().getClassLoader().getResource("Drivers//chromedriver.exe").toExternalForm();
	private String extensionpath = getClass().getClassLoader().getResource("Extension//extension_3_11_4_0.crx").toExternalForm();
	
	public String getChromeDriverPath() {
		return chromeDriverPath;
	}
	
	public String getExtensionpath() {
		return extensionpath;
	}
	
	

}
