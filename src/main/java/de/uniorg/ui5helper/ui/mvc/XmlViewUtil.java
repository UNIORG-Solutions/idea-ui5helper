package de.uniorg.ui5helper.ui.mvc;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;

public class XmlViewUtil {
    public static Key<String> CONTROLLER_NAME = Key.create("ui5_helper.xmlview.controllerName");

    public static boolean isXmlFragment(PsiFile file) {
        if (!(file instanceof XmlFile)) {
            return false;
        }

        XmlFile xmlFile = (XmlFile) file;

        final XmlTag rootTag = xmlFile.getRootTag();
        return rootTag != null && (rootTag.getLocalName().equals("FragmentDefinition"));
    }

    public static boolean isXmlView(PsiFile file) {
        if (!(file instanceof XmlFile)) {
            return false;
        }

        XmlFile xmlFile = (XmlFile) file;

        final XmlTag rootTag = xmlFile.getRootTag();
        return rootTag != null && (rootTag.getLocalName().equals("View") || rootTag.getLocalName().equals("XMLView"));
    }

    public static String getControllerName(PsiFile file) {
        if (!isXmlView(file)) {
            return null;
        }

        try {
            XmlTag viewTag = ((XmlFile) file).getRootTag();
            if (!isXmlView(file) || viewTag == null) {
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
