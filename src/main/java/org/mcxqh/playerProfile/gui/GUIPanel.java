package org.mcxqh.playerProfile.gui;

public enum GUIPanel {
    TEST(null),

    // Profile
    MENU_PROFILE(null),

    // Status
    STATUS_CUSTOM_NAME(null),
    STATUS_CUSTOM_COLOR(null),
    STATUS_DETAIL(new GUIPanel[]{STATUS_CUSTOM_COLOR, STATUS_CUSTOM_NAME}),
    STATUS_LIST(new GUIPanel[]{STATUS_DETAIL}),
    MENU_STATUS(new GUIPanel[]{STATUS_LIST}),

    // Guild
    MENU_GUILD(null),

    // Team
    MENU_TEAM(null),

    // Group
    MENU_GROUP(null),

    // Title
    TITLE_AWARD_IDENTITY(null),
    TITLE_AWARD_COLOR(null),
    TITLE_AWARD_NAME(null),
    TITLE_AWARD(new GUIPanel[]{TITLE_AWARD_NAME, TITLE_AWARD_COLOR, TITLE_AWARD_IDENTITY}),
    TITLE_DETAIL(null),
    TITLE_LIST(new GUIPanel[]{TITLE_DETAIL}),
    MENU_TITLE(new GUIPanel[]{TITLE_LIST, TITLE_AWARD}),

    MENU(new GUIPanel[]{MENU_GROUP, MENU_GUILD, MENU_PROFILE, MENU_TITLE, MENU_STATUS, MENU_TEAM});


    public final GUIPanel[] subPanel;
    GUIPanel(GUIPanel[] subPanel) {
        this.subPanel = subPanel;
    }
}
