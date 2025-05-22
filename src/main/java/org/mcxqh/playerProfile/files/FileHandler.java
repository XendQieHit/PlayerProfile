package org.mcxqh.playerProfile.files;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.players.Profile;
import org.mcxqh.playerProfile.players.profile.identity.AuthLevel;
import org.mcxqh.playerProfile.players.profile.identity.Identity;
import org.mcxqh.playerProfile.players.profile.title.Title;
import org.mcxqh.playerProfile.players.profile.status.Status;

import java.io.*;
import java.util.logging.Logger;

public class FileHandler {

    private final File rootFolder = new File("plugins/PlayerProfile");
    private final File profileFolder = new File(rootFolder, "Profile");
    private final File statusFolder = new File(rootFolder, "Status");
    private final File titleFolder = new File(rootFolder, "Title");

    public FileHandler() {
    }

    public void sendErrorMsg(CommandSender sender, String errText, Exception e) {
        Logger.getLogger("PlayerProfile").severe(errText + e);
        sender.spigot().sendMessage(new ComponentBuilder(errText + e).color(net.md_5.bungee.api.ChatColor.RED).create());
        throw new RuntimeException(e);
    }

    /**
     * Get player's status setting, which is configured by this player.
     * If there isn't exist json type of config file, this will also create new one.
     */
    public JsonArray getStatus(Player player) {
        JsonArray jsonArray = null;
        String errText = "获取状态配置文件失败：";
        for (int i = 0; i < 2; i++) {
            FileHandler fileHandler = new FileHandler();
            try {
                jsonArray = fileHandler.getStatusRaw(player);
                break;
            } catch (NullPointerException e) {
                try {
                    fileHandler.resetStatus(player);
                } catch (IOException ex) {
                    sendErrorMsg(player, errText, ex);
                }
            } catch (FileNotFoundException e) {
                try {
                    fileHandler.createStatus(player);
                } catch (IOException ex) {
                    sendErrorMsg(player, errText, ex);
                }
            }
        }
        if (jsonArray == null || jsonArray.isEmpty()) {
            sendErrorMsg(player, errText, new NullPointerException());
        }
        return jsonArray;
    }

    public void createStatus(Player player) throws IOException {
        Logger.getLogger("PlayerProfile").info("444");
        createStatus(Data.PROFILE_MAP_WITH_UUID.get(player.getUniqueId()));
    }

    /**
     * Create a preprocessing status file. This method usually applied in command executor.
     */
    public void createStatus(Profile profile) throws IOException {
        if (profile == null) Logger.getLogger("PlayerProfile").info("profile is null.");
        String fileName = profile.getRawName() + "@" + profile.getUniqueId() + ".json";
        File statusFile = new File(statusFolder, fileName);

        // check status file
        if (!statusFile.exists()) {
            if (!statusFolder.exists()) {
                statusFolder.mkdir();
            }
            statusFile.createNewFile();
            Logger.getLogger("PLayerProfile").info("12346");

            // Initialize status file
            JsonWriter writer = new JsonWriter(new FileWriter(statusFile));

            Gson gson = new Gson();
            writer.beginArray();

            for (Status status : profile.getStatusManager().getStatuses()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.add(status.getClass().getSimpleName(), gson.toJsonTree(status));
                writer.jsonValue(gson.toJson(jsonObject));
            }
            writer.endArray();
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
        Logger.getLogger("PlayerProfile").info("222");
        if (statusFolder.exists() && statusSettingFile.exists()) {
            Logger.getLogger("PlayerProfile").info("111");
            statusSettingFile.delete();
        }

        // generate new title file
        createStatus(player);
    }

    /**
     * Write status setting in status's json file.
     */
    public void saveStatus(Player player, JsonArray StatusSetting) throws IOException {
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
     *
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
     * Get player's title setting, which is configured by player.
     * If there isn't exist json type of config file, this will also create new one.
     *
     * @return <code>JsonArray</code>
     */
    public JsonArray getTitle(Player player) {
        JsonArray jsonArray = null;
        String errText = "获取称号配置文件失败：";
        for (int attempt = 0; attempt < 2; attempt++) {
            try {
                jsonArray = getTitleRaw(player);
                break;
            } catch (FileNotFoundException e) {
                try {
                    createTitle(player);
                } catch (IOException ex) {
                    sendErrorMsg(player, errText, ex);
                }
            } catch (NullPointerException e) {
                try {
                    resetTitle(player);
                } catch (IOException ex) {
                    sendErrorMsg(player, errText, ex);
                }
            }
        }
        if (jsonArray == null || jsonArray.isEmpty()) {
            sendErrorMsg(player, errText, new NullPointerException());
        }
        return jsonArray;
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
                    "This is a testing title!\nhi!",
                    new Identity(AuthLevel.PERSONAL, player.getUniqueId(), player.getName(), null, null)
            );
            jsonArray.add(title.toJson());

            writer.write(jsonArray.toString());
            writer.close();
            return;
        }
        Logger.getLogger("PlayerProfile").info("没删掉！！！！！");
    }


    public JsonArray getTitleRaw(Player player) throws FileNotFoundException {
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
}
