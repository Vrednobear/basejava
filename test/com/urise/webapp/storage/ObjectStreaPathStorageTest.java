package com.urise.webapp.storage;

public class ObjectStreaPathStorageTest extends AbstractStorageTest {

    public ObjectStreaPathStorageTest() {
        super(new ObjectStreamStorage(STORAGE_DIR));
    }
}