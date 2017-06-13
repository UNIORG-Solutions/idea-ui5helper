package de.uniorg.ui5helper.codeInsight.json;

import com.intellij.json.psi.*;
import com.intellij.psi.PsiElement;
import de.uniorg.ui5helper.ui5.manifest.Route;
import de.uniorg.ui5helper.ui5.manifest.RoutingConfig;
import de.uniorg.ui5helper.ui5.manifest.RoutingDefaults;
import de.uniorg.ui5helper.ui5.manifest.Target;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ManifestJsonUtil {
    static boolean matchesJsonPath(PsiElement element, String[] path) {
        for (int i = path.length - 1; i >= 0; i--) {
            JsonProperty property = getParentProperty(element);
            if (property == null) {
                return false;
            }
            if (!path[i].equals("?") && !property.getName().equals(path[i])) {
                return false;
            }

            element = property;
        }

        return true;
    }

    static JsonProperty getParentProperty(PsiElement start) {
        do {
            start = start.getParent();
        } while (start != null && !(start instanceof JsonProperty));

        if (start == null) {
            return null;
        }

        return (JsonProperty) start;
    }

    static JsonObject getRoutingObject(PsiElement start) {
        JsonProperty property = getParentProperty(start);
        while (property != null && !property.getName().equals("routing")) {
            property = getParentProperty(property);
        }

        return (JsonObject) property.getValue();
    }

    static JsonObject getRoutingObject(JsonFile file) throws IllegalArgumentException {
        if (!(file.getTopLevelValue() instanceof JsonObject)) {
            throw new IllegalArgumentException("Not a manifest file");
        }

        JsonObject mf = (JsonObject) file.getTopLevelValue();
        JsonProperty ui5Prop = mf.findProperty("sap.ui5");
        if (ui5Prop == null || !(ui5Prop.getValue() instanceof JsonObject)) {
            throw new IllegalArgumentException("Not a manifest file");
        }

        JsonProperty routingProp = ((JsonObject) ui5Prop.getValue()).findProperty("routing");
        if (routingProp == null || !(routingProp.getValue() instanceof JsonObject)) {
            throw new IllegalArgumentException("Not a manifest file");
        }

        return (JsonObject) routingProp.getValue();
    }

    @Nullable
    public static String getNamespace(JsonFile manifest) throws IllegalArgumentException {
        if (!(manifest.getTopLevelValue() instanceof JsonObject)) {
            throw new IllegalArgumentException("Not a manifest file");
        }

        JsonObject mf = (JsonObject) manifest.getTopLevelValue();
        JsonProperty ui5Prop = mf.findProperty("sap.app");
        if (ui5Prop == null || !(ui5Prop.getValue() instanceof JsonObject)) {
            throw new IllegalArgumentException("Not a manifest file");
        }

        JsonProperty id = ((JsonObject) ui5Prop.getValue()).findProperty("id");
        if (id != null && id.getValue() instanceof JsonStringLiteral) {
            return ((JsonStringLiteral) id.getValue()).getValue();
        }

        return null;
    }

    static RoutingConfig getRoutingConfig(JsonFile manifest) throws IllegalArgumentException {
        RoutingDefaults defaults = null;
        Route[] routes = new Route[0];
        Target[] targets = new Target[0];

        JsonObject routing = getRoutingObject(manifest);

        JsonProperty config = routing.findProperty("config");
        if (config != null && config.getValue() instanceof JsonObject) {
            JsonObject cfgObj = (JsonObject) config.getValue();
            defaults = buildRoutingDefaults(cfgObj);
        }

        JsonProperty routesArr = routing.findProperty("routes");
        if (routesArr != null && routesArr.getValue() instanceof JsonArray) {
            JsonArray routesList = (JsonArray) routesArr.getValue();
            routes = buildRoutes(routesList);
        }

        JsonProperty targetsArr = routing.findProperty("targets");
        if (targetsArr != null && targetsArr.getValue() instanceof JsonObject) {
            JsonObject targetsList = (JsonObject) targetsArr.getValue();
            targets = buildTargets(targetsList);
        }

        return new RoutingConfig(defaults, routes, targets);
    }

    private static Target[] buildTargets(JsonObject targetsList) {
        return targetsList.getPropertyList()
                .stream()
                .map(ManifestJsonUtil::createTarget)
                .toArray(Target[]::new);
    }

    private static Target createTarget(JsonProperty targetProperty) {
        final Target trgt = new Target();
        trgt.setName(targetProperty.getName());

        if (!(targetProperty.getValue() instanceof JsonObject)) {
            return null;
        }

        ((JsonObject) targetProperty.getValue()).getPropertyList().forEach(jsonProperty -> {
            switch (jsonProperty.getName()) {
                case "viewName":
                    if (jsonProperty.getValue() instanceof JsonStringLiteral) {
                        trgt.setViewName(((JsonStringLiteral) jsonProperty.getValue()).getValue());
                    }
                    break;

                case "viewPath":
                    if (jsonProperty.getValue() instanceof JsonStringLiteral) {
                        trgt.setViewPath(((JsonStringLiteral) jsonProperty.getValue()).getValue());
                    }
                    break;

                case "viewType":
                    if (jsonProperty.getValue() instanceof JsonStringLiteral) {
                        trgt.setViewType(((JsonStringLiteral) jsonProperty.getValue()).getValue());
                    }
                    break;

                case "viewLevel":
                    if (jsonProperty.getValue() instanceof JsonNumberLiteral) {
                        trgt.setViewLevel((int) ((JsonNumberLiteral) jsonProperty.getValue()).getValue());
                    }
                    break;

                case "parent":
                    if (jsonProperty.getValue() instanceof JsonStringLiteral) {
                        trgt.setParent(((JsonStringLiteral) jsonProperty.getValue()).getValue());
                    }
                    break;

            }
        });

        return trgt;
    }

    private static Route[] buildRoutes(JsonArray routesList) {
        return routesList.getValueList()
                .stream()
                .map(ManifestJsonUtil::createRoute)
                .filter(Objects::nonNull)
                .toArray(Route[]::new);
    }

    private static Route createRoute(JsonValue value) {
        Route rt = new Route();
        if (!(value instanceof JsonObject)) {
            return null;
        }

        ((JsonObject) value).getPropertyList().forEach(jsonProperty -> {
            switch (jsonProperty.getName()) {
                case "pattern":
                    if (jsonProperty.getValue() instanceof JsonStringLiteral) {
                        rt.setPattern(((JsonStringLiteral) jsonProperty.getValue()).getValue());
                    }
                    break;
                case "name":
                    if (jsonProperty.getValue() instanceof JsonStringLiteral) {
                        rt.setName(((JsonStringLiteral) jsonProperty.getValue()).getValue());
                    }
                    break;
                case "target":
                    if (jsonProperty.getValue() instanceof JsonStringLiteral) {
                        rt.setTargets(new String[]{((JsonStringLiteral) jsonProperty.getValue()).getValue()});
                    } else if (jsonProperty.getValue() instanceof JsonArray) {
                        rt.setTargets(
                                ((JsonArray) jsonProperty.getValue()).getValueList()
                                        .stream()
                                        .map(targetValue -> {
                                            if (targetValue instanceof JsonStringLiteral) {
                                                return ((JsonStringLiteral) targetValue).getValue();
                                            }
                                            return null;
                                        })
                                        .filter(Objects::nonNull)
                                        .toArray(String[]::new)
                        );
                    }
                    break;

            }
        });

        return rt;
    }

    private static RoutingDefaults buildRoutingDefaults(JsonObject cfgObj) {
        final RoutingDefaults dflt = new RoutingDefaults();
        cfgObj.getPropertyList().forEach(jsonProperty -> {
            switch (jsonProperty.getName()) {
                case "routerClass":
                    if (jsonProperty.getValue() instanceof JsonStringLiteral) {
                        dflt.setRouterClass(((JsonStringLiteral) jsonProperty.getValue()).getValue());
                    }
                    break;
                case "viewType":
                    if (jsonProperty.getValue() instanceof JsonStringLiteral) {
                        dflt.setViewType(((JsonStringLiteral) jsonProperty.getValue()).getValue());
                    }
                    break;
                case "viewPath":
                    if (jsonProperty.getValue() instanceof JsonStringLiteral) {
                        dflt.setViewPath(((JsonStringLiteral) jsonProperty.getValue()).getValue());
                    }
                    break;
                case "controlId":
                    if (jsonProperty.getValue() instanceof JsonStringLiteral) {
                        dflt.setControlId(((JsonStringLiteral) jsonProperty.getValue()).getValue());
                    }
                    break;
                case "controlAggregation":
                    if (jsonProperty.getValue() instanceof JsonStringLiteral) {
                        dflt.setControlAggregation(((JsonStringLiteral) jsonProperty.getValue()).getValue());
                    }
                    break;
            }
        });

        return dflt;
    }
}
