package de.ovgu.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Properties;

/**
 * 
 * @author shuo wang
 * 
 *         this class is used to read the configuration file which located in
 *         the conf folder.
 * 
 * 
 */
public class ConfigurationReader {

	private static ConfigurationReader creader;

	// define the server port
	private int gameServerPort = 0;
	private String gameServerIp = "";
	private String clusterName = "";
	private String keySpaceName = "";
	private String cassandraClusterIp = "";

	private ConfigurationReader() {
		// better use log to output

		System.out.println("read the configuration file from --"
				+ this.getConfigurationPath());
		// this class should not create more than one insTance. a private
		// constructor can prevent us from creating a new instance
		// we can use getInstance()
		this.loadPropertiesFromFile();
	}

	/**
	 * the only way to get the single instance of this class
	 * 
	 * @return ConfigurationReader
	 */
	public static ConfigurationReader getInstance() {
		if (creader == null) {
			creader = new ConfigurationReader();
		}
		return creader;
	}

	/**
	 * read the parameters in the configuration file
	 */
	private void loadPropertiesFromFile() {
		Properties properties = new Properties();
		try {
			InputStream inputStream = new BufferedInputStream(
					new FileInputStream(getConfigurationPath()));
			properties.load(inputStream);

		} catch (IOException e) {
			System.out.print("failed to load configuration file.");
			return;
		}

		this.setGameServerPort(Integer.parseInt(properties.getProperty("port")));
		
		System.out.println("All parameters loaded ");
		System.out.println(this.toString());
		
		// this.setCassandraClusterIp(properties.getProperty("cassandraIp"));
	}

	/**
	 * it tells where is the configuration file located when something goes
	 * wrong ,check the path first
	 * 
	 * @return
	 */
	private String getConfigurationPath() {
		String fileNameAndFolder = "conf"+File.separator+"serverconfig.properties";
		String path = this.getClass().getProtectionDomain().getCodeSource()
				.getLocation().getPath();
		File file = new File(path);
		//System.out.println(file.getPath());
		//System.out.println(file.getParent());	
		path = file.getParent()+File.separator+fileNameAndFolder;
		return path;
	}

	public int getGameServerPort() {
		return gameServerPort;
	}

	public void setGameServerPort(int gameServerPort) {
		this.gameServerPort = gameServerPort;
	}

	public String getGameServerIp() {
		return gameServerIp;
	}

	public void setGameServerIp(String gameServerIp) {
		this.gameServerIp = gameServerIp;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public String getKeySpaceName() {
		return keySpaceName;
	}

	public void setKeySpaceName(String keySpaceName) {
		this.keySpaceName = keySpaceName;
	}

	public String getCassandraClusterIp() {
		return cassandraClusterIp;
	}

	public void setCassandraClusterIp(String cassandraClusterIp) {
		this.cassandraClusterIp = cassandraClusterIp;
	}

	public String toString(){
		StringBuilder sb=new StringBuilder("==============Parameters in the configuration file==============\n");
		sb.append("Serverport====>").append(this.getGameServerPort());
		
		sb.append("\n").append("================================================================");
	return sb.toString();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ConfigurationReader cReader = ConfigurationReader.getInstance();
		System.out.println(cReader.toString());
	}

}
