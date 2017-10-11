/*
 * GameLogic.java
 * Author: Harrison Downs
 * Purpose: Responsible for handling game logic, AKA: things like breaking tiles and movement
 * 
 */

package src;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.Sound;
import org.newdawn.slick.tiled.TiledMap;

public class GameLogic
  extends Thread
{
  // array of entities
  public Entity[] entities = null;
  public int delta = 0;
  public boolean go = true;
  public static boolean updateLoop = false;
  
  // game map variables
  public int width = 0;
  public int height = 0;
  public Tile[][] map = null;
  
  // given by Core.java
  public Player player = null;
  private GameContainer arg0 = null;
  public int sx = 0;
  public int sy = 0;
  int heightpt = 6;
  int widthpt = 6;
  
  // movement variables
  boolean upa = false;
  boolean downa = false;
  boolean righta = false;
  boolean lefta = false;
  
  // state variables
  boolean breaking = false;
  boolean spawnedZombies = false;
  
  // variables for breaking tiles
  int pdir = 0;
  int breakingc = 0;
  
  // GUI variables
  public boolean mapon = false;
  public boolean crafting = false;
  
  // rendering variables
  public Alphabit[][] alphabits = null;
  public Color[][] mappy = null;
  public ArrayList<Particle> particles = null;

  /*
	 * getCoord() - Function responsible for wrapping coordinates. If the 
	 * coordinate is bigger than the max, wrap the coordinate around in
	 * one dimension.
	 * Inputs:
	 * c - The coordinate to check. Equal to either the x or y value but
	 * 		 the function doesn't necessarily know that.
	 * b - The maximum for that axis. AKA the width or height of the tile map.
	 * Returns: The integer value of the coordinate.
	 */
  private int getCoord(int c, int b)
  {
    if (c < 0) {
      c = b + c;
    } else if (c >= b) {
      c = 0 + (c - b);
    }
    return c;
  }
  
  /*
   * randInt() - Function responsible for generating a random integer.
   * Inputs:
   * maxValue - the maximum integer value that can be generated
   * Returns: a random int between 0 and maxValue
   * 
   */
  public int randInt(int maxValue)
  {
    int random = (int)(Math.random() * maxValue);
    return random;
  }
  
  public GameLogic(Tile[][] mappyer, int w, int h, Entity[] e, Player play, GameContainer arg1, Alphabit[][] a, Color[][] m)
  {
    this.map = mappyer;
    this.width = w;
    this.height = h;
    this.entities = e;
    this.player = play;
    this.arg0 = arg1;
    this.alphabits = a;
    this.mappy = m;
    setCraftedItems(CraftedItem.getItems());
    setPriority(10);
    start();
  }
  
  /*
   * run() - the main game logic loop. Responsible for calling all other 
   * 		 functions. run() is operated on its own thread.
   * 
   */
  public void run()
  {
    while (this.go)
    {
      if (updateLoop)
      {
        this.map = Core.returnMap();
        
        updateOnScreenTiles();
        
        // Input updating
        Input input = this.arg0.getInput();
        
        // update movement variables
        updateMovement(input);
        
        // select correct tile in inventory
        handleInventorySelection(input);
        
        // break tiles/entities if necessary (player holding down spacebar)
        breakTilesAndEntities(input);
        
        // place a tile if necessary
        placeTile(input);
        
        // craft tiles
        craftingLogic(input);
        
        // spawn zombies if player is holding a button
        spawnZombies(input);
        
        // creates the overhead map
        createOverheadMap(input);
        
        this.sx = (this.player.x - 43);
        this.sy = (this.player.y - 32);
        
        // loads in the test map from memory (DEBUG TOOL)
        if (input.isKeyDown(38)) {
          try
          {
            File file = new File("GAME.map");
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            
            output.close();
          }
          catch (IOException e)
          {
            e.printStackTrace();
          }
        }
        updateLoop = false;
        this.delta = 0;
      }
      try
      {
        Thread.sleep(1L);
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }
    System.out.println("ENTITIES SHUTTING DOWN!");
  }
  
  
  /*
   * updateOnScreenTiles() - will run through all tiles on screen and update
   * 						 them if necessary (change grass, water, etc.) 
   * 
   */
  public void updateOnScreenTiles(){
	// main loops for all tiles on screen
      for (int x = 0; x < 86; x++) {
        for (int y = 0; y < 64; y++)
        {
      	  
      	// gets the absolute coordinate values for the screen tiles
          int xc = this.player.x - 43 + x;
          int yc = this.player.y - 32 + y;
          if (xc < 0) {
            xc = this.width + xc;
          } else if (xc >= this.width) {
            xc = 0 + (xc - this.width);
          }
          if (yc < 0) {
            yc = this.height + yc;
          } else if (yc >= this.height) {
            yc = 0 + (yc - this.height);
          }
          
          // updates the tiles if they are updatable
          Tile tiley = this.map[yc][xc];
          if ((tiley.id > 0) && (tiley.id <= 4))
          {
            Grass grass = (Grass)tiley;
            grass.updateTile(xc, yc);
          }
          else if ((tiley.id == 22) || (tiley.id == 23))
          {
            Water water = (Water)tiley;
            water.updateTile(xc, yc);
          }
          else if (tiley.id == 9)
          {
            Furnace f = (Furnace)tiley;
            f.updateTile(this.delta);
          }
        }
      }
      
  }
  
  /*
   * updateMovement() - updates the players movement variables and moves if
   * 				    necessary. Also will update tiles the player walks over.
   * Inputs:
   * input - the variable that contains what keys are pressed
   */
  public void updateMovement(Input input){
      this.player.movec += this.delta;
      Tile tiler = this.map[this.player.y][this.player.x];
      // up movement
      if (input.isKeyDown(17))
      {
        this.player.getClass();
        if ((this.player.movec >= 50) && (!input.isKeyDown(16)))
        {
          int yc = getCoord(this.player.y - 1, this.height);
          int xc = this.player.x;
          if (!this.map[yc][xc].getCollidable())
          {
            if ((tiler.id == 1) && (
              (tiler.getValue() == "W") || (tiler.getValue() == "M"))) {
              tiler.setValue("n");
            }
            this.player.y -= 1;
            if (this.player.y < 0) {
              this.player.y = (this.height - 1);
            }
            this.player.movec = 0;
          }
        }
      }
      
      // left movement
      if (input.isKeyDown(30))
      {
        this.player.getClass();
        if ((this.player.movec >= 50) && (!input.isKeyDown(16)))
        {
          int xc = getCoord(this.player.x - 1, this.width);
          int yc = this.player.y;
          if (!this.map[yc][xc].getCollidable())
          {
            if (tiler.id == 1) {
              tiler.setValue("n");
            }
            this.player.x -= 1;
            if (this.player.x < 0)
            {
              this.player.x = (this.width - 1);
              this.sx = (this.width - 43);
            }
            this.player.movec = 0;
          }
        }
      }
      
      // down movement
      if (input.isKeyDown(31))
      {
        this.player.getClass();
        if ((this.player.movec >= 50) && (!input.isKeyDown(16)))
        {
          int yc = getCoord(this.player.y + 1, this.height);
          int xc = this.player.x;
          if (!this.map[yc][xc].getCollidable())
          {
            if (tiler.id == 1) {
              tiler.setValue("n");
            }
            this.player.y += 1;
            if (this.player.y >= this.height) {
              this.player.y = 0;
            }
            this.player.movec = 0;
          }
        }
      }
      
      // right movement
      if (input.isKeyDown(32))
      {
        this.player.getClass();
        if ((this.player.movec >= 50) && (!input.isKeyDown(16)))
        {
          int xc = getCoord(this.player.x + 1, this.width);
          int yc = this.player.y;
          if (!this.map[yc][xc].getCollidable())
          {
            if (tiler.id == 1) {
              tiler.setValue("n");
            }
            this.player.x += 1;
            if (this.player.x >= this.width) {
              this.player.x = 0;
            }
            this.player.movec = 0;
          }
        }
      }
  }
  
  
  /*
   * handleInventorySelection() - responsible for selecting the correct item in
   * 							  the players inventory.
   * Inputs:
   * input - The variable the contains which keys are currently pressed
   * 
   */
  public void handleInventorySelection(Input input){
	// handles which item in the players inventory is currently selected
      this.player.inventory.lastgo += this.delta;
      if (input.isKeyDown(27))
      {
        this.player.inventory.getClass();
        if (this.player.inventory.lastgo > 400)
        {
          this.player.inventory.cur += 1;
          if (this.player.inventory.cur > this.player.inventory.invmax) {
            this.player.inventory.cur = 0;
          }
          this.player.inventory.lastgo = 0;
        }
      }
      if (input.isKeyDown(26))
      {
        this.player.inventory.getClass();
        if (this.player.inventory.lastgo > 400)
        {
          this.player.inventory.cur -= 1;
          if (this.player.inventory.cur < 0) {
            this.player.inventory.cur = this.player.inventory.invmax;
          }
          this.player.inventory.lastgo = 0;
        }
      }
      if ((!input.isKeyDown(26)) && (!input.isKeyDown(27)))
      {
        this.player.inventory.lastgo = 9999;
        if (this.player.inventory.lastgo < 0) {
          this.player.inventory.lastgo = 0;
        }
      }
  }
  
  
  /*
   * breakTilesAndEntities() - will check if the player is holding down space
   * 						   bar and go to break all tiles/entities in their
   * 						   path if necessary.
   * Inputs:
   * input - the variable that contains whether a certain key is pressed
   * 
   */
  public void breakTilesAndEntities(Input input){
      if (input.isKeyDown(57))
      {
        int spx = this.player.x;
        int spy = this.player.y;
        if (!this.breaking)
        {
      	// gets direction
          if (input.isKeyDown(30))
          {
            spx--;
            this.pdir = 3;
          }
          else if (input.isKeyDown(31))
          {
            spy++;
            this.pdir = 2;
          }
          else if (input.isKeyDown(32))
          {
            spx++;
            this.pdir = 1;
          }
          else if (input.isKeyDown(17))
          {
            spy--;
            this.pdir = 0;
          }
          this.breakingc = 0;
          this.breaking = true;
        }
        else
        {
          this.breakingc += this.delta;
          if ((input.isKeyDown(30)) && (this.pdir == 3))
          {
            spx--;
          }
          else if ((input.isKeyDown(31)) && (this.pdir == 2))
          {
            spy++;
          }
          else if ((input.isKeyDown(32)) && (this.pdir == 1))
          {
            spx++;
          }
          else if ((input.isKeyDown(17)) && (this.pdir == 0))
          {
            spy--;
          }
          else
          {
            this.breaking = false;
            this.breakingc = 0;
            this.pdir = 0;
          }
        }
        spx = getCoord(spx, this.width);
        spy = getCoord(spy, this.height);
        
        // checks entities to see which one needs to be broken
        for (int z = 0; z < 500; z++)
        {
          Entity e = this.entities[z];
          if ((e != null) && (e.x == spx) && (e.y == spy) && (e.id == 1))
          {
            e.health -= this.player.damage;
            Entity.soldierSwipe.play();
            if (e.health <= 0)
            {
              for (int g = 0; g < 20; g++) {
                this.particles.add(new Particle(this.entities[z].x * 12, this.entities[z].y * 12, this.width, this.height, Color.red, 12));
              }
              Core.zombiesDead += 1;
              this.entities[z] = null;
            }
          }
        }
        
        // if the target is a breakable tile
        if ((this.map[spy][spx].getBreakable()) && (this.breakingc >= this.map[spy][spx].breaktime))
        {
          this.breakingc = 0;
          Tile newtile = new Tile();
          
          // break the tile and change is depending on its current value
          Tile tiley = this.map[spy][spx];
          tiley.getBreak().play();
          newtile.setValue(tiley.getValue());
          newtile.setColor(tiley.getColor());
          newtile.id = tiley.id;
          newtile.setCollidable(tiley.getCollidable());
          if ((!tiley.artificial) && (tiley.isStone()))
          {
            tiley.setValue("-");
            tiley.setBreakable(false);
            
            tiley.id = 24;
            Tile.tmap.setTileId(spx, spy, Tile.groundlayer, tiley.id);
          }
          else
          {
            Tile tilei = new Grass();
            int ran = randInt(2);
            String s = "w";
            if (ran == 0)
            {
              s = "w";
              tilei.id = 4;
              int grass = randInt(2);
              if (grass == 0)
              {
                s = "W";
                tilei.id = 3;
              }
            }
            else if (ran == 1)
            {
              s = "m";
              tilei.id = 1;
              int grass = randInt(2);
              if (grass == 0)
              {
                s = "M";
                tilei.id = 2;
              }
            }
            int rand = randInt(2);
            
            tilei.setValue("n");
            tilei.originalVal = s;
            tilei.setCollidable(false);
            tilei.setBreakable(false);
            Tile.tmap.setTileId(spx, spy, Tile.groundlayer, tilei.id);
            if (rand == 0) {
              tilei.setColor(new Color(106, 66, 45));
            } else {
              tilei.setColor(new Color(106, 66, 45));
            }
            tilei.brown = true;
            this.map[spy][spx] = tilei;
          }
          tiley.setCollidable(false);
          
          this.player.inventory.add(newtile, 1);
        }
      }
      
      // if its an alphabit, add it to the players inventory
      if (this.alphabits[this.player.y][this.player.x] != null)
      {
        this.player.inventory.add(this.alphabits[this.player.y][this.player.x], 1);
        Entity.chime.play();
        this.alphabits[this.player.y][this.player.x] = null;
      }
  }
  
  /*
   * placeTile() - will check whether to place a tile and do so if necessary
   * Inputs:
   * input - the variable that contains whether a certain key is pressed
   * 
   */
  public void placeTile(Input input){
      if ((input.isKeyDown(16)) && (this.player.inventory.nums[this.player.inventory.cur] > 0))
      {
        int spx = this.player.x;
        int spy = this.player.y;
        
        // variable used to decide whether to place a tile or not
        boolean goahead = false;
        
        // get direction for placement
        if ((input.isKeyDown(30)) && (!this.lefta))
        {
          this.lefta = true;
          spx--;
          goahead = true;
        }
        if ((input.isKeyDown(31)) && (!this.downa))
        {
          this.downa = true;
          spy++;
          goahead = true;
        }
        if ((input.isKeyDown(32)) && (!this.righta))
        {
          this.righta = true;
          spx++;
          goahead = true;
        }
        if ((input.isKeyDown(17)) && (!this.upa))
        {
          this.upa = true;
          spy--;
          goahead = true;
        }
        spx = getCoord(spx, this.width);
        spy = getCoord(spy, this.height);
        if (!input.isKeyDown(30)) {
          this.lefta = false;
        }
        if (!input.isKeyDown(31)) {
          this.downa = false;
        }
        if (!input.isKeyDown(32)) {
          this.righta = false;
        }
        if (!input.isKeyDown(17)) {
          this.upa = false;
        }
        
        // check to make sure there's no entity there
        for (int q = 0; q < 500; q++)
        {
          Entity e = this.entities[q];
          if ((e != null) && (e.x == spx) && (e.y == spy)) {
            goahead = false;
          }
        }
        
        // code for placing a soldier
        if ((goahead) && (this.player.inventory.tiles[this.player.inventory.cur].id == 8) && (input.isKeyDown(34)))
        {
          Entity e = new Soldier();
          Soldier s = (Soldier)e;
          
          Soldier.particles = this.particles;
          Core.soldiersPlaced += 1;
          if (Core.curEntityC < 500)
          {
            s.x = spx;
            s.y = spy;
            s.originalx = spx;
            s.originaly = spy;
            this.entities[Core.curEntityC] = s;
            Core.curEntityC += 1;
          }
          this.player.inventory.add(this.player.inventory.tiles[this.player.inventory.cur], -1);
        }
        
        // code for placing a worker
        else if ((goahead) && (this.player.inventory.tiles[this.player.inventory.cur].id == 8))
        {
          Entity e = new Worker();
          
          Worker s = (Worker)e;
          Worker.particles = this.particles;
          if (Core.curEntityC < 500)
          {
            s.x = spx;
            s.y = spy;
            this.entities[Core.curEntityC] = s;
            Core.curEntityC += 1;
          }
          this.player.inventory.add(this.player.inventory.tiles[this.player.inventory.cur], -1);
        }
        
        // furnace code
        else if ((goahead) && (this.player.inventory.tiles[this.player.inventory.cur].id == 9))
        {
          Tile newtile = Tile.getFurnace();
          this.map[spy][spx] = newtile;
          Tile.tmap.setTileId(spx, spy, Tile.groundlayer, newtile.id);
          this.player.inventory.add(newtile, -1);
        }
        
        // otherwise, place the actual tile
        else if (goahead)
        {
          Tile newtile = new Tile();
          Tile oldtile = this.player.inventory.tiles[this.player.inventory.cur];
          
          newtile.setValue(oldtile.getValue());
          newtile.setColor(oldtile.getColor());
          newtile.setCollidable(oldtile.getCollidable());
          newtile.setBreakable(true);
          newtile.id = oldtile.id;
          newtile.artificial = true;
          this.map[spy][spx] = newtile;
          Tile.tmap.setTileId(spx, spy, Tile.groundlayer, newtile.id);
          this.player.inventory.add(oldtile, -1);
        }
      }
  }
  
  
  /*
   * craftingLogic() - will run all code for crafting items as well as checking
   * 				   what to render
   * Inputs:
   * input - the variable that contains whether a certain key is pressed
   * 
   */
  public void craftingLogic(Input input){
      if (input.isKeyDown(23))
      {
        if ((!this.mapon) && (this.crafting))
        {
          int count = 0;
          boolean gotcha = false;
          if (input.isKeyDown(11))
          {
            count = 0;
            gotcha = true;
          }
          else if (input.isKeyDown(2))
          {
            count = 1;
            gotcha = true;
          }
          if (gotcha)
          {
            Tile cItem = getCraftedItems()[count].item;
            CraftedItem c = getCraftedItems()[count];
            boolean canCraft = true;
            for (int p = 0; p < c.requirements.length; p++)
            {
              Tile tilir = CraftedItem.getTile(c.requirements[p][0]);
              if (this.player.inventory.getItemCount(tilir) < c.requirements[p][1]) {
                canCraft = false;
              }
            }
            if (canCraft)
            {
              for (int p = 0; p < c.requirements.length; p++)
              {
                Tile tilir = CraftedItem.getTile(c.requirements[p][0]);
                this.player.inventory.remove(tilir, c.requirements[p][1]);
              }
              this.player.inventory.add(getCraftedItems()[count].item, c.nummade);
            }
          }
        }
        else if (!this.mapon)
        {
          this.crafting = true;
        }
      }
      else if (!input.isKeyDown(23)) {
        this.crafting = false;
      }
  }
  
  /*
   * spawnZombies() - will spawn 100 zombies if a key is pressed
   * Inputs:
   * input - the variable that contains whether a certain key is pressed
   * 
   */
  public void spawnZombies(Input input){
	// spawns zombies
      if ((input.isKeyDown(48)) && (!this.spawnedZombies))
      {
        for (int u = 0; u < 100; u++)
        {
          Entity z = new Zombie();
          z.x = randInt(this.width);
          z.y = randInt(this.height);
          this.entities[Core.curEntityC] = z;
          Core.curEntityC += 1;
        }
        this.spawnedZombies = true;
      } 
  }
  
  
  /*
   * createOverheadMap() - will build the overhead map variable that can then be 
   * 					   rendered by Core.java
   * Inputs:
   * input - the variable that contains whether a certain key is pressed
   * 
   */
  public void createOverheadMap(Input input){
      if (input.isKeyDown(50))
      {
        this.mapon = true;
        for (int bigx = 0; bigx < 360 / this.widthpt; bigx++) {
          for (int bigy = 0; bigy < 360 / this.heightpt; bigy++)
          {
            int wc = 0;
            int gc = 0;
            int mc = 0;
            
            // goes through a chunk and counts each type of tile
            // (stone, water, and grass)
            for (int x = 0; x < this.widthpt; x++) {
              for (int y = 0; y < this.heightpt; y++)
              {
                int pax = this.player.x / this.widthpt;
                pax *= this.widthpt;
                int pay = this.player.y / this.heightpt;
                pay *= this.heightpt;
                
                int newx = bigx * this.widthpt + x + pax - 180;
                int newy = bigy * this.heightpt + y + pay - 180;
                newx = getCoord(newx, this.width);
                newy = getCoord(newy, this.height);
                Tile tileu = this.map[newy][newx];
                if (tileu.isGrass()) {
                  gc++;
                }
                if (tileu.isWater()) {
                  wc++;
                }
                if (tileu.isStone()) {
                  mc++;
                }
              }
            }
            // gets the highest count of grass, water, and stone
            // sets the color on the map to that tile
            if ((gc >= wc) && (gc >= mc)) {
              this.mappy[bigy][bigx] = Color.green;
            } else if ((wc >= gc) && (wc >= mc)) {
              this.mappy[bigy][bigx] = Color.blue;
            } else if ((mc >= gc) && (mc >= wc)) {
              this.mappy[bigy][bigx] = Color.gray;
            }
          }
        }
      }
      else
      {
        this.mapon = false;
      }
  }
  
  // updates all entities
  public void updateEntities()
  {
    for (int x = 0; x < this.entities.length; x++)
    {
      Entity e = this.entities[x];
      e.updateEntity(this.map, this.width, this.height, this.delta);
    }
  }
  
  public static CraftedItem[] getCraftedItems()
  {
    return CraftedItem.getItems();//craftedItems;
  }
  
  public static void setCraftedItems(CraftedItem[] craftedItems)
  {
    craftedItems = craftedItems;
  }
}
