package bms.dbLoaderPackage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DbLoader {
	private Properties properties;
	
	public DbLoader() {
		properties = new Properties();
		
		try {
            FileInputStream fileInputStream = new FileInputStream("dbConfig.properties");
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public String getDatabaseUrl() {
        return properties.getProperty("db.url");
    }
}
