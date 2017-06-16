package de.uniorg.ui5helper.framework;

import com.intellij.lang.javascript.JavaScriptFileType;
import com.intellij.lang.javascript.psi.JSArrayLiteralExpression;
import com.intellij.lang.javascript.psi.JSCallExpression;
import com.intellij.lang.javascript.psi.JSExpression;
import com.intellij.lang.javascript.psi.JSLiteralExpression;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.include.FileIncludeInfo;
import com.intellij.psi.impl.include.FileIncludeProvider;
import com.intellij.util.Consumer;
import com.intellij.util.indexing.FileContent;
import de.uniorg.ui5helper.ProjectComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

import static de.uniorg.ui5helper.ui5.general.FileType.JAVASCRIPT;

public class IncludeProvider extends FileIncludeProvider {
    @NotNull
    @Override
    public String getId() {
        return "ui5helper.framework.includeprovider";
    }

    @Override
    public boolean acceptFile(VirtualFile file) {
        return file.getFileType() == JavaScriptFileType.INSTANCE;
    }

    @Override
    public void registerFileTypesUsedForIndexing(@NotNull Consumer<FileType> fileTypeSink) {
        fileTypeSink.consume(JavaScriptFileType.INSTANCE);
    }

    @NotNull
    @Override
    public FileIncludeInfo[] getIncludeInfos(FileContent content) {
        PsiFile psiContent = content.getPsiFile();
        JSCallExpression call = JSTreeUtil.getDefineCall(psiContent);
        if (call == null) {
            return FileIncludeInfo.EMPTY;
        }

        if (call.getArguments().length >= 2 && call.getArguments()[0] instanceof JSArrayLiteralExpression) {
            return Arrays.stream(((JSArrayLiteralExpression) call.getArguments()[0]).getExpressions())
                    .map(this::resolveIncludeInfo)
                    .filter(Objects::nonNull)
                    .toArray(FileIncludeInfo[]::new);
        }

        return FileIncludeInfo.EMPTY;
    }

    private FileIncludeInfo resolveIncludeInfo(JSExpression expression) {
        if (!(expression instanceof JSLiteralExpression)) {
            return null;
        }

        JSLiteralExpression lit = (JSLiteralExpression) expression;

        if (!lit.isQuotedLiteral()) {
            return null;
        }

        String filePath = (String) lit.getValue();

        if (filePath == null || filePath.startsWith("sap")) {
            return null;
        }

        if (filePath.startsWith(".")) {
            return new FileIncludeInfo(FileUtil.toSystemIndependentName(filePath + ".js"));
        } else {
            PsiFile[] possibleFiles = expression.getProject().getComponent(ProjectComponent.class).getPathResolver().tryLookupFile(filePath, JAVASCRIPT);
            if (possibleFiles.length == 1) {
                return new FileIncludeInfo(FileUtil.toSystemIndependentName(possibleFiles[0].getVirtualFile().getPath()));
            }
        }

        return null;
    }
}
