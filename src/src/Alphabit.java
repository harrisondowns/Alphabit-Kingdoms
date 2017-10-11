package src;

import org.newdawn.slick.Color;

public class Alphabit
  extends Tile
{
  public Alphabit()
  {
    this.id = 8;
    setBreakable(true);
    setCollidable(true);
    setColor(Color.white);
  }
  
  public int x = 0;
  public int y = 0;
  public String val = "a";
  private static final String values = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+-=|\\[];,./{}:<>?`~";
  private static final int timetill = 100;
  private int clock = 0;
  
  public String returnValue(int delta)
  {
    this.clock += delta;
    if (this.clock >= 100)
    {
      int maxval = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+-=|\\[];,./{}:<>?`~".length();
      int randval = randInt(maxval - 1);
      this.val = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+-=|\\[];,./{}:<>?`~".substring(randval, randval + 1);
      this.clock = 0;
    }
    return this.val;
  }
  
  public static int randInt(int maxValue)
  {
    int random = (int)(Math.random() * maxValue);
    return random;
  }
  
  public String getValue()
  {
    return this.val;
  }
}
