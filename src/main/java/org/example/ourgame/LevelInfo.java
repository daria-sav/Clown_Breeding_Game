package org.example.ourgame;

import java.util.HashMap;
// antud klass on koostatud, et säilitada infot iga klouni taseme kohta, et lihtsamalt seda kasutada
public class LevelInfo {
    private String name;
    private int cost;

    // konstruktor
    public LevelInfo(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }

    // getterid
    public String getName() {
        return name;
    }

    public int getCost() {
        return (int) Math.pow(cost, 3) + 5;
    }
    // Märkus iseendale, järgmiseks etapiks : !! если будем менять формулу стоимости, то не забыть поменять sout в покупке первого
}