package de.uniorg.ui5helper.ui5;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.intellij.util.io.HttpRequests;
import gnu.trove.THashMap;

import java.io.IOException;
import java.util.Map;

public class ApiService {
    private String[] libraries = new String[]{
            "sap.f", "sap.m", "sap.tnt", "sap.ui.codeeditor", "sap.ui.commons", "sap.ui.core", "sap.ui.demokit",
            "sap.ui.dt", "sap.ui.fl", "sap.ui.layout", "sap.ui.suite", "sap.ui.table", "sap.ui.unified", "sap.ui.ux3",
            "sap.uxap"
    };

    private Map<String, ApiIndex> indicies = new THashMap<>();

    private Map<ApiId, ApiDocumentation> cache = new THashMap<>();

    private void fetchDocs(String version, String library) {
        ApiIndex index = this.getIndex(version);

        final ApiId id = new ApiId(version, library);
        String libModule = library.replace('.', '/');
        if (version.equals("latest")) {
            version = "";
        }
        final String url = "https://openui5nightly.hana.ondemand.com/" + version + "/test-resources/" + libModule + "/designtime/api.json";


        Runnable update = () -> {

            System.out.println("fetching api docs for " + id + " from " + url);
            String response = null;
            try {
                response = HttpRequests.request(url).accept("application/json").forceHttps(true).gzip(true).useProxy(true).readString(null);
            } catch (IOException e) {
                System.out.println("fetching api docs for " + id + " failed " + e.getMessage());
                e.printStackTrace();
                return;
            }

            System.out.println("got json doc for" + id);
            JsonParser parser = new JsonParser();

            JsonElement docs = parser.parse(response);

            if (docs.isJsonObject()) {
                try {
                    ApiDocumentation api = new ApiDocumentation(docs.getAsJsonObject());
                    cache.put(id, api);
                    api.createIndex(index);
                    System.out.println("got api docs for " + id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

                System.out.println("not a json doc: " + id);
            }
        };

        //ApplicationManager.getApplication().executeOnPooledThread(update);
        (new Thread(update)).start();
    }

    public ApiIndex getIndex(String version) {
        if (!this.indicies.containsKey(version)) {
            this.indicies.put(version, new ApiIndex());
        }

        return this.indicies.get(version);
    }

    public void prefetchDocs(String version) {
        for (String library : libraries) {
            this.fetchDocs(version, library);
        }
    }


    public boolean has(String version, String library) {
        if (!this.cache.containsKey(new ApiId(version, library))) {
            this.fetchDocs(version, library);
            return false;
        }

        return true;
    }

    public ApiDocumentation get(String version, String library) {
        if (this.has(version, library)) {
            return this.cache.get(new ApiId(version, library));
        }

        return null;
    }


    private class ApiId {
        private final String version;
        private final String library;

        ApiId(String version, String library) {
            this.version = version;
            this.library = library;
        }

        @Override
        public int hashCode() {
            return (this.library + "@" + this.version).hashCode();
        }

        @Override
        public String toString() {
            return this.library + "@" + this.version;
        }
    }
}
