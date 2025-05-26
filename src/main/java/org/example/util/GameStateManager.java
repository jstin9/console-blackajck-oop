package org.example.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.model.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GameStateManager {
    private static final String SAVE_DIRECTORY = "saves";
    private static final String FILE_EXTENSION = ".json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    static {
        try {
            Path savesDir = Paths.get(SAVE_DIRECTORY);
            if (!Files.exists(savesDir)) {
                Files.createDirectories(savesDir);
            }
        } catch (IOException e) {
            System.err.println(Messages.ERROR_CREATING_SAVE_DIR + e.getMessage());
        }
    }

    public static boolean savePlayer(Player player) {
        if (player == null) {
            return false;
        }
        
        String fileName = player.getName() + FILE_EXTENSION;
        String filePath = SAVE_DIRECTORY + File.separator + fileName;
        
        try (FileWriter writer = new FileWriter(filePath)) {
            PlayerSaveData saveData = new PlayerSaveData(player.getName(), player.getBalance());
            gson.toJson(saveData, writer);
            return true;
        } catch (IOException e) {
            System.err.println(Messages.ERROR_SAVING_PLAYER + e.getMessage());
            return false;
        }
    }

    public static Player loadPlayer(String playerName) {
        String fileName = playerName + FILE_EXTENSION;
        String filePath = SAVE_DIRECTORY + File.separator + fileName;
        
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        
        try (FileReader reader = new FileReader(file)) {
            PlayerSaveData saveData = gson.fromJson(reader, PlayerSaveData.class);
            return new Player(saveData.name(), saveData.balance());
        } catch (IOException e) {
            System.err.println(Messages.ERROR_LOADING_PLAYER + e.getMessage());
            return null;
        }
    }

    public static List<String> getSavedPlayerNames() {
        List<String> playerNames = new ArrayList<>();
        File saveDir = new File(SAVE_DIRECTORY);
        
        if (saveDir.exists() && saveDir.isDirectory()) {
            File[] files = saveDir.listFiles((dir, name) -> name.endsWith(FILE_EXTENSION));
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    playerNames.add(fileName.substring(0, fileName.length() - FILE_EXTENSION.length()));
                }
            }
        }
        
        return playerNames;
    }

    private record PlayerSaveData(String name, int balance) {
    }
}
