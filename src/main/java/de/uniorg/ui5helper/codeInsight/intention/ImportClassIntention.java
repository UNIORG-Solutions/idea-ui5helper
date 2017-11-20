package de.uniorg.ui5helper.codeInsight.intention;

import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.TemplateManager;
import com.intellij.lang.javascript.JavaScriptFileType;
import com.intellij.lang.javascript.psi.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import de.uniorg.ui5helper.ui5.ModuleApiSymbol;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import static de.uniorg.ui5helper.ProjectComponent.isEnabled;
import static de.uniorg.ui5helper.framework.JSTreeUtil.getDefineCall;

public class ImportClassIntention extends BaseIntentionAction {

    private final String className;
    private final ModuleApiSymbol doc;

    public ImportClassIntention(String className, ModuleApiSymbol namespace) {
        this.className = className;
        this.doc = namespace;
    }


    @NotNull
    @Override
    public String getText() {
        return "Create import for " + this.doc.getName();
    }

    /**
     * Returns the name of the family of intentions. It is used to externalize
     * "auto-show" state of intentions. When user clicks on a lightbulb in intention list,
     * all intentions with the same family name get enabled/disabled.
     * The name is also shown in settings tree.
     *
     * @return the intention family name.
     * @see IntentionManager#registerIntentionAndMetaData(IntentionAction, String...)
     */
    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return "UI5 Classes";
    }

    /**
     * Checks whether this intention is available at a caret offset in file.
     * If this method returns true, a light bulb for this intention is shown.
     *
     * @param project the project in which the availability is checked.
     * @param editor  the editor in which the intention will be invoked.
     * @param file    the file open in the editor.
     * @return true if the intention is available, false otherwise.
     */
    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
        return isEnabled(project) && file.getFileType().equals(JavaScriptFileType.INSTANCE);
    }

    /**
     * Called when user invokes intention. This method is called inside command.
     * If {@link #startInWriteAction()} returns true, this method is also called
     * inside write action.
     *
     * @param project the project in which the intention is invoked.
     * @param editor  the editor in which the intention is invoked.
     * @param file    the file open in the editor.
     */
    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        JSCallExpression defineCall = getDefineCall(file);
        if (defineCall == null) {
            return;
        }

        JSExpression[] defineArguments = defineCall.getArguments();
        if (!(defineArguments[0] instanceof JSArrayLiteralExpression) || !(defineArguments[1] instanceof JSFunctionExpression)) {
            System.out.println("no define call found");
            return;
        }

        JSArrayLiteralExpression requiredFiles = (JSArrayLiteralExpression) defineArguments[0];
        JSFunctionExpression constructor = (JSFunctionExpression) defineArguments[1];
        int oldOffset = editor.getCaretModel().getOffset();
        this.addArgumentToConstructor(project, editor, constructor);
        this.addFileToImports(project, editor, requiredFiles);
        editor.getCaretModel().moveToOffset(oldOffset);
        editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);

    }

    private void addArgumentToConstructor(Project project, Editor editor, JSFunctionExpression constructor) throws IncorrectOperationException {
        TemplateManager templateManager = TemplateManager.getInstance(project);
        Template x = templateManager.createTemplate("", "");

        x.addTextSegment(", " + this.className);
        PsiElement argumentList = constructor.getFirstChild();
        while (!(argumentList instanceof JSParameterList)) {
            argumentList = argumentList.getNextSibling();
            if (argumentList == null) {
                throw new IncorrectOperationException("was heir los?");
            }
        }

        PsiElement lastPoint = argumentList.getLastChild();
        editor.getCaretModel().moveToOffset(lastPoint.getTextOffset(), true);
        templateManager.startTemplate(editor, x);
        templateManager.finishTemplate(editor);
    }

    private void addFileToImports(Project project, Editor editor, JSArrayLiteralExpression requiredFiles) {
        TemplateManager templateManager = TemplateManager.getInstance(project);
        Template x = templateManager.createTemplate("", "");

        x.addTextSegment(",\n\"");
        x.addTextSegment(this.doc.getModuleName());
        x.addTextSegment("\"");

        PsiElement lastPoint = requiredFiles.getLastChild().getPrevSibling();
        editor.getCaretModel().moveToOffset(lastPoint.getTextRange().getEndOffset());
        templateManager.startTemplate(editor, x);
        templateManager.finishTemplate(editor);
    }
}
