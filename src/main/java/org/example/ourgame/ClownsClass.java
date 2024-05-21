package org.example.ourgame;

import java.io.Serializable;
import java.util.Random;

// antud klass on klounide objektide jaoks ning nende töötlemisega
public class ClownsClass implements Comparable<ClownsClass>, Serializable {
    private static final long serialVersionUID = 1L;
    private static int idCounter = 0;
    private int id;
    private String name;
    private int clownLevel;
    private String picture;

    public ClownsClass(String name, int clownLevel, String picture) {
        this.id = ++idCounter;
        this.name = name;
        this.clownLevel = clownLevel;
        this.picture = picture;
    }

    //getterid ja setterid
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getClownLevel() {
        return clownLevel;
    }

    public String getPicture() {
        return picture;
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