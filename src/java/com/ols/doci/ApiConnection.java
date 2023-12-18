package com.ols.doci;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Utility class to be extended.
 *
 * @author Brandon
 */
public abstract class ApiConnection
{
    public static void init()
    {
        PARSER = new Gson();
    }

    protected static Gson PARSER;

    public static Request prepareGetRequest(@NotNull String url, @Nullable String authorization)
    {
        return prepareGetRequest(prepareUrl(url, null), authorization);
    }

    public static Request prepareGetRequest(@NotNull HttpUrl url, @Nullable String authorization)
    {
        Request.Builder requestBuilder = new Request.Builder()
                .get()
                .url(url);

        if (authorization != null && !authorization.isBlank()) {
            requestBuilder.addHeader("Authorization", authorization);
        }

        return requestBuilder.build();
    }

    public static Request prepareJsonRequest(@NotNull String url, @Nullable String authorization, @NotNull String payload)
    {
        return prepareRequest(prepareUrl(url, null), authorization, "application/json", payload);
    }

    public static Request prepareJsonRequest(@NotNull HttpUrl url, @Nullable String authorization, @NotNull String payload)
    {
        return prepareRequest(url, authorization, "application/json", payload);
    }

    public static Request prepareFormRequest(@NotNull String url, @Nullable String authorization, @NotNull String payload)
    {
        return prepareRequest(prepareUrl(url, null), authorization, "application/x-www-form-urlencoded", payload);
    }

    public static Request prepareFormRequest(@NotNull HttpUrl url, @Nullable String authorization, @NotNull String payload)
    {
        return prepareRequest(url, authorization, "application/x-www-form-urlencoded", payload);
    }

    public static Request prepareRequest(@NotNull String url, @Nullable String authorization, @NotNull String mediaType, @NotNull String payload)
    {
        return prepareRequest(prepareUrl(url, null), authorization, mediaType, payload);
    }

    public static Request prepareRequest(@NotNull HttpUrl url, @Nullable String authorization, @NotNull String mediaType, @NotNull String payload)
    {
        MediaType mediaType2 = MediaType.parse(mediaType);
        RequestBody body = RequestBody.create(payload, mediaType2);
        Request.Builder builder = new Request.Builder()
                .post(body)
                .url(url)
                .addHeader("Content-Type", mediaType);

        if (authorization != null && !authorization.isBlank()) {
            builder.addHeader("Authorization", authorization);
        }

        return builder.build();
    }

    public static Request prepareFileRequest(@NotNull String url, @Nullable String authorization, @NotNull File payload)
    {
        return prepareFileRequest(prepareUrl(url, null), authorization, payload);
    }

    /**
     * Common method to isolate a request constructor.
     *
     * @param url
     * @param authorization
     * @param payload
     * @return
     */
    public static Request prepareFileRequest(@NotNull HttpUrl url, @Nullable String authorization, @NotNull File payload)
    {
        MediaType mediaType = MediaType.parse("multipart/form-data");
        RequestBody body = RequestBody.create(payload, mediaType);
        Request.Builder builder = new Request.Builder()
                .post(body)
                .url(url)
                .addHeader("Content-Type", "multipart/form-data");

        if (authorization != null && !authorization.isBlank()) {
            builder.addHeader("Authorization", authorization);
        }

        return builder.build();
    }

    /**
     * Prepares an URL to be used.
     *
     * @param url to prepare
     * @param queryParams to attach to the URL
     * @return the prepared URL.
     */
    public static HttpUrl prepareUrl(@NotNull String url, @Nullable Map<String, String> queryParams)
    {
        HttpUrl base = HttpUrl.parse(url);
        if (base == null) {
            Log.error("On APIConnection#prepareUrl, passed url <{0}>", url);
            return base;
        }

        if (queryParams == null) {
            return base;
        }

        HttpUrl.Builder builder = base.newBuilder();
        for (Map.Entry<String, String> param : queryParams.entrySet()) {
            builder.addQueryParameter(param.getKey(), param.getValue());
        }
        return builder.build();
    }

    /**
     * Removes a JSON string from any problematic wrapping
     *
     * @param response
     * @return
     * @throws java.io.IOException
     */
    public static String cleanJsonResponse(Response response) throws IOException
    {
        ResponseBody rBody = response.body();
        if (rBody == null) {
            Log.error("Request response should include a body.");
            return null;
        }

        // parse the json response
        String jsonString = rBody.string();
        if (jsonString.startsWith("\"")) {
            jsonString = jsonString.substring(1, jsonString.length() - 1);
        }
        if (jsonString.contains("\\\"")) {
            jsonString = jsonString.replaceAll("\\\\\\\"", "\"");
        }

        return jsonString;
    }

    /**
     * Removes a JSON string from any problematic wrapping
     *
     * @param response
     * @return
     * @throws java.io.IOException
     */
    public static JsonObject parseResponse(Response response) throws IOException
    {
        String jsonString = cleanJsonResponse(response);

        // instantiate a JsonObject
        JsonElement je = JsonParser.parseString(jsonString);
        if (!je.isJsonObject()) {
            Log.error("Response body should be a JsonObject");
            return null;
        }
        return je.getAsJsonObject();
    }
}
