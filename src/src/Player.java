package src;

import org.newdawn.slick.Color;

public class Player
{
  public int x = 43;
  public int y = 32;
  public final int movespeed = 50;
  public int health = 100;
  public int maxhealth = 100;
  public int damage = 100;
  public int movec = 0;
  public boolean damaged = false;
  public int damageT = 0;
  public static final int TIME_FOR_DAMAGE_ANIMATION = 1000;
  public String value = "A";
  public final Color color = Color.white;
  public Inventory inventory = new Inventory();
}
