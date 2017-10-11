package src;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.tiled.TiledMap;

public class Tile
{
  private String value = "w";
  public String originalVal = "w";
  public int id = 0;
  private Color color = Color.green;
  public int tsw = 0;
  public boolean brown = false;
  public boolean artificial = false;
  public final int wtg = 49999;
  public final int waterC = 500;
  public final int probToRevert = 1000;
  public static final String downGrass = "n";
  public static final String path = ".";
  public static final String water = "~";
  public static final int treeBT = 400;
  public static final int stoneBT = 1000;
  public static TiledMap tmap = null;
  public static int groundlayer = 0;
  private boolean collidable = false;
  private boolean breakable = false;
  public int breakC = 0;
  public int breaktime = 1000;
  private static Sound breaknoise;
  
  public static void setBreak(String name)
    throws SlickException
  {
    breaknoise = new Sound(name);
  }
  
  public Sound getBreak()
  {
    return breaknoise;
  }
  
  public String getValue()
  {
    return this.value;
  }
  
  public void setValue(String val)
  {
    this.value = val;
  }
  
  public void setColor(Color color1)
  {
    this.color = color1;
  }
  
  public Color getColor()
  {
    return this.color;
  }
  
  public static int randInt(int maxValue)
  {
    int random = (int)(Math.random() * maxValue);
    return random;
  }
  
  public boolean getCollidable()
  {
    return this.collidable;
  }
  
  public void setCollidable(boolean val)
  {
    this.collidable = val;
  }
  
  public void setBreakable(boolean val)
  {
    this.breakable = val;
  }
  
  public boolean getBreakable()
  {
    return this.breakable;
  }
  
  public static Tile getFurnace()
  {
    Tile furnace = new Furnace();
    furnace.setValue("O");
    furnace.id = 9;
    Furnace f = (Furnace)furnace;
    f.updateCounter = randInt(50);
    furnace.setColor(Color.gray);
    furnace.setBreakable(true);
    furnace.setCollidable(true);
    furnace.breaktime = 1000;
    return furnace;
  }
  
  public boolean isGrass()
  {
    boolean ret = false;
    if ((this.id > 0) && (this.id <= 4)) {
      ret = true;
    }
    return ret;
  }
  
  public boolean isStone()
  {
    boolean ret = false;
    if ((this.id == 44) || (this.id == 45)) {
      ret = true;
    }
    return ret;
  }
  
  public boolean isWater()
  {
    boolean ret = false;
    if ((this.id == 22) || (this.id == 23)) {
      ret = true;
    }
    return ret;
  }
}
