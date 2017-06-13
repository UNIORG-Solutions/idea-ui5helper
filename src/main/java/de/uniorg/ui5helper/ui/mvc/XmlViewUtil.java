package de.uniorg.ui5helper.ui.mvc;

import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;

public class XmlViewUtil {
    public static boolean isXmlView(PsiFile file) {
        if (!(file instanceof XmlFile)) {
            return false;
        }

        XmlFile xmlFile = (XmlFile) file;

        try {
            return xmlFile.getRootTag().getLocalName().equals("View") || xmlFile.getRootTag().getLocalName().equals("XMLView");
        } catch (NullPointerException npe) {
            return false;
        }
    }

    public static String getControllerName(PsiFile file) {
        if (!isXmlView(file)) {
            return null;
        }

        try {
            XmlTag viewTag = ((XmlFile) file).getRootTag();
            if (!viewTag.getLocalName().equals("View") && !viewTag.getLocalName().equals("XMLView")) {
                return null;
            }

            XmlAttribute controllerNameAttr = viewTag.getAttribute("controllerName");

            if (controllerNameAttr == null) {
                return null;
            }

            return controllerNameAttr.getValue();
        } catch (NullPointerException npe) {
            return null;
        }
    }
}
