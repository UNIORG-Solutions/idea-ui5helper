package de.uniorg.ui5helper.ui5.receive;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intellij.util.io.HttpRequests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpsClient implements ApiClient {

    private String host = "https://openui5nightly.hana.ondemand.com/";

    public HttpsClient(String host) {
        if (!host.endsWith("/")) {
            host = host + "/";
        }
        this.host = host;
    }

    public HttpsClient() {
    }

    @Override
    public String fetchDocs(String version, String library) throws IOException {
        String libModule = library.replace('.', '/');
        if (version.equals("latest")) {
            version = "";
        }
        return fetchString(version + "/test-resources/" + libModule + "/designtime/api.json");
    }

    private String fetchString(String url) throws IOException {
        return HttpRequests.request(this.host + url).accept("application/json").forceHttps(true).gzip(true).useProxy(true).readString(null);
    }

    public List<String> getAvailableLibraries(String version) throws IOException {
        if (version.equals("latest")) {
            version = "";
        }

        String response = fetchString(version + "/resources/sap-ui-version.json");

        JsonParser parser = new JsonParser();
        JsonElement versions = parser.parse(response);

        if (!versions.isJsonObject() || !((JsonObject) versions).has("libraries")) {
            return new ArrayList<>();
        }

        JsonArray libraries = ((JsonObject) versions).getAsJsonArray("libraries");

        List<String> libNames = new ArrayList<>();
        for (JsonElement library : libraries) {
            if (!library.isJsonObject()) {
                continue;
            }

            String name = ((JsonObject) library).getAsJsonPrimitive("name").getAsString();
            if (!name.startsWith("theme")) {
                libNames.add(name);
            }
        }

        return libNames;
    }

    @Override
    public List<String> getVersions() throws IOException {
        String response = fetchString("neo-app.json");

        JsonParser parser = new JsonParser();

        JsonElement neoApp = parser.parse(response);

        if (!neoApp.isJsonObject()) {
            return new ArrayList<>();
        }

        ArrayList<String> versions = new ArrayList<>();

        JsonArray routes = ((JsonObject) neoApp).getAsJsonArray("routes");
        routes.forEach(route -> {
            JsonObject routeObj = (JsonObject) route;

            String path = routeObj.getAsJsonPrimitive("path").getAsString();
            String name = path.replaceFirst("/", "");
            if (path.equals("/")) {
                name = "latest";
            }
            versions.add(name);
        });

        return versions;
    }
}
