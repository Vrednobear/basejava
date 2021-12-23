package com.urise.webapp;

import com.urise.webapp.storage.SqlStorage;
import com.urise.webapp.storage.Storage;

import java.io.*;
import java.util.Properties;

public class Config {
  //  protected static final File PROPERTY_FILE = new File(getHomeDir(), "config\\resumes.properties");
    protected static final File PROPERTY_FILE = new File("D:\\Inteships\\basejava\\config\\resumes.properties");
    private static final Config INSTANCE = new Config();
    private final Properties props = new Properties();
    private final File storageDir;
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;
    private final Storage storage;

    private Config() {
        try (InputStream is = new FileInputStream(PROPERTY_FILE)) {
            props.load(is);
            storageDir = new File(props.getProperty("storage.dir"));
            storage = new SqlStorage(props.getProperty("db.url"),
                    props.getProperty("db.user"),props.getProperty("db.password"));
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

    public Storage getStorage() {
        return storage;
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

    private static String getHomeDir(){
        String homeDir = System.getProperty("homeDir");
        File file = new File(homeDir == null ? "." : homeDir);
        if(!file.isDirectory()){
            throw new IllegalStateException("Invalid config file" + PROPERTY_FILE.getAbsolutePath());
        }return homeDir;
    }
}

    
   