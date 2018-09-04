package de.uniorg.ui5helper.framework;

import com.intellij.lang.ASTNode;
import com.intellij.lang.javascript.JSElementTypes;
import com.intellij.lang.javascript.JavaScriptFileType;
import com.intellij.lang.javascript.index.FrameworkIndexingHandler;
import com.intellij.lang.javascript.index.JSSymbolUtil;
import com.intellij.lang.javascript.psi.*;
import com.intellij.lang.javascript.psi.impl.JSCallExpressionImpl;
import com.intellij.lang.javascript.psi.impl.JSReferenceExpressionImpl;
import com.intellij.lang.javascript.psi.literal.JSLiteralImplicitElementProvider;
import com.intellij.lang.javascript.psi.resolve.JSEvaluateContext;
import com.intellij.lang.javascript.psi.resolve.JSSimpleTypeProcessor;
import com.intellij.lang.javascript.psi.resolve.JSTypeEvaluator;
import com.intellij.lang.javascript.psi.stubs.JSElementIndexingData;
import com.intellij.lang.javascript.psi.stubs.JSImplicitElement;
import com.intellij.lang.javascript.psi.stubs.impl.JSImplicitElementImpl;
import com.intellij.lang.javascript.psi.stubs.impl.JSImplicitParameterStructure;
import com.intellij.lang.javascript.psi.types.*;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndexImpl;
import de.uniorg.ui5helper.index.JavascriptClassIndexer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class IndexingHandler extends FrameworkIndexingHandler {

    @NotNull
    public String[] interestedProperties() {
        return new String[]{"metadata", "properties"};
    }

    @Override
    public boolean processProperty(@Nullable String name, @NotNull JSProperty value, @NotNull JSElementIndexingData outData) {
        if (name == null) {
            return true;
        }
        if ("properties".equals(name)) {
            if (outData.getImplicitElements() == null) {
                return false;
            }
            if (value.getContext() != null
                    && value.getContext().getContext() != null
                    && value.getContext().getContext().getContext() instanceof JSProperty
                    && ((JSProperty) value.getContext().getContext().getContext()).getName().equals("metadata")) {
                outData.getImplicitElements().removeIf(jsImplicitElement -> jsImplicitElement.getTypeString() == null && (jsImplicitElement.getName().startsWith("get") || jsImplicitElement.getName().startsWith("set")));
            }


        } else if ("metadata".equals(name)) {
            if (!(value.getValue() instanceof JSObjectLiteralExpression)) {
                return true;
            }
            JSObjectLiteralExpression object = (JSObjectLiteralExpression) value.getValue();
            String namespace = this.getMetadataNamespace(object);

            boolean keepIndexing = true;
            JSProperty properties = object.findProperty("properties");
            if (namespace != null && properties != null && properties.getValue() instanceof JSObjectLiteralExpression) {
                JSObjectLiteralExpression props = (JSObjectLiteralExpression) properties.getValue();
                addProperties(outData, namespace, props);
                keepIndexing = false;
            }

            JSProperty aggregations = object.findProperty("aggregations");
            if (namespace != null && aggregations != null && aggregations.getValue() instanceof JSObjectLiteralExpression) {
                JSObjectLiteralExpression props = (JSObjectLiteralExpression) aggregations.getValue();
                addAggregations(outData, namespace, props);
                keepIndexing = false;
            }

            return keepIndexing;
        }

        return true;
    }

    private void addAggregations(JSElementIndexingData outData, String namespace, JSObjectLiteralExpression props) {
        JSSymbolUtil.forEachIdentifierProperty(props, (titleCasedPropName, property) -> {
            if (property.getValue() == null || !(property.getValue() instanceof JSObjectLiteralExpression)) {
                return;
            }
            String typeString = getPropertyType(property);
            if (isMultipleAggregation(property)) {
                String singularName = getAggregationSingularName(titleCasedPropName, (JSObjectLiteralExpression) property.getValue());
                JSQualifiedName qualifiedName = JSQualifiedNameImpl.fromQualifiedName(namespace);
                /* get_s
                 * indexOf_
                 * add_
                 * insert_
                 * remove_
                 * removeAll_s
                 * destroy_s
                 */
                outData.addImplicitElement(createGetter(namespace, property, titleCasedPropName, typeString + "[]"));

                outData.addImplicitElement(createAccessorFunction(
                        "indexOf" + singularName,
                        property,
                        qualifiedName,
                        builder -> {
                            List<JSImplicitParameterStructure> insertParameters = new ArrayList<>();
                            insertParameters.add(new JSImplicitParameterStructure("oObject", typeString, false, false, false));
                            builder.setParameters(insertParameters);
                            builder.setTypeString("integer");
                        }
                ));

                outData.addImplicitElement(createAccessorFunction(
                        "add" + singularName,
                        property,
                        qualifiedName,
                        builder -> {
                            List<JSImplicitParameterStructure> insertParameters = new ArrayList<>();
                            insertParameters.add(new JSImplicitParameterStructure("oObject", typeString, false, false, false));
                            builder.setParameters(insertParameters);
                        }
                ));

                outData.addImplicitElement(createAccessorFunction(
                        "insert" + singularName,
                        property,
                        qualifiedName,
                        builder -> {
                            List<JSImplicitParameterStructure> insertParameters = new ArrayList<>();
                            insertParameters.add(new JSImplicitParameterStructure("oObject", typeString, false, false, false));
                            insertParameters.add(new JSImplicitParameterStructure("iIndex", "integer", false, false, false));
                            builder.setParameters(insertParameters);
                        }
                ));

                outData.addImplicitElement(createAccessorFunction(
                        "remove" + singularName,
                        property,
                        qualifiedName,
                        builder -> {
                            List<JSImplicitParameterStructure> removeParameters = new ArrayList<>();
                            removeParameters.add(new JSImplicitParameterStructure("vObject", typeString + "|integer", false, false, false));
                            builder.setParameters(removeParameters);
                        }
                ));

                outData.addImplicitElement(createAccessorFunction(
                        "removeAll" + titleCasedPropName,
                        property,
                        qualifiedName,
                        null
                ));

                outData.addImplicitElement(createAccessorFunction(
                        "destroy" + titleCasedPropName,
                        property,
                        qualifiedName,
                        null
                ));

            } else {
                outData.addImplicitElement(createGetter(namespace, property, titleCasedPropName, typeString));
                outData.addImplicitElement(createSetter(namespace, property, titleCasedPropName, typeString));
            }
        });
    }

    private String getAggregationSingularName(String titleCasedPropName, JSObjectLiteralExpression property) {
        JSProperty singularName = property.findProperty("singularName");

        if (singularName != null && singularName.getValue() instanceof JSLiteralExpression && ((JSLiteralExpression) singularName.getValue()).isQuotedLiteral()) {
            return ((JSLiteralExpression) singularName.getValue()).getSignificantValue();
        }

        return guessSingularName(titleCasedPropName);
    }

    private String guessSingularName(String plural) {
        final String[] pluralEndings = new String[]{
                "children", "ies", "ves", "oes", "ses", "ches", "shes", "xes", "s"
        };
        final String[] pluralReplace = new String[]{
                "child", "y", "f", "o", "s", "ch", "sh", "x", ""
        };

        for (int i = 0; i < pluralEndings.length; i++) {
            if (plural.toLowerCase().endsWith(pluralEndings[i])) {
                return plural.replaceFirst(pluralEndings[i] + "$", pluralReplace[i]);
            }
        }

        return plural;
    }

    private JSImplicitElement createAccessorFunction(String name, PsiElement element, JSQualifiedName namespace, @Nullable Consumer<JSImplicitElementImpl.Builder> builderConsumer) {

        return createElement(name, element, builder -> {
            builder.setNamespace(namespace)
                    .setContext(JSContext.INSTANCE)
                    .setType(JSImplicitElement.Type.Function)
                    .setUserString("ui5_accessor");

            if (builderConsumer != null) builderConsumer.accept(builder);
        });
    }

    private JSImplicitElement createElement(String name, PsiElement element, Consumer<JSImplicitElementImpl.Builder> builderConsumer) {
        JSImplicitElementImpl.Builder builder = new JSImplicitElementImpl.Builder(name, element);
        builderConsumer.accept(builder);
        return builder.toImplicitElement();
    }

    @NotNull
    private JSImplicitElement createGetter(String namespace, JSProperty property, String propName, String typeString) {
        return createAccessorFunction(
                JSSymbolUtil.suggestGetterName(propName),
                property,
                JSQualifiedNameImpl.fromQualifiedName(namespace),
                builder -> builder.setTypeString(typeString)
        );
    }

    @NotNull
    private JSImplicitElement createSetter(String namespace, JSProperty property, String propName, String typeString) {
        return createAccessorFunction(
                JSSymbolUtil.suggestSetterName(propName),
                property,
                JSQualifiedNameImpl.fromQualifiedName(namespace),
                builder -> {
                    List<JSImplicitParameterStructure> parameters = new ArrayList<>();
                    parameters.add(new JSImplicitParameterStructure("value", typeString, false, false, false));
                    builder.setParameters(parameters);
                }
        );
    }


    private void addProperties(@NotNull JSElementIndexingData outData, String namespace, JSObjectLiteralExpression props) {
        JSSymbolUtil.forEachIdentifierProperty(props, (titleCasedPropName, property) -> {
            String typeString = getPropertyType(property);
            outData.addImplicitElement(createGetter(namespace, property, titleCasedPropName, typeString));
            outData.addImplicitElement(createSetter(namespace, property, titleCasedPropName, typeString));
        });
    }

    private String getMetadataNamespace(JSObjectLiteralExpression value) {
        PsiElement node = value.getParent();
        while (node != null && !(node instanceof JSCallExpression)) {
            node = node.getParent();
        }

        JSCallExpression callExpression = (JSCallExpression) node;
        if (callExpression == null
                || callExpression.getMethodExpression() == null
                || !callExpression.getMethodExpression().getText().endsWith("extend")) {
            return null;
        }

        JSExpression firstArg = callExpression.getArguments()[0];
        if (firstArg != null && firstArg instanceof JSLiteralExpression) {
            return ((JSLiteralExpression) firstArg).getValue().toString();
        }

        return null;
    }

    private boolean isMultipleAggregation(JSProperty jsProperty) {
        try {
            if (jsProperty.getValue() != null && jsProperty.getValue() instanceof JSObjectLiteralExpression) {
                JSObjectLiteralExpression propertyDefinition = (JSObjectLiteralExpression) jsProperty.getValue();
                if (propertyDefinition.findProperty("multiple") != null && (propertyDefinition.findProperty("multiple").getValue() instanceof JSLiteralExpression)) {
                    if (((JSLiteralExpression) propertyDefinition.findProperty("multiple").getValue()).isBooleanLiteral()) {
                        return (boolean) ((JSLiteralExpression) propertyDefinition.findProperty("multiple").getValue()).getValue();
                    }
                }
            }
        } catch (NullPointerException npe) {
            return false;
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

    private int findParamInList(JSParameter param, JSParameter[] params) {
        for (int i = 0; i < params.length; i++) {
            if (params[i].equals(param)) {
                return i;
            }
        }

        throw new IndexOutOfBoundsException("param not found");
    }

    @Override
    public boolean addTypeFromResolveResult(@NotNull JSTypeEvaluator evaluator, @NotNull JSEvaluateContext context, @NotNull PsiElement result) {
        if (result instanceof JSParameter) {
            JSParameter param = (JSParameter) result;
            if (param.getDeclaringFunction() == null) {
                return false;
            }

            JSParameter[] params = param.getDeclaringFunction().getParameterVariables();
            int index = this.findParamInList(param, params);
            if (!(param.getDeclaringFunction().getContext() instanceof JSArgumentList)) {
                return false;
            }

            JSArgumentList defineArguments = (JSArgumentList) param.getDeclaringFunction().getContext();
            JSExpression files = defineArguments.getArguments()[0];
            if (!(files instanceof JSArrayLiteralExpression)) {
                return false;
            }

            JSExpression[] expressions = ((JSArrayLiteralExpression) files).getExpressions();
            if (expressions.length <= index) {
                return false;
            }

            JSExpression fileName = expressions[index];

            if (fileName instanceof JSLiteralExpression && ((JSLiteralExpression) fileName).getValue() instanceof String && !DumbService.isDumb(result.getProject())) {
                GlobalSearchScope scope = GlobalSearchScope.getScopeRestrictedByFileTypes(GlobalSearchScope.everythingScope(result.getProject()), JavaScriptFileType.INSTANCE);


                String moduleName = (String) ((JSLiteralExpression) fileName).getValue();
                moduleName = moduleName.replaceAll("/", ".");
                Collection<VirtualFile> fileCollection = FileBasedIndexImpl.getInstance().getContainingFiles(JavascriptClassIndexer.KEY, moduleName, scope);
                JSTypeSource source = JSTypeSourceFactory.createTypeSource(result, false);
                if (fileCollection.size() == 1) {
                    VirtualFile file = fileCollection.toArray(new VirtualFile[1])[0];
                    PsiFile targetFile = PsiManager.getInstance(result.getProject()).findFile(file);
                    if (targetFile != null) {
                        JSCallExpression defineCall = JSTreeUtil.getDefineCall(targetFile);
                        if (defineCall != null) {
                            JSCallExpression decl = JSTreeUtil.findClassDeclaration(defineCall);
                            if (decl != null) {
                                source = JSTypeSourceFactory.createTypeSource(decl, false);
                            } else {
                                source = JSTypeSourceFactory.createTypeSource(defineCall, false);
                            }
                        } else {
                            source = JSTypeSourceFactory.createTypeSource(targetFile, false);
                        }
                    }
                }
                JSType type = JSNamedTypeFactory.createType(moduleName + ".prototype.constructor", source, JSTypeContext.PROTOTYPE);

                evaluator.addType(type, result);
                return true;
            }

        }

        return super.addTypeFromResolveResult(evaluator, context, result);
    }

    @Nullable
    @Override
    public PsiElement findModule(@NotNull PsiElement result) {
        return super.findModule(result);
    }

    @Override
    public JSLiteralImplicitElementProvider createLiteralImplicitElementProvider(@NotNull String calledMethodName) {
        if ("define".equals(calledMethodName)) {
            return new JSLiteralImplicitElementProvider() {
                public void fillIndexingData(@NotNull JSLiteralExpression argument, @NotNull JSCallExpression callExpression, @NotNull JSElementIndexingData outIndexingData) {
                    JSExpression[] arguments = callExpression.getArguments();
                }
            };
        }
        if ("extend".equals(calledMethodName)) {
            return new JSLiteralImplicitElementProvider() {
                public void fillIndexingData(@NotNull JSLiteralExpression argument, @NotNull JSCallExpression callExpression, @NotNull JSElementIndexingData outIndexingData) {
                    JSExpression[] arguments = callExpression.getArguments();
                    if (arguments.length >= 2 && arguments[0] instanceof JSLiteralExpression && ((JSLiteralExpression) arguments[0]).isQuotedLiteral()) {
                        String name = (String) ((JSLiteralExpression) arguments[0]).getValue();
                        if (name == null || name.lastIndexOf(".") == -1) {
                            return;
                        }

                        String namespace = name.substring(0, name.lastIndexOf("."));
                        String className = name.substring(name.lastIndexOf(".") + 1);
                        JSImplicitElementImpl.Builder builder = (new JSImplicitElementImpl.Builder(className, argument))
                                .setNamespace(JSQualifiedNameImpl.fromNamepath(namespace))
                                .setType(JSImplicitElement.Type.Class)
                                .setUserString("ui5_class");

                        outIndexingData.addImplicitElement(builder.toImplicitElement());

                        if (!DumbService.isDumb(argument.getProject())) {
                            JSTypeEvaluator.evaluateTypes((JSExpression) callExpression.getMethodExpression().getFirstChild(), callExpression.getContainingFile(), new JSSimpleTypeProcessor());
                        }
                    }
                }
            };
        }

        return super.createLiteralImplicitElementProvider(calledMethodName);
    }

    @Nullable
    @Override
    public String resolveContextFromProperty(JSObjectLiteralExpression objectLiteralExpression, boolean returnPropertiesNamespace) {
        if (objectLiteralExpression.getContext() instanceof JSArgumentList) {
            JSArgumentList argumentList = (JSArgumentList) objectLiteralExpression.getContext();
            JSExpression[] args = argumentList.getArguments();
            if (args[0] instanceof JSLiteralExpression && ((JSLiteralExpression) args[0]).getValue() != null) {
                JSLiteralExpression name = (JSLiteralExpression) args[0];
                return name.getValue().toString();
            }
        }
        return super.resolveContextFromProperty(objectLiteralExpression, returnPropertiesNamespace);
    }

    @NotNull
    @Override
    public String[] inheritanceMethodNames() {
        return new String[]{"extend"};
    }

    @Override
    public boolean shouldCreateStubForCallExpression(ASTNode node) {
        ASTNode methodExpression = JSCallExpressionImpl.getMethodExpression(node);
        if (methodExpression != null && methodExpression.getElementType() == JSElementTypes.REFERENCE_EXPRESSION && JSReferenceExpressionImpl.getQualifierNode(methodExpression) == null) {
            String name = JSReferenceExpressionImpl.getReferenceName(methodExpression);
            if ("extend".equals(name) || "define".equals(name)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int getVersion() {
        return 12;
    }

    static {
        JSImplicitElementImpl.ourUserStringsRegistry.registerUserString("ui5_class");
        JSImplicitElementImpl.ourUserStringsRegistry.registerUserString("ui5_accessor");
    }
}
