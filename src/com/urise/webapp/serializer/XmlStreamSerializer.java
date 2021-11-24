package com.urise.webapp.serializer;

import com.urise.webapp.model.*;
import com.urise.webapp.util.XmlParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class XmlStreamSerializer implements SerializationStrategy {
    private XmlParser parser;

    public XmlStreamSerializer() {
        parser = new XmlParser(Resume.class, Organization.class, Link.class,
                OrganizationSection.class, TextSection.class, ListSection.class);
    }

    @Override
    public void doWrite(Resume r, OutputStream os) {
        Writer writer = new OutputStreamWriter(os, StandardCharsets.UTF_8);
        parser.marshall(r, writer);
    }

    @Override
    public Resume doRead(InputStream is)  {
        Reader reader = new InputStreamReader(is,StandardCharsets.UTF_8);
        return parser.unmarshall(reader);
    }
}
