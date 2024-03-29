//import javax.swing.*; //not used
//import java.awt.*; //not used
//import java.awt.event.*; //not used
import java.util.*;
public class Player extends Entity
{
    public String useMode = "Tool";
    
    public Player()
    {
        super();
        getInventory().setGunPrimary(new Sword());
        getInventory().setGunBody(new MagnetoRepulser());
        getInventory().setGunSecondary(new TripleSlingshot());
        getInventory().setArmorHead(new Hat());
        getInventory().setArmorChest(new Shirt());
        getInventory().setArmorLegs(new Pants());
        getInventory().setArmorFeet(new Shoes());
        getInventory().setArmorHands(new Gloves());
        getInventory().setToolPrimary(new Axe());
        getInventory().setToolSecondary(new Multitool());
        getInventory().addAM(new ManualArmorTurret());
        getInventory().addAM(new ManualArmorTurret());
        getInventory().addAM(new ManualArmorTurret());
        getInventory().addAM(new ManualArmorTurret());
        getInventory().addAM(new EnemyTracker());
        getInventory().turrets.add(new Turret());
        calculateArmor();
        setDefaultHealth(500);
        setHealth(getDefaultHealth());
        setPositionX(960);
        setDisplayPosX(960);
        setPositionY(540);
        setDisplayPosY(540);
        regenFactor = 1;
        defaultRegenFactor = 1;
        regenSpeed = 1000;
        defaultRegenSpeed = 1000;
    }
    
    public int getChangeX()
    {
        return getDisplayPosX() - (int) getPositionX();
    }
    public int getChangeY()
    {
        return getDisplayPosY() - (int) getPositionY();
    }
    
    public void playerMove(ArrayList<String> arrows)
    {
        move(arrows);
    }
    
    public void firePrimary(){
        getInventory().getGunPrimary().fire(genVector());
    }
    public void fireSecondary() {
        getInventory().getGunSecondary().fire(genVector());
    }
    
    public void fireBody() {
        getInventory().getGunBody().fire();
    }
    
    public void usePrimary() {
        getInventory().getToolPrimary().use();
    }
    public void useSecondary() {
        getInventory().getToolSecondary().use();
    }
    
    public void use(int mB, double mX, double mY) {
        calculateDirection(mX, mY);
        if(useMode == "Tool") {
            use(mX, mY, mB);
        } else if(useMode == "Gun") {
            fire(mX, mY, mB);
        }
    }
    
    public void fire(double mX, double mY, int mB) {
        if(mB == 1) {
            firePrimary();
        } else if(mB == 3) {
            fireSecondary();
        }
    }
    
    public void use(double mX, double mY, int mB) {
        if(mB == 1) {
            getInventory().getToolPrimary().use(mX, mY);
        } else if(mB == 3) {
            getInventory().getToolSecondary().use(mX, mY);
        }
    }
    
    public void fireATs() {
        ArrayList<ManualArmorTurret> mats = new ArrayList<ManualArmorTurret>();
        for(ArmorModule am : getInventory().getArmorModules()) {
            if(am instanceof ManualArmorTurret) {
                mats.add((ManualArmorTurret)am);
            }
        }
        for(ManualArmorTurret mat : mats) {
            mat.fire();
        }
    }
    
    public void calculateDirection(double mX, double mY) {
        double numer = mY - getDisplayPosY();
        double denum = mX - getDisplayPosX();
        double slope = numer / denum;
        slope = -1*slope;
        if(mX > MainProgram.player.getDisplayPosX()) {
            setsDirect("right");
        } else if(mX < MainProgram.player.getDisplayPosX()){
            setsDirect("left");
        } else if(slope == Double.NEGATIVE_INFINITY) {
            setsDirect("up");
        } else if(slope == Double.POSITIVE_INFINITY) {
            setsDirect("down");
        }
        setDirection(slope);
    }
    
    public Vector genVector() {
        Vector vector = new Vector(getPositionX(), getPositionY(), getsDirect(), getDirection());
        return vector;
    }
    
    public void calculateArmor() {
        setArmor(getArmor() + getInventory().getArmorHead().armor + getInventory().getArmorChest().armor + getInventory().getArmorLegs().armor + getInventory().getArmorHands().armor + getInventory().getArmorFeet().armor);
    }
    
    public void switchMode() {
        if(useMode == "Tool") {
            useMode = "Gun";
        } else if(useMode == "Gun") {
            useMode = "Tool";
        }
    }
    
    public void placeTurret(double mX, double mY) {
        Turret turret = getInventory().turrets.get(0);
        turret.setPositionX(mX - getChangeX());
        turret.setPositionY(mY - getChangeY());
        turret.fixDisplayCoords();
        MainProgram.turrets.add(turret);
        getInventory().turrets.remove(0);
    }

}