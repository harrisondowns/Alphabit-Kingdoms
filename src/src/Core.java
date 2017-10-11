/*
 * Core.java
 * Author: Harrison Downs
 * Purpose: Contains the "core" files of the game. All of the rendering code is 
 * 		    in this file. Also responsible for spawning threads the run things 
 * 			like GameLogic and Particles.
 * 
 */

package src;

import java.io.PrintStream;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.tiled.TiledMap;

public class Core
extends BasicGame
{
	
	/*
	 * VARIABLES
	 * The following variables are constants and other such variables that
	 * are needed to run the game. Many of these are adjustable to fit the 
	 * needs of the program.
	 * 
	 */
	private static final boolean playMusic = true;

	// map dimensions
	private static final int width = 500; 
	private static final int height = 500; 
	
	// screen dimensions
	private static final int screenwidth = 1024; 
	private static final int screenWidthInTiles = 86;
	private static final int screenheight = 808;
	private static final int screenHeightInTiles = 64;
	
	// max number of entities on the map
	public static final int entityC = 500;
	
	// max number of particles on the map
	public static final int particleC = 1000;
	
	// max number of cows on the map (subset of entities)
	private static final int cows = 200;
	
	// counters for how many zombies have been killed and how many soliders exist
	public static int zombiesDead = 0;
	public static int soldiersPlaced = 0;

	//GUI switches for what screen to display (if any)
	public static boolean gameover = false;
	private static boolean titleScreen = true;
	public boolean mapon = false;
	
	// number of entities on the field (starts with only cows on field.
	public static int curEntityC = cows;
	
	
	
	// constant array of alphabits that get rendered at the title screen
	private static Alphabit[] aArray = new Alphabit[20];
	private static int[] arx = { 50, 6, 0, 500, 600, 325, 225, 895, 1000, 79, 200, 176, 400, 259, 321, 761, 876, 976, 523, 475 };
	private static int[] ary = { 40, 50, 700, 100, 127, 145, 600, 650, 160, 677, 567, 100, 623, 778, 800, 675, 160, 10, 699, 600 };

	// array the actual map array. Consists of Tile objects
	public static Tile[][] maper = new Tile[Core.width][Core.height];
	
	// TileMap object for rendering
	public TiledMap tmap = null;
	
	// the overhead map
	public Color[][] mappy = new Color[60][60];
	
	// Alphabits, entities, and particle arrays
	public Alphabit[][] alphabits = new Alphabit[Core.width][Core.height];
	public Entity[] entities = new Entity[Core.entityC];
	public ArrayList<Particle> particles = new ArrayList();
	
	// the "runners". Called each iteration of the main game loop.
	// Entity Runner - handles all AI and their logic
	// Game Logic - handles things like player movement, crafting, etc.
	// Particle Runner - handles the updating of particles
	public EntityRunner erunner = null;
	public GameLogic gl = null;
	public ParticleRunner pr = null;
	
	// Player object and a health variable
	public Player player = new Player();
	public int healthinc = 50;
	
	// Delta variables used for frame smoothing
	public int delta = 0;
	public final int renderTime = 10;
	public int renderC = 0;
	
	int heightpt = 6;
	int widthpt = 6;
	int frameC = 0;
	int frameD = 0;
	
	
	
	// switches to prevent simultaneous movement
	boolean upa = false;
	boolean downa = false;
	boolean righta = false;
	boolean lefta = false;
	
	

	
	

	// resources that get loaded in init()
	static Image title;
	static Image gameDone;
	java.awt.Font font;
	TrueTypeFont ttp;
	
	public Core()
	{
		super("Alphabit Kingdoms the Null Void");
	}
	
	
	
	
    
	/*
	 * randInt() - returns a random integer value between 0 and maxValue.
	 */
	public int randInt(int maxValue)
	{
		int random = (int)(Math.random() * maxValue);
		return random;
	}

	/*
	 * init() - function responsible for initializing resources and the map.
	 * Inputs: 
	 * container - the Game Container 
	 */
	public void init(GameContainer container) throws SlickException
	{

		// Set the sound effects and start playing the background music.
		Tile.setBreak("res/click2.ogg");
		Entity.setZomNoises("res/zombie attack.ogg", "res/Swipe.ogg");
		Entity.setSolNoises("res/swords.ogg");
		Entity.setMiscNoises("res/chime.ogg");
		if (playMusic == true){
			Music openingMenuMusic = new Music("res/Pamgaea.ogg");
			openingMenuMusic.loop();
		}

		// Screen Resolution
		AppGameContainer cont = new AppGameContainer(this);
		cont.setDisplayMode(Core.screenwidth, Core.screenheight, false);
		cont.setShowFPS(true);
		container.setTargetFrameRate(120);

		// Initialize the font
		java.awt.Font awtFont = new java.awt.Font("Courier New", 1, 18);
		this.ttp = new TrueTypeFont(awtFont, true);

		// Title and game over images
		title = new Image("res/AlphabitTitle.png");
		gameDone = new Image("res/AlphabitGameover.png");

		// Initialize the alphabit array to NULL and then add some to the array
		for (int x = 0; x < Core.width; x++) {
			for (int y = 0; y < Core.height; y++) {
				this.alphabits[y][x] = null;
			}
		}
		for (int x = 0; x < aArray.length; x++)
		{
			Alphabit a = new Alphabit();
			a.x = arx[x];
			a.y = ary[x];
			aArray[x] = a;
		}

		// Create the cows!
		for (int x = 0; x < Core.cows; x++)
		{
			Entity e = new Entity();
			e.x = randInt(Core.width);
			e.y = randInt(Core.height);
			e.lastmove = randInt(e.getMovespeed());
			this.entities[x] = e;
		}

		// World generation
		WorldGeneration generator = new WorldGeneration();
		maper = generator.generateMap(maper, this.alphabits, 500, 500);

		// Create all loop controllers (Entity Runner, Game Logic, and Particles)
		this.erunner = new EntityRunner(maper, Core.width, Core.height, this.entities, 
				this.player, this.particles);
		this.gl = new GameLogic(maper, Core.width, Core.height, this.entities, this.player, 
				container, this.alphabits, this.mappy);
		this.gl.particles = this.particles;
		this.pr = new ParticleRunner(this.particles);


		try
		{
			// Loads in a tile map from memory as a placeholder. Used to create a 
			// tiled map object.
			this.tmap = new TiledMap("res/PlaceholderMap1.tmx", true);
			Tile.tmap = this.tmap;
			Tile.groundlayer = this.tmap.getLayerIndex("Ground");
			System.out.println(this.tmap.getLayerCount());
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}

		// Once the tile map is loaded in, override it with the generator results.
		int layer = this.tmap.getLayerIndex("Ground");
		for (int x = 0; x < Core.width; x++) {
			for (int y = 0; y < Core.height; y++) {
				if (maper[y][x] != null) {
					this.tmap.setTileId(x, y, layer, maper[y][x].id);
				}
			}
		}
	}

	/*
	 * returnMap() - getter function for the map array.
	 */
	public static synchronized Tile[][] returnMap()
	{
		return maper;
	}


	/*
	 * update() - responsible for handling all the non-tile render data. Will 
	 * update Entities, Game Logic, and Particles in that order.
	 * container - Container 
	 * arg1 - the time in ms since the last frame (the "delta")
	 */
	public void update(GameContainer container, int delta)
			throws SlickException
	{
		// Update the time delta's for all different variables that need it.
		this.delta = delta;
		this.player.damageT += this.delta;
		this.renderC += this.delta;
		this.frameD += this.delta;
		this.frameC += 1;
		Input input = container.getInput();
		if ((!titleScreen) && (!gameover))
		{
			if (!EntityRunner.updateEntities)
			{
				this.erunner.delta = this.delta;
				EntityRunner.updateEntities = true;
			}
			if (!GameLogic.updateLoop)
			{
				this.gl.delta = this.delta;
				GameLogic.updateLoop = true;
			}
			if ((!ParticleRunner.updateEntities) && (this.particles.size() > 0))
			{
				this.pr.delta = this.delta;
				ParticleRunner.updateEntities = true;
			}
		}
		else if (!gameover)
		{
			// starts game if at title screen
			if (input.isKeyDown(Keyboard.KEY_N)) {
				titleScreen = false;
			} 
			else{
				for (int i = 0; i < aArray.length; i++){
					aArray[i].returnValue(this.delta);
				}
			}
		}
	}

	/*
	 * getCoord() - Function responsible for wrapping coordinates. If the 
	 * coordinate is bigger than the max, wrap the coordinate around in
	 * one dimension.
	 * Inputs:
	 * coord - The coordinate to check. Equal to either the x or y value but
	 * 		 the function doesn't necessarily know that.
	 * max - The maximum for that axis. AKA the width or height of the tile map.
	 * Returns: The integer value of the coordinate.
	 */
	private int getCoord(int coord, int max)
	{
		if (coord < 0) {
			coord = max + coord;
		} else if (coord >= max) {
			coord = 0 + (coord - max);
		}
		return coord;
	}

	/*
	 * main(String[] args) - Main function responsible for starting game and 
	 * initializing the game container.
	 * Inputs:
	 * 	- Args: Command line arguments. Should be empty.
	 */
	public static void main(String[] args)
			throws SlickException
	{
		Core game = new Core();

		AppGameContainer container = new AppGameContainer(game);

		container.start();
	}

	
	
	
	
	/*
	 ############################################
	 #											#
	 #              render functions            #
	 #											#
	 ############################################
	 */
	

	
	/*
	 * render() - main rendering function. Delegates graphics things to all other
	 * functions.
	 * Inputs: 
	 * container - Game Container (not used but required for LWJGL render() )
	 * g - the Graphics container.
	 */
	public void render(GameContainer container, Graphics g)
			throws SlickException
	{
		Tile[][] map = returnMap();

		// Handles gameover scenario
		if ((this.player.health <= 0) && 
				(!gameover))
		{
			gameover = true;
			titleScreen = true;
		}
		
		if (!titleScreen)
		{
			if (!this.gl.mapon)
			{
				// render all things from the main game screen
				renderCore(g, map);
			}
			else
			{	// render the overhead map
				renderOverhead(g);
			}
			
			// render damage to screen
			renderDamage(g);
			
			// render lower GUI (health bar and inventory)
			renderLowerGUI(g);
		}
		else if (!gameover)
		{
			// Draw the title screen (including the little animated alphabits)
			g.drawImage(title, 0.0F, 0.0F);
			for (int u = 0; u < aArray.length; u++)
			{
				g.setColor(Color.white);
				g.drawString(aArray[u].val, aArray[u].x, aArray[u].y);
			}
		}
		else
		{
			// render the game over screen
			g.drawImage(gameDone, 0.0F, 0.0F);
		}
	}
	
	/*
	 * renderCore() - Responsible for rendering the tile map, entities,
	 * particles and crafting menu. AKA, all things that are rendered from
	 * the main game screen (not the overhead screen or title)
	 * Inputs:
	 * g - the Graphics container
	 * map - the map in Tile objects.
	 */
   public void renderCore(Graphics g, Tile[][] map){
		// render the main map
		renderTiles(g);
		
		// render the cows
		renderEntities(g);
		
		// render particles 
		renderParticles(g);
		
		// render the player character
		g.setColor(Color.black);
		g.fillRect((this.player.x - this.gl.sx) * 12, (this.player.y - this.gl.sy) * 12, 12.0F, 12.0F);
		g.setColor(this.player.color);
		g.drawString(this.player.value, (this.player.x - this.gl.sx) * 12 + 1, (this.player.y - this.gl.sy) * 12 - 2 + 0);
		
		// render the crafting menu
		if (this.gl.crafting)
		{
			renderCrafting(g);
		}
   }
  
	
	/*
	 * renderTiles() - renders the game map
	 * Input:
	 * g - the Graphics container for rendering
	 */
	public void renderTiles(Graphics g)
	{
		// stitch the tile map together so that it looks like it's continous
		stitchMap();
		
		// render the ALPHABITS
		renderAlphabits(g);
	}
	
	/*
	 * stitchMap() - renders the map so that the entire screen is always 
	 * filled. Will make the game world look like it's continous in all
	 * directions and loops properly.
	 */
	private void stitchMap(){
		int px = this.player.x;
		int py = this.player.y;
		int sapx = this.gl.sx;
		int sapy = this.gl.sy;
		
		// main map render.
		this.tmap.render(0, 0, sapx, sapy, Core.screenWidthInTiles, Core.screenHeightInTiles);
		
		// if the player is on the far left side of the world (x ~~ 0)
		if (px - (Core.screenWidthInTiles / 2) < 0)
		{
			int startx = getCoord(px - (Core.screenwidth / 2), Core.width);
			int starty = py - 32;
			this.tmap.render(0, 0, startx, starty, Math.abs(px - 43), Core.screenHeightInTiles);
		}
		
		// if the player is on the far right side of the world (x ~~ width)
		else if (px + (Core.screenWidthInTiles / 2) >= Core.width)
		{
			int startx1 = getCoord(px + 43, Core.width) - Core.screenWidthInTiles;
			int starty1 = py - (Core.screenHeightInTiles / 2);
			this.tmap.render(0, 0, startx1, starty1, Core.screenWidthInTiles, Core.screenHeightInTiles);
		}
		
		// if the player is on the top side of the world (y ~~ 0)
		if (py - (Core.screenHeightInTiles / 2) < 0)
		{
			int startx = px - 43;
			int starty = getCoord(py - 32, Core.height);
			this.tmap.render(0, 0, startx, starty, Core.screenWidthInTiles, Math.abs(py - 32));
		}
		
		// if the player is on the bottom of the world (y ~~ height)
		else if (py + (Core.screenHeightInTiles / 2) >= Core.height)
		{
			int startx1 = px - 43;
			int starty1 = getCoord(py + 32, 500) - 64;
			this.tmap.render(0, 0, startx1, starty1, 86, 64);
		}
		
		// render the corner parts of the stitched map
		
		// top left corner
		if ((px - 43 < 0) && (py - 32 < 0)) {
			this.tmap.render(0, 0, getCoord(px - 43, 500), getCoord(py - 32, 500), 86, 64);
		} 
		// bottom left corner
		else if ((px - 43 < 0) && (py + 32 >= 500)) {
			this.tmap.render(0, 0, getCoord(px - 43, 500), getCoord(py + 32, 500) - 64, 86, 64);
		} 
		// bottom right corner
		else if ((px + 43 >= 500) && (py + 32 >= 500)) {
			this.tmap.render(0, 0, getCoord(px + 43, 500) - 86, getCoord(py + 32, 500) - 64, 86, 64);
		} 
		// top right corner
		else if ((px + 43 >= 500) && (py - 32 < 0)) {
			this.tmap.render(0, 0, getCoord(px + 43, 500) - 86, getCoord(py - 32, 500), 86, 64);
		}
	}
	/*
	 * renderAlphabits() - renders alphaBits to the screen
	 * Inputs:
	 * g - the Graphics container
	 */
	private void renderAlphabits(Graphics g){
		for (int y = 0; y < Core.screenHeightInTiles; y++) {
			for (int x = 0; x < Core.screenWidthInTiles; x++)
			{
				int xc = x + this.gl.sx;
				int yc = y + this.gl.sy;
				xc = getCoord(xc, Core.width);
				yc = getCoord(yc, Core.height);
				if (this.alphabits[yc][xc] != null)
				{
					g.setColor(Color.black);
					g.fillRect(x * 12, y * 12, 12.0F, 12.0F);
					g.setColor(this.alphabits[yc][xc].getColor());
					g.drawString(this.alphabits[yc][xc].returnValue(this.delta), x * 12, y * 12 + 0 - 3);
				}
			}
		}
	}
	
   /*
    * renderEntities() - responsible for rendering the cows and zombies
    * Inputs:
    * g - the Graphics container
    */
   private void renderEntities(Graphics g){
   	for (int w = 0; w < Core.entityC; w++) {
			if (this.entities[w] != null)
			{
				int x = getCoord(this.entities[w].x - this.gl.sx, Core.width);
				int y = getCoord(this.entities[w].y - this.gl.sy, Core.height);
				
				// check if the coords of the cow are in the screen bounds
				if ((x >= 0) && (x < Core.screenWidthInTiles) 
				 && (y >= 0) && (y < Core.screenHeightInTiles))
				{
					// render the cows by blacking out all tiles behind them
					Entity e = this.entities[w];
					g.setColor(Color.black);
					g.fillRect(x * 12, y * 12, 12.0F, 12.0F);
					
					// actually render the cows/Zombie
					g.setColor(e.color);
					g.drawString(e.value, x * 12, y * 12 - 3);
					if ((e.message != null) && (e.message.message != null))
					{
						// render a message (such as MOO or BRAINS) above the entities
						g.setColor(Message.color);
						int rectX = x * 12 - g.getFont().getWidth(e.message.message) / 2 + 3;
						int rectY = y * 12 - 12;
						g.fillRect(rectX, rectY, g.getFont().getWidth(e.message.message) + 10, 18.0F);
						g.setColor(Color.black);
						g.drawString(e.message.message, x * 12 - g.getFont().getWidth(e.message.message) / 2 + 6, y * 12 - 12);
					}
				}
			}
		}
   }
   
   /*
    * renderParticles() - renders all the particles 
    * Inputs:
    * g - the Graphics container
    */
   private void renderParticles(Graphics g){
   	for (int u = 0; u < this.particles.size(); u++) {
			if ((this.particles.get(u) != null) && (!((Particle)this.particles.get(u)).die))
			{
				
				// calculate screen coordinates
				Particle p = (Particle)this.particles.get(u);
				int x = p.x / 12;
				int ox = p.x - x * 12;
				x = getCoord(x - this.gl.sx, Core.width);
				int y = p.y / 12;
				int oy = p.y - y * 12;
				y = getCoord(y - this.gl.sy, Core.height);
				
				// make sure the particle is on the screen
				if ((x >= 0) && (x < Core.screenWidthInTiles) 
				 && (y >= 0) && (y < Core.screenHeightInTiles))
				{
					g.setColor(p.getColor());
					g.drawString(".", x * 12 + ox, y * 12 + oy);
				}
			}
		}
   }
   
   /*
    * renderOverhead() - renders the overhead map
    * Inputs:
    * g - the Graphics container
    */
   private void renderOverhead(Graphics g){
   	
   	// DEBUG statistics
   	g.drawString("X:" + this.player.x, 0.0F, 50.0F);
		g.drawString("Y:" + this.player.y, 0.0F, 64.0F);
		g.drawString("Zs Dead:" + zombiesDead, 0.0F, 78.0F);
		g.drawString("Solders:" + soldiersPlaced, 0.0F, 92.0F);
		g.drawString("SX:" + this.gl.sx, 0.0F, 106.0F);
		g.drawString("SY:" + this.gl.sy, 0.0F, 120.0F);
		
		// Render the actual overhead map
		for (int x = 0; x < 60; x++) {
			for (int y = 0; y < 60; y++)
			{
				g.setColor(this.mappy[y][x]);
				g.drawString("#", x * 12 + 120, y * 12 + 24);
			}
		}
		// render the border around the map (brown frame)
		g.setColor(new Color(106, 66, 45));
		for (int x = 0; x < 62; x++) {
			g.drawString("#", x * 12 + 108, 12.0F);
		}
		for (int x = 0; x < 62; x++) {
			g.drawString("#", x * 12 + 108, 744.0F);
		}
		for (int y = 0; y < 62; y++) {
			g.drawString("#", 108.0F, y * 12 + 12);
		}
		for (int y = 0; y < 62; y++) {
			g.drawString("#", 840.0F, y * 12 + 12);
		}
		
		// render the player character (always centered)
		g.setColor(Color.white);
		g.drawString("#", 480.0F, 384.0F);
	}
   
   /*
    * renderCrafting() - renders the crafting menu
    * Inputs:
    * g - the Graphics container
    */
   private void renderCrafting(Graphics g){
   	g.setColor(Color.gray);
		g.fillRect(50.0F, 50.0F, 920.0F, 400.0F);
		g.setColor(Color.black);
		g.drawString("Welcome to the best Crafting Menu Ever Designed!", 100.0F, 50.0F);
		g.drawString("------------------------------------------------", 100.0F, 64.0F);
		CraftedItem[] ca = GameLogic.getCraftedItems();
		g.setColor(Color.black);
		for (int u = 0; u < ca.length; u++) {
			System.out.println("PRINT: " + ca[u].item.getValue());

			g.drawString(u + " - " + ca[u].item.getValue() + " - " + ca[u].name + " - " + ca[u].reqs, 60.0F, u * 20 + 80);
		}
   }
   
   /*
    * renderDamage() - responsible for rendering the red bars when the player
    * takes damage
    * Inputs:
    * g - the Graphics container for rendering
    */
   private void renderDamage(Graphics g){
   	if ((this.player.damaged) && (this.player.damageT < 1000))
		{
			g.setColor(Color.red);
			for (int x = 0; x < 86; x++) {
				g.drawString("#", x * 12, 0.0F);
			}
			for (int x = 0; x < 86; x++) {
				g.drawString("#", x * 12, 756.0F);
			}
			for (int y = 0; y < 64; y++) {
				g.drawString("#", 0.0F, y * 12);
			}
			for (int y = 0; y < 64; y++) {
				g.drawString("#", 1014.0F, y * 12);
			}
		}
		else
		{
			this.player.damaged = false;
			this.player.damageT = 0;
		}
   }
   
   /*
    * renderLowerGUI() - responsible for rendering the lower half of the GUI
    * (things like the health bar and inventory)
    * Inputs:
    * g - the Graphics container for rendering
    */
   private void renderLowerGUI(Graphics g){
   	double curhealth = this.player.health / this.player.maxhealth;
		int numhp = (int)(this.healthinc * curhealth);
		String s = "HP: ";
		for (int x = 0; x < numhp; x++) {
			s = s + "#";
		}
		g.setColor(Color.red);
		g.drawString(s, 200.0F, 770.0F);
		for (int x = 0; x < this.player.inventory.invmax; x++)
		{
			if ((this.player.inventory.tiles[x] != null) && (!this.player.inventory.tiles[x].getValue().equals("")))
			{
				Tile t = this.player.inventory.tiles[x];
				g.setColor(t.getColor());
				if (t.id == 8)
				{
					Alphabit a = (Alphabit)t;
					a.returnValue(this.delta);
				}
				g.drawString(t.getValue() + "  x  " + this.player.inventory.nums[x], x * 120, 794.0F);
			}
			g.setColor(Color.gray);
			g.drawString(" ||", x * 120 + 96, 794.0F);
		}
		g.setColor(Color.white);
		g.drawString("[         ]", this.player.inventory.cur * 120 - 6, 792.0F);
   }
   
   


}
