package ru.ragnok123.mcpacketstealer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import net.novatech.jbprotocol.GameEdition;

public class Config {

	public static String IP;
	public static int PORT;
	public static GameEdition EDITION;

	private static final String CONFIG_PATH = "config.properties";
	private static final Properties properties = new Properties();
	private static boolean gotAllRequiredProperties = true;

	static {

		File file = new File(CONFIG_PATH);
		boolean isEmpty = true;

		properties.clear();

		try {
			file.getParentFile().mkdirs();
			isEmpty = file.createNewFile();
		} catch (IOException e) {
			System.out.println(String.format("Could not create '%s' file.", CONFIG_PATH) + e);
		}

		if (!isEmpty) {
			try (FileInputStream fis = new FileInputStream(file)) {
				properties.load(fis);
			} catch (IOException e) {
				System.out.println(String.format("Could not read from '%s'.", CONFIG_PATH) + e);
			}
		}
		IP = getOrFail("server.ip");
		PORT = Integer.valueOf(getOrFail("server.port"));
		EDITION = parseEdition(getOrFail("edition"));

		try (FileWriter fw = new FileWriter(file)) {
			properties.store(fw, "Properties for PacketStealer");
		} catch (IOException e) {
			System.out.println(String.format("Could not write to '%s'.", CONFIG_PATH) + e);
		}

		if (!gotAllRequiredProperties) {
			throw new RuntimeException("Failed to fetch all required configuration properties");
		}
	}
	
	private static GameEdition parseEdition(String edition) {
		return switch(edition) {
		case "java" -> GameEdition.JAVA;
		case "bedrock" -> GameEdition.BEDROCK;
		default -> null;
		};
	}

	private static String getOrDefault(String key, String defaultValue) {
		if (!properties.containsKey(key) || properties.getProperty(key).equals("")) {
			System.out.println(String.format("Config '%s' does not contain key '%s'. Using default value: '%s'",
					CONFIG_PATH, key, defaultValue));
			properties.setProperty(key, defaultValue);
		}

		return properties.getProperty(key);
	}

	private static String getOrFail(String key) {
		if (!properties.containsKey(key) || properties.getProperty(key).equals("")) {
			System.out.println(
					String.format("Config '%s' does not contain value for required key '%s'", CONFIG_PATH, key));
			gotAllRequiredProperties = false;
			properties.setProperty(key, "");
			return null;
		}
		return properties.getProperty(key);
	}

}
