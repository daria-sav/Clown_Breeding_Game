package org.example.ourgame;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    }

    public void setWorlds(HashMap<Integer, WorldLevel> worlds) {
        this.worlds = worlds;
    }

    public void setCurrentWorld(int worldLevel) {
        this.currentWorld = this.worlds.get(worldLevel);
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
        return new ArrayList<>(worlds.get(currentWorldId).getClowns().values());
    }

    public List<ClownsClass> getAvailableClowns() {
        return new ArrayList<>(worlds.get(currentWorldId).getClowns().values());
    }

    public void buyClown(int clownLevel) {
        double cost = Math.pow(clownLevel, 3) + 5;
        if (cost <= moneyInWallet) {
            moneyInWallet -= cost;
            int newMaxLevel = World.addClown(clownLevel, currentWorld.getClownIndex(), clownInfoMap, maxOpenedClown);
            maxOpenedClown = Math.max(maxOpenedClown, newMaxLevel);
            // Требуется обновить GUI
            gameGUI.updateClownDisplay(); // Обновление отображения клоунов
            gameGUI.updateMoneyDisplay(); // Обновление отображения денег
        }
    }

    public void slapClown(ClownsClass clown) {
        double moneyEarned = clown.slapTheClown();
        moneyInWallet += moneyEarned;
        System.out.println("You earned " + moneyEarned + " tears. Total: " + moneyInWallet);
        gameGUI.updateMoneyDisplay();
        gameGUI.showAlert("Clown Slapped", "You earned " + moneyEarned + " tears");
    }

    public void switchWorld(int worldLevel) {
        setCurrentWorld(worldLevel);
        openedWorldsList[worldLevel - 1] = true;  // Отмечаем мир как открытый
        gameGUI.updateClownDisplay();
        gameGUI.updateWorldsDisplay(); // Обновляем отображение списка миров
    }


    public void breedClowns(int clown1Id, int clown2Id) {
        World.breeding(clown1Id, clown2Id, currentWorld.getClowns(), clownInfoMap, maxOpenedClown, worlds, currentWorld.getLevel(), openedWorldsList, gameGUI);
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

}
