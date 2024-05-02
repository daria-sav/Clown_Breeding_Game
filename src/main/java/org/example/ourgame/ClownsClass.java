package org.example.ourgame;

import java.util.Random;

// antud klass on klounide objektide jaoks ning nende töötlemisega
public class ClownsClass implements Comparable<ClownsClass> {
    private String name;
    private int clownLevel;

    // konstruktor
    public ClownsClass(String name, int clownLevel) {
        this.name = name;
        this.clownLevel = clownLevel;
    }

    //getterid ja setterid
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return clownLevel;
    }

    public void setLevel(int level) {
        this.clownLevel = level;
    }

    /**
     * antud meetod simuleerib klouni peksmist ja arvutab välja teenitud pisarate arvu.
     * @return - teenitud raha
     */
    public double slapTheClown () {
        // juhusliku arvu loomine
        Random random = new Random();
        int tearsCost = (random.nextInt(8) + 2) * clownLevel;

        // genereerime mõned fraasid kasutaja jaoks
        int phraseNumber = random.nextInt(3);
        switch (phraseNumber) {
            case 0:
                System.out.println("Yippee! Kloun " + name + " nuttab!");
                System.out.println("Teda pekstes teenisid " + tearsCost + " pisaraid uue klouni ostmiseks)))");
                break;
            case 1:
                System.out.println( name + " sai laksu)");
                System.out.println("Klouni peeksmisega pressisid sa temalt välja " + tearsCost + " pisarat!");
                break;
            case 2:
                System.out.println("Yippee! Kloun " + name + " nuttab!");
                System.out.println("+ " + tearsCost + " pisarat!");
                break;
        }
        return tearsCost;
    }

    /**
     * antud meetod võrdleb kahe klouni taset vastavalt nende tasemele
     * @param theSecondClown the object to be compared.
     * @return
     */
    @Override
    public int compareTo(ClownsClass theSecondClown) {
        return Integer.compare(this.clownLevel, theSecondClown.clownLevel);
    }
}