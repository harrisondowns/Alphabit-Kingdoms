README
Alphabit Kingdoms
Harrison Downs

This is an ASCII art game made circa ~2014-2015 as a personal project of mine. The goal of the game is to collect “alphabits” to create specialized warriors that will help you fight off a zombie invasion. Most of the code is there for that purpose, but it is tied together in more of a prototype way.

Controls:
WASD - Move
E - Crafting
Q - Place block
[] - change inventory selection
Space - break block 

The entire project is written in Java using the slick2D library (an extension of LWJGL). All graphics in the game are rendered using ASCII characters. Some of the big challenges I faced while writing this game were: 
- Rendering: One of the first big challenges was finding a way to render hundreds of ASCII letters to the screen without sacrificing performance. These tiles also had to be multicolored and some were animated.
- Game World: The game world wasn't going to be large, so it needed to be able to loop around the edges like Pacman. The AI also had to be able to interact with this looping world in a way that made sense.
- Logic: With 200 entities on the map at all times, finding ways to keep the game logic updating smoothly was a must.
- Cows: Okay, so the cows weren't actually that hard to implement but I still love them. “Moo!”

The entire project is available under the GNU General Public License.
Repo link: https://github.com/harrisondowns/Alphabit-Kingdoms

If I were to redo the project, the first priority would be finishing the gameplay condition to make it fun and less-prototypeish. I would add more types of items and tiles to the game, as well as add more unique types of units to spice up combat. I would also love to make the game multiplayer at some point in the future.