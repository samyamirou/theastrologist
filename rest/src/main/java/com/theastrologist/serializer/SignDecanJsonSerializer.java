package com.theastrologist.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.theastrologist.domain.Degree;
import com.theastrologist.domain.SignDecan;

import java.lang.reflect.Type;

public class SignDecanJsonSerializer implements JsonSerializer<SignDecan> {

    @Override
    public JsonElement serialize(SignDecan signDecan, Type type, JsonSerializationContext jsonSerializationContext) {
        final JsonObject jsonObject = new JsonObject();
        SignDecan newDecan = new SignDecan(signDecan.getBaseSign(), signDecan.getRelatedSign());
        jsonObject.addProperty("baseSign", newDecan.getBaseSign().name());
        jsonObject.addProperty("relatedSign", newDecan.getRelatedSign().name());
        jsonObject.addProperty("decanNumber", newDecan.getDecanNumber());
        return jsonObject;
    }
}
