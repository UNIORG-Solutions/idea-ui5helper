package de.uniorg.ui5helper.ui5;

import gnu.trove.THashMap;

import java.util.Map;

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
