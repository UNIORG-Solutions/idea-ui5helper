package de.uniorg.ui5helper.remote;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intellij.util.io.HttpRequests;
import com.intellij.util.text.SemVer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Ui5InfoService {
    private static Ui5InfoService instance = new Ui5InfoService();

    private Ui5InfoService() {

    }

    public static List<Ui5Version> getVersions() throws IOException {
        String response = HttpRequests.request("https://openui5.hana.ondemand.com/neo-app.json").accept("application/json").forceHttps(true).gzip(true).useProxy(true).readString(null);

        JsonParser parser = new JsonParser();

        JsonElement neoApp = parser.parse(response);

        if (!neoApp.isJsonObject()) {
            return new ArrayList<>();
        }

        ArrayList<Ui5Version> versions = new ArrayList<>();

        JsonArray routes = ((JsonObject) neoApp).getAsJsonArray("routes");
        routes.forEach(route -> {
            JsonObject routeObj = (JsonObject) route;

            String path = routeObj.getAsJsonPrimitive("path").getAsString();
            SemVer version = SemVer.parseFromText(routeObj.getAsJsonObject("target").getAsJsonPrimitive("version").getAsString());
            String name = routeObj.getAsJsonPrimitive("description").getAsString();

            if (path.equals("/")) {
                name = "latest";
            }
            versions.add(new Ui5Version(name, path, version));
        });

        return versions;
    }

    public static Ui5InfoService getInstance() {
        return instance;
    }

    public void updateApiDocs(String version) {

    }
}
