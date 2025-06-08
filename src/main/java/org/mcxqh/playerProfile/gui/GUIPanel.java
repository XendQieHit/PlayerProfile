package org.mcxqh.playerProfile.gui;

public enum GUIPanel {
    // Common
    TEST(PanelType.COMMON),

    // Profile
    MENU_PROFILE(PanelType.COMMON),

    // Status
    STATUS_CUSTOM_NAME(PanelType.ANVIL),
    STATUS_CUSTOM_COLOR(PanelType.COMMON),
    STATUS_DETAIL(PanelType.COMMON),
    STATUS_LIST(PanelType.LIST),
    MENU_STATUS(PanelType.COMMON),

    // Guild
    MENU_GUILD(PanelType.COMMON),

    // Team
    MENU_TEAM(PanelType.COMMON),

    // Group
    MENU_GROUP(PanelType.COMMON),

    // Title
    TITLE_AWARD_IDENTITY(PanelType.COMMON),
    TITLE_AWARD_COLOR(PanelType.COMMON),
    TITLE_AWARD_NAME(PanelType.ANVIL),
    TITLE_AWARD(PanelType.COMMON),
    TITLE_DETAIL(PanelType.COMMON),
    TITLE_LIST(PanelType.LIST),
    MENU_TITLE(PanelType.COMMON),

    MENU(PanelType.COMMON);


    public final PanelType panelType;
    GUIPanel(PanelType panelType) {
        this.panelType = panelType;
    }

    public enum PanelType {
        COMMON, LIST, ANVIL;
    }
}
