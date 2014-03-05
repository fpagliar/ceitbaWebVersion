package ar.edu.itba.paw.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class ReadProperties {
	public static Properties loadXML(String fileName) throws InvalidPropertiesFormatException, IOException {
		Properties prop = new Properties();
		prop.loadFromXML(getFileInputStream(fileName));
		return prop;
	}
	
	public static Properties load(String fileName) throws IOException {
		Properties prop = new Properties();
		prop.load(getFileInputStream(fileName));
		return prop;
	}
	
	private static FileInputStream getFileInputStream(String fileName) throws IOException {
		String current = new File( "." ).getCanonicalPath();
		FileInputStream fis;
		try {
			fis = new FileInputStream(current + "/src/main/webapp/WEB-INF/" + fileName);
		} catch (FileNotFoundException e) {
			fis = new FileInputStream(current + "/../webapps/t-loresto/WEB-INF/" + fileName);
		}
		return fis;
	}
}
