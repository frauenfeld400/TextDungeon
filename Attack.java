public class Attack {
    private int dmg, dmgRange, positionsToAttack, canAttackFrom, accMod, statusPerc;
    private String attackName, verb, attackType, status;
    public Attack() {
        dmg = 0; dmgRange = 0; positionsToAttack = 0; canAttackFrom = 0; accMod = 0;
        attackName = "noName"; verb = "noVerb"; attackType = "noType";
    }

    public Attack(int dmg, int dmgRange, int positionsToAttack, int canAttackFrom, int accMod, String attackType,
                  String attackName, String verb, String status, int statusPerc){
        this.dmg = dmg; this.dmgRange = dmgRange; this.positionsToAttack = positionsToAttack;
        this.canAttackFrom = canAttackFrom; this.accMod = accMod; this.attackType = attackType;
        this.attackName = attackName; this.verb = verb;
        this.status = status; this.statusPerc = statusPerc;
    }
    // getters time
    // no setters as these are dependent on file

    public int getDmg() {
        return this.dmg;
    }
    public int getDmgRange() {
        return this.dmgRange;
    }
    public int getPositionsToAttack() {
        return this.positionsToAttack;
    }
    public int getCanAttackFrom() {
        return this.canAttackFrom;
    }
    public int getAccMod() {
        return this.accMod;
    }
    public String getAttackType() {
        return this.attackType;
    }
    public String getAttackName() {
        return this.attackName;
    }
    public String getVerb() {
        return this.verb;
    }
    public String getStatus() {return this.status;}
    public int getStatusPerc() {return this.statusPerc;}
}
