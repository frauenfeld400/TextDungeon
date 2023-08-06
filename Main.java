import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Path;
import java.io.*;
import java.util.Random;

public class Main extends Character {
    /*
        This program is a mockup of the video game Darkest Dungeon.
        The purpose of this program is to solidify my object-oriented programming skills.
        It will make use of multiple other classes for monsters and heroes alike.
        This program features:
        Save / Loading []
        Multiple heroes stored in some file [x]
        Multiple enemies stored in some file [x]

        We need to store gamestate within the program somehow.

     */
    // global variables
    static Scanner scnr = new Scanner(System.in);
    // TODO: may replace enemies and heroes arrays with some global hashtables. but might not be necessary
    // TODO: might just do it for enemies. heroes are not randomly selected between rounds.
    public static Character[] enemies;
    public static Character[] heroes; // holds all heroes for lookups
    public static int enemiesIndex = 0;
    public static int heroesIndex = 0;
    public static boolean debug = false;
    public static int numHeroes = 0;
    public static int numEnemies = 0;

    // game-state related global vars

    public static int currentRound = 0;
    public static Character[] turnOrder = new Character[8];
    public static Character[] selectedHeroes = new Character[4];
    public static Character[] currentEnemies = new Character[4];

    public static void main(String[] args) {
        // main will include the main menu. a different method will handle game loop.
        // this will call to the loading method.

        showMain();

        boolean running = true;
        initLists(debug);
        while(running) {
            String input = scnr.nextLine();
            String[] inputBroken = input.split(" "); // handle input

            switch(inputBroken[0]) {
                case ("1"):
                    create(debug);
                    break;
                case ("2"):
                    load(debug);
                    break;
                case ("3"):
                    running = false;
                    break;
                case("4"):
                    if (debug) {
                        System.out.println("Debug is now OFF");
                        debug = false;
                    } else {
                        System.out.println("Debug is now ON");
                        debug = true;
                    }

                    break;
                default:
                    System.out.println("Invalid input.");
            }
        }
    }
    public static boolean create(boolean debug) {

        /*

        this method begins if the player selected "new game".
        if there is not a save file, it will jump right to making one.
        if a player decides to delete a save file, a new one will be created right away.
        the new save file will then be built by the player, who must select 4 heroes.
        this method is "done" until i decide to add status effects, where some things will have to be changed here.

         */

        Path path = Path.of(System.getProperty("user.dir") + "\\save.txt"); // get path
        File file = new File(System.getProperty("user.dir") + "\\save.txt"); // make file obj

        try { // create new file, or dont if it exists

            if (file.createNewFile()) {
                if (debug) {
                    System.out.println("no save file found, making a new one");
                }
                String saveFileHeader = "!TextDungeon Save File\n!The file uses '!' as 'comments' " +
                        "or lines not to be read.\n! The save file is split: Heroes, Turn Order, Enemies, separated " +
                        "by '!!'. \n";
                Files.writeString(path, saveFileHeader);

            } else {
                if (debug) {
                    System.out.println("save file exists");
                }
                System.out.println("There is already a save file here. Delete it? y/n");

                boolean deciding = true;

                while (deciding) {
                    ///////////////////////////////////////////////////////////////////////////////////////////////////
                    String in = scnr.nextLine();
                    String[] inBroken = in.split(" ");
                    switch(inBroken[0].toLowerCase()) {
                        case ("n"):
                            return false;
                        case("y"):
                            deciding = false;
                            // new inner switch statement for this decision
                            System.out.println("Are you sure you want to delete the file? y/n");
                            boolean deciding2 = true;
                            // beginning of decision loop
                            while (deciding2){
                                ///////////////////////////////////////////////////////////////////////////////////////
                                in = scnr.nextLine();
                                inBroken = in.split(" ");

                                switch (inBroken[0].toLowerCase()) {
                                    case ("n") -> deciding2 = false;
                                    case ("y") -> {
                                        deciding2 = false;
                                        if (file.delete()) {
                                            if (debug) {
                                                System.out.println("file deleted successfully");
                                            }
                                        } else {
                                            System.out.println("Failed to delete the save file.");
                                        }
                                        if (!file.createNewFile()) { // make a new save file immediately
                                            System.out.println("Could not create file.");
                                            return false;
                                        }
                                        String saveFileHeader = "!TextDungeon Save File\n";
                                        Files.writeString(path, saveFileHeader);
                                    }
                                    default -> System.out.println("Invalid option. Delete save file? y/n");
                                }
                                ///////////////////////////////////////////////////////////////////////////////////////
                            }
                            // end of case "y" to delete file
                            break;

                        default:
                            System.out.println("Invalid option. Delete old file? y/n");
                    }
                    ///////////////////////////////////////////////////////////////////////////////////////////////////
                    // end of deciding to modify or delete save file. boolean "modify" holds choice
                }
            }

        } catch (IOException e) {
            System.out.println("Some error occurred with creating a new file. Exiting");
            System.exit(1);
        }
        // if we get here, then either there was no save file, or user deleted theirs.
        // handle selecting characters here
        // make sure heroes and enemies files exist

        System.out.println("There are " + numHeroes + " heroes to choose from. You may select 4 of the following list." +
                "\nTo view more information about a hero, enter the number by their name.\nTo select a hero, " +
                "enter the number by their name, a space, then an S.\nExample: 4 S\nTo remove a hero, type the number and R." +
                "\n\nTo see the list again, type" + " \"show\". To see all this again, type \"help\"." +
                "\nTo see your selected heroes, type \"selection\".\nType \"quit\" " +
                "to return to the main menu.");
        System.out.println("You have selected " + 0 + " of 4 heroes.");
        System.out.println("\nHeroes list:");

        printHeroesList();

        boolean choosing = true;
        int numHeroesChosen = 0;
        ArrayList<Character> chosenHeroes = new ArrayList<Character>();

        while(choosing) {
            // parse input
            String in = scnr.nextLine();
            String[] inSplit = in.split(" ");
            try {
                // inSplit[0] should be the num. inSplit[1] should be either S or D, for selecting or deleting.
                int heroSelected = Integer.parseInt(inSplit[0]);

                if (heroSelected < numHeroes + 1 && inSplit.length == 1 && heroSelected != 0) {

                    int selection = Integer.parseInt(inSplit[0]) - 1; // selection is not 0 indexed.
                    // correct input, looking for just hero information

                    System.out.println("Hero information: " + heroes[selection].getName());
                    System.out.println("HP: " + heroes[selection].getHP() + " SPD: " + heroes[selection].getSPD() +
                            " ACC: " + heroes[selection].getACC());

                    System.out.println("All attacks:");
                    Attack[] attacks = heroes[selection].getAttackList();
                    // a bunch of printing stuff that is DONE until i add statuses TODO remember to fix when add status
                    for (int i = 0; i < heroes[selection].getNumAttacks(); i++) {
                        System.out.println(attacks[i].getAttackName() + ", deals " + attacks[i].getDmg() +
                                " to " + (attacks[i].getDmg() + attacks[i].getDmgRange()) + " "
                                + attacks[i].getAttackType() + " damage, " + "attacks positions 1-" +
                                attacks[i].getCanAttackFrom() + ", with an ACC mod of " + attacks[i].getAccMod() + ".");
                    }

                } else if (heroSelected < numHeroes + 1 && inSplit.length == 2 &&
                        heroSelected != 0 && inSplit[1].equals("S") && numHeroesChosen < 4) {
                    // add hero to arraylist of chosen heroes. at the end of this entire operation,
                    // we will add all heroes from list into the save file.
                    // increment numHeroesChosen at the end.

                    System.out.println("Hero selected: " + heroes[heroSelected-1].getName());
                    chosenHeroes.add(heroes[heroSelected-1]);
                    ++numHeroesChosen;

                } else if (heroSelected < numHeroes + 1 && inSplit.length == 2 &&
                        heroSelected != 0 && inSplit[1].equals("R") && numHeroesChosen > 0) {
                    // remove hero from arraylist
                    if (!chosenHeroes.remove(heroes[heroSelected-1])) {
                        System.out.println("Hero " + heroes[heroSelected-1].getName() + " was never selected" +
                                " or is not in selection.");
                    } else {
                        System.out.println("Hero " + heroes[heroSelected-1].getName() + " was removed from selection.");
                        --numHeroesChosen;
                    }

                } else if (heroSelected == 0 || heroSelected > numHeroes) {
                    System.out.println("Invalid input.");
                } else {
                    System.out.println("Invalid input.");
                }

            } catch (NumberFormatException e) {
                // incorrect input or "show" or "quit" or dev "forcequit".
                String otherInput = inSplit[0].toLowerCase();

                switch (otherInput) {
                    case "show" -> {
                        System.out.println("You have selected " + numHeroesChosen + " of 4 heroes.");
                        printHeroesList();
                    }
                    case "quit" -> {
                        if (numHeroesChosen < 4) {
                            System.out.println("Please finish selecting heroes. You have only selected " + numHeroesChosen
                                    + " out of 4.");
                            continue;
                        }
                        choosing = false;
                    }
                    case "forcequit" -> {
                        showMain();
                        choosing = false;
                    }
                    case "help" ->
                            System.out.println("There are " + numHeroes + " heroes to choose from. You may select 4 of the following list." +
                                    "\nTo view more information about a hero, enter the number by their name.\nTo select a hero, " +
                                    "enter the number by their name, a space, then an S.\nExample: 4 S\nTo remove a hero, type the number and R." +
                                    "\n\nTo see the list again, type" + " \"show\". To see all this again, type \"help\"." +
                                    "\nTo see your selected heroes, type \"selection\".\nType \"quit\" " +
                                    "to return to the main menu.");
                    case "selection" -> {
                        System.out.println("Current selection of heroes:");
                        for (int i = 0; i < chosenHeroes.size(); i++) {
                            System.out.println("Hero " + (i + 1) + ": " + chosenHeroes.get(i).getName());
                        }
                    }
                    default -> System.out.println("Invalid input.");
                }
            }
        }
        System.out.println("Saving selection...");
        for (int i = 0; i < chosenHeroes.size(); i++) {
            try {
                System.out.println("Selection saved: " + chosenHeroes.get(i).getName());

                Files.writeString(path, chosenHeroes.get(i).getName() + ",CURRENT_HP: " +
                                Integer.toString(chosenHeroes.get(i).getHP()) +", CURRENT_STRESS: 0" +
                                ",NICKNAME: none,POS: " + i + "\n",
                        StandardOpenOption.APPEND); // write to save file: HERO NAME, HP, NICKNAME.

            } catch (IOException e) {
                System.out.println("Error writing to save file, hero not added.");
                e.printStackTrace();
            }
        }
        System.out.println("Done.");
        // finally, load into internal gamestate.

        populate(debug);
        for (int i = 0; i < 4; i++) {
            selectedHeroes[i] = chosenHeroes.get(i);
        }
        // TODO: need to write enemies into save file
        showMain();
        return true;
    }
    public static void printHeroesList() {
        for (int i = 0; i < numHeroes; i++) {
            System.out.println(i+1 + " " + heroes[i].getName());
        }
    }
    public static boolean save(boolean debug) {
        // saves the game by overriding
        // the target destination save file with gamestate vars.
        return true;
    }

    public static void populate(boolean debug) {
        // populate the enemies array. only used for new games.
        Random randGen = new Random();

        for (int i = 0; i < 4; i++) {
            int select = randGen.nextInt(numEnemies);
            currentEnemies[i] = enemies[select];
        }
    }
    public static boolean load(boolean debug) {

        Path path = Path.of(System.getProperty("user.dir") + "\\save.txt"); // get path
        File save = new File(System.getProperty("user.dir") + "\\save.txt"); // make file obj

        if (!save.exists()) {
            // there is no save file! make a new save game and populate it.
            System.out.println("There is no save file. Starting a new game...");

            if (!create(debug)) {
                System.out.println("Some error in creating new game. Exiting");
                System.exit(-1);
            }
            // we are done here. exit
            return true;

        } else {
            // loading from some save file

            // save file is strictly structured such that 1st 4 elements are chosen characters,
            // 2nd 4 elements are current enemies,
            // 3rd 8 elements consists of turn order,
            // and final element is round number.
            // these are separated by "!!".

            int position = 0; // 0: chosen chars. 1: enemies. etc

            try {

                BufferedReader saveReader = new BufferedReader(new FileReader(save));
                String in;

                int heroIndex = 0;
                int enemyIndex = 0;

                while ((in = saveReader.readLine()) != null) {
                    if (in.equals("!")) {
                        continue; // ignore comments
                    }
                    if (in.equals("!!")) {
                        ++position;
                        continue;
                    }
                    if (position == 0) {

                        String[] givenHero = in.split(",");
                        selectedHeroes[heroIndex] = findHero(givenHero[0]);

                        int curHP = Integer.parseInt(givenHero[1].split(" ")[1]);
                        selectedHeroes[heroIndex].setHP(curHP);

                        int curStress = Integer.parseInt(givenHero[2].split(" ")[1]);
                        selectedHeroes[heroIndex].setCurStress(curStress);

                        String nickname = givenHero[3].split(" ")[1];
                        selectedHeroes[heroIndex].setCustomName(nickname);
                        ++heroIndex;

                    } else if (position == 1) {

                        String[] givenEnemy = in.split(",");
                        currentEnemies[enemyIndex] = findEnemy(givenEnemy[0]);

                        int curHP = Integer.parseInt(givenEnemy[1].split(" ")[1]);
                        currentEnemies[enemyIndex].setHP(curHP);
                    }
                }
            } catch (Exception e) {
                // should never happen, redundant
                System.out.println("Somehow could not find save file. Exiting");
                System.exit(-1);
            }


        }
        // loads from a path to a tmp file. loads from both saved user data and enemy data.
        // tmp file will be used for:
        /*
            * current game-state
            * quick lookup for enemy information
            * looking for which enemies to spawn (determined by region)
         */
        return true;
    }
    public static Character findEnemy(String enemyName) {

        for (Character enemy : enemies) {
            if (enemyName.equals(enemy.getName())) {
                return enemy;
            }
        }
        System.exit(-1);
        return null;
    }
    public static Character findHero(String heroClass) {
        // find a hero given the class
        for (Character hero : heroes) {
            if (heroClass.equals(hero.getName())) {
                return hero;
            }
        }
        // failure, hero not in list
        System.out.println("Hero not in list! Exiting");
        System.exit(-1);
        return null;
    }
    public static void gameLoop(boolean debug) {
        // the game loop.
        /*
            loads from tmp file first, if exists. then, enters loop.
            if no tmp file, create() one.
            in loop, handle a round counter. upon all enemy deaths, set counter to 0.
            when resetting round counter, give the player an option to proceed (new region or same) or camp.
            proceeding spawns new enemies. camping brings up a new options window to recover.

            enemies and players share an 8-slot turn array. the placement of the character
            in the array determines when they go. the placement is slightly random,
            some random num 1 to 3 is rolled, then added to their SPD stat, and they are sorted.
            the higher the resulting number, the closer they are to index 0, or the start.
            the pointer to the character is reset when getting to index 7, or an empty index.

            enemies and players also have a 4-slot placement array, one each.
            some attacks move monsters or players back and forth in this array, by up to 3 places.
            when it is player's turn, they will be presented with the options the hero has.
            each hero can either attack some monster in some pre-determined range in the enemy placement array,
            or heal someone in their placement array. such options are stored in game files.

         */
    }
    public static void initLists(boolean debug) {
        // initializes hero and enemy lists, as well as num enemies and heroes.
        File heroesFile = new File(System.getProperty("user.dir") + "\\heroinfo.txt");
        // first line is always num heroes.
        try {
            BufferedReader br = new BufferedReader(new FileReader(heroesFile)); // set up reader
            try {
                String numHStr = br.readLine();
                numHeroes = Integer.parseInt(numHStr.trim()); // get numHeroes from top of file
                heroes = new Character[numHeroes]; // init heroes list
            } catch (IOException e) {
                System.out.println("Some IOException in reading line. Exiting");
                System.exit(1); // shouldn't happen
            }
            try {
                // set up reading lines
                String inputStr;
                while ((inputStr = br.readLine()) != null) { // loop through file
                    if (inputStr.charAt(0) == '!') {
                        if (debug) {
                            System.out.println("DEBUG: read ! char while reading heroes");
                        }
                        continue; // ignore ! case, indicates comment
                    }
                    if (debug) {
                        System.out.println("Input: " + inputStr);
                    }
                    // we are now reading in hero information
                    // first line holds name, total hp, spd, acc, num attacks, in that order, split by spaces

                    String[] infoBroken = inputStr.split(" ");
                    Character hero = new Character(infoBroken[0], Integer.parseInt(infoBroken[1]),
                                  Integer.parseInt(infoBroken[2]), Integer.parseInt(infoBroken[3]),
                                  Integer.parseInt(infoBroken[4]), infoBroken[5]);

                    // lines afterwards will be attacks, so time to continue in an inner loop until !! is reached.

                    inputStr = br.readLine(); // increment line before loop to get to first attack.

                    while (!inputStr.equals("!!")) {
                        if (debug) {
                            System.out.println("Adding attacks, inputstr: " + inputStr);
                        }
                        infoBroken = inputStr.split(" ");
                        // infoBroken now holds attack name (str), damage, dmg range, dmg type (str), pos to attack,
                        // pos can attack from, acc mod, verb(str) in that order.
                        hero.addAttack(infoBroken[0], Integer.parseInt(infoBroken[1]), Integer.parseInt(infoBroken[2]),
                                infoBroken[3], Integer.parseInt(infoBroken[4]), Integer.parseInt(infoBroken[5]),
                                Integer.parseInt(infoBroken[6]), infoBroken[7], infoBroken[8],
                                Integer.parseInt(infoBroken[9]));
                        inputStr = br.readLine();
                    }

                    // if we are here, time for new character.
                    heroes[heroesIndex] = hero;
                    heroesIndex++;
                }
            } catch (IOException e) {
                System.out.println("some exception while loading heroes, exiting");
                System.exit(1);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error! heroesinfo.txt is missing from root directory. Exiting\n");
            System.exit(1);
        }
        // now do practically the same thing but with enemies list
        // code will be compacted and without comments, it is the same as above
        // TODO: you can combine this with code above with if statements. might do later.
        File enemiesFile = new File(System.getProperty("user.dir") + "\\enemyinfo.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(enemiesFile));
            try {
                String numEStr = br.readLine();
                numEnemies = Integer.parseInt(numEStr.trim());
                enemies = new Character[numEnemies];
            } catch (IOException e) {
                System.out.println("Some IOException in reading line. Exiting");
                System.exit(1);
            }
            try {
                String inputStr;
                while ((inputStr = br.readLine()) != null) { // loop through file
                    if (inputStr.charAt(0) == '!') {
                        if (debug) {
                            System.out.println("DEBUG: read ! char while reading enemies");
                        }
                        continue; // ignore ! case, indicates comment
                    }
                    if (debug) {
                        System.out.println("Input: " + inputStr);
                    }
                    String[] infoBroken = inputStr.split(" ");
                    Character enemy = new Character(infoBroken[0], Integer.parseInt(infoBroken[1]),
                                   Integer.parseInt(infoBroken[2]), Integer.parseInt(infoBroken[3]),
                                   Integer.parseInt(infoBroken[4]), infoBroken[5]);
                    inputStr = br.readLine(); // increment line before loop to get to first attack.
                    while (!inputStr.equals("!!")) {
                        if (debug) {
                            System.out.println("Adding attacks, inputstr: " + inputStr);
                        }
                        infoBroken = inputStr.split(" ");
                        enemy.addAttack(infoBroken[0], Integer.parseInt(infoBroken[1]), Integer.parseInt(infoBroken[2]),
                                infoBroken[3], Integer.parseInt(infoBroken[4]), Integer.parseInt(infoBroken[5]),
                                Integer.parseInt(infoBroken[6]), infoBroken[7], infoBroken[8],
                                Integer.parseInt(infoBroken[9]));
                        inputStr = br.readLine();
                    }
                    enemies[enemiesIndex] = enemy;
                    enemiesIndex++;
                }
            } catch (IOException e) {
                System.out.println("some exception while loading heroes, exiting");
                System.exit(1);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error! heroesinfo.txt is missing from root directory. Exiting\n");
            System.exit(1);
        }
    }
    public static void showMain() {
        System.out.println("""
                Darkest (Text) Dungeon
                Made by Brandon Frauenfeld

                Type a number and press enter to select your option.

                1. New Game
                2. Load Game
                3. Quit
                4. Debug mode toggle
                """);
    }
}