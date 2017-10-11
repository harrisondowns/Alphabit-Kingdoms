package src;

import java.io.PrintStream;
import java.util.ArrayList;

public class ParticleRunner
  extends Thread
{
  public ArrayList<Particle> particles = null;
  public int delta = 0;
  public boolean go = true;
  public static boolean updateEntities = false;
  private Player player = null;
  
  public ParticleRunner(ArrayList<Particle> part)
  {
    this.particles = part;
    setPriority(1);
    start();
  }
  
  public void run()
  {
    while (this.go)
    {
      if (updateEntities) {
        for (int u = 0; u < this.particles.size(); u++)
        {
          Particle p = (Particle)this.particles.get(u);
          if (p != null) {
            p.move(this.delta);
          }
        }
      }
      for (int u = 0; u < this.particles.size(); u++) {
        if (((Particle)this.particles.get(u)).die) {
          this.particles.remove(u);
        }
      }
      updateEntities = false;
      this.delta = 0;
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
}
