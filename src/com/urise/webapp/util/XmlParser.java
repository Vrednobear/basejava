package com.urise.webapp.util;

import javax.xml.bind.*;
import java.io.Reader;
import java.io.Writer;

public class XmlParser {
    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;

    public XmlParser(Class...classesToBeBound) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(classesToBeBound);
            marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING,"UTF-8");

            unmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }

    public void marshall(Object object, Writer writer){
        try {
            marshaller.marshal(object,writer);
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }


    public <T> T unmarshall(Reader reader){
        try {
            return (T) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            throw  new IllegalStateException(e);
        }
    }

}
