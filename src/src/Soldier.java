package src;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Sound;

public class Soldier
  extends Entity
{
  private int movespeed = 300;
  private static final int MIN_DISTANCE_FOR_ENEMY = 5;
  public int guardRange = 5;
  public int originalx = 0;
  public int originaly = 0;
  public static ArrayList<Particle> particles = null;
  boolean canx = false;
  boolean cany = false;
  int xd = 0;
  int yd = 0;
  
  public Soldier()
  {
    this.value = "A";
    this.health = 200;
    this.damage = 50;
    this.id = 2;
    this.color = Color.cyan;
  }
  
  public void updateEntity(Tile[][] map, int width, int height, int delta, Player player, Entity[] entities)
  {
    this.lastmove += delta;
    if (this.health <= 100) {
      this.value = "a";
    }
    if (this.lastmove >= this.movespeed)
    {
      int dir = getDir(player, width, height, map, entities);
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
      if ((!map[sy][sx].getCollidable()) && (getDistance(this.originalx, this.originaly, sx, sy, width, height) <= this.guardRange))
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
  
  private int getDistance(int x1, int y1, int x2, int y2, int width, int height)
  {
    int xdist = Math.abs(x1 - x2);
    xdist = Math.abs(width - xdist);
    int ydist = Math.abs(y1 - y2);
    ydist = Math.abs(height - ydist);
    int xdist1 = Math.abs(x1 - x2);
    int ydist1 = Math.abs(y1 - y2);
    int xd = 0;
    int yd = 0;
    if (xdist < xdist1) {
      xd = xdist;
    } else {
      xd = xdist1;
    }
    if (ydist < ydist1) {
      yd = ydist;
    } else {
      yd = ydist1;
    }
    int dist = xd * xd + yd * yd;
    
    dist = (int)Math.sqrt(dist);
    return dist;
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
  
  private int getDir(Player player, int width, int height, Tile[][] map, Entity[] entities)
  {
    int leastdist = 99999999;
    int leastind = 0;
    int ret = -1;
    Entity find = null;
    for (int u = 0; u < Core.curEntityC; u++)
    {
      Entity e = entities[u];
      if (e != null) {
        if (e.id == 1)
        {
          int dist = getDistance(this.x, this.y, e.x, e.y, width, height);
          if ((dist < 5) && (dist < leastdist))
          {
            find = e;
            leastind = u;
            leastdist = dist;
          }
        }
      }
    }
    if ((leastdist <= 1) && (find.id == 1))
    {
      find.health -= this.damage;
      Entity.soldierSwipe.play();
      if (find.health <= 0)
      {
        Core.zombiesDead += 1;
        Entity.zombieDeath.play();
        for (int g = 0; g < 20; g++) {
          particles.add(new Particle(entities[leastind].x * 12, entities[leastind].y * 12, width, height, Color.red, 12));
        }
        entities[leastind] = null;
      }
    }
    if ((find == null) && (
      (this.x != this.originalx) || (this.y != this.originaly)))
    {
      find = new Entity();
      find.x = this.originalx;
      find.y = this.originaly;
    }
    if (find != null)
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
