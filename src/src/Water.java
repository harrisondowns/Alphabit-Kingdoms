package src;

import org.newdawn.slick.tiled.TiledMap;

public class Water
  extends Tile
{
  public Water()
  {
    this.id = 22;
  }
  
  public void chooseType()
  {
    if (randInt(2) == 0)
    {
      setValue("~");
      this.id = 22;
    }
    else
    {
      setValue("-");
      this.id = 23;
    }
  }
  
  public void updateTile(int x, int y)
  {
    int random = randInt(500);
    String val = getValue();
    if (random == 0) {
      if (val == "~")
      {
        setValue("-");
        this.id = 23;
        tmap.setTileId(x, y, Tile.groundlayer, this.id);
      }
      else if (val == "-")
      {
        setValue("~");
        this.id = 22;
        tmap.setTileId(x, y, Tile.groundlayer, this.id);
      }
    }
  }
}
