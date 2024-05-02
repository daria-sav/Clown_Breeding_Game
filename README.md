# Breeding_Clowns_Game

на вторую часть:
- добавть кнопки:
- все окна, открывающиеся кнопками, открываются поверх открытого на данный момент мира, мир в это время становиться чуть менее ярким.
  плашка денег и конпки остаются какими были
- кнопка переключения между мирами
  (некативные заблюрены. переключаться можно по щелчку мышки или гулять меджу ними стрелками клавы (-> и тп), 
  запускать Enter)
- окошко денег
- кнопка магаза
- кнопка меню (выход с сохранением или начать новыю игру)
- галерея клоунов
  (закрытые черного цвета, переименовывать только открытых там же,
  переходить между мирами в верхней плашке (в закрытые не пускать))

- https://drive.google.com/drive/folders/1rR6JotkL6hcOPr0nJmqA1fbLoZx0lDxR?usp=drive_link
  ссылка на таблицу, продумать тематику миров и персонажей клоунов. 
  нарисовать клоунов.
  нарисовать фоны и декорции.
  в зависимости от дизайна фона и декораций ограничить клоунам пространство для передвижения
- задать немного самостоятельного движения клоунам
- сделать анимацию плача
  
- подумать о работе при изменениеи параметров окна (т.е. дое*аться до Антона)
- добавить горячие клавиши
- добавить реакции на ошибки
- запихнуть в файл имена и описания клоунов из кода, чтобы потом их доставать  
- писать в файл состояние игры, чтобы не сбрасывалось при выходе

**Autorid:**
Daria Savtsenko, Elisabeth Serikova

**Projekti Põhjalik Kirjeldus:**
Projekti eesmärk on luua Java-mäng, mis loob klounide maailma, kus kasutajal on võimalus osta kloune, neid omavahel ristata, avada klounikarpe ning teenida pisaraid (raha). Programm koosneb mitmest klassist, mis on loodud erinevate funktsioonide jaoks, sealhulgas klounide, karbide, boonuste ja maailma taseme klassid.

**Iga Klassi Kohta:**
1. Bonuses:
     - `tearsForBonuses(int level)`: genereerib ja tagastab pisarate arvu vastavalt tasemele ja juhuslikule tegurile.

2. BoxesClass:
   - Tühi klass, millel ei ole meetodeid. On tehtud graafilises etapis kasutamiseks.

3. ClownsClass:
     - `slapTheClown()`: simuleerib klouni peksmist ja arvutab välja teenitud pisarate arvu.
   - `compareTo meetod`: võrdleb kahe klouni taset vastavalt nende tasemele.

4. LevelInfo:
     - `getCost()`: arvutab ja tagastab taseme maksumuse vastavalt sellele, milline on kasutajal taseme valimisel.

5. World:
     - mitu meetodit, nagu `addClown`, `deleteClowns`, `breeding`, `buying`, mis vastutavad klounide lisamise, eemaldamise, ristamise ja ostmise eest vastavalt kasutaja tegevustele.
   - `main` meetod: programmi käivitamine ja kasutajaliides.

6. WorldLevel:
     - `clownsLevelInTheBox(int maxOpenedClown, int boxIndex)`: genereerib klouni taseme vastavalt kasutaja tasemele ja juhuslikule tegurile.
     - `startBoxesGenerator()`: algatab karbikoguja, mis loob uusi karpe vastavalt ajaintervallile.
     - `generateBox()`: loob uue karbi.
     - `bonusesGenerator()`: algatab boonuste genereerija, mis loob uusi boonuseid vastavalt ajaintervallile.
     - `addBonusToList()`: lisab uue boonuse boonuste loendisse.
     - `collectBonuses()`: kogub kõik boonused ja arvutab nende koguarvu pisarates.

**Projekti Tegemise Protsess:**
Idee tekkimine, struktureerimine ja tööplaani loomine, koodi kirjutamine (klasside loomine, meetodite arendamine ja optimeerimine), testimine, parandamine

**Iga Rühmaliikme Panus:**
Proovisime paarisprogrameerimist. Tegime põhimõtteliselt kõik koos. Oli umbes 22 tundi paaristööd.

**Mured:**
Kuna see oli meie esimene kogemus selles projektitüübis, tekkis olukord, kus meil oli vaja päris palju tööd üle teha. Me proovisime enne klassi või meetodite loomist mõelda, kuidas me tahame seda kasutada ja kuidas seda teha efektiivsemalt, aga pärast tuli välja, et oli võimalik seda teha paremini. Oli raske kirjutada HashMap-i optimaalses vormis. 

**Hinnang Töö Lõpptulemusele:**
Oleme oma tulemusega rahul: kõik, mis oli plaanis, on tehtud. Küll tuleb arendada töö efektiivsust, kuna praegu mõjutab meie tööd töökogemuse puudus.

**Selgitus ja/või Näited Testimisest:**
Klassis World on pärast kommentaari "//testimine" kirjeldatud mitmeid testimise näiteid ja protseduure, mis on läbi viidud programmi osade eraldi ja tervikuna toimimise kontrollimiseks. 

//testimine

int ourWorldLevel = 1;
WorldLevel currentWorld = ourWorlds.get(ourWorldLevel);
HashMap<Integer, ClownsClass> currentWorldClowns = currentWorld.getClownIndex();

// Testime klounide loomist ja lisamist maailma tasemele

maxOpenedClown += addClown(6, currentWorldClowns, levelInfoMap, maxOpenedClown);
maxOpenedClown += addClown(6, currentWorldClowns, levelInfoMap, maxOpenedClown);

// Testime klounide ristamist

breeding(0, 1, currentWorldClowns, levelInfoMap, maxOpenedClown, ourWorlds, ourWorldLevel);

// Testime klounide ostmist ja rahakoti vähendamist

maxOpenedClown = addClown(2, currentWorldClowns, levelInfoMap, maxOpenedClown);
maxOpenedClown = addClown(2, currentWorldClowns, levelInfoMap, maxOpenedClown);

// Testime klounide ristamist uuesti pärast ostu

breeding(0, 1, currentWorldClowns, levelInfoMap, maxOpenedClown, ourWorlds, ourWorldLevel);

// Testime klounide peksmist ja pisarate teenimist

currentWorldClowns.get(0).slapTheClown();

// Testime uue klouni genereerimist pärast karbikukkumist

maxOpenedClown = addClown(6, currentWorldClowns, levelInfoMap, maxOpenedClown);

// Testime klouni peksmist ja rahakoti suurenemist

moneyInWallet += currentWorldClowns.get(1).slapTheClown();

// Kuvame klounide andmed pärast kõiki tegevusi

System.out.println("Näitame meie HashMapi:");
for (Integer key : currentWorldClowns.keySet()) {
    ClownsClass value = currentWorldClowns.get(key);
    System.out.println("Võti: " + key + ", Väärtus: " + value.getName());
}
currentWorld.startBoxesGenerator();

// Simuleerime aja möödumist ja uue klouni genereerimist karbist

Timer timer = new Timer();
timer.schedule(new TimerTask() {
    @Override
    public void run() {
        int newClownLevel = currentWorld.clownsLevelInTheBox(maxOpenedClown, 0);
        System.out.println(newClownLevel + " Uus tase, mis kukkus karbist");
        maxOpenedClown = addClown(newClownLevel, currentWorldClowns, levelInfoMap, maxOpenedClown);
    }
}, 1 * 60 * 1000); // 1 minut millisekundites

// Testime klounide kogumist boonustest

int collectedTears = currentWorld.collectBonuses();
System.out.println("Kogusime " + collectedTears + " pisaraid!");
