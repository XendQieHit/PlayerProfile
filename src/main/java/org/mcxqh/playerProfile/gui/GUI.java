package org.mcxqh.playerProfile.gui;

import org.mcxqh.playerProfile.gui.java.menu;
import org.mcxqh.playerProfile.gui.java.status.*;
import org.mcxqh.playerProfile.gui.java.test;
import org.mcxqh.playerProfile.gui.java.title.*;

import java.util.HashMap;
import java.util.Map;

public class GUI {
    public static final test TEST = new test();
    public static final menu MENU = new menu();
    public static final status STATUS = new status();
    public static final statusList STATUS_LIST = new statusList();
    public static final statusDetail STATUS_DETAIL = new statusDetail();
    public static final statusCustomName STATUS_CUSTOM_NAME = new statusCustomName();
    public static final statusCustomColor STATUS_CUSTOM_COLOR = new statusCustomColor();
    public static final title TITLE = new title();
    public static final titleList TITLE_LIST = new titleList();
    public static final titleDetail TITLE_DETAIL = new titleDetail();
    public static final titleAward TITLE_AWARD = new titleAward();
    public static final titleAwardName TITLE_AWARD_NAME = new titleAwardName();
    public static final titleAwardColor TITLE_AWARD_COLOR = new titleAwardColor();
    public static final titleAwardIdentity TITLE_AWARD_IDENTITY = new titleAwardIdentity();

    public static final Map<GUIPanel, GUITemplate> GUI_EXECUTE_MAP_WITH_GUI_PANEL = new HashMap<>();

    static {
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.TEST, GUI.TEST);
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.MENU, GUI.MENU);
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.MENU_STATUS, GUI.STATUS);
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.STATUS_LIST, GUI.STATUS_LIST);
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.STATUS_DETAIL, GUI.STATUS_DETAIL);
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.STATUS_CUSTOM_NAME, GUI.STATUS_CUSTOM_NAME);
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.STATUS_CUSTOM_COLOR, GUI.STATUS_CUSTOM_COLOR);
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.MENU_TITLE, GUI.TITLE);
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.TITLE_LIST, GUI.TITLE_LIST);
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.TITLE_DETAIL, GUI.TITLE_DETAIL);
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.TITLE_AWARD, GUI.TITLE_AWARD);
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.TITLE_AWARD_COLOR, GUI.TITLE_AWARD_COLOR);
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.TITLE_AWARD_IDENTITY, GUI.TITLE_AWARD_IDENTITY);
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.TITLE_AWARD_NAME, GUI.TITLE_AWARD_NAME);
    }
}