package com.urise.webapp.storage;

import com.urise.webapp.serializer.JsonStreamSerializer;
import com.urise.webapp.serializer.XmlStreamSerializer;

public class JsonPathStorageTest extends AbstractStorageTest {

    public JsonPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(),new JsonStreamSerializer()));

    }
}