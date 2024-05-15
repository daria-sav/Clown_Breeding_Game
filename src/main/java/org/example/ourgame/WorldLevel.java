package org.example.ourgame;

import org.example.ourgame.Bonuses;
import org.example.ourgame.BoxesClass;

import java.util.*;
import java.util.Timer;
// antud klass on mõeldut tasemete moodustamiseks, ning nende töötlemiseks
public class WorldLevel {
    //iga maailma jaoks on omad karbid, boonused ning klounid (isiklik HashMap)

    private int level; // maailma tase
    private int minClownLevel; // minimaalne kluni tase, mis kuulub selle maailmale
    private int maxClownLevel; // maksimaalne klouni tase, mis kuulun selle maailmale
    private HashMap<Integer, BoxesClass> boxes = new HashMap<Integer, BoxesClass>(); // maailma karbid
    private HashMap<Integer, ClownsClass> clownIndex; // maailma klounid
    private ArrayList<Bonuses> bonuses = new ArrayList<Bonuses>(); // maailma boonused
    private Timer timer;
    // muutuja, mida kasutame karpi loomisel, et igal karpil oli oma unikaalne indeks
    private int boxCounter = 0;
    private String backgroundImage;
    private HashMap<Integer, ClownsClass> clowns;

    // konstruktor
    public WorldLevel(int level, int minClownLevel, String backgroundImage) {
        this.level = level;
        this.minClownLevel = minClownLevel;
        this.maxClownLevel = minClownLevel + 5;
        this.clownIndex = new HashMap<>();
        this.timer = new Timer();
        this.backgroundImage = backgroundImage;
        this.clowns = new HashMap<>();
    }

    // getterid
    public int getLevel() {
        return level;
    }

    public HashMap<Integer, BoxesClass> getBoxes() {
        return boxes;
    }

    public HashMap<Integer, ClownsClass> getClownIndex() {
        return clownIndex;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public HashMap<Integer, ClownsClass> getClowns() {
        return clowns;
    }

    /**
     * antud meetod simuleerib karpi avamist, genereerides klouni taseme vastavalt kasutaja tasemele ja juhuslikule tegurile
     * @param maxOpenedClown - maksimaalne tase, millisega oli juba avatud kloun
     * @param boxIndex - karpi indeks, mida soovime avada
     * @return - uue klouni tase
     */
    public int clownsLevelInTheBox(int maxOpenedClown, int boxIndex) {
        // kustutame karpi
        boxes.remove(boxIndex);

        // arvutame, millise tasemega võiks luua uue klouni
        if (maxOpenedClown <= minClownLevel + 2) {
            return minClownLevel;
        }
        if (maxOpenedClown <= minClownLevel + 4) {
            Random random = new Random();
            int generatedColwnsLevel = random.nextInt(2) + minClownLevel;
            return generatedColwnsLevel;
        }
        if (maxOpenedClown == maxClownLevel) {
            Random random = new Random();
            int generatedColwnsLevel = random.nextInt(3) + minClownLevel;
            return generatedColwnsLevel;
        } else { return 0;}

    }

    /**
     * antud meetod kasutab meetodi generateBox, et iga 15 sekundit genereerida uue karpi
     */
    public void startBoxesGenerator() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                generateBox();
            }
        }, 0, 15000 + (level - 1) * 5000 ); //iga 15 sekundit + 5 iga maailma taseme suurendamisel
    } // kutsume seda org.example.ourgame.World (meie main klassis) iga avatud maailmade jaoks

    /**
     * antud meetod loob uue karpi oma unikaalse indeksiga ning lisab seda HashMap'i
     */
    public void generateBox() {
        if (boxes.size() < 6) {
            BoxesClass box = new BoxesClass();
            boxes.put(boxCounter++, box);
            System.out.println("Kukkus uus karp!");
            System.out.println("Praegu sul on " + boxes.size() + " karp(i)!");
        }
    }

    //bonuses + pärast muuta seda meetodi, et boonus ei kukkuks mähgu alguses

    /**
     * antud meetod töötab nagu startBoxesGenerator, ehk kasutades meetodi addBonusToList, genereerib iga 3-4 minutid üht boonust
     */
    public void bonusesGenerator() {
        Random random = new Random();
        int time = random.nextInt(2);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                addBonusToList();
            }
        }, 0, 60000 * 3 + time * 30000); // boonus kukkub iga 3-4 minutid
    }

    /**
     * genereerib boonust ning lisab seda järjendisse
     */
    private void addBonusToList() {
        if (bonuses.size() < 4) {
            Bonuses bonus = new Bonuses();
            bonuses.add(bonus);
            System.out.println("Keegi valas popkorni maha( Tõsta!");
        }
    }

    /**
     * antud meetod korjab kõik boonused, mis on selles maailmas
     * @return - tagastab pisarate arvu, mida kasutaja korjas boonustest
     */
    public int collectBonuses() {
        int sum = 0;
        for (Bonuses element : bonuses) {
            sum += element.tearsForBonuses(level);
        }
        System.out.println("Sa korjasid " + bonuses.size() + " popkorni!");
        bonuses.clear();
        System.out.println("Popkorni maha tõstmisest sa teenisid " + sum + " pisaraid)");
        return sum;
    }
    //main klassis (org.example.ourgame.World) lisame seda meie rahakotti
}