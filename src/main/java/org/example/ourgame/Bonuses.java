package org.example.ourgame;

import java.util.Random;
// klass boonuste objektide jaoks
public class Bonuses {
    /**
     * Antud meetod genereerib ja tagastab pisarate arvu vastavalt tasemele ja juhuslikule tegurile
     * @param level - maailmatase, millises mängime
     * @return - tagastab, kui palju raha teenib mängija
     */
    public int tearsForBonuses(int level) {
        //juhusliku arvu loomine
        Random random = new Random();
        int tearsCost = (random.nextInt(5)) * level; //multiply by world lvl
        return tearsCost;
    }
}