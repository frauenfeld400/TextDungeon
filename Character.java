public class Character extends Attack {
    private int HP, SPD, ACC, curListPlace, numAttacks, curStress, curHP;
    /*
        HP: total HP of character
        SPD: speed of character
        ACC: accuracy
        curListPlace: current place in turn order (may not need)
        rest self explanatory
     */
    private String name, customName;
    private Attack[] attackList;

    private String[] quips;
    // a string array of quips that the character will say
    // in response to certain situations, sometimes

    public Character() { // default constructor
        name = "noName";
        HP = 0;
        curHP = 0;
        SPD = 0;
        ACC = 0;
        numAttacks = 0;
        curListPlace = 0;
        curStress = 0;
        customName = "noCustomName";
    }

    public Character(String name, int HP, int SPD, int ACC, int numAttacks, String customName) {
        // sets up a character
        // intended to be loaded from file
        this.HP = HP;
        this.curHP = HP;
        this.SPD = SPD;
        this.ACC = ACC;
        this.name = name;
        this.curStress = 0;
        this.numAttacks = numAttacks;
        this.curListPlace = 0;
        attackList = new Attack[numAttacks];
        this.customName = customName;
    }

    public void addAttack(String attackName, int DMG, int DMGRANGE, String attackType, int positionsToAttack,
                          int canAttackFrom, int ACCMOD, String verb, String status, int statusPerc) {

        // adds attacks to the attack list
        if (curListPlace >= numAttacks) {
            System.out.println("Error with adding attack: > numAttacks");
            return;
        }
        attackList[curListPlace] = new Attack(DMG, DMGRANGE, positionsToAttack, canAttackFrom, ACCMOD, attackType,
                attackName, verb, status, statusPerc);
        ++curListPlace; // to properly add in things to the list

    }
    // getters and setters time

    public int getHP() {
        return this.HP;
    }
    public int getCurStress(){
        return this.curStress;
    }
    public int getSPD(){
        return this.SPD;
    }
    public int getACC() {
        return this.ACC;
    }
    public int getCurListPlace() {
        return this.curListPlace;
    }
    public int getNumAttacks() {
        return this.numAttacks; // probably do not need this one
    }
    public String getName(){
        return this.name;
    }
    public Attack[] getAttackList() {
        return this.attackList;
    }
    public String getCustomName() {return this.customName;}
    // setters for HP

    public void setHP(int dmg) {
        this.curHP = this.curHP - dmg; // always subtract, healing is negative dmg
    }
    public void setCurStress(int dmg) {
        this.curStress = this.curStress + dmg; // always add, healing is negative dmg
    }
    // name too
    public void setCustomName(String newName) {
        this.customName = newName; // player chars only
    }
}