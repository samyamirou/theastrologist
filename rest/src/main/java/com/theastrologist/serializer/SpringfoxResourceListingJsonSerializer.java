package com.theastrologist.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import springfox.documentation.service.ResourceListing;

/**
 * {@code SpringfoxResourceListingJsonSerializer} is a JSON serializer for
 * Springfox's {@code ResourceListing} class.
 * <p/>
 *
 * @author Indra Basak
 * @since 11/23/17
 */
public class SpringfoxResourceListingJsonSerializer
		implements JsonSerializer<ResourceListing> {

	@Override
	public JsonElement serialize(ResourceListing resourceListing, Type type,
								 JsonSerializationContext context) {
		final JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("apiVersion", resourceListing.getApiVersion());

		final JsonElement apis = context.serialize(resourceListing.getApis());
		jsonObject.add("apis", apis);

		final JsonElement securityScheme =
				context.serialize(resourceListing.getSecuritySchemes());
		jsonObject.add("securityScheme", securityScheme);

		final JsonElement info = context.serialize(resourceListing.getInfo());
		jsonObject.add("info", info);

		return jsonObject;
	}
}
