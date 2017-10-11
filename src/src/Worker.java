package src;

import java.util.ArrayList;
import org.newdawn.slick.Color;

public class Worker
  extends Soldier
{
  private int movespeed = 300;
  private static final int MIN_DISTANCE_FOR_FURNACE = 20;
  public static ArrayList<Particle> particles = null;
  boolean canx = false;
  boolean cany = false;
  int xd = 0;
  int yd = 0;
  private int counteryvar = 0;
  private static final int furnaceTimer = 2500;
  
  public Worker()
  {
    this.value = "a";
    this.health = 10;
    this.damage = 0;
    this.id = 2;
    this.color = Color.cyan;
  }
  
  public void updateEntity(Tile[][] map, int width, int height, int delta, Player player, Entity[] entities)
  {
    this.lastmove += delta;
    this.counteryvar += delta;
    if (this.health <= 100) {
      this.value = "a";
    }
    if (this.lastmove >= this.movespeed)
    {
      int dir = getDir(player, width, height, map, entities, delta);
      int sx = this.x;
      int sy = this.y;
      if (dir == 0) {
        sy--;
      }
      if (dir == 1) {
        sx++;
      }
      if (dir == 2) {
        sy++;
      }
      if (dir == 3) {
        sx--;
      }
      sx = getCoord(sx, width);
      sy = getCoord(sy, height);
      if (!map[sy][sx].getCollidable())
      {
        this.x = sx;
        this.y = sy;
        this.lastmove = 0;
      }
      else if (this.xd >= this.yd)
      {
        this.canx = false;
      }
      else
      {
        this.cany = false;
      }
    }
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
  
  private int getDir(Player player, int width, int height, Tile[][] map, Entity[] entities, int delta)
  {
    int leastdist = 99999999;
    int ret = -1;
    Entity find = null;
    for (int sx = 0; sx < 40; sx++) {
      for (int sy = 0; sy < 40; sy++)
      {
        int xa = getCoord(this.x + sx - 20, width);
        int ya = getCoord(this.y + sy - 20, height);
        int actx = this.x + sx - 20;
        int acty = this.y + sy - 20;
        if ((map[ya][xa] != null) && (map[ya][xa].id == 9))
        {
          int dist = (this.y - acty) * (this.y - acty) + (this.x - actx) * (this.x - actx);
          dist = (int)Math.sqrt(dist);
          if ((dist < leastdist) && (dist < 20))
          {
            find = new Entity();
            find.x = xa;
            find.y = ya;
            leastdist = dist;
          }
        }
      }
    }
    if ((leastdist <= 1) && (this.counteryvar >= 2500))
    {
      for (int g = 0; g < 5; g++)
      {
        int rand = randInt(2);
        Particle p = new Particle(find.x * 12 - 2, find.y * 12 - 2, width, height, Color.red, 3);
        p.setTimeToGo(100);
        if (rand == 0) {
          p.setColor(Color.orange);
        }
        particles.add(p);
      }
      this.counteryvar = 0;
    }
    else if (find != null)
    {
      int xdist = Math.abs(this.x - find.x);
      xdist = Math.abs(width - xdist);
      int ydist = Math.abs(this.y - find.y);
      ydist = Math.abs(height - ydist);
      
      int xdist1 = Math.abs(this.x - find.x);
      int ydist1 = Math.abs(this.y - find.y);
      if (xdist < xdist1) {
        this.xd = xdist;
      } else {
        this.xd = xdist1;
      }
      if (ydist < ydist1) {
        this.yd = ydist;
      } else {
        this.yd = ydist1;
      }
      if (((this.x != find.x) && (this.xd >= this.yd)) || (!this.cany)) {
        if (xdist < xdist1)
        {
          if (find.x < this.x) {
            ret = 1;
          } else {
            ret = 3;
          }
        }
        else if (xdist > xdist1) {
          if (find.x < this.x) {
            ret = 3;
          } else {
            ret = 1;
          }
        }
      }
      if (((this.y != find.y) && (this.yd > this.xd)) || (!this.canx)) {
        if (ydist < ydist1)
        {
          if (find.y < this.y) {
            ret = 2;
          } else {
            ret = 0;
          }
        }
        else if (ydist > ydist1) {
          if (find.y < this.y) {
            ret = 0;
          } else {
            ret = 2;
          }
        }
      }
    }
    this.canx = true;
    this.cany = true;
    return ret;
  }
}
