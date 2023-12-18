package ols.doci.process.pdf;

import com.sun.istack.Nullable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import com.ols.doci.ApiConnection;
import com.ols.doci.EnvLoader;
import com.ols.doci.Log;
import com.ols.doci.LogLevel;

/**
 * @author Luis Brayan
 */
public class DociApiConsumer extends ApiConnection
{
    public static void init()
    {
        LogLevel level = EnvLoader.getLogLevel("api-doci-endpoint");
        VERBOSE = level.canVerbose();
        DEBUG = level.canDebug();

        // api connection
        TIMEOUT = EnvLoader.getInteger("api-doci-endpoint", "timeoutMinutes", 1);
        AUTHORIZATION = EnvLoader.getString("api-doci-endpoint", "authorization");

        DYNAMO_ENDPOINT = EnvLoader.getString("api-doci-endpoint", "dynamoEndpoint");
        GENERATOR_ENDPOINT = EnvLoader.getString("api-doci-endpoint", "pdfEndpoint");
    }

    private static boolean VERBOSE = false;
    private static boolean DEBUG = false;

    private static int TIMEOUT = 5;
    private static String AUTHORIZATION;
    private static String DYNAMO_ENDPOINT;
    private static String GENERATOR_ENDPOINT;

    private static OkHttpClient CLIENT;

    protected static OkHttpClient getClient()
    {
        if (CLIENT != null) {
            return CLIENT;
        }
        CLIENT = new OkHttpClient().newBuilder()
                .connectTimeout(TIMEOUT, TimeUnit.MINUTES)
                .writeTimeout(TIMEOUT, TimeUnit.MINUTES)
                .readTimeout(TIMEOUT, TimeUnit.MINUTES)
                .build();
        return CLIENT;
    }

    @Nullable
    public static DociStorageResponse getData(DociStoragePayload payload)
    {
        String payloadStr = PARSER.toJson(payload);
        Request request = prepareJsonRequest(DYNAMO_ENDPOINT, AUTHORIZATION, payloadStr);
        if (DEBUG) {
            Log.debug("PDF data request <{0}>", request);
        }
        if (VERBOSE) {
            Log.debug("PDF data request payload <{0}>", payloadStr);
        }

        try (Response response = getClient().newCall(request).execute()) {
            if (response.code() != 200) {
                Log.error("PDF data response has unsupported code <{0}>", response.code());
                return null;
            }

            ResponseBody rBody = response.body();
            if (rBody == null) {
                Log.error("PDF data response should include a body.");
                return null;
            }

            String jsonResponse = rBody.string();
            if (VERBOSE) {
                Log.verbose("PDF data response body <{0}>", jsonResponse);
            }

            return PARSER.fromJson(jsonResponse, DociStorageResponse.class);
        } catch (IOException ex) {
            Log.error(ex, "On DociApiConsumer#getPdfData");
            return null;
        }
    }

    public static boolean generatePdf(DociPdfPayload payload)
    {
        String payloadStr = PARSER.toJson(payload);
        Request request = prepareJsonRequest(GENERATOR_ENDPOINT, null, payloadStr)
                .newBuilder()
                .addHeader("Accept", "application/json")
                .build();

        if (DEBUG) {
            Log.debug("PDF request <{0}>", request);
        }
        if (VERBOSE) {
            Log.debug("PDF request payload <{0}>", payloadStr);
        }

        try (Response response = getClient().newCall(request).execute()) {
            if (response.code() != 200) {
                Log.error("PDF response has unsupported code <{0}>", response.code());
                return false;
            }

            ResponseBody rBody = response.body();
            if (rBody == null) {
                Log.error("PDF response should include a body.");
                return false;
            }

            String jsonResponse = rBody.string();
            if (VERBOSE) {
                Log.verbose("PDF response body <{0}>", jsonResponse);
            }

            return true;
        } catch (IOException ex) {
            Log.error(ex, "On DociApiConsumer#getPdf");
            return false;
        }
    }
}
