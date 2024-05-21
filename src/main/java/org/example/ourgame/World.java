package org.example.ourgame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


public class World {
    private static int clownCounter = 0;
    //klouni lisamise meetod
    public static int addClown(int level, HashMap<Integer, ClownsClass> clownIndex, HashMap<Integer, String[]> levelInfoMap, int maxOpenedClown) {
        String[] clownData = levelInfoMap.get(level);
        if (clownData != null) {
            ClownsClass clown = new ClownsClass(clownData[0], level, clownData[1]);
            System.out.println("Pilt: " + clownData[1]);
            clownIndex.put(clownCounter++, clown);
            System.out.println("Adding clown: " + clown.getName() + ", Level: " + level);
            return Math.max(level, maxOpenedClown);
        } else {
            System.out.println("No clown data available for level " + level);
            return maxOpenedClown;
        }
    }
    //klouni kustutamise meetod. on vaja klouni aretuseks
    public static void deleteClowns (int clownIndeks, HashMap<Integer, ClownsClass> clownsClassHashMap) {
        if (clownsClassHashMap.containsKey(clownIndeks)) {
            clownsClassHashMap.remove(clownIndeks);
            //System.out.println("Kloun indeksiga " + clownIndeks + " on eemaldatud!");
        } else {
            System.out.println("Kloun indeksiga " + clownIndeks + " ei eksisteeri((((");
        }
    }
    //klouni aretuse meetod
    public static void breeding (int clown1Indeks, int clown2Indeks, HashMap<Integer, ClownsClass> clownIndex, HashMap<Integer, String[]> levelInfoMap, int maxOpenedClown, HashMap<Integer, WorldLevel> ourWorlds, int ourWorldLevel, boolean[] openedWorldsList, GameGUI gui) {
        if (clownIndex.containsKey(clown1Indeks) && clownIndex.containsKey(clown2Indeks)) {
            //kas klounid on yldse olemas
            ClownsClass clown1 = clownIndex.get(clown1Indeks);
            ClownsClass clown2 = clownIndex.get(clown2Indeks);

            //kas nende tase on sama
            if (clown1.getClownLevel() == clown2.getClownLevel()) {
                int newLevel = clown1.getClownLevel() + 1;

                clownIndex.remove(clown1Indeks);
                clownIndex.remove(clown2Indeks);

                //kui klounid jaavad saamas maailmas
                if((newLevel - 1)%6 != 0) {
                    addClown(newLevel, clownIndex, levelInfoMap, maxOpenedClown);
                } else {
                    //kui kloun l2heb paremasse maalima
                    if(ourWorldLevel < 6) {
                        HashMap<Integer, ClownsClass> newClownIndex = ourWorlds.get(ourWorldLevel + 1).getClowns();
                        addClown(newLevel, newClownIndex, levelInfoMap, maxOpenedClown);
                        //ei ole vaja informeerida kasutajat iga kord
                        if(openedWorldsList[ourWorldLevel] != true) {
                            openedWorldsList[ourWorldLevel] = true;
                            System.out.println("Palju õnne! Sa avasid uue maalima!");
                            System.out.println("Nüüd saad tähe 'G' abil liikuda maailmade vahel");
                        }
                    } else {
                        System.out.println("Sa saavutasid kõike!");
                        System.out.println("Game over!");
                    }
                }
                gui.updateClownDisplay();
            }
        }
    }
    //klouni ostmise meetod
    public static void buying(double moneyInWallet, int clownLevel, HashMap<Integer, String[]> levelInfoMap, HashMap<Integer, ClownsClass> currentWorldClowns, int maxOpenedClown) {
        int clownCost = (int) (Math.pow(clownLevel, 3) + 5);
        if(clownCost <= moneyInWallet) {
            moneyInWallet -= clownCost;
            System.out.println("Ostmine õnnestus!" + "Sa raiskasid " + clownCost + " pisaraid tühise klouni peale, ning sul jäi täpselt " + moneyInWallet + " pisaraid <3");
            addClown(clownLevel, currentWorldClowns, levelInfoMap, maxOpenedClown);
        } else {
            System.out.println("Sorry, sul ei ole piisavalt raha :(");
        }
    }

    public static HashMap<Integer, String[]> readFileClowns (String faliName) {
        //clowns from file
        File clownsFile = new File("textFiles", faliName);
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


    public static void main(String[] args) {
        //System.out.println(readFileClowns("clownsInfo.txt"));
//        Thread gameThread = new Thread(() -> {
//
//            HashMap<Integer, String[] > levelInfoMap = readFileClowns("clownsInfo.txt");
//
//            //stardikapital
//            int moneyInWallet = 6;
//            int maxOpenedClown = 0;
//
//            HashMap<Integer, WorldLevel> ourWorlds= new HashMap<>();
//
//            //millisele maailmale kuulub kloun lvl-iga
//            ourWorlds.put(1, new WorldLevel(1, 1, ""));
//            ourWorlds.put(2, new WorldLevel(2, 7, ""));
//            ourWorlds.put(3, new WorldLevel(3, 13, ""));
//            ourWorlds.put(4, new WorldLevel(4, 19, ""));
//            ourWorlds.put(5, new WorldLevel(5, 25, ""));
//            ourWorlds.put(6, new WorldLevel(6, 31, ""));
//
//            //millised maailmad on juba kasutajaga avatud
//            boolean[] openedWorldsList = {true, false, false, false, false, false};
//
//            //Start
//            Scanner scanner = new Scanner(System.in);
//            //tervitus
//            System.out.println("Tere tuulemas Clown Breeding Game-is!\n" +
//                    "Pakkun sulle läbida enne mängu alustamist lühikese õpetuse!");
//
//            //lühike koolitus:
//            //maailma tase, kus me oleme
//            int ourWorldLevel = 1;
//            //maailm, millega me töötame
//            WorldLevel currentWorld = ourWorlds.get(ourWorldLevel);
//            //klounid maailmas, kus me töötame
//            HashMap<Integer, ClownsClass> currentWorldClowns = currentWorld.getClownIndex();
//
//            //esimene karp
//            currentWorld.generateBox();
//            //karbi avamise täht
//            System.out.println("\n" +
//                    //"У тебя упала первая коробка! в ней лежит твой первый клоун!\n" +
//                    //"Чтобы открыть коробку, тебе нужно нажать букву 'K'." +
//                    "Sinu esimene karp on maha kukkunud! See sisaldab teie esimest klouni!\n" +
//                    "Karbi avamiseks pead vajutama 'K'.");
//            char firstBox = scanner.next().charAt(0);
//            if (firstBox == 'K') {
//                int newLevel = currentWorld.clownsLevelInTheBox(maxOpenedClown, 0);
//                maxOpenedClown = addClown(newLevel, currentWorldClowns, levelInfoMap, maxOpenedClown);
//                System.out.println("Igas maailmas kukuvad karbid aeg-ajalt välja");
//            }
//
//
//            //esimene peksmine
//            //peksmise täht
//            System.out.println("\n" +
//                    //"Что-то клоун выглядит слишком радостным, пора показать ему что жизнь несправедлива!\n" +
//                    //"Ты хочешь стать богаче?\n" +
//                    //"В этой игре слезы клоунов - валюта)\n" +
//                    //"Пора научиться бить их!!!\n" +
//                    //"Чтобы ударить клоуна, тебе нужно нажать букву 'L'." +
//                    "Kloun näeb liiga õnnelik välja, on aeg talle näidata, et elu on ebaõiglane!\n" +
//                    "Kas sa tahad saada rikkamaks?\n" +
//                    "Selles mängus klouni pisarad on valuuta)\n" +
//                    "On aeg õppetada sind vägistada!!!\n" +
//                    "Klouni tabamiseks tuleb vajutada 'L'.\");");
//            char firstSlap = scanner.next().charAt(0);
//            if (firstSlap == 'L') {
//                moneyInWallet += currentWorldClowns.get(0).slapTheClown();
//                System.out.println("Praegu sul on " + moneyInWallet + " pisaraid!");
//            }
//
//            //ostmise täht
//            System.out.println("\n" +
//                    //"Давай теперь научимся тратить заработанные деньги, чтобы покупать ещё больше клоунов!\n" +
//                    //"Чтобы работать с магазином, тебе нужно нажать букву 'P'." +
//                    "Nüüd õpime, kuidas teenitud pisaraid kulutada, et osta veelgi rohkem kloune!\n" +
//                    "Poe juhtimiseks peate vajutama tähte 'P'.");
//            char openShop = scanner.next().charAt(0);
//            if (openShop == 'P') {
//                System.out.println("Sa saad osta neid:");
//                //kui maxOpenedClown < 5, siis kirjutame ainult esimese elemendi
//                if (maxOpenedClown < 5) {
//                    System.out.println("Nimi: Peeter Paanika" +
//                            " Lvl: 1" +
//                            " Hind: 6 pisaraid\n");
//                    //!!märkus iseendale:
//                    ////если будем менять формулу стоимости, то не забыть поменять этот sout
//                } else {
//                    //on võimalik osta 3 lvl-i võrra vähem
//                    for (int i = 1; i <= maxOpenedClown - 3; i++) {
//
//                        String[] info = levelInfoMap.get(i);
//                        System.out.println("Nimi: " + info[0] +
//                                " Lvl: " + i +
//                                " Hind: " + (Math.pow(i, 3) + 5) + " pisaraid\n");
//                    }
//                }
//                System.out.println("Esimese taseme klouni ostmiseks vajuta '1'!");
//                int firstClownsLevel = scanner.nextInt();
//                if (firstClownsLevel == 1) {
//                    buying(moneyInWallet, 1, levelInfoMap, currentWorldClowns, maxOpenedClown);
//                }
//                //esimene ost
//                System.out.println("\n" +
//                        "Õnnitleme sind esimese ostu puhul!");
//            }
//
//            //aretuse täht
//            System.out.println("\n" +
//                    //"Теперь у тебя есть два клоуна одинакового уровня!\n" +
//                    //"Между ними промелькнула искра страсти!\n" +
//                    //"Помоги иcполнить их мечту!\n" +
//                    //"Для того, чтобы скрестить их, нажми букву 'X'" +
//                    "Nüüd on sul kaks samal tasemel klouni!\n" +
//                    "Nende vahel sähvatas kire säde!\n" +
//                    "Aidata ja muuta nende unistused reaalsuseks!\n" +
//                    "Nende ületamiseks vajutage 'X'");
//            char firstBreeding = scanner.next().charAt(0);
//            if (firstBreeding == 'X') {
//                //System.out.println("Вот клоуны, которых ты можешь сейчас скреститьы:");
//                System.out.println("Siin on klounid, keda sa saad ristida");
//                for (Integer key : currentWorldClowns.keySet()) {
//                    ClownsClass value = currentWorldClowns.get(key);
//                    System.out.println("Indeks: " + key + " Klouni nimi: " + value.getName());
//                }
//                //System.out.println("Скопируй и вставь индекс первого клоуна для скрещивания.");
//                System.out.println("Sisesta esimese klouni indeks ristamiseks");
//                int cloun1Index = scanner.nextInt();
//                //System.out.println("А теперь выбери его пару.");
//                System.out.println("Nüüd vali tema paarikaaslane.");
//                int cloun2Index = scanner.nextInt();
//
//                //aretus
//                breeding(cloun1Index, cloun2Index, currentWorldClowns, levelInfoMap, maxOpenedClown, ourWorlds, ourWorldLevel, openedWorldsList);
//            }
//            System.out.println("Õnnitlused! Sa läbisid koolituse!\n" +
//                    "Nüüd saad jätkata klounide paljunemisega ja avada uusi tasemeid klounidele!");
//
//            //mäng
//            currentWorld.startBoxesGenerator();
//            while (true) {
//                System.out.println("Siin on teie klounid selles maailmas: " + ourWorldLevel);
//                for (Integer key : currentWorldClowns.keySet()) {
//                    ClownsClass value = currentWorldClowns.get(key);
//                    System.out.println("Indeks: " + key + " Klouni nimi: " + value.getName());
//                }
//                System.out.println("Siin on teie rahakotis olevate pisarate arv: " + moneyInWallet);
//
//                //pärast maailma valimist
//                System.out.println("Mida sa soovid teha?");
//                System.out.println("K - Ava karp, L - Löö klouni, P - Ava pood, X - Ristida klounid, G - Vaheta maailma");
//
//                //loeme kasutaja sisendit
//                char actionChoice = scanner.next().charAt(0);
//
//                //töötleme sisendit
//                switch (actionChoice) {
//                    case 'K': //karbi avamine
//                        System.out.println("Valisid karbi avamise.");
//                        System.out.println("Vali karp, mida soovite avada:");
//                        HashMap<Integer, BoxesClass> boxes = currentWorld.getBoxes();
//
//                        for (Integer key : boxes.keySet()) {
//                            System.out.println("Indeks: " + key + " karp");
//                        }
//                        //avame valitud karbi
//                        int boxIndex = scanner.nextInt();
//                        int newLevel = currentWorld.clownsLevelInTheBox(maxOpenedClown, boxIndex);
//                        maxOpenedClown = addClown(newLevel, currentWorldClowns, levelInfoMap, maxOpenedClown);
//                        break;
//                    case 'X': //aretus
//                        System.out.println("Otsustasid paaritada kaks klouni, loodetavasti sul on kaks, kes on samal tasemel,\n" +
//                                "muidu ei õnnestu su salasoove teostada)");
//                        System.out.println("Palun vali klounid, keda soovid paaritada oma nimekirjast: ");
//                        //print nimekiri
//                        for (Integer key : currentWorldClowns.keySet()) {
//                            ClownsClass value = currentWorldClowns.get(key);
//                            System.out.println("Indeks: " + key + " Klouni nimi: " + value.getName());
//                        }
//                        System.out.println("Sisesta esimese klouni indeks ristamiseks.");
//                        int cloun1Index = scanner.nextInt();
//                        System.out.println("Nüüd vali tema paarikaaslane.");
//                        int cloun2Index = scanner.nextInt();
//                        breeding(cloun1Index, cloun2Index, currentWorldClowns, levelInfoMap, maxOpenedClown, ourWorlds, ourWorldLevel, openedWorldsList);
//                        break;
//                    case 'L': //peksmine
//                        System.out.println("Sa valisid lüüa vaest klouni.");
//                        System.out.println("Palun vali klouni, keda soovid peksta oma olemasolevate hulgast:");
//                        for (Integer key : currentWorldClowns.keySet()) {
//                            ClownsClass value = currentWorldClowns.get(key);
//                            System.out.println("Indeks: " + key + " Klouni nimi: " + value.getName());
//                        }
//                        int clownIndeks = scanner.nextInt();
//                        moneyInWallet += currentWorldClowns.get(clownIndeks).slapTheClown();
//                        System.out.println("Praegu sul on " + moneyInWallet + " pisaraid!");
//                        break;
//                    case 'P': //pood
//                        System.out.println("Otsustasid käia mustal turul, et osta rohkem kloune.");
//                        System.out.println("Loodetavasti teil on selle jaoks piisavalt vahendeid.");
//
//                        System.out.println("Siin on nimekiri klounidest, mida saad osta: ");
//                        if (maxOpenedClown < 5) {
//                            System.out.println("Nimi: Peeter Paanika" +
//                                    " Lvl: 1" +
//                                    " Hind: 6 pisaraid\n");
//                            //!!märkus iseendale:
//                            ////если будем менять формулу стоимости, то не забыть поменять этот sout
//                        } else {
//                            for (int i = 1; i <= maxOpenedClown - 3; i++) {
//                                String[] info = levelInfoMap.get(i);
//                                System.out.println("Nimi: " + info[0] +
//                                        " Lvl: " + i +
//                                        " Hind: " + (Math.pow(i, 3) + 5) + " pisaraid\n");
//                            }
//                        }
//                        System.out.println("Valige klouni tase, et teda osta!");
//                        int clownLevelForBuying = scanner.nextInt();
//
//                        buying(moneyInWallet, clownLevelForBuying, levelInfoMap, currentWorldClowns, maxOpenedClown);
//                        break;
//                    case 'G': //vali maalma, kus tahad mängida
//                        System.out.println("Siin on maailmad, mida oled avanud:");
//
//                        for (int i = 0; i < openedWorldsList.length; i++) {
//                            if (openedWorldsList[i]) {
//                                System.out.println("Maailm " + (i + 1) + " on avatud");
//                                // Saab lisada muud meetodid lisateabe kuvamiseks maailma kohta, kui vajalik
//                            }
//                        }
//                        ourWorldLevel = scanner.nextInt();
//                        ourWorldLevel = ourWorldLevel;
//                        //maailm, millega me töötame
//                        currentWorld = ourWorlds.get(ourWorldLevel);
//                        //klounidi nimekiri (aktuaalses maailmas)
//                        currentWorldClowns = currentWorld.getClownIndex();
//                        //alustame karbide genereerumise
//                        currentWorld.startBoxesGenerator();
//                        System.out.println("Avasid maailma tasemega " + ourWorldLevel);
//                        break;
//                    default:
//                        System.out.println("Nõutud tegevust ei tunta.");
//                }
//            }
//        });
//        gameThread.start();

        //testimine
/*
        int ourWorldLevel = 1;
        org.example.ourgame.WorldLevel currentWorld = ourWorlds.get(ourWorldLevel);
        HashMap<Integer, org.example.ourgame.ClownsClass> currentWorldClowns = currentWorld.getClownIndex();

        maxOpenedClown += addClown(6, currentWorldClowns, levelInfoMap, maxOpenedClown);
        maxOpenedClown += addClown(6, currentWorldClowns, levelInfoMap, maxOpenedClown);

        breeding(0, 1, currentWorldClowns, levelInfoMap, maxOpenedClown, ourWorlds, ourWorldLevel);

        maxOpenedClown = addClown(2, currentWorldClowns, levelName, maxOpenedClown);
        maxOpenedClown = addClown(2, currentWorldClowns, levelName, maxOpenedClown);

        breeding(0, 1, currentWorldClowns, levelName, maxOpenedClown);

        currentWorldClowns.get(0).slapTheClown();

        maxOpenedClown = addClown(6, currentWorldClowns, levelName, maxOpenedClown);

        moneyInWallet += currentWorldClowns.get(1).slapTheClown();

        System.out.println("Наш хасхМап");
        for (Integer key : currentWorldClowns.keySet()) {
            org.example.ourgame.ClownsClass value = currentWorldClowns.get(key);
            System.out.println("Key: " + key + ", Value: " + value.getName());
        }
        currentWorld.startBoxesGenerator();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

            }
        }, 1 * 60 * 1000); // 5 минут в миллисекундах

        int newClownLevel = currentWorld.clownsLevelInTheBox(maxOpenedClown, 0);
        System.out.println(newClownLevel + " Новый уровень, который выпал из коробки");
        maxOpenedClown = addClown(newClownLevel, currentWorldClowns, levelName, maxOpenedClown);

        System.out.println(moneyInWallet + " money");

*/
    }
}