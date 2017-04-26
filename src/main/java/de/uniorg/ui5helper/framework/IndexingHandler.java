package de.uniorg.ui5helper.framework;

import com.intellij.lang.ASTNode;
import com.intellij.lang.javascript.JSElementTypes;
import com.intellij.lang.javascript.index.FrameworkIndexingHandler;
import com.intellij.lang.javascript.index.JSSymbolUtil;
import com.intellij.lang.javascript.psi.*;
import com.intellij.lang.javascript.psi.impl.JSCallExpressionImpl;
import com.intellij.lang.javascript.psi.impl.JSReferenceExpressionImpl;
import com.intellij.lang.javascript.psi.literal.JSLiteralImplicitElementProvider;
import com.intellij.lang.javascript.psi.stubs.JSElementIndexingData;
import com.intellij.lang.javascript.psi.stubs.JSImplicitElement;
import com.intellij.lang.javascript.psi.stubs.impl.JSImplicitElementImpl;
import com.intellij.lang.javascript.psi.stubs.impl.JSImplicitParameterStructure;
import com.intellij.lang.javascript.psi.types.JSContext;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class IndexingHandler extends FrameworkIndexingHandler {

    @NotNull
    public String[] interestedProperties() {
        return new String[]{"metadata"};
    }

    @Override
    public boolean processProperty(String name, @Nullable JSElement value, @NotNull JSElementIndexingData outData) {
        switch (name) {
            case "metadata":
                if (!(value instanceof JSObjectLiteralExpression)) {
                    return false;
                }
                JSObjectLiteralExpression object = (JSObjectLiteralExpression) value;

                JSProperty properties = object.findProperty("properties");
                if (properties != null && properties.getValue() instanceof JSObjectLiteralExpression) {
                    JSObjectLiteralExpression props = (JSObjectLiteralExpression) properties.getValue();
                    JSSymbolUtil.forEachIdentifierProperty(props, (titleCasedPropName, property) -> {
                        String typeString = getPropertyType(property);
                        JSImplicitElementImpl.Builder getterBuilder = (new JSImplicitElementImpl.Builder(JSSymbolUtil.suggestGetterName(titleCasedPropName), property)).setProperties(JSImplicitElement.Property.GetFunction);
                        getterBuilder.setNamespace(((JSProperty) value.getParent()).getNamespace())
                                .setContext(JSContext.INSTANCE)
                                .setProperties(JSImplicitElement.Property.GetFunction)
                                .setType(JSImplicitElement.Type.Function)
                                .setTypeString(typeString);

                        outData.addImplicitElement(getterBuilder.toImplicitElement());
                        JSImplicitElementImpl.Builder setterBuilder = (new JSImplicitElementImpl.Builder(JSSymbolUtil.suggestSetterName(titleCasedPropName), property)).setProperties(JSImplicitElement.Property.SetFunction);
                        List<JSImplicitParameterStructure> parameters = new ArrayList<>();
                        parameters.add(new JSImplicitParameterStructure("value", typeString, false, false, false));
                        setterBuilder.setNamespace(((JSProperty) value.getParent()).getNamespace())
                                .setContext(JSContext.INSTANCE)
                                .setProperties(JSImplicitElement.Property.GetFunction)
                                .setType(JSImplicitElement.Type.Function)
                                .setParameters(parameters);
                        outData.addImplicitElement(setterBuilder.toImplicitElement());
                    });
                }
        }

        return false;
    }

    private String getPropertyType(JSProperty jsProperty) {
        String type = "mixed";
        if (jsProperty.getValue() != null && jsProperty.getValue() instanceof JSObjectLiteralExpression) {
            JSObjectLiteralExpression propertyDefinition = (JSObjectLiteralExpression) jsProperty.getValue();
            if (propertyDefinition.findProperty("type") != null && (propertyDefinition.findProperty("type").getValue() instanceof JSLiteralExpression)) {
                type = StringUtil.unquoteString(((JSObjectLiteralExpression) jsProperty.getValue()).findProperty("type").getValue().getText());
            }
        }

        return type;
    }

    public JSLiteralImplicitElementProvider createLiteralImplicitElementProvider(@NotNull String calledMethodName) {
        if ("extend".equals(calledMethodName)) {
            return new JSLiteralImplicitElementProvider() {
                public void fillIndexingData(@NotNull JSLiteralExpression argument, @NotNull JSCallExpression callExpression, @NotNull JSElementIndexingData outIndexingData) {
                    JSExpression[] arguments = callExpression.getArguments();
                    if (arguments.length >= 2 && arguments[0] instanceof JSLiteralExpression && ((JSLiteralExpression) arguments[0]).isQuotedLiteral()) {
                        // das ist dann wohl ein "asdf.extend('',.." call

                        String name = (String) ((JSLiteralExpression) arguments[0]).getValue();
                        if (name == null || name.lastIndexOf(".") == -1) {
                            return;
                        }
                        String namespace = name.substring(0, name.lastIndexOf("."));
                        String className = name.substring(name.lastIndexOf(".") + 1);
                        JSImplicitElementImpl.Builder builder = (new JSImplicitElementImpl.Builder(className, argument))
                                .setContext(JSContext.STATIC)
                                .setNamespace(JSQualifiedNameImpl.fromNamepath(namespace))
                                .setType(JSImplicitElement.Type.Class)
                                .setUserString("ui5_class");
                        outIndexingData.addImplicitElement(builder.toImplicitElement());
                    }
                }
            };
        }

        return super.createLiteralImplicitElementProvider(calledMethodName);
    }

    @NotNull
    @Override
    public String[] interestedMethodNames() {
        return new String[]{"extend"};
    }

    @Override
    public boolean shouldCreateStubForCallExpression(ASTNode node) {
        ASTNode methodExpression = JSCallExpressionImpl.getMethodExpression(node);
        if (methodExpression != null && methodExpression.getElementType() == JSElementTypes.REFERENCE_EXPRESSION && JSReferenceExpressionImpl.getQualifierNode(methodExpression) == null) {
            String name = JSReferenceExpressionImpl.getReferenceName(methodExpression);
            if ("extend".equals(name)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int getVersion() {
        return 10;
    }

    static {
        JSImplicitElementImpl.ourUserStringsRegistry.registerUserString("ui5_class");
    }
}
