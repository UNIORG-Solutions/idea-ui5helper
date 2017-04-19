package de.uniorg.ui5helper.ui5.receive;

import java.io.IOException;
import java.util.List;

public interface ApiClient {
    /**
     * fetches the api.json for the given library and version
     *
     * @param version API version or "latest"
     * @param library library name (e.g. sap.ui.core)
     * @return the api content
     */
    String fetchDocs(String version, String library) throws IOException;

    List<String> getVersions() throws IOException;

    List<String> getAvailableLibraries(String version) throws IOException;
}
