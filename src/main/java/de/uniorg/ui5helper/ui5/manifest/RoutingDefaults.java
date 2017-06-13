package de.uniorg.ui5helper.ui5.manifest;

public class RoutingDefaults {
    private String routerClass;
    private String controlId;
    private String controlAggregation;
    private String viewType = "XML";
    private String viewPath;

    public String getRouterClass() {
        return routerClass;
    }

    public void setRouterClass(String routerClass) {
        this.routerClass = routerClass;
    }

    public String getControlId() {
        return controlId;
    }

    public void setControlId(String controlId) {
        this.controlId = controlId;
    }

    public String getControlAggregation() {
        return controlAggregation;
    }

    public void setControlAggregation(String controlAggregation) {
        this.controlAggregation = controlAggregation;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public String getViewPath() {
        return viewPath;
    }

    public void setViewPath(String viewPath) {
        this.viewPath = viewPath;
    }
}
