package de.uniorg.ui5helper.codeInsight;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.lang.javascript.psi.JSCallExpression;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndexImpl;
import de.uniorg.ui5helper.Icons;
import de.uniorg.ui5helper.index.mvc.NaiveXmlViewIndexer;
import de.uniorg.ui5helper.ui.mvc.ControllerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class GotoViewProvider extends RelatedItemLineMarkerProvider {

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element, Collection<? super RelatedItemLineMarkerInfo> result) {
        Collection<RelatedItemLineMarkerInfo> info = collect(element);
        result.addAll(info);
    }

    private Collection<RelatedItemLineMarkerInfo> collect(@NotNull PsiElement psiElement) {
        List<RelatedItemLineMarkerInfo> list = new LinkedList<>();
        if (!ControllerUtil.isControllerDeclaration(psiElement)) {
            return list;
        }

        String controllerName = ControllerUtil.getNameFromDeclaration((JSCallExpression) psiElement);

        Collection<VirtualFile> fileCollection = FileBasedIndexImpl.getInstance().getContainingFiles(NaiveXmlViewIndexer.KEY, controllerName, GlobalSearchScope.getScopeRestrictedByFileTypes(GlobalSearchScope.allScope(psiElement.getProject()), XmlFileType.INSTANCE));

        fileCollection.iterator().forEachRemaining(
                virtualFile -> {
                    PsiFile targetFile = PsiManager.getInstance(psiElement.getProject()).findFile(virtualFile);
                    if (targetFile == null) {
                        return;
                    }

                    NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(Icons.OPENUI5)
                            .setTarget(targetFile)
                            .setTooltipText("go to View");

                    list.add(builder.createLineMarkerInfo(psiElement));
                }
        );

        return list;
    }
}
