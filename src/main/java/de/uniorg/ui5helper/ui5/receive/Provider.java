package de.uniorg.ui5helper.ui5.receive;

import com.google.gson.*;
import de.uniorg.ui5helper.cache.CacheStorage;
import de.uniorg.ui5helper.ui5.ApiDocumentation;
import de.uniorg.ui5helper.ui5.ApiIndex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by masch on 4/19/17.
 */
public class Provider {

    private final String LIBS = "libs.json";
    private final String VERSIONS = "versions.json";

    private final CacheStorage storage;
    private final ApiClient client;

    public Provider(CacheStorage storage, ApiClient client) {
        this.storage = storage;
        this.client = client;
    }

    public void refreshAvailableVersions(Consumer<List<String>> callback) {
        new Thread(() -> {
            try {
                List<String> versions = this.client.getVersions();
                this.writeListToCache("_", VERSIONS, versions);
                callback.accept(versions);
            } catch (IOException e) {
                e.printStackTrace();
                callback.accept(new LinkedList<>());
            }
        }).start();
    }

    public void getAvailableVersions(Consumer<List<String>> callback) {
        try {
            if (this.storage.has("_", VERSIONS)) {
                this.readListFromCache("_", VERSIONS, callback);
                return;
            } else {
                System.err.println("CACHE MISS: falling back to remote api");
            }
        } catch (IOException ex) {
            System.err.println("IOException while reading cache: " + ex.getMessage());
            System.err.println("falling back to remote api");
        }

        this.refreshAvailableVersions(callback);
    }

    public void getApiIndex(String version, Consumer<ApiIndex> callback) {
        this.getAvailableLibraries(version, libs -> {
            List<CompletableFuture<String>> jsons = libs.parallelStream().map(libName -> {
                CompletableFuture<String> contentFuture = new CompletableFuture<>();
                this.getApiJson(version, libName, contentFuture::complete);
                return contentFuture;
            }).collect(Collectors.toList());

            CompletableFuture
                    .allOf(jsons.toArray(new CompletableFuture[jsons.size()]))
                    .thenApply(v ->
                            jsons.stream()
                                    .map(CompletableFuture::join)
                                    .collect(Collectors.toList())
                    )
                    .thenApply(apis -> {
                        ApiIndex index = new ApiIndex();
                        apis.forEach(apiJson -> {
                            if (apiJson == null) {
                                return;
                            }

                            this.buildApiDocumentation(apiJson, index);
                        });

                        callback.accept(index);

                        return true;
                    }).join();
        });
    }

    private void buildApiDocumentation(String apiJson, ApiIndex index) {
        JsonParser parser = new JsonParser();
        JsonElement docs = parser.parse(apiJson);
        if (docs.isJsonObject()) {
            try {
                ApiDocumentation api = new ApiDocumentation(docs.getAsJsonObject());
                api.createIndex(index);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getApiJson(String version, String libName, Consumer<String> callback) {
        try {
            if (this.storage.has(version, libName)) {
                this.readApiFromCache(version, libName, callback);
                return;
            }
        } catch (IOException ex) {
            System.err.println("IOException while reading cache: " + ex.getMessage());
            System.err.println("falling back to remote api");
        }

        this.refreshApiJson(version, libName, callback);
    }

    private void readApiFromCache(String version, String libName, Consumer<String> callback) throws IOException {
        String api = this.storage.get(version, libName);
        callback.accept(api);
    }

    private void refreshApiJson(String version, String libName, Consumer<String> callback) {
        new Thread(() -> {
            try {
                String api = this.client.fetchDocs(version, libName);
                this.writeApiToCache(version, libName, api);
                callback.accept(api);
            } catch (IOException e) {
                e.printStackTrace();
                callback.accept(null);
            }
        }).start();
    }

    private void writeApiToCache(String version, String libName, String api) throws IOException {
        this.storage.store(version, libName, api);
    }

    public void getAvailableLibraries(String version, Consumer<List<String>> callback) {
        try {
            if (this.storage.has(version, LIBS)) {
                this.readListFromCache(version, LIBS, callback);
                return;
            } else {
                System.err.println("getAvailableLibraries(" + version + "): CACHE MISS: falling back to remote api");
            }
        } catch (IOException ex) {
            System.err.println("IOException while reading cache: " + ex.getMessage());
            System.err.println("falling back to remote api");
        }

        this.refreshAvailableLibraries(version, callback);
    }

    public void refreshAvailableLibraries(String version, Consumer<List<String>> callback) {
        new Thread(() -> {
            try {
                List<String> libs = this.client.getAvailableLibraries(version);
                this.writeListToCache(version, LIBS, libs);
                callback.accept(libs);
            } catch (IOException e) {
                e.printStackTrace();
                callback.accept(new LinkedList<>());
            }
        }).start();
    }

    private void writeListToCache(String version, String file, List<String> data) throws IOException {
        JsonArray arr = new JsonArray();
        data.forEach(arr::add);
        final Gson gson = new GsonBuilder().create();
        try {
            this.storage.store(version, file, gson.toJson(arr));
        } catch (IOException e) {
            System.err.println("IOException while writing cache: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void readListFromCache(String version, String file, Consumer<List<String>> callback) throws IOException {
        JsonArray arr = (new JsonParser()).parse(this.storage.get(version, file)).getAsJsonArray();

        final List<String> versions = new ArrayList<>();
        for (JsonElement jsonElement : arr) {
            versions.add(jsonElement.getAsString());
        }

        callback.accept(versions);
    }
}
