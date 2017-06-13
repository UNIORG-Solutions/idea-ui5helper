package de.uniorg.ui5helper.ui5.manifest;

public class Target {
    private String name;
    private String viewName;
    private String viewType;
    private String viewPath;
    private int viewLevel;
    private String controlId;
    private String controlAggregation;
    private String parent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
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

    public int getViewLevel() {
        return viewLevel;
    }

    public void setViewLevel(int viewLevel) {
        this.viewLevel = viewLevel;
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

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}
