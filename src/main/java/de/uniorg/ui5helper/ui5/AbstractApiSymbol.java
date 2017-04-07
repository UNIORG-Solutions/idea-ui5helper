package de.uniorg.ui5helper.ui5;

import org.jetbrains.annotations.NotNull;

abstract class AbstractApiSymbol implements ApiSymbol {

    protected String name;

    protected String description;

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


}
