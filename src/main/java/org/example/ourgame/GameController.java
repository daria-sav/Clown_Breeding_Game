package org.example.ourgame;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameController {
    private double moneyInWallet;
    private HashMap<Integer, WorldLevel> worlds;
    private WorldLevel currentWorld;
    private int maxOpenedClown;
    private HashMap<Integer, String[]> clownInfoMap;
    private int currentWorldId;
    private GameGUI gameGUI; // Ссылка на GUI для обратных вызовов
    private boolean[] openedWorldsList;
    private static final Logger LOGGER = Logger.getLogger(GameController.class.getName());
    private static int clownCounter = 0;
    public int getCurrentWorldId() {
        return currentWorldId;
    }

    public boolean isWorldOpen(int worldLevel) {
        return worldLevel <= maxOpenedClown / 6 + 1; // Your logic for determining if a world is open
    }

    public void switchWorld(int worldLevel) {
        if (isWorldOpen(worldLevel)) {
            currentWorldId = worldLevel;
            setCurrentWorld(worldLevel);
            gameGUI.updateClownDisplay();
            gameGUI.updateWorldsDisplay();
        } else {
            showAlert("Maailm on suletud", "Sa ei saa veel siseneda sellesse maailma.");
        }
    }

    public void showAlert(String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Mängu teavitus");
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }


    public GameController(int initialMoney, int initialMaxClown, GameGUI gameGUI) {
        this.moneyInWallet = initialMoney;
        this.maxOpenedClown = initialMaxClown;
        this.gameGUI = gameGUI;
        this.worlds = new HashMap<>();
        this.openedWorldsList = new boolean[6]; // Предполагаем, что у нас 6 миров
        Arrays.fill(this.openedWorldsList, false);
        this.openedWorldsList[0] = true; // Первый мир открыт по умолчанию
        this.clownInfoMap = World.readFileClowns("clownsInfo.txt"); // Загрузка данных о клоунах при инициализации
        initializeWorlds();
        currentWorldId = 1; // Начинаем с первого мира
    }

    private void initializeWorlds() {
        worlds.put(1, new WorldLevel(1, 1, "background1.jpg"));
        worlds.put(2, new WorldLevel(2, 7, "wallpaper.jpg"));
        worlds.put(3, new WorldLevel(3, 13, "wallpaper.jpg"));

        // Установка начального мира после инициализации
        setCurrentWorld(1);
    }

    public void setWorlds(HashMap<Integer, WorldLevel> worlds) {
        this.worlds = worlds;
    }

    public void setCurrentWorld(int worldLevel) {
        this.currentWorld = this.worlds.get(worldLevel);
        System.out.println("Set current world to " + worldLevel + " with " + (currentWorld != null ? currentWorld.getClowns().size() : "null") + " clowns");
    }

    public WorldLevel getCurrentWorld() {
        return currentWorld;
    }

    public HashMap<Integer, WorldLevel> getWorlds() {
        return worlds;
    }

    public int getMaxOpenedClown() {
        return maxOpenedClown;
    }

    public double getMoney() {
        return moneyInWallet;
    }

    public boolean[] getOpenedWorldsList() {
        return openedWorldsList;
    }

    public List<ClownsClass> getCurrentClowns() {
        if (currentWorld != null && worlds.containsKey(currentWorldId)) {
            List<ClownsClass> clowns = new ArrayList<>(worlds.get(currentWorldId).getClowns().values());
            System.out.println("Current world has " + clowns.size() + " clowns");
            return clowns;
        } else {
            System.out.println("No current world or world is not initialized");
            return new ArrayList<>();
        }
    }

    public List<ClownsClass> getAvailableClowns() {
        List<ClownsClass> availableClowns = new ArrayList<>();
        int maxLevelAvailable = Math.max(1, maxOpenedClown - 3);  // Минимум 1 уровень

        // Фильтрация доступных клоунов по максимально доступному уровню и текущему миру
        for (int level = 1; level <= maxLevelAvailable; level++) {
            if (clownInfoMap.containsKey(level)) {
                String[] clownData = clownInfoMap.get(level);
                availableClowns.add(new ClownsClass(clownData[0], level, clownData[1]));
            }
        }

        return availableClowns;
    }

    public void buyClown(int clownLevel) {
        double cost = Math.pow(clownLevel, 3) + 5;
        if (cost <= moneyInWallet && currentWorld != null) {
            moneyInWallet -= cost;
            maxOpenedClown = addClown(clownLevel, currentWorld.getClowns(), clownInfoMap, maxOpenedClown);
            setCurrentWorld(currentWorldId);  // Переустанавливаем currentWorld
            gameGUI.updateClownDisplay();  // Обновляем GUI для отображения нового клоуна
            gameGUI.updateMoneyDisplay();  // Обновляем отображение денег
        } else {
            LOGGER.warning("Failed to buy clown: Insufficient funds or no current world.");
        }
    }

    public static int addClown(int level, HashMap<Integer, ClownsClass> clownIndex, HashMap<Integer, String[]> levelInfoMap, int maxOpenedClown) {
        String[] clownData = levelInfoMap.get(level);
        if (clownData != null) {
            ClownsClass clown = new ClownsClass(clownData[0], level, clownData[1]);
            clownIndex.put(clown.getId(), clown);
            System.out.println("Adding clown: " + clown.getName() + ", Level: " + level);
            return Math.max(level, maxOpenedClown);
        } else {
            System.out.println("No clown data available for level " + level);
            return maxOpenedClown;
        }
    }

    public static void deleteClowns (int clownIndeks, HashMap<Integer, ClownsClass> clownsClassHashMap) {
        if (clownsClassHashMap.containsKey(clownIndeks)) {
            clownsClassHashMap.remove(clownIndeks);
            //System.out.println("Kloun indeksiga " + clownIndeks + " on eemaldatud!");
        } else {
            System.out.println("Kloun indeksiga " + clownIndeks + " ei eksisteeri((((");
        }
    }

    public void slapClown(ClownsClass clown) {
        double moneyEarned = clown.slapTheClown();
        moneyInWallet += moneyEarned;
        System.out.println("You earned " + moneyEarned + " tears. Total: " + moneyInWallet);
        gameGUI.updateMoneyDisplay();
        gameGUI.showAlert("Clown Slapped", "You earned " + moneyEarned + " tears");
    }

    /*public void switchWorld(int worldLevel) {
        setCurrentWorld(worldLevel);
        openedWorldsList[worldLevel - 1] = true;  // Отмечаем мир как открытый
        System.out.println("Switched to world " + worldLevel);
        gameGUI.updateClownDisplay();
        gameGUI.updateWorldsDisplay(); // Обновляем отображение списка миров
    }*/


    //klouni aretuse meetod
    public void breeding(int clown1Id, int clown2Id) {
        ClownsClass clown1 = currentWorld.getClowns().get(clown1Id);
        ClownsClass clown2 = currentWorld.getClowns().get(clown2Id);

        if (clown1 == null || clown2 == null) {
            System.out.println("One of the clowns was not found.");
            return; // Если один из клоунов не найден
        }

        if (clown1.getClownLevel() == clown2.getClownLevel()) {
            int newLevel = clown1.getClownLevel() + 1;

            currentWorld.getClowns().remove(clown1Id);
            currentWorld.getClowns().remove(clown2Id);
            System.out.println("Clowns removed from current world, breeding new clown at level: " + newLevel);

            if ((newLevel - 1) % 6 != 0) {
                addClown(newLevel, currentWorld.getClowns(), clownInfoMap, maxOpenedClown);
            } else {
                // Обработка перехода клоуна в новый мир
                int nextWorldId = currentWorldId + 1;
                if (currentWorldId < worlds.size()) {
                    HashMap<Integer, ClownsClass> nextWorldClowns = worlds.get(nextWorldId).getClowns();
                    addClown(newLevel, nextWorldClowns, clownInfoMap, maxOpenedClown);
                    openedWorldsList[nextWorldId - 1] = true;
                }
            }

            // Вызовите метод обновления интерфейса для удаления старых изображений
            gameGUI.updateClownDisplay();
        } else {
            System.out.println("Clowns have different levels, cannot breed.");
        }
    }




    public ClownsClass getClownById(int id) {
        if (currentWorld != null) {
            return currentWorld.getClowns().get(id);
        }
        return null; // Возвращаем null, если мир не найден или в мире нет клоуна с таким ID
    }

    public void saveGame() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("game_data.bin"))) {
            out.writeObject(worlds);
            out.writeDouble(moneyInWallet);
            out.writeInt(maxOpenedClown);
            out.writeInt(currentWorldId);
            out.writeObject(openedWorldsList);
            LOGGER.info("Game saved successfully.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving game: ", e);
        }
    }

    public void loadGame() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("game_data.bin"))) {
            worlds = (HashMap<Integer, WorldLevel>) in.readObject();
            moneyInWallet = in.readDouble();
            maxOpenedClown = in.readInt();
            currentWorldId = in.readInt();
            openedWorldsList = (boolean[]) in.readObject();
            setCurrentWorld(currentWorldId);
            gameGUI.updateClownDisplay();
            gameGUI.updateWorldsDisplay();
            LOGGER.info("Game loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error loading game: ", e);
        }
    }

    public HashMap<Integer, String[]> readFileClowns(String fileName) {
        HashMap<Integer, String[]> clowns = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("textFiles" + File.separator + fileName))) {
            String line;
            int currentWorld = 1; // Start with world 1
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("//word")) {
                    currentWorld = Integer.parseInt(line.substring(6).trim()); // Update current world
                    continue; // Skip to the next line
                }
                if (!line.isEmpty() && !line.startsWith("//")) {
                    String[] parts = line.split(";");
                    if (parts.length == 3) {
                        String[] values = {parts[0].trim(), "world" + currentWorld + File.separator + parts[2].trim()};
                        try {
                            int key = Integer.parseInt(parts[1].trim());
                            clowns.put(key, values);
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid number format: " + parts[1].trim());
                        }
                    } else {
                        System.err.println("Invalid line format: " + line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return clowns;
    }

}
