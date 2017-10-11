package src;

import java.io.PrintStream;
import java.util.ArrayList;

public class EntityRunner
  extends Thread
{
  public Entity[] entities = null;
  public int delta = 0;
  public boolean go = true;
  public static boolean updateEntities = false;
  public int width = 0;
  public int height = 0;
  public Tile[][] map = null;
  private Player player = null;
  private ArrayList<Particle> particles;
  
  public EntityRunner(Tile[][] mappy, int w, int h, Entity[] e, Player play, ArrayList<Particle> p)
  {
    this.map = mappy;
    this.width = w;
    this.height = h;
    this.entities = e;
    this.player = play;
    this.particles = p;
    setPriority(1);
    start();
  }
  
  public void run()
  {
    while (this.go)
    {
      if (updateEntities)
      {
        this.map = Core.returnMap();
        for (int x = 0; x < this.entities.length; x++)
        {
          Entity e = this.entities[x];
          if (e != null) {
            if (e.id == 0)
            {
              e.updateEntity(this.map, this.width, this.height, this.delta);
            }
            else if (e.id == 1)
            {
              Zombie z = (Zombie)e;
              z.updateEntity(this.map, this.width, this.height, this.delta, this.player, this.particles, this.entities);
            }
            else if (e.id == 2)
            {
              Soldier s = (Soldier)e;
              s.updateEntity(this.map, this.width, this.height, this.delta, this.player, this.entities);
            }
            else if (e.id == 3)
            {
              Worker w = (Worker)e;
              w.updateEntity(this.map, this.width, this.height, this.delta, this.player, this.entities);
            }
          }
        }
        updateEntities = false;
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
  
  public void updateEntities()
  {
    for (int x = 0; x < this.entities.length; x++)
    {
      Entity e = this.entities[x];
      e.updateEntity(this.map, this.width, this.height, this.delta);
    }
  }
}
