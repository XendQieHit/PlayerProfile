package org.mcxqh.playerProfile.gui;

public enum GUIPanel {
    // Common
    TEST(GUI.TEST, PanelType.COMMON, null),

    MENU(GUI.MENU, PanelType.COMMON, null),
    // Profile
    MENU_PROFILE(GUI.PanelType.COMMON, MENU),

    // Status
    MENU_STATUS(PanelType.COMMON, MENU),
    STATUS_LIST(PanelType.LIST, MENU_STATUS),
    STATUS_DETAIL(PanelType.COMMON, MENU_STATUS),
    STATUS_CUSTOM_NAME(PanelType.ANVIL, STATUS_DETAIL),
    STATUS_CUSTOM_COLOR(PanelType.COMMON, STATUS_DETAIL),

    // Guild
    MENU_GUILD(PanelType.COMMON, MENU),

    // Team
    MENU_TEAM(PanelType.COMMON, MENU),

    // Group
    MENU_GROUP(PanelType.COMMON, MENU),

    // Title,
    MENU_TITLE(PanelType.COMMON, MENU),
    TITLE_LIST(PanelType.LIST, MENU_TITLE),
    TITLE_DETAIL(PanelType.COMMON, MENU_TITLE),
    TITLE_AWARD(PanelType.COMMON, MENU_TITLE),
    TITLE_AWARD_NAME(PanelType.ANVIL, TITLE_AWARD),
    TITLE_AWARD_PLAYER(PanelType.COMMON, TITLE_AWARD),
    TITLE_AWARD_IDENTITY(PanelType.COMMON, TITLE_AWARD),
    TITLE_AWARD_DESCRIPTION(PanelType.WRITABLE_BOOK, TITLE_AWARD);


    public final GUI instance;
    public final PanelType panelType;
    public final GUIPanel parentGUIPanel;
    GUIPanel(GUI instance, PanelType panelType, GUIPanel parentGUIPanel) {
        this.instance = instance;
        this.panelType = panelType;
        this.parentGUIPanel = parentGUIPanel;
    }

    public enum PanelType {
        COMMON, LIST, ANVIL, WRITABLE_BOOK;
    }
}
