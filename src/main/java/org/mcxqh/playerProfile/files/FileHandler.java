package org.mcxqh.playerProfile.files;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.players.Profile;
import org.mcxqh.playerProfile.players.profile.identity.Identity;
import org.mcxqh.playerProfile.players.profile.title.Title;
import org.mcxqh.playerProfile.players.profile.status.Status;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

public class FileHandler {

    private final File rootFolder = new File("plugins/PlayerProfile");
    private final File profileFolder = new File(rootFolder, "Profile");
    private final File statusFolder = new File(rootFolder, "Status");
    private final File titleFolder = new File(rootFolder, "Title");

    public FileHandler() {}

    /**
     * Get player's status setting, which is configured by this player.
     * If there isn't exist json type of config file, this will also create new one.
     */
    public JsonArray getStatus(Player player) {
        JsonArray jsonArray = null;
        for (int i = 0; i < 2; i++) {
            FileHandler fileHandler = new FileHandler();
            try {
                jsonArray = fileHandler.getStatusRaw(player);
                break;
            } catch (NullPointerException e) {
                try {
                    fileHandler.resetStatus(player);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } catch (FileNotFoundException e) {
                try {
                    fileHandler.createStatus(player);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        if (jsonArray == null || jsonArray.isEmpty()) {
            Logger.getLogger("PlayerProfile").info("获取状态配置文件失败");
            player.spigot().sendMessage(new ComponentBuilder("获取状态配置文件失败").color(ChatColor.RED).create());
            throw new NullPointerException();
        }
        return jsonArray;
    }

    /**
     * Create a preprocessing status file. This method usually applied in command executor.
     */
    public void createStatus(Player player) throws IOException {
        String fileName = player.getName() + "@" + player.getUniqueId() + ".json";
        File statusFile = new File(statusFolder, fileName);

        // check status file
        if (!statusFile.exists()) {
            if (!statusFolder.exists()) {
                statusFolder.mkdir();
            }
            statusFile.createNewFile();
            Logger.getLogger("PLayerProfile").info("12346");

            Profile profile = Data.profileMapWithUUID.get(player.getUniqueId());

            // Initialize status file
            BufferedWriter writer = new BufferedWriter(new FileWriter(statusFile));
            JsonArray json = new JsonArray();

            Gson gson = new Gson();

            for (Class<? extends Status> statusClass : profile.getStatusManager().getStatusClassArrayList()) {
                JsonObject status = new JsonObject();
                try {
                    status.addProperty(statusClass.getSimpleName(), gson.toJson(statusClass.getDeclaredConstructor().newInstance()));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
                json.add(gson.toJson(status));
            }
            writer.write(json.toString());
            writer.close();
        }
    }


    public JsonArray getStatusRaw(Player player) throws FileNotFoundException, NullPointerException {
        String fileName = player.getName() + "@" + player.getUniqueId() + ".json";
        File statusSettingFile = new File(statusFolder, fileName);

        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(statusSettingFile));
        JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);
        if (jsonArray == null || jsonArray.isEmpty()) {
            throw new NullPointerException();
        }
        return jsonArray;
    }

    /**
     * Reset player's status setting. It is also used for new player to build status setting file.
     */
    public void resetStatus(Player player) throws IOException {
        String fileName = player.getName() + "@" + player.getUniqueId() + ".json";
        File statusSettingFile = new File(statusFolder, fileName);

        // delete title file
        if (statusFolder.exists() && statusSettingFile.exists()) {
            statusSettingFile.delete();
        }

        // generate new title file
        createStatus(player);
    }

    /**
     * Write status setting in status's json file.
     */
    public void saveStatus(Player player, JsonObject StatusSetting) throws IOException {
        String fileName = player.getName() + "@" + player.getUniqueId() + ".json";
        File statusSettingFile = new File(statusFolder, fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(statusSettingFile))) {
            writer.write(StatusSetting.toString());
        }
    }

    /**
     * Create a new profile file, which named according to player.
     */
    public void createProfile(Player player) throws IOException {
        final File profileSettingFile = new File(profileFolder, player.getName() + "@" + player.getUniqueId() + ".json");

        // check title file
        if (!profileSettingFile.exists()) {
            if (!profileFolder.exists()) {
                profileFolder.mkdir();
            }
            // create new title file
            profileSettingFile.createNewFile();

            // Initialize new title file
            BufferedWriter writer = new BufferedWriter(new FileWriter(profileSettingFile));
            JsonObject json = new JsonObject();
            // {playerName: player.getName()}
            json.addProperty("playerName", player.getName());

            // {name: example, color: AQUA}
            JsonObject titleExample = new JsonObject();
            titleExample.addProperty("name", player.getName());
            titleExample.addProperty("color", "AQUA");

            // {title: [exampleTitle, ...,]}
            JsonArray titleArray = new JsonArray();
            titleArray.add(titleExample);
            json.add("title", titleArray);
            // Initialized title json will be: {name: player.getName(), title: [{name: exampleTitle, color: AQUA}]}
            // write in
            writer.write(json.toString());
        }
    }

    /**
     * Get player's profile setting, which is configured by player.
     * If there isn't exist json type of config file, this will also create new one.
     * @return <code>JsonObject</code>
     */
    public JsonObject getProfile(Player player) throws FileNotFoundException {
        final File profileSettingFile = new File(profileFolder, player.getName() + "@" + player.getUniqueId() + ".json");

        // check profile file
        if (!profileSettingFile.exists()) {
            try {
                createProfile(player);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // read profile file
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(profileSettingFile));
        return gson.fromJson(reader, JsonObject.class);
    }

    /**
     * Create player's title.
     */
    public void createTitle(Player player) throws IOException {
        final File titleFile = new File(titleFolder, player.getName() + "@" + player.getUniqueId() + ".json");
        if (!titleFile.exists()) {
            if (!titleFolder.exists()) {
                titleFolder.mkdir();
            }
            titleFile.createNewFile();

            BufferedWriter writer = new BufferedWriter(new FileWriter(titleFile));
            JsonArray jsonArray = new JsonArray();

            Title title = new Title(
                    "testTitle",
                    org.bukkit.ChatColor.DARK_AQUA,
                    "This is a testing title!\nhi!",
                    new Identity(null)
            );
            jsonArray.add(title.toJson());

            writer.write(jsonArray.toString());
            writer.close();
            return;
        }
        Logger.getLogger("PlayerProfile").info("没删掉！！！！！");
    }

    /**
     * Get player's title setting, which is configured by player.
     * If there isn't exist json type of config file, this will also create new one.
     * @return <code>JsonArray</code>
     */
    public JsonArray getTitle(Player player) throws FileNotFoundException {
        final File titleFile = new File(titleFolder, player.getName() + "@" + player.getUniqueId() + ".json");
        // read title file
        Gson gson = new Gson();
        JsonArray json;
        try (JsonReader reader = new JsonReader(new FileReader(titleFile))) {
            Logger.getLogger("PlayerProfile").info("init reader");
            json = gson.fromJson(reader, JsonArray.class);
            Logger.getLogger("PlayerProfile").info(json.toString());
        } catch (IOException e) {
            Logger.getLogger("PlayerProfile").severe("这怎么关不掉！！！！！");
            throw new FileNotFoundException();
        }
        return json.getAsJsonArray();
    }

    public void resetTitle(Player player) throws IOException {
        final String titleFileName = player.getName() + "@" + player.getUniqueId() + ".json";
        final File titleFile = new File(titleFolder, titleFileName);

        if (titleFile.delete()) {
            Logger.getLogger("PlayerProfile").info("delete done!");
        }
        createTitle(player);
    }

    public void saveTitle(Player player, JsonArray jsonArray) throws IOException {
        final File titleFile = new File(titleFolder, player.getName() + "@" + player.getUniqueId() + ".json");
        JsonWriter jsonWriter = new JsonWriter(new FileWriter(titleFile));
        jsonWriter.jsonValue(jsonArray.toString());
        jsonWriter.close();
    }

    /**
     * Reset player's all title. This method SHOULD BE executed ONLY by operator.
     */
    public void resetProfile(Player player) throws IOException {
        String fileName = player.getName() + "@" + player.getUniqueId() + ".json";
        final File titleSettingFile = new File(profileFolder, fileName);

        // check title file, to avoid occurring error while delete file.
        if (statusFolder.exists() && titleSettingFile.exists()) {
            titleSettingFile.delete();
        }
        // create new title file
        createProfile(player);
    }

    /**
     * Write profile in profile's json file.
     */
    public void saveProfile(Player player, JsonObject profile) throws IOException {
        String fileName = player.getName() + "@" + player.getUniqueId() + ".json";
        File profileFile = new File(profileFolder, fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(profileFile))) {
            writer.write(profile.toString());
        }
    }


    /**
     * Here are the methods extend above ones, which can handle error automatically.
     * But it's need to provide an executor.
     */

/*
    public void createStatus(Player executor, Player player) {
        try {

            createStatus(player);

            executor.spigot().sendMessage(new ComponentBuilder("Setting successfully!").create());
        } catch (IOException e) {
            executor.spigot().sendMessage(new ComponentBuilder("Can't write successfully: " + e).color(ChatColor.RED).create());
        }
    }

    public JsonObject getStatus(Player executor, Player player) {
        JsonObject jsonObject = getStatus(player);
        executor.spigot().sendMessage(new ComponentBuilder("获取状态文件").color(ChatColor.YELLOW).create());

        return jsonObject;
    }

    public void resetStatus(Player executor, Player player) {
        try {

            resetStatus(player);

            // send message of successfully reset
            executor.spigot().sendMessage(new ComponentBuilder("重置成功！").create());
        } catch (IOException e) {
            executor.spigot().sendMessage(new ComponentBuilder("重置失败: " + e).color(ChatColor.RED).create());
            throw new RuntimeException(e);
        }
    }

    public void saveStatus(Player executor, Player player, JsonObject StatusSetting) {
        try {

            saveStatus(player, StatusSetting);

            executor.spigot().sendMessage(new ComponentBuilder("保存成功！").color(ChatColor.YELLOW).create());
        } catch (IOException e) {
            executor.spigot().sendMessage(new ComponentBuilder("读写失败: " + e).color(ChatColor.RED).create());
            throw new RuntimeException(e);
        }
    }

    */
/**
     * Create a json file of module profile for this player.
     * If this player has already had profile json file, this method will not execute.
     *//*

    public void createProfile(Player executor, Player player) {
        try {

            createProfile(player);

            executor.spigot().sendMessage(new ComponentBuilder("Setting successfully!").create());
        } catch (IOException e) {
            executor.spigot().sendMessage(new ComponentBuilder("Can't write successfully: " + e).create());
        }
    }

    */
/**
     * Get player's profile from corresponding json file. Use in command processing.
     *//*

    public JsonObject getProfile(Player executor, Player player) {
        JsonObject jsonObject;
        try {

            jsonObject = getProfile(player);

        } catch (FileNotFoundException e) {
            player.spigot().sendMessage(new ComponentBuilder("个人档案不存在: " + e).color(ChatColor.RED).create());
            throw new RuntimeException(e);
        }
        return jsonObject;
    }

    */
/**
     * Get player's title from profile. Use in command processing.
     *//*

    public JsonArray getTitle(Player executor, Player player) {
        JsonArray jsonArray;
        try {

            jsonArray = getTitle(player);

        } catch (FileNotFoundException e) {
            executor.spigot().sendMessage(new ComponentBuilder("获取称号文件失败：找不到称号文件." + e).color(ChatColor.RED).create());
            throw new RuntimeException(e);
        }
        return jsonArray;
    }

    */
/**
     * Reset player's profile. This method just implement delete and call <code>createProfile</code> method。
     *//*

    public void resetProfile(Player executor, Player player) {
        try {

            resetProfile(player);

        } catch (IOException e) {
            executor.spigot().sendMessage(new ComponentBuilder("重置失败：" + e).color(net.md_5.bungee.api.ChatColor.RED).create());
            throw new RuntimeException(e);
        }
        executor.spigot().sendMessage(new ComponentBuilder("称号重置成功！").color(ChatColor.YELLOW).create());
    }

    */
/**
     * Write JsonObject in profile json file。
     *//*

    public void saveProfile(Player executor, Player player, JsonObject profile) {
        try {

            saveProfile(player, profile);

            executor.spigot().sendMessage(new ComponentBuilder("保存成功！").color(ChatColor.YELLOW).create());
        } catch (IOException e) {
            executor.spigot().sendMessage(new ComponentBuilder("读写失败: " + e).color(ChatColor.RED).create());
            throw new RuntimeException(e);
        }
    }
}
*/
