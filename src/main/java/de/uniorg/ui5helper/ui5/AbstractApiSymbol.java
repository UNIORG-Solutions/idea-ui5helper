package de.uniorg.ui5helper.ui5;

import org.jetbrains.annotations.NotNull;

abstract class AbstractApiSymbol implements ApiSymbol, DeprecateableInterface {

    protected String name;

    protected String description;

    protected Deprecation deprecation = null;

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @NotNull
    @Override
    public String getDescription() {
        return description;
    }

    public boolean isDeprecated() {
        return deprecation != null;
    }

    public Deprecation getDeprecation() {
        return deprecation;
    }

}
