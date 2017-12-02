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
import java.util.Objects;
import java.util.stream.Collectors;

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

        if (controllerName == null) {
            return list;
        }

        Collection<VirtualFile> fileCollection = FileBasedIndexImpl.getInstance().getContainingFiles(NaiveXmlViewIndexer.KEY, controllerName, GlobalSearchScope.getScopeRestrictedByFileTypes(GlobalSearchScope.allScope(psiElement.getProject()), XmlFileType.INSTANCE));

        PsiManager psiManager = PsiManager.getInstance(psiElement.getProject());
        List<PsiFile> targets = fileCollection.stream()
                .map(psiManager::findFile)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (targets.size() > 0) {
            NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(Icons.OPENUI5)
                    .setTargets(targets)
                    .setNamer(psiFile -> {
                        if (psiFile instanceof PsiFile) {
                            return ((PsiFile) psiFile).getVirtualFile().getName();
                        }

                        return psiFile.toString();
                    })
                    .setTooltipText("go to View");

            list.add(builder.createLineMarkerInfo(psiElement));
        }

        return list;
    }
}
