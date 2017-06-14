package de.uniorg.ui5helper.binding.lang;

import com.intellij.lang.Language;

public class BindingLanguage extends Language {
    public static final BindingLanguage INSTANCE = new BindingLanguage();

    private BindingLanguage() {
        super("UI5 Binding");
    }
}
