package de.uniorg.ui5helper.ui5;

import gnu.trove.THashMap;

import java.util.Map;
import java.util.stream.Stream;

public class ApiIndex {

    private Map<String, ApiDocumentation> index = new THashMap<>();

    public void register(String className, ApiDocumentation docs) {
        this.index.put(className, docs);
    }

    public ApiSymbol[] findClass(String classname) {
        return index
                .keySet()
                .stream()
                .filter(name -> name.endsWith("." + classname))
                .map(name -> this.index.get(name).get(name))
                .toArray(ApiSymbol[]::new);
    }

    public ApiSymbol[] findInNamespace(String namespace) {
        return index.keySet().stream()
                .filter(name -> name.startsWith(namespace + "."))
                .map(this::lookup)
                .toArray(ApiSymbol[]::new);
    }

    /**
     * Finds all known elements that are a direct child of the given namespace.
     * For example
     * sap.m.Page is a direct child of sap.m
     * sap.m.semantic.Page is not
     *
     * @param namespace
     * @return a stream of matching api symbols
     */
    public Stream<ApiSymbol> findDirectInNamespace(String namespace) {
        return index.keySet().stream()
                .filter(name -> name.startsWith(namespace + "."))
                .filter(name -> !name.replace(namespace + ".", "").contains("."))
                .map(this::lookup);
    }

    /**
     * Finds all known elements that are a direct child of the given namespace.
     * For example
     * sap.m.Page is a direct child of sap.m
     * sap.m.semantic.Page is not
     *
     * @param namespace
     * @return a stream of T
     */
    public <T extends ApiSymbol> Stream<T> findDirectInNamespace(String namespace, Class<T> tClass) {
        return this.findDirectInNamespace(namespace)
                .filter(tClass::isInstance)
                .map(apiSymbol -> (T) apiSymbol);
    }

    public ApiSymbol lookup(String className) {
        if (!this.index.containsKey(className)) {
            return null;
        }

        return this.index.get(className).get(className);
    }

    public ApiSymbol lookup(String namespace, String className) {
        return this.lookup(namespace + "." + className);
    }
}
