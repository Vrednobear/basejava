package com.urise.webapp;

import java.io.*;
import java.util.Properties;

public class Config {
    protected static final File PROPERTY_FILE = new File("D:\\Inteships\\basejava\\config\\resumes.properties");
    private static final Config INSTANCE = new Config();
    private Properties props = new Properties();
    private File storageDir;
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    private Config() {
        try (InputStream is = new FileInputStream(PROPERTY_FILE)) {
            props.load(is);
            storageDir = new File(props.getProperty("storage.dir"));
            dbUrl = props.getProperty("db.url");
            dbUser = props.getProperty("db.user");
            dbPassword = props.getProperty("db.password");
        } catch (IOException e) {
         throw  new IllegalStateException("Invalid config file " + PROPERTY_FILE.getAbsolutePath());
        }
    }

    public static Config getInstance() {
        return INSTANCE;
    }

    public File getStorageDir() {
        return storageDir;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }
}

    
   