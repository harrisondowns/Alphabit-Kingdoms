package src;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Sound;
import org.newdawn.slick.tiled.TiledMap;

public class Zombie
  extends Entity
{
  private static int movespeed = 300;
  public int lastdir = 0;
  public int dirc = 0;
  int deltabreak = 0;
  private boolean trymove = false;
  private static final int MIN_DISTANCE_FOR_ENEMY = 1000;
  
  public Zombie()
  {
    this.value = "Z";
    this.id = 1;
    this.health = 100;
    this.damage = 20;
    this.color = Color.red;
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
  
  boolean canx = true;
  boolean cany = true;
  
  public void updateEntity(Tile[][] map, int width, int height, int delta, Player player, ArrayList<Particle> particles, Entity[] entities)
  {
    this.lastmove += delta;
    if (this.health <= 50) {
      this.value = "z";
    }
    if (this.trymove) {
      this.deltabreak += delta;
    }
    this.messageDC += delta;
    if ((this.message == null) && (this.messageDC > this.messageNonDisplay))
    {
      this.message = new Message();
      this.message.message = "Brains!";
      this.messageDC = 0;
    }
    else if (this.message != null)
    {
      this.message.displayc += delta;
      if (this.message.displayc > this.message.displaytime) {
        this.message = null;
      }
    }
    if (this.lastmove >= movespeed)
    {
      Entity find = null;
      int leastdist = 99999999;
      int leastind = 0;
      int ret = -1;
      Entity p = new Entity();
      p.x = player.x;
      p.y = player.y;
      int xdist12 = Math.abs(this.x - p.x);
      xdist12 = Math.abs(width - xdist12);
      int ydist12 = Math.abs(this.y - p.y);
      ydist12 = Math.abs(height - ydist12);
      int xdist11 = Math.abs(this.x - p.x);
      int ydist11 = Math.abs(this.y - p.y);
      int xd1 = 0;
      int yd1 = 0;
      if (xdist12 < xdist12) {
        xd1 = xdist12;
      } else {
        xd1 = xdist11;
      }
      if (ydist12 < ydist12) {
        yd1 = ydist12;
      } else {
        yd1 = ydist11;
      }
      int dist1 = xd1 * xd1 + yd1 * yd1;
      
      dist1 = (int)Math.sqrt(dist1);
      if (dist1 < 1000)
      {
        find = p;
        find.id = -1;
        find.health = player.health;
        leastdist = dist1;
        leastind = -1;
      }
      for (int u = 0; u < 500; u++)
      {
        Entity e = entities[u];
        if (e != null) {
          if (e.id == 2)
          {
            int dist = getDistance(this.x, this.y, e.x, e.y, width, height);
            if ((dist < 1000) && (dist < leastdist))
            {
              find = e;
              leastind = u;
              leastdist = dist;
            }
          }
        }
      }
      if ((leastdist <= 1) && (find.id == 2) && (leastind != -1))
      {
        find.health -= this.damage;
        if ((find.health <= 0) && (leastind != -1))
        {
          Entity.soldierSwipe.play();
          for (int g = 0; g < 20; g++) {
            particles.add(new Particle(entities[leastind].x * 12, entities[leastind].y * 12, width, height, Color.cyan, 12));
          }
          entities[leastind] = null;
        }
      }
      else if ((leastind == -1) && (leastdist <= 1))
      {
        player.health -= this.damage;
        Entity.zombieSwipe.play();
        player.health -= this.damage;
        player.damaged = true;
        player.damageT = 0;
      }
      if (find != null)
      {
        int sx = this.x;
        int sy = this.y;
        int xdist = Math.abs(this.x - find.x);
        xdist = Math.abs(width - xdist);
        int ydist = Math.abs(this.y - find.y);
        ydist = Math.abs(height - ydist);
        
        int xdist1 = Math.abs(this.x - find.x);
        int ydist1 = Math.abs(this.y - find.y);
        
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
        if (this.lastmove >= movespeed)
        {
          this.lastmove = 0;
          if ((this.x != find.x) || (this.y != find.y))
          {
            if (((this.x != find.x) && (xd >= yd)) || (!this.cany)) {
              if (xdist < xdist1)
              {
                if (find.x < this.x) {
                  sx++;
                } else {
                  sx--;
                }
              }
              else if (xdist > xdist1) {
                if (find.x < this.x) {
                  sx--;
                } else {
                  sx++;
                }
              }
            }
            if (((this.y != find.y) && (yd > xd)) || (!this.canx)) {
              if (ydist < ydist1)
              {
                if (find.y < this.y) {
                  sy++;
                } else {
                  sy--;
                }
              }
              else if (ydist > ydist1) {
                if (find.y < this.y) {
                  sy--;
                } else {
                  sy++;
                }
              }
            }
          }
          this.canx = true;
          this.cany = true;
          sx = getCoord(sx, width);
          sy = getCoord(sy, height);
          if (!map[sy][sx].getCollidable())
          {
            this.trymove = false;
            this.x = sx;
            this.y = sy;
            this.lastmove = 0;
            this.deltabreak = 0;
          }
          else if (!this.trymove)
          {
            this.trymove = true;
            if (xd >= yd) {
              this.canx = false;
            } else {
              this.cany = false;
            }
          }
          else if (map[sy][sx].getBreakable())
          {
            if (map[sy][sx].breakC < map[sy][sx].breaktime)
            {
              map[sy][sx].breakC += this.deltabreak;
            }
            else
            {
              map[sy][sx] = new Tile();
              map[sy][sx].setValue("-");
              map[sy][sx].id = 24;
              Tile.tmap.setTileId(sx, sy, Tile.groundlayer, 24);
            }
          }
        }
      }
      this.lastmove = 0;
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
}
