package jp.co.isol.omiya.test.typetemplate;

/**
 * @author susumu.omiya
 *
 */
public class TypeTemplate {

    /**
     * @param args
     */
    public static void main(String[] args) {

        Fighter<Sword> arthur = new Fighter<Sword>("アーサー", new Sword("エクスカリバー", 500, 200));
        Fighter<HeaveySword> guts = new Fighter<HeaveySword>("ガッツ", new HeaveySword("大剣", 500, 200));
        Fighter<Lance> kufurin = new Fighter<Lance>("クーフーリン", new Lance("ゲイ・ボルグ", 500, 200));
        
        System.out.println(arthur.useWeapon().attack(kufurin));
        System.out.println(kufurin.useWeapon().attack(arthur));
        System.out.println(guts.useWeapon().attack(arthur));
        
        arthur.useWeapon().skillSwordBash(kufurin);
        kufurin.useWeapon().skillSamidare(arthur);
        guts.useWeapon().skillSwordTackle(arthur);
        
    }
}

class Fighter<T extends Weapon> {

    private String name;
    private T weapon;
    private boolean isWeaponUsable = true;
    
    Fighter(String name,T weapon) {
        this.setName(name);
        this.weapon = weapon;
    }
    
    public T useWeapon() {
        return weapon;
    }
    
    public boolean isWeaponUsable() {
        return isWeaponUsable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

interface Weapon {
    public String getName();
    public <T extends Weapon> int attack(Fighter<T> target);
    public int defence();
}

abstract class AbstractWeapon implements Weapon {
    protected String name;
    protected int baseDamageValue;
    protected int baseDefenseValue;
    protected double offensiveEffect = 1.00D;
    protected double defensiveEffect = 1.00D;

    public AbstractWeapon(String name, int baseAttack, int baseDeffense) {
        this.name = name;
        this.baseDamageValue = baseAttack;
        this.baseDefenseValue = baseDeffense;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public <T extends Weapon> int attack(Fighter<T> target) {
        return (int) (baseDamageValue * offensiveEffect) - (target.isWeaponUsable() ? target.useWeapon().defence() : 0);
    }
    @Override
    public int defence() {
        return (int) (baseDefenseValue * defensiveEffect);
    }
}

class Sword extends AbstractWeapon {
    
    public Sword(String name, int baseAttack, int baseDeffense) {
        super(name, baseAttack, baseDeffense);
    }

    public <T extends Weapon> int skillSwordBash(Fighter<T> target) {
        offensiveEffect *= 2;
        int ret = attack(target);
        offensiveEffect = 1;
        return ret;
    }
    
}

class HeaveySword extends Sword {
    
    public HeaveySword(String name, int baseAttack, int baseDeffense) {
        super(name, baseAttack, baseDeffense);
        offensiveEffect *= 1.2;
        defensiveEffect *= 0.8;
    }
    
    public int skillSwordTackle(Fighter<?> target) {
        offensiveEffect *= 1.5;
        int ret = attack(target);
        offensiveEffect = 1;
        return ret;
    }
}

class Lance extends AbstractWeapon {
    
    public Lance(String name, int baseAttack, int baseDeffense) {
        super(name, baseAttack, baseDeffense);
    }

    public <T extends Weapon> int skillSamidare(Fighter<T> target) {
        offensiveEffect *= 1.5;
        int ret = attack(target);
        offensiveEffect = 1;
        return ret;
    }
}