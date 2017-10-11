package src;

import org.newdawn.slick.Color;

public class CraftedItem
{
  // associated tile object for a crafted item
  public Tile item = null;
  
  // total number of crafted items
  public static final int itemC = 2;
  
  // name of item and requirements
  public String name = "Item";
  public String reqs = "Requirements";
  public int[][] requirements = null;
  
  // static variables for crafting requirements
  public static Tile stone = null;
  public static Tile wood = null;
  
  public int nummade = 0;
  
  public CraftedItem()
  {
    stone = new Tile();
    stone.id = 44;
    stone.setValue("#");
    stone.setCollidable(true);
    stone.setBreakable(true);
    stone.breaktime = 1000;
    stone.setColor(Color.gray);
    
    wood = new Tile();
    wood.setValue("#");
    wood.id = 43;
    wood.setBreakable(true);
    wood.setCollidable(true);
    wood.breaktime = 400;
    wood.setColor(new Color(106, 66, 45));
  }
  /*
   * getItems() - returns an array of CraftedItem objects for use
   * by the GUI/Game loop for rendering and handling crafting.
   * All data for CraftedItems is stored in this function.
   * 
   */
  public static CraftedItem[] getItems()
  {
    CraftedItem[] ret = new CraftedItem[2];
    
    // furnace tile
    Tile furnace = new Furnace();
    furnace.setValue("O");
    furnace.id = 9;
    furnace.setColor(Color.gray);
    furnace.setBreakable(true);
    furnace.setCollidable(true);
    furnace.breaktime = 1000;
    CraftedItem furnaceI = new CraftedItem();
    furnaceI.name = "Furnace";
    furnaceI.reqs = "8 Stone, 1 Wood";
    furnaceI.item = furnace;
    furnaceI.requirements = new int[2][2];
    furnaceI.requirements[0][0] = 43;furnaceI.requirements[0][1] = 1;
    furnaceI.requirements[1][0] = 44;furnaceI.requirements[1][1] = 8;
    furnaceI.nummade = 1;
    ret[0] = furnaceI;
    
    // wooden floor tile
    Tile floor = new Tile();
    floor.setValue("+");
    floor.id = 5;
    floor.setColor(Color.orange);
    floor.setBreakable(false);
    floor.setCollidable(false);
    CraftedItem floorI = new CraftedItem();
    floorI.name = "Wooden Floor";
    floorI.reqs = "1 Wood";
    floorI.item = floor;
    floorI.requirements = new int[1][2];
    floorI.requirements[0][0] = 43;floorI.requirements[0][1] = 1;
    floorI.nummade = 1;
    ret[1] = floorI;
    
    return ret;
  }
  
  /*
   * getTile() - returns a static tile object for use in the crafting system.
   * (usually use for materials)
   * Inputs:
   *  - id: the id of the tile being asked for
   * Returns: the Tile object.
   */
  public static Tile getTile(int id)
  {
    Tile ret = null;
    if (id == 43) {
      ret = wood;
    } else if ((id == 44) || (id == 45)) {
      ret = stone;
    }
    return ret;
  }
}
