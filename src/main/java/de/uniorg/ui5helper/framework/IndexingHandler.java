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
import com.intellij.lang.javascript.psi.resolve.JSSimpleTypeProcessor;
import com.intellij.lang.javascript.psi.resolve.JSTypeEvaluator;
import com.intellij.lang.javascript.psi.stubs.JSElementIndexingData;
import com.intellij.lang.javascript.psi.stubs.JSImplicitElement;
import com.intellij.lang.javascript.psi.stubs.impl.JSImplicitElementImpl;
import com.intellij.lang.javascript.psi.stubs.impl.JSImplicitFunctionImpl;
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

public class IndexingHandler extends FrameworkIndexingHandler {

    @NotNull
    public String[] interestedProperties() {
        return new String[]{"metadata", "properties"};
    }

    @Override
    public boolean processProperty(@Nullable String name, @NotNull JSProperty value, @NotNull JSElementIndexingData outData) {
        switch (name) {
            case "properties":
                if (outData.getImplicitElements() == null) {
                    return false;
                }
                if (value.getContext() != null
                        && value.getContext().getContext() != null
                        && value.getContext().getContext().getContext() instanceof JSProperty
                        && ((JSProperty) value.getContext().getContext().getContext()).getName().equals("metadata")) {
                    outData.getImplicitElements().removeIf(jsImplicitElement -> jsImplicitElement.getTypeString() == null && (jsImplicitElement.getName().startsWith("get") || jsImplicitElement.getName().startsWith("set")));
                }

                break;
            case "metadata":
                if (!(value instanceof JSObjectLiteralExpression)) {
                    return true;
                }
                JSObjectLiteralExpression object = (JSObjectLiteralExpression) value;
                String namespace = this.getMetadataNamespace(object);

                JSProperty properties = object.findProperty("properties");
                if (namespace != null && properties != null && properties.getValue() instanceof JSObjectLiteralExpression) {
                    JSObjectLiteralExpression props = (JSObjectLiteralExpression) properties.getValue();
                    JSSymbolUtil.forEachIdentifierProperty(props, (titleCasedPropName, property) -> {
                        String typeString = getPropertyType(property);
                        JSImplicitElementImpl.Builder getterBuilder = new JSImplicitElementImpl.Builder(JSSymbolUtil.suggestGetterName(titleCasedPropName), property);
                        getterBuilder.setNamespace(JSQualifiedNameImpl.fromQualifiedName(namespace))
                                .setContext(JSContext.INSTANCE)
                                .setType(JSImplicitElement.Type.Function)
                                .setTypeString(typeString)
                                .setUserString("ui5_accessor");

                        outData.addImplicitElement(getterBuilder.toImplicitElement());
                        JSImplicitFunctionImpl.Builder setterBuilder = (new JSImplicitFunctionImpl.Builder(JSSymbolUtil.suggestSetterName(titleCasedPropName), property));
                        List<JSImplicitParameterStructure> parameters = new ArrayList<>();
                        parameters.add(new JSImplicitParameterStructure("value", typeString, false, false, false));
                        setterBuilder.setNamespace(JSQualifiedNameImpl.fromQualifiedName(namespace))
                                .setContext(JSContext.INSTANCE)
                                .setType(JSImplicitElement.Type.Function)
                                .setParameters(parameters)
                                .setUserString("ui5_accessor");
                        outData.addImplicitElement(setterBuilder.toImplicitElement());
                    });

                    return false;
                }
        }

        return true;
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
    public boolean addTypeFromResolveResult(JSTypeEvaluator evaluator, PsiElement result, boolean hasSomeType) {
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
                JSTypeSource source = JSTypeSourceFactory.createTypeSource(result);
                if (fileCollection.size() == 1) {
                    VirtualFile file = fileCollection.toArray(new VirtualFile[1])[0];
                    PsiFile targetFile = PsiManager.getInstance(result.getProject()).findFile(file);
                    if (targetFile != null) {
                        JSCallExpression defineCall = JSTreeUtil.getDefineCall(targetFile);
                        if (defineCall != null) {
                            JSCallExpression decl = JSTreeUtil.findClassDeclaration(defineCall);
                            if (decl != null) {
                                source = JSTypeSourceFactory.createTypeSource(decl);
                            } else {
                                source = JSTypeSourceFactory.createTypeSource(defineCall);
                            }
                        } else {
                            source = JSTypeSourceFactory.createTypeSource(targetFile);
                        }
                    }
                }
                JSType type = JSNamedType.createType(moduleName + ".prototype.constructor", source, JSTypeContext.PROTOTYPE);

                evaluator.addType(type, result);
                return true;
            }

        }

        return super.addTypeFromResolveResult(evaluator, result, hasSomeType);
    }

    @Nullable
    @Override
    public PsiElement findModule(@NotNull PsiElement result) {
        return super.findModule(result);
    }

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
    public String[] interestedMethodNames() {
        return new String[]{"extend", "define"};
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
