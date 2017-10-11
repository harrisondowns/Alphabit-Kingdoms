package src;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class WorldGeneration
{
  private final int patha = 1000;
  private final int lakeC = 100;
  private final int kingC = 10;
  private final int treeC = 100;
  private final int mountC = 5;
  private final int mountV = 30000;
  private final int mountS = 100;
  private final int alphaC = 200;
  private final boolean generateAlphabits = true;
  
  public int randInt(int maxValue)
  {
    int random = (int)(Math.random() * maxValue);
    return random;
  }
  
  public Tile[][] grabMap(Tile[][] map, int width, int height, String path)
  {
    TiledMap mappy = null;
    try
    {
      mappy = new TiledMap(path, false);
    }
    catch (SlickException e)
    {
      e.printStackTrace();
    }
    int ground = mappy.getLayerIndex("Ground");
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++)
      {
        int id = mappy.getTileId(x, y, ground);
        if ((id == 1) || (id >= 9))
        {
          Tile tile = new Grass();
          int ran = randInt(2);
          String s = "w";
          if (ran == 0)
          {
            s = "w";
            int grass = randInt(2);
            if (grass == 0) {
              s = "W";
            }
          }
          else if (ran == 1)
          {
            s = "m";
            int grass = randInt(2);
            if (grass == 0) {
              s = "M";
            }
          }
          tile.setValue(s);
          tile.originalVal = s;
          tile.setColor(Color.green);
          if (id == 9) {
            tile.setColor(new Color(0, 70, 0));
          } else if (id == 10) {
            tile.setColor(new Color(255, 174, 201));
          } else if (id == 11) {
            tile.setColor(new Color(0, 130, 0));
          } else if (id == 12) {
            tile.setColor(new Color(255, 201, 14));
          }
          map[y][x] = tile;
        }
        else if (id == 2)
        {
          Tile tiler = new Water();
          tiler.id = 2;
          tiler.setValue("~");
          Water ti = (Water)tiler;
          ti.chooseType();
          ti.setCollidable(true);
          
          map[y][x] = tiler;
        }
        else if (id == 3)
        {
          Tile tiler = new Tile();
          tiler.setValue("#");
          tiler.setCollidable(true);
          tiler.id = 4;
          tiler.setColor(Color.gray);
          map[y][x] = tiler;
        }
        else if (id == 4)
        {
          Tile tiler = new Tile();
          tiler.setValue(".");
          tiler.id = 3;
          tiler.setColor(Color.gray);
          map[y][x] = tiler;
        }
        else if (id == 5)
        {
          Tile tiler = new Tile();
          tiler.setValue(".");
          tiler.id = 3;
          tiler.setColor(new Color(139, 69, 19));
          map[y][x] = tiler;
        }
        else if (id == 6)
        {
          Tile tiler = new Tile();
          tiler.setValue("#");
          tiler.setCollidable(true);
          tiler.id = 6;
          tiler.setColor(new Color(255, 201, 14));
          map[y][x] = tiler;
        }
        else if (id == 7)
        {
          Tile tiler = new Tile();
          tiler.setValue("#");
          tiler.id = 4;
          tiler.setCollidable(true);
          tiler.setColor(new Color(106, 66, 45));
          map[y][x] = tiler;
        }
      }
    }
    return map;
  }
  
  public Tile[][] generateMap(Tile[][] map, Alphabit[][] alphabits, int width, int height)
  {
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++)
      {
        int ran = randInt(2);
        Tile tiley = new Grass();
        String s = "w";
        if (ran == 0)
        {
          s = "w";
          tiley.id = 1;
          int grass = randInt(2);
          if (grass == 0) {
            s = "W";
          }
          tiley.id = 2;
        }
        else if (ran == 1)
        {
          s = "m";
          tiley.id = 3;
          int grass = randInt(2);
          if (grass == 0)
          {
            s = "M";
            tiley.id = 4;
          }
        }
        int rand = randInt(2);
        
        tiley.setValue(s);
        tiley.originalVal = s;
        if (rand == 0) {
          tiley.setColor(Color.green);
        } else {
          tiley.setColor(new Color(0, 180, 0));
        }
        map[y][x] = tiley;
      }
    }
    map = generateMountains(map, mountC, width, height);
    map = generateLakes(map, lakeC, width, height);
    map = generateKingdoms(map, kingC, width, height);
    for (int u = 0; u < width * height / 100; u++)
    {
      int y = randInt(height);
      int x = randInt(width);
      
      Tile tiler = new Tile();
      tiler.setValue("#");
      tiler.id = 43;
      tiler.setBreakable(true);
      tiler.setCollidable(true);
      tiler.breaktime = 400;
      tiler.setColor(new Color(106, 66, 45));
      map[y][x] = tiler;
    }
    for (int u = 0; u < alphaC; u++)
    {
      Alphabit alphy = new Alphabit();
      int spy = randInt(height);
      int spx = randInt(width);
      alphabits[spy][spx] = alphy;
    }
    return map;
  }
  
  public Tile[][] generateLakes(Tile[][] map, int num, int width, int height)
  {
    for (int a = 0; a < num; a++)
    {
      int curx = randInt(width);
      int cury = randInt(height);
      for (int u = 0; u < patha; u++)
      {
        int oldx = curx;
        int oldy = cury;
        int dir = randInt(4);
        if (dir == 0) {
          cury--;
        } else if (dir == 1) {
          cury++;
        } else if (dir == 2) {
          curx++;
        } else if (dir == 3) {
          curx--;
        }
        curx = getCoord(curx, width);
        cury = getCoord(cury, height);
        Tile tiler = new Water();
        tiler.id = 21;
        tiler.setValue("~");
        Water ti = (Water)tiler;
        ti.chooseType();
        ti.setCollidable(true);
        
        tiler.setColor(Color.blue);
        if (randInt(2) == 0) {
          tiler.setColor(new Color(0, 0, 255));
        }
        if (map[cury][curx].id != 7)
        {
          map[cury][curx] = tiler;
        }
        else
        {
          curx = oldx;
          cury = oldy;
        }
      }
    }
    return map;
  }
  
  public Tile[][] generateKingdoms(Tile[][] map, int num, int width, int height)
  {
    for (int n = 0; n < num; n++)
    {
      int x = randInt(width);
      int y = randInt(height);
      
      generateKingdom(map, x, y, width, height);
    }
    return map;
  }
  
  public Tile[][] generateKingdom(Tile[][] map, int xy, int yy, int width, int height)
  {
    int size = randInt(7) + 4;
    int door = size / 2;
    for (int x = 0; x < size; x++) {
      for (int y = 0; y < size; y++)
      {
        int xc = xy + x;
        int yc = yy + y;
        if (xc >= width) {
          xc = 0 + (xc - width);
        }
        if (yc >= height) {
          yc = 0 + (yc - height);
        }
        Tile tiler = new Tile();
        tiler.setValue(".");
        if ((x == 0) || (y == 0) || (y == size - 1) || (x == size - 1))
        {
          tiler.setValue("#");
          tiler.id = 45;
          tiler.setCollidable(true);
          tiler.setBreakable(true);
          tiler.breaktime = 1000;
        }
        tiler.setColor(Color.gray);
        map[yc][xc] = tiler;
      }
    }
    int xc = xy + door - 1;
    int yc = yy + size - 1;
    if (xc >= width) {
      xc = 0 + (xc - width);
    }
    if (yc >= height) {
      yc = 0 + (yc - height);
    }
    Tile tiler = new Tile();
    tiler.setValue(".");
    tiler.id = 5;
    tiler.setColor(Color.gray);
    map[yc][xc] = tiler;
    xc = xy + door;
    if (xc >= width) {
      xc = 0 + (xc - width);
    }
    tiler = new Tile();
    tiler.setValue(".");
    tiler.id = 5;
    tiler.setColor(Color.gray);
    map[yc][xc] = tiler;
    return map;
  }
  
  public Tile[][] generateMountains(Tile[][] map, int num, int width, int height)
  {
    for (int a = 0; a < num; a++)
    {
      int curx = randInt(width);
      int cury = randInt(height);
      for (int u = 0; u < mountV; u++)
      {
        int dir = randInt(4);
        if (dir == 0) {
          cury--;
        } else if (dir == 1) {
          cury++;
        } else if (dir == 2) {
          curx++;
        } else if (dir == 3) {
          curx--;
        }
        curx = getCoord(curx, width);
        cury = getCoord(cury, height);
        Tile tiler = new Tile();
        tiler.id = 44;
        tiler.setValue("#");
        tiler.artificial = false;
        tiler.setCollidable(true);
        tiler.setBreakable(true);
        tiler.breaktime = 1000;
        if (randInt(2) == 0)
        {
          tiler.setColor(Color.gray);
          tiler.id = 45;
        }
        else
        {
          tiler.setColor(Color.lightGray);
          tiler.id = 44;
        }
        map[cury][curx] = tiler;
      }
    }
    return map;
  }
  
  public Tile[][] fillHoles(Tile[][] map, int loop, int width, int height)
  {
    int[] fillers = { 1, 2 };
    for (int v = 0; v < loop; v++) {
      for (int t = 0; t < fillers.length; t++) {
        for (int x = 0; x < width; x++) {
          for (int y = 0; y < height; y++)
          {
            Tile tiler = map[y][x];
            if (tiler.id == fillers[t])
            {
              boolean fill = true;
              int xc = x;
              if (xc < 0) {
                xc = width - 1;
              }
              if (x <= 0)
              {
                fill = false;
              }
              else
              {
                int comp = map[y][(x - 1)].id;
                if ((x <= 0) || (map[y][(x - 1)].id != comp)) {
                  fill = false;
                } else if ((y <= 0) || (map[(y - 1)][x].id != comp)) {
                  fill = false;
                }
                if ((x >= width - 1) || (map[y][(x + 1)].id != comp)) {
                  fill = false;
                }
                if ((y >= height - 1) || (map[(y + 1)][x].id != comp)) {
                  fill = false;
                }
              }
              if (fill)
              {
                int comp = map[y][(x - 1)].id;
                if (comp == 1)
                {
                  int ran = randInt(2);
                  String s = "w";
                  if (ran == 0)
                  {
                    s = "w";
                    int grass = randInt(2);
                    if (grass == 0) {
                      s = "W";
                    }
                  }
                  else if (ran == 1)
                  {
                    s = "m";
                    int grass = randInt(2);
                    if (grass == 0) {
                      s = "M";
                    }
                  }
                  int rand = randInt(2);
                  
                  Tile tiley = new Grass();
                  tiley.setValue(s);
                  tiley.originalVal = s;
                  if (rand == 0) {
                    tiley.setColor(Color.green);
                  } else {
                    tiley.setColor(new Color(0, 180, 0));
                  }
                  map[y][x] = tiley;
                }
                if (comp == 2)
                {
                  Tile tiley = new Water();
                  tiley.id = 2;
                  tiley.setValue("~");
                  Water ti = (Water)tiley;
                  ti.chooseType();
                  ti.setCollidable(true);
                  tiley.setColor(new Color(0, 0, 255));
                  map[y][x] = tiley;
                }
              }
            }
          }
        }
      }
    }
    return map;
  }
  
  private int getCoord(int c, int b)
  {
    if (c < 0) {
      c = b + c;
    } else if (c >= b) {
      c = 0 + (c - b);
    }
    return c;
  }
}
