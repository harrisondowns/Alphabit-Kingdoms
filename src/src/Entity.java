package src;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class Entity
{
  public int x = 0;
  public int y = 0;
  public int health = 100;
  public int id = 0;
  public String value = "c";
  public Color color = Color.white;
  private static int movespeed = 1000;
  public int lastmove = 0;
  public int damage = 100;
  public static Sound zombieDeath;
  public static Sound zombieSwipe;
  public static Sound soldierSwipe;
  public static Sound chime;
  public Message message = null;
  public int messageNonDisplay = 30000;
  public int messageDC = 0;
  
  public Entity()
  {
    this.messageDC = randInt(this.messageNonDisplay);
  }
  
  public int randInt(int maxValue)
  {
    int random = (int)(Math.random() * maxValue);
    return random;
  }
  
  public static void setZomNoises(String name, String name2)
    throws SlickException
  {
    zombieDeath = new Sound(name);
    zombieSwipe = new Sound(name2);
  }
  
  public static void setSolNoises(String name)
    throws SlickException
  {
    soldierSwipe = new Sound(name);
  }
  
  public static void setMiscNoises(String name)
    throws SlickException
  {
    chime = new Sound(name);
  }
  
  public void updateEntity(Tile[][] map, int width, int height, int delta)
  {
    this.lastmove += delta;
    this.messageDC += delta;
    if ((this.message == null) && (this.messageDC > this.messageNonDisplay))
    {
      this.message = new Message();
      this.message.message = "Moo!";
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
      int dir = randInt(4);
      int sx = this.x;
      int sy = this.y;
      if (dir == 0) {
        sx++;
      } else if (dir == 1) {
        sx--;
      } else if (dir == 2) {
        sy++;
      } else if (dir == 3) {
        sy--;
      }
      sx = getCoord(sx, width);
      sy = getCoord(sy, height);
      if (!map[sy][sx].getCollidable())
      {
        this.x = sx;
        this.y = sy;
        this.lastmove = 0;
      }
    }
  }
  
  public int getMovespeed()
  {
    return movespeed;
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
