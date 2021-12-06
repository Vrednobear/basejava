package com.urise.webapp.storage;

import com.urise.webapp.Config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class SqlStorageTest extends AbstractStorageTest {
    public SqlStorageTest() {
        super(Config.getInstance().getStorage());
       // super(new SqlStorage(config.getDbUrl(),config.getDbUser(),config.getDbPassword()));
    }
}
