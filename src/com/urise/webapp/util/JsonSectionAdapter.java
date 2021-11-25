package com.urise.webapp.util;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.lang.reflect.Type;
import java.time.LocalDate;

public class    JsonSectionAdapter <T> implements JsonSerializer<T>, JsonDeserializer<T> {
    private static final String CLASSNAME = "CLASSNAME";
    private static final String INSTANCE = "INSTANCE";
    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonPrimitive primitive = (JsonPrimitive) jsonObject.get(CLASSNAME);
        String className = primitive.getAsString();

        try {
            Class clazz  = Class.forName(className);
            return context.deserialize(jsonObject.get(INSTANCE),clazz);
        } catch (ClassNotFoundException e) {
           throw new JsonParseException(e.getMessage());
        }
    }

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject retValue = new JsonObject();
        retValue.addProperty(CLASSNAME,src.getClass().getName());
        JsonElement elem = context.serialize(src);
        retValue.add(INSTANCE,elem);
        return retValue;
    }
}
