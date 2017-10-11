package src;

import org.newdawn.slick.Color;

public class Inventory
{
  public Inventory()
  {
    for (int x = 0; x < this.invmax; x++)
    {
      this.tiles[x] = new Tile();
      this.tiles[x].setValue("");
      this.tiles[x].setColor(Color.gray);
      this.nums[x] = 0;
    }
  }
  
  int invmax = 9;
  public int cur = 0;
  public int lastgo = 0;
  private static final int limit = 999;
  public final int goc = 400;
  public Tile[] tiles = new Tile[this.invmax];
  public int[] nums = new int[this.invmax];
  
  public void add(Tile tiley, int number)
  {
    boolean already = false;
    int index = 0;
    for (int x = 0; x < this.invmax; x++)
    {
      Tile yo = this.tiles[x];
      if (((tiley.id == yo.id) || ((tiley.isStone()) && (yo.isStone()))) && (yo != null) && ((tiley.getValue().equals(yo.getValue())) || (tiley.id == 8)) && (this.nums[x] < 999))
      {
        index = x;
        already = true;
        break;
      }
    }
    if (already)
    {
      this.nums[index] += number;
      if (this.nums[index] <= 0)
      {
        this.tiles[index] = new Tile();
        this.tiles[index].setValue("");
        this.tiles[index].setColor(Color.gray);
      }
    }
    else
    {
      boolean space = false;
      for (int x = 0; x < this.invmax; x++) {
        if (this.tiles[x].getValue().equals(""))
        {
          index = x;
          space = true;
          break;
        }
      }
      if (space)
      {
        this.tiles[index] = tiley;
        this.nums[index] += number;
        if (this.nums[index] <= 0)
        {
          this.tiles[index] = new Tile();
          this.tiles[index].setValue("");
          this.tiles[index].setColor(Color.gray);
        }
      }
    }
  }
  
  public void remove(Tile tiley, int number)
  {
    int curC = number;
    while (curC > 0) {
      for (int x = 0; x < this.invmax; x++)
      {
        Tile yo = this.tiles[x];
        if (((tiley.id == yo.id) || ((tiley.isStone()) && (yo.isStone()))) && ((tiley.getValue().equals(yo.getValue())) || (tiley.id == 8)) && (this.nums[x] < 999))
        {
          while ((this.nums[x] > 0) && (curC > 0))
          {
            this.nums[x] -= 1;
            curC--;
          }
          if (this.nums[x] <= 0)
          {
            this.tiles[x] = new Tile();
            this.tiles[x].setValue("");
            this.tiles[x].setColor(Color.gray);
          }
          if (curC <= 0) {
            break;
          }
        }
      }
    }
  }
  
  public int getItemCount(Tile tiley)
  {
    int c = 0;
    for (int x = 0; x < this.invmax; x++)
    {
      Tile yo = this.tiles[x];
      if (((tiley.id == yo.id) || ((tiley.isStone()) && (yo.isStone()))) && ((tiley.getValue().equals(yo.getValue())) || (tiley.id == 8)) && (this.nums[x] < 999)) {
        c += this.nums[x];
      }
    }
    return c;
  }
}
