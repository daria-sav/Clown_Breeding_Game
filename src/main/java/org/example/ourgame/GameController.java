package org.example.ourgame;

import java.io.IOException;
import java.util.HashMap;

public class GameController {
    private int moneyInWallet;
    private HashMap<Integer, WorldLevel> worlds;
    private WorldLevel currentWorld;
    private int maxOpenedClown; // Общий максимальный уровень открытого клоуна для всех миров

    public GameController(int initialMoney, HashMap<Integer, WorldLevel> worlds, int initialMaxClown) {
        this.moneyInWallet = initialMoney;
        this.worlds = worlds;
        this.currentWorld = worlds.get(1);  // Начинаем с первого мира
        this.maxOpenedClown = initialMaxClown;  // Начальное значение для maxOpenedClown
    }

    public boolean buyClown(int clownLevel) throws IOException {
        HashMap<Integer, String[]> levelInfoMap = World.readFileClowns("clownsInfo.txt"); // Чтение информации о клоунах
        int cost = (int) (Math.pow(clownLevel, 3) + 5);

        if (moneyInWallet >= cost) {
            moneyInWallet -= cost;
            // Добавление клоуна с обновлением максимального уровня открытого клоуна
            maxOpenedClown = World.addClown(clownLevel, currentWorld.getClownIndex(), levelInfoMap, maxOpenedClown);
            return true;
        } else {
            System.out.println("Недостаточно средств или уровень клоуна слишком высок.");
            return false;
        }
    }

    public int getMoney() {
        return moneyInWallet;
    }

    public void setCurrentWorld(int worldLevel) {
        if (worlds.containsKey(worldLevel)) {
            this.currentWorld = worlds.get(worldLevel);
        }
    }

    public WorldLevel getCurrentWorld() {
        return currentWorld;
    }

    public int getMaxOpenedClown() {
        return maxOpenedClown;
    }
}
