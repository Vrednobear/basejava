package com.urise.webapp.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import com.urise.webapp.model.Section;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.EnumMap;

public class JsonParser {
   private static Gson GSON = new GsonBuilder()
           .registerTypeAdapter(Section.class,new JsonSectionAdapter())
           .registerTypeAdapter(EnumMap.class, (InstanceCreator<EnumMap>) type -> {
               Type[] types = (((ParameterizedType) type).getActualTypeArguments());
               return new EnumMap((Class<?>) types[0]);
           })
           .create();

   public static <T> T read(Reader reader, Class<T> clazz){
       return GSON.fromJson(reader,clazz);
   }

   public static <T> void write(T object, Writer writer){
        GSON.toJson(object,writer);
   }

}
