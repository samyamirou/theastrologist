package com.theastrologist.serializer;

import com.google.gson.*;
import com.theastrologist.domain.Degree;

import java.lang.reflect.Type;

public class DegreeJsonDeserializer implements JsonSerializer<Degree> {
    @Override
    public JsonElement serialize(Degree degree, Type type, JsonSerializationContext jsonSerializationContext) {

        final JsonObject jsonObject = new JsonObject();
        Degree newDegree = new Degree(degree.getBaseDegree());
        jsonObject.addProperty("baseDegree", newDegree.getBaseDegree());
        jsonObject.addProperty("degree", newDegree.getDegree());
        jsonObject.addProperty("minutes", newDegree.getMinutes());
        return jsonObject;
    }
}
