package de.uniorg.ui5helper.framework.Annotator;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiElement;
import de.uniorg.ui5helper.Icons;
import de.uniorg.ui5helper.ui5.IconInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IconPreview implements LineMarkerProvider {
    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement psiElement) {
        Pattern pattern = Pattern.compile("sap-icon://([a-z0-9-]*)");
        Matcher matcher = pattern.matcher(psiElement.getText());
        if (matcher.matches()) {
            String code = matcher.group(1);
            Optional<Character> charCode = IconInfo.getIconCode(code);
            if (charCode.isPresent()) {
                return new LineMarkerInfo(
                        psiElement,
                        psiElement.getTextRange(),
                        Icons.INSTANCE.fromIconFont(charCode.get().charValue()),
                        0,
                        null,
                        null,
                        GutterIconRenderer.Alignment.LEFT
                );
            }
        }

        return null;
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> list, @NotNull Collection<LineMarkerInfo> collection) {
    }
}
