package src;

import org.newdawn.slick.Color;
import org.newdawn.slick.tiled.TiledMap;

public class Grass
  extends Tile
{
  public static final int chanceOfGreen = 200;
  
  public Grass()
  {
    this.id = 1;
  }
  
  public void updateTile(int x, int y)
  {
    int random = randInt(49999);
    String val = getValue();
    if (random == 0) {
      if (val == "w")
      {
        setValue("W");
        this.id = 3;
        tmap.setTileId(x, y, Tile.groundlayer, this.id);
      }
      else if (val == "W")
      {
        setValue("w");
        this.id = 4;
        tmap.setTileId(x, y, Tile.groundlayer, this.id);
      }
      else if (val == "M")
      {
        setValue("m");
        this.id = 1;
        tmap.setTileId(x, y, Tile.groundlayer, this.id);
      }
      else if (val == "m")
      {
        setValue("M");
        this.id = 2;
        tmap.setTileId(x, y, Tile.groundlayer, this.id);
      }
    }
    if (val == "n")
    {
      int randy = randInt(1000);
      if (randy == 0)
      {
        setValue(this.originalVal);
        if (this.brown)
        {
          int a = randInt(2);
          if (a == 0) {
            setColor(Color.green);
          } else {
            setColor(new Color(0, 180, 0));
          }
          this.brown = false;
        }
      }
    }
  }
}
