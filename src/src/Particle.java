package src;

import org.newdawn.slick.Color;

public class Particle
{
  public int x = 0;
  public int y = 0;
  public static final int normalDeathPCount = 20;
  private int xv = 0;
  private int yv = 0;
  private Color color = Color.red;
  private int height = 0;
  private int width = 0;
  private int timeToGo = 0;
  private int curCountTD = 0;
  private int countToDie = 0;
  private int curTime = 0;
  public boolean die = false;
  
  public Particle(int a, int b, int w, int h, Color c, int d)
  {
    this.x = a;
    this.y = b;
    this.x += randInt(d);
    this.y += randInt(d);
    this.height = h;
    this.width = w;
    this.color = c;
    this.timeToGo = ((randInt(6) + 1) * 10);
    this.countToDie = (randInt(10) + 2);
    this.xv = (randInt(5) - 2);
    this.yv = (randInt(5) - 2);
  }
  
  public void setTimeToGo(int a)
  {
    this.timeToGo = a;
  }
  
  public Color getColor()
  {
    return this.color;
  }
  
  public void move(int delta)
  {
    this.curTime += delta;
    if (this.curTime >= this.timeToGo)
    {
      this.x += this.xv;
      this.y += this.yv;
      
      this.curCountTD += 1;
      this.curTime = 0;
    }
    if (this.curCountTD >= this.countToDie) {
      this.die = true;
    }
  }
  
  public void setColor(Color c)
  {
    this.color = c;
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
  
  private int randInt(int maxValue)
  {
    int random = (int)(Math.random() * maxValue);
    return random;
  }
}
