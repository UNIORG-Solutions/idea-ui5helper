package de.uniorg.ui5helper.index;

import com.intellij.lang.javascript.JavaScriptFileType;
import com.intellij.lang.javascript.psi.*;
import com.intellij.util.indexing.*;
import com.intellij.util.io.EnumeratorStringDescriptor;
import com.intellij.util.io.KeyDescriptor;
import de.uniorg.ui5helper.framework.JSTreeUtil;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class JavascriptClassIndexer extends ScalarIndexExtension<String> {

    public static final ID<String, Void> KEY = ID.create("ui5.helper.js.classes.index");
    private final KeyDescriptor<String> myKeyDescriptor = new EnumeratorStringDescriptor();

    @Override
    public boolean dependsOnFileContent() {
        return true;
    }

    @NotNull
    @Override
    public ID<String, Void> getName() {
        return KEY;
    }


    private String getClassName(JSCallExpression extendCall) {
        if (extendCall.getArguments().length == 0) {
            return null;
        }

        JSExpression firstArgument = extendCall.getArguments()[0];
        if (!(firstArgument instanceof JSLiteralExpression)) {
            return null;
        }

        if (((JSLiteralExpression) firstArgument).getValue() instanceof String) {
            return (String) ((JSLiteralExpression) firstArgument).getValue();
        }

        return null;
    }

    @NotNull
    @Override
    public DataIndexer<String, Void, FileContent> getIndexer() {
        return inputData -> {
            Map<String, Void> map = new THashMap<>();
            JSCallExpression defineCall = JSTreeUtil.getDefineCall(inputData.getPsiFile());
            if (defineCall == null) {
                return map;
            }

            JSFunctionExpression factory = (JSFunctionExpression) defineCall.getArguments()[1];
            if (factory == null) {
                return map;
            }

            JSReturnStatement ret = findReturn(factory.getBody());

            if (ret == null) {
                return map;
            }

            if (ret.getExpression() instanceof JSCallExpression && ((JSCallExpression) ret.getExpression()).getMethodExpression().getText().endsWith("extend")) {
                JSCallExpression extendCall = (JSCallExpression) ret.getExpression();

                String className = this.getClassName(extendCall);
                if (className != null) {
                    map.put(className, null);
                }
            }

            return map;
        };
    }

    private JSReturnStatement findReturn(JSSourceElement[] statements) {
        for (JSSourceElement jsSourceElement : statements) {
            if (jsSourceElement instanceof JSBlockStatement) {
                JSReturnStatement tmp = findReturn(((JSBlockStatement) jsSourceElement).getStatements());
                if (tmp != null) {
                    return tmp;
                }
            }
            if (jsSourceElement instanceof JSReturnStatement) {
                return (JSReturnStatement) jsSourceElement;
            }
        }

        return null;
    }

    private JSReturnStatement findReturn(JSStatement[] statements) {
        for (JSStatement jsSourceElement : statements) {
            if (jsSourceElement instanceof JSBlockStatement) {
                JSReturnStatement tmp = findReturn(((JSBlockStatement) jsSourceElement).getStatements());
                if (tmp != null) {
                    return tmp;
                }
            }
            if (jsSourceElement instanceof JSReturnStatement) {
                return (JSReturnStatement) jsSourceElement;
            }
        }

        return null;
    }

    @NotNull
    @Override
    public KeyDescriptor<String> getKeyDescriptor() {
        return this.myKeyDescriptor;
    }

    @Override
    public int getVersion() {
        return 2;
    }

    @NotNull
    @Override
    public FileBasedIndex.InputFilter getInputFilter() {
        return (file) -> file.getFileType() instanceof JavaScriptFileType;
    }

}
