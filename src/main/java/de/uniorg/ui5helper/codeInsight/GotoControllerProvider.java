package de.uniorg.ui5helper.codeInsight;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.lang.javascript.JavaScriptFileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.indexing.FileBasedIndexImpl;
import de.uniorg.ui5helper.Icons;
import de.uniorg.ui5helper.index.mvc.NaiveControllerIndexer;
import de.uniorg.ui5helper.ui.mvc.ControllerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GotoControllerProvider extends RelatedItemLineMarkerProvider {

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element, Collection<? super RelatedItemLineMarkerInfo> result) {
        Collection<RelatedItemLineMarkerInfo> info = collect(element);
        result.addAll(info);
    }

    private Collection<RelatedItemLineMarkerInfo> collect(@NotNull PsiElement psiElement) {
        List<RelatedItemLineMarkerInfo> list = new LinkedList<>();
        if (!(psiElement instanceof XmlTag)) {
            return list;
        }

        XmlTag xmlElement = (XmlTag) psiElement;
        if (!xmlElement.getLocalName().equalsIgnoreCase("View") && !xmlElement.getLocalName().equalsIgnoreCase("XMLView")) {
            return list;
        }

        XmlAttribute controllerName = xmlElement.getAttribute("controllerName");
        if (controllerName == null || controllerName.getValue() == null) {
            return list;
        }

        Collection<VirtualFile> fileCollection = FileBasedIndexImpl.getInstance().getContainingFiles(NaiveControllerIndexer.KEY, controllerName.getValue(), GlobalSearchScope.getScopeRestrictedByFileTypes(GlobalSearchScope.allScope(psiElement.getProject()), JavaScriptFileType.INSTANCE));

        fileCollection.iterator().forEachRemaining(
                virtualFile -> {
                    PsiFile targetFile = PsiManager.getInstance(psiElement.getProject()).findFile(virtualFile);
                    if (targetFile == null) {
                        return;
                    }

                    Map<String, PsiElement> declarations = ControllerUtil.findDeclarations(targetFile);

                    PsiElement target = targetFile;
                    if (declarations.containsKey(controllerName.getValue())) {
                        target = declarations.get(controllerName.getValue());
                    }

                    NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(Icons.OPENUI5)
                            .setTarget(target)
                            .setTooltipText("go to Controller");

                    list.add(builder.createLineMarkerInfo(controllerName));
                }
        );

        return list;
    }
}
