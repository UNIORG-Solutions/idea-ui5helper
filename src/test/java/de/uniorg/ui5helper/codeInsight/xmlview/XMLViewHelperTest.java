package de.uniorg.ui5helper.codeInsight.xmlview;

import com.intellij.lang.xml.XMLLanguage;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import de.uniorg.ui5helper.ProjectComponent;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class XMLViewHelperTest extends LightPlatformCodeInsightFixtureTestCase {
    public void setUp() throws java.lang.Exception {
        super.setUp();
        myFixture.getProject().getComponent(ProjectComponent.class).enableProject(null);
    }

    public void testGetPossibleTags() {
        XmlFile file = (XmlFile) this.createLightFile("Test.view.xml", XMLLanguage.INSTANCE, "<mvc:View xmlns:mvc=\"sap.ui.core.mvc\" xmlns=\"sap.m\">" +
                "    <Table>" +
                "    </Table>" +
                "</mvc:View>");

        XmlTag table = file.getRootTag().getSubTags()[0];

        Collection<XMLViewHelper.PossibleTag> tags = XMLViewHelper.getPossibleTags(file, table);

        Set<String> checkMap = tags.stream().map(tag -> tag.namespace + ":" + (tag.prefix != null ? tag.prefix : "_") + ":" + tag.name).peek(System.out::println).collect(Collectors.toSet());

        assertTrue(checkMap.contains("sap.m::StandardListItem"));
        assertTrue(checkMap.contains("sap.m::ListItemBase"));
        assertTrue(checkMap.contains("sap.m::NotificationListItem"));
        assertTrue(checkMap.contains("sap.m::CustomListItem"));
        assertTrue(checkMap.contains("sap.m::GroupHeaderListItem"));
        assertTrue(checkMap.contains("sap.m::InputListItem"));
        assertTrue(checkMap.contains("sap.m::ActionListItem"));
        assertTrue(checkMap.contains("sap.m::CustomTreeItem"));
        assertTrue(checkMap.contains("sap.m::NotificationListGroup"));
        assertTrue(checkMap.contains("sap.m::FacetFilterItem"));
        assertTrue(checkMap.contains("sap.m::NotificationListBase"));
        assertTrue(checkMap.contains("sap.m::ColumnListItem"));
        assertTrue(checkMap.contains("sap.m::FeedListItem"));
        assertTrue(checkMap.contains("sap.m::TreeItemBase"));
        assertTrue(checkMap.contains("sap.m::DisplayListItem"));
        assertTrue(checkMap.contains("sap.m::ObjectListItem"));
        assertTrue(checkMap.contains("sap.m::StandardTreeItem"));
        assertTrue(checkMap.contains("sap.m::contextMenu"));
        assertTrue(checkMap.contains("sap.m::swipeContent"));
        assertTrue(checkMap.contains("sap.m::headerToolbar"));
        assertTrue(checkMap.contains("sap.m::items"));
        assertTrue(checkMap.contains("sap.m::tooltip"));
        assertTrue(checkMap.contains("sap.m::customData"));
        assertTrue(checkMap.contains("sap.m::layoutData"));
        assertTrue(checkMap.contains("sap.m::dependents"));
        assertTrue(checkMap.contains("sap.m::columns"));
        assertTrue(checkMap.contains("sap.m::infoToolbar"));
        assertTrue(checkMap.contains("sap.m::dragDropConfig"));
        assertFalse(checkMap.contains("sap.m::Table"));
    }

    public void testGetPossibleTagsWithNS() {
        XmlFile file = (XmlFile) this.createLightFile("Test.view.xml", XMLLanguage.INSTANCE, "<mvc:View xmlns:mvc=\"sap.ui.core.mvc\" xmlns:m=\"sap.m\">" +
                "    <m:Table>" +
                "    </m:Table>" +
                "</mvc:View>");

        XmlTag table = file.getRootTag().getSubTags()[0];

        Collection<XMLViewHelper.PossibleTag> tags = XMLViewHelper.getPossibleTags(file, table);

        Set<String> checkMap = tags.stream().map(tag -> tag.namespace + ":" + (tag.prefix != null ? tag.prefix : "_") + ":" + tag.name).peek(System.out::println).collect(Collectors.toSet());

        assertTrue(checkMap.contains("sap.m:m:StandardListItem"));
        assertTrue(checkMap.contains("sap.m:m:ListItemBase"));
        assertTrue(checkMap.contains("sap.m:m:NotificationListItem"));
        assertTrue(checkMap.contains("sap.m:m:CustomListItem"));
        assertTrue(checkMap.contains("sap.m:m:GroupHeaderListItem"));
        assertTrue(checkMap.contains("sap.m:m:InputListItem"));
        assertTrue(checkMap.contains("sap.m:m:ActionListItem"));
        assertTrue(checkMap.contains("sap.m:m:CustomTreeItem"));
        assertTrue(checkMap.contains("sap.m:m:NotificationListGroup"));
        assertTrue(checkMap.contains("sap.m:m:FacetFilterItem"));
        assertTrue(checkMap.contains("sap.m:m:NotificationListBase"));
        assertTrue(checkMap.contains("sap.m:m:ColumnListItem"));
        assertTrue(checkMap.contains("sap.m:m:FeedListItem"));
        assertTrue(checkMap.contains("sap.m:m:TreeItemBase"));
        assertTrue(checkMap.contains("sap.m:m:DisplayListItem"));
        assertTrue(checkMap.contains("sap.m:m:ObjectListItem"));
        assertTrue(checkMap.contains("sap.m:m:StandardTreeItem"));
        assertTrue(checkMap.contains("sap.m:m:contextMenu"));
        assertTrue(checkMap.contains("sap.m:m:swipeContent"));
        assertTrue(checkMap.contains("sap.m:m:headerToolbar"));
        assertTrue(checkMap.contains("sap.m:m:items"));
        assertTrue(checkMap.contains("sap.m:m:tooltip"));
        assertTrue(checkMap.contains("sap.m:m:customData"));
        assertTrue(checkMap.contains("sap.m:m:layoutData"));
        assertTrue(checkMap.contains("sap.m:m:dependents"));
        assertTrue(checkMap.contains("sap.m:m:columns"));
        assertTrue(checkMap.contains("sap.m:m:infoToolbar"));
        assertTrue(checkMap.contains("sap.m:m:dragDropConfig"));
    }
}
