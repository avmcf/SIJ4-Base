package com.vconsulte.sij.base;

/**
 * This class holds common properties and methods for the example classes.
 */

public class Base {
    private static String user;
    private static String password;
    private static String folderName;
    private static String fileName;
    private static String url;
    private static String pastaPDF;
    
	public static void doUsage(String message) {
		System.out.println(message);
		System.exit(0);
	}
	
	public static String getUrl() {
		return url;
	}

	public void setUrl(String setUrl) {
		Base.url = setUrl;
	}
	
	public static String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		Base.password = password;
	}

	public static String getUser() {
		return user;
	}

	public void setUser(String user) {
		Base.user = user;
	}

	public static String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		Base.folderName = folderName;
	}
	
	public static String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		Base.fileName = fileName;
	}
	
	public static String getPastaPDF() {
		return pastaPDF;
	}

	public void setPastaPDF(String pastaPDF) {
		Base.pastaPDF = pastaPDF;
	}

}
