package com.fabian.vilo.cards;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by Fabian on 22/10/15.
 */
public class Deserializer<T> implements JsonDeserializer<T> {
    @Override
    public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        JsonElement content = je.getAsJsonObject();

        // Deserialize it. You use a new instance of Gson to avoid infinite recursion to this deserializer
        return new Gson().fromJson(content, type);

    }
}