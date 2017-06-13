package de.uniorg.ui5helper.ui5.manifest;

public class RoutingConfig {
    private RoutingDefaults defaults;

    private Route[] routes;

    private Target[] targets;

    public RoutingConfig(RoutingDefaults defaults, Route[] routes, Target[] targets) {
        this.defaults = defaults;
        this.routes = routes;
        this.targets = targets;
    }

    public RoutingDefaults getDefaults() {
        return defaults;
    }

    public Route[] getRoutes() {
        return routes;
    }

    public Target[] getTargets() {
        return targets;
    }
}
