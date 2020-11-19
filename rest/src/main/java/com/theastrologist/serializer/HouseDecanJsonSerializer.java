package com.theastrologist.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.theastrologist.domain.HouseDecan;
import com.theastrologist.domain.SignDecan;

import java.lang.reflect.Type;

public class HouseDecanJsonSerializer implements JsonSerializer<HouseDecan> {

    @Override
    public JsonElement serialize(HouseDecan houseDecan, Type type, JsonSerializationContext jsonSerializationContext) {
        final JsonObject jsonObject = new JsonObject();
        HouseDecan newDecan = new HouseDecan(houseDecan.getBaseHouse(), houseDecan.getRelatedHouse());
        jsonObject.addProperty("baseHouse", newDecan.getBaseHouse().name());
        jsonObject.addProperty("relatedHouse", newDecan.getRelatedHouse().name());
        jsonObject.addProperty("decanNumber", newDecan.getDecanNumber());
        return jsonObject;
    }
}
