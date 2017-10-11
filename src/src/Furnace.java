package src;

import org.newdawn.slick.Color;

public class Furnace
  extends Tile
{
  public Furnace()
  {
    this.id = 9;
    this.flames = Color.orange;
  }
  
  public Color flames = null;
  public static final String flameVal = "@";
  public static final int updateTime = 50;
  public int updateCounter = 0;
  private int redval = 0;
  private int orangeval = 0;
  int fstage = 0;
  
  public void updateTile(int delta)
  {
    this.updateCounter += delta;
    if (this.updateCounter >= 50)
    {
      if (this.fstage == 0)
      {
        this.redval += 6;
        if (this.redval >= 255)
        {
          this.redval = 255;
          this.fstage = 1;
        }
      }
      else if (this.fstage == 1)
      {
        this.orangeval += 3;
        if (this.orangeval >= 140) {
          this.fstage = 2;
        }
      }
      else if (this.fstage == 2)
      {
        this.orangeval -= 3;
        if (this.orangeval <= 0)
        {
          this.orangeval = 0;
          this.fstage = 3;
        }
      }
      else
      {
        this.redval -= 6;
        if (this.redval <= 0)
        {
          this.redval = 0;
          this.fstage = 0;
        }
      }
      this.flames = new Color(this.redval, this.orangeval, 0);
      this.updateCounter = 0;
    }
  }
}
