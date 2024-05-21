package org.example.ourgame;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

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
    private GameGUI gameGUI; // Viide GUI-le tagasihelistuste jaoks
    private boolean[] openedWorldsList;
    private static final Logger LOGGER = Logger.getLogger(GameController.class.getName());
    private static int clownCounter = 0;

    public int getCurrentWorldId() {
        return currentWorldId;
    }

    /**
     * Kontrollib, kas maailm on avatud
     * @param worldLevel - maailma tase
     * @return boolean - kas maailm on avatud
     */
    public boolean isWorldOpen(int worldLevel) {
        return worldLevel <= maxOpenedClown / 6 + 1; // Teie loogika maailma avamise määramiseks
    }

    /**
     * Vahetab maailma
     * @param worldLevel - maailma tase
     */
    public void switchWorld(int worldLevel) {
        if (isWorldOpen(worldLevel)) {
            currentWorldId = worldLevel;
            setCurrentWorld(worldLevel);
            gameGUI.updateClownDisplay();
            gameGUI.updateWorldsDisplay();
            gameGUI.updateBackground(worldLevel); // Обновляем фон при переключении мира
        } else {
            gameGUI.showAlert("Maailm on suletud", "Sa ei saa veel siseneda sellesse maailma.");
        }
    }

    /**
     * Kuvab teate
     * @param header - teate pealkiri
     * @param content - teate sisu
     */
    public void showAlert(String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Mängu teavitus");
            alert.setHeaderText(header);
            alert.setContentText(content);

            // Rakendame CSS-stiili
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            dialogPane.getStyleClass().add("alert");

            alert.showAndWait();
        });
    }

    /**
     * Konstruktor, mis seadistab algväärtused
     * @param initialMoney - algne raha
     * @param initialMaxClown - algne maksimaalne avatud klouni tase
     * @param gameGUI - viide GUI-le
     */
    public GameController(int initialMoney, int initialMaxClown, GameGUI gameGUI) {
        this.moneyInWallet = initialMoney;
        this.maxOpenedClown = initialMaxClown;
        this.gameGUI = gameGUI;
        this.worlds = new HashMap<>();
        this.openedWorldsList = new boolean[6]; // Eeldame, et meil on 6 maailma
        Arrays.fill(this.openedWorldsList, false);
        this.openedWorldsList[0] = true; // Esimene maailm on vaikimisi avatud
        this.clownInfoMap = readFileClowns("clownsInfo.txt"); // Laadime klounide andmed initsialiseerimisel
        initializeWorlds();
        currentWorldId = 1; // Alustame esimesest maailmast
    }

    /**
     * Algväärtustab maailmad
     */
    private void initializeWorlds() {
        worlds.put(1, new WorldLevel(1, 1, "/world1/background_1.png"));
        worlds.put(2, new WorldLevel(2, 7, "/world2/background_2.png"));
        worlds.put(3, new WorldLevel(3, 13, "/world3/background_3.png"));

        // Algse maailma seadistamine pärast initsialiseerimist
        setCurrentWorld(1);
    }

    /**
     * Antud meetod-----
     * @param worldLevel
     * @return
     */
    public String getBackgroundPathForWorld(int worldLevel) {
        WorldLevel world = worlds.get(worldLevel);
        return world != null ? world.getBackgroundImage() : "/default/background.png";
    }

    /**
     * Seadistab maailmad
     * @param worlds - maailmade kaart
     */
    public void setWorlds(HashMap<Integer, WorldLevel> worlds) {
        this.worlds = worlds;
    }

    /**
     * Seadistab praeguse maailma
     * @param worldLevel - maailma tase
     */
    public void setCurrentWorld(int worldLevel) {
        this.currentWorld = this.worlds.get(worldLevel);
        System.out.println("Set current world to " + worldLevel + " with " + (currentWorld != null ? currentWorld.getClowns().size() : "null") + " clowns");
    }

    /**
     * Tagastab praeguse maailma
     * @return WorldLevel - praegune maailm
     */
    public WorldLevel getCurrentWorld() {
        return currentWorld;
    }

    /**
     * Tagastab maailmade kaardi
     * @return HashMap<Integer, WorldLevel> - maailmade kaart
     */
    public HashMap<Integer, WorldLevel> getWorlds() {
        return worlds;
    }

    /**
     * Tagastab maksimaalselt avatud klouni taseme
     * @return int - maksimaalselt avatud klouni tase
     */
    public int getMaxOpenedClown() {
        return maxOpenedClown;
    }

    /**
     * Tagastab rahasumma rahakotis
     * @return double - rahasumma
     */
    public double getMoney() {
        return moneyInWallet;
    }

    /**
     * Tagastab avatud maailmade nimekirja
     * @return boolean[] - avatud maailmade nimekiri
     */
    public boolean[] getOpenedWorldsList() {
        return openedWorldsList;
    }

    /**
     * Tagastab praegused klounid
     * @return List<ClownsClass> - praeguste klounide nimekiri
     */
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

    /**
     * Tagastab saadaval olevad klounid
     * @return List<ClownsClass> - saadaval olevate klounide nimekiri
     */
    public List<ClownsClass> getAvailableClowns() {
        List<ClownsClass> availableClowns = new ArrayList<>();

        if (currentWorld != null) {
            int worldMinLevel = currentWorld.getMinClownLevel();
            int worldMaxLevel = currentWorld.getMaxClownLevel();
            int minLevel = 0;
            int maxLevel = 0;
            if (maxOpenedClown <= worldMinLevel + 2) {
                if (currentWorldId != 1) {
                    return availableClowns;
                }
                maxLevel = worldMinLevel;
            } else {
                maxLevel = maxOpenedClown - 2;
                minLevel = worldMinLevel;
            }

            // Iga taseme jaoks, mis on avatud ja mitte kõrgem kui maksimaalselt avatud tase
            for (int level = minLevel; level <= maxLevel && level <= worldMaxLevel; level++) {
                if (clownInfoMap.containsKey(level)) {
                    String[] clownData = clownInfoMap.get(level);
                    availableClowns.add(new ClownsClass(clownData[0], level, clownData[1]));
                }
            }
        }
        return availableClowns;
    }

    /**
     * Tagastab kõik avatud klounid
     * @return List<ClownsClass> - kõik avatud klounid
     */
    public List<ClownsClass> getAllOpenedClowns() {
        List<ClownsClass> allOpenedClowns = new ArrayList<>();
        for (int level = 1; level <= maxOpenedClown; level++) {
            if (clownInfoMap.containsKey(level)) {
                String[] clownData = clownInfoMap.get(level);
                allOpenedClowns.add(new ClownsClass(clownData[0], level, clownData[1]));
            }
        }
        return allOpenedClowns;
    }

    /**
     * Ostab klouni
     * @param clownLevel - klouni tase
     */
    public void buyClown(int clownLevel) {
        double cost = Math.pow(clownLevel, 3) + 5;
        if (cost <= moneyInWallet && currentWorld != null) {
            moneyInWallet -= cost;
            maxOpenedClown = addClown(clownLevel, currentWorld.getClowns(), clownInfoMap, maxOpenedClown);
            setCurrentWorld(currentWorldId);  // Praeguse maailma uuesti seadistamine
            gameGUI.updateClownDisplay();  // Uuendame GUI klounide kuvamiseks
            gameGUI.updateMoneyDisplay();  // Uuendame raha kuvamist
        } else {
            LOGGER.warning("Failed to buy clown: Insufficient funds or no current world.");
        }
    }

    /**
     * Lisab klouni
     * @param level - klouni tase
     * @param clownIndex - klounide kaart
     * @param levelInfoMap - tasemete info kaart
     * @param maxOpenedClown - maksimaalselt avatud klouni tase
     * @return int - uuendatud maksimaalselt avatud klouni tase
     */
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


    /**
     * Lööb klouni
     * @param clown - klouni objekt
     */
    public void slapClown(ClownsClass clown) {
        double moneyEarned = clown.slapTheClown();
        moneyInWallet += moneyEarned;
        System.out.println("You earned " + moneyEarned + " tears. Total: " + moneyInWallet);
        gameGUI.updateMoneyDisplay();
        gameGUI.showAlert("Clown Slapped", "You earned " + moneyEarned + " tears");
    }

    /**
     * Klounide aretamise meetod
     * @param clown1Id - esimese klouni ID
     * @param clown2Id - teise klouni ID
     */
    public void breeding(int clown1Id, int clown2Id) {
        ClownsClass clown1 = currentWorld.getClowns().get(clown1Id);
        ClownsClass clown2 = currentWorld.getClowns().get(clown2Id);

        if (clown1 == null || clown2 == null) {
            System.out.println("One of the clowns was not found.");
            return; // Kui üks klounidest pole leitud
        }

        if (clown1.getClownLevel() == clown2.getClownLevel()) {
            int newLevel = clown1.getClownLevel() + 1;

            currentWorld.getClowns().remove(clown1Id);
            currentWorld.getClowns().remove(clown2Id);
            System.out.println("Clowns removed from current world, breeding new clown at level: " + newLevel);

            if ((newLevel - 1) % 6 != 0) {
                maxOpenedClown = addClown(newLevel, currentWorld.getClowns(), clownInfoMap, maxOpenedClown);
            } else {
                // Klouni viimine uude maailma
                int nextWorldId = currentWorldId + 1;
                if (currentWorldId < worlds.size()) {
                    HashMap<Integer, ClownsClass> nextWorldClowns = worlds.get(nextWorldId).getClowns();
                    maxOpenedClown = addClown(newLevel, nextWorldClowns, clownInfoMap, maxOpenedClown);
                    openedWorldsList[nextWorldId - 1] = true;
                }
            }

            gameGUI.updateClownDisplay();
            gameGUI.updateWorldsDisplay();
        } else {
            System.out.println("Clowns have different levels, cannot breed.");
        }
    }

    /**
     * Tagastab klouni ID järgi
     * @param id - klouni ID
     * @return ClownsClass - klouni objekt
     */
    public ClownsClass getClownById(int id) {
        if (currentWorld != null) {
            return currentWorld.getClowns().get(id);
        }
        return null; // Tagastame null, kui maailma ei leitud või maailmas pole sellise ID-ga klouni
    }

    /**
     * Salvestab mängu
     */
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

    /**
     * Laadib mängu
     */
    public void loadGame() {
        File file = new File("game_data.bin");
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
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
        } else {
            LOGGER.info("Save file not found. Starting new game.");
        }
    }

    /**
     * Loeb klounide andmed failist
     * @param failiName - faili nimi
     * @return HashMap<Integer, String[]> - klounide andmete kaart
     */
    public static HashMap<Integer, String[]> readFileClowns (String failiName) {
        //clowns from file
        File clownsFile = new File("textFiles", failiName);
        HashMap<Integer, String[]> clowns = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(clownsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("//")) {
                    String[] parts = line.split(";");
                    if (parts.length == 3) {
                        String[] values = {parts[0].trim(), parts[2].trim()};
                        try {
                            int key = Integer.parseInt(parts[1].trim());
                            clowns.put(key, values);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number format: " + parts[1].trim());
                        }
                    } else {
                        System.out.println("Invalid line format: " + line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clowns;
    }
}
