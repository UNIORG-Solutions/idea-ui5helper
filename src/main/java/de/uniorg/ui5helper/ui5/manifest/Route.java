package de.uniorg.ui5helper.ui5.manifest;

public class Route {
    private String pattern;
    private String name;
    private String[] targets;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getTargets() {
        return targets;
    }

    public void setTargets(String[] targets) {
        this.targets = targets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Route)) return false;

        Route route = (Route) o;

        return pattern.equals(route.pattern);
    }

    @Override
    public int hashCode() {
        return pattern.hashCode();
    }
}
