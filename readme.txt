Text Dungeon is a clone of the video game "Darkest Dungeon", presented in a text-based format, written in Java. 
This clone focuses entirely on the combat, and not any of the dungeoneering aspects of the original game.

Darkest Dungeon's combat consists of 4 heroes fighting 4 or less monsters. Each hero has health, 4 attacks, 
and a stress bar. Upon losing enough health, a hero enters "death's door" where they may die with any damage
taken depending on their deathblow resistance. Upon reaching 100 stress, a hero may have a mental break, or 
become inspired and buffed. 

The combat is turn-based, with their turn in the order being determined by their SPEED stat. All characters
have a SPEED stat, as well as an ACCURACY stat, which determines their chance to hit another character. This
chance to hit is also determined by a DODGE stat. Some attacks can only target enemies in certain posiitons, 1-4.

This clone is designed to implement this combat system, and randomly generate encounters one after the other
until the player decides to save and quit or dies. This clone is also designed to be moddable very easily,
so it draws from all of its character data from easily modifiable .txt files, that hold instructions on how to
make your own characters, hero or enemy. Although, a .json file would probably be better, with in-game character
generation, so I might try and add that after completing the base of the project.

Every character is an object with its own attributes. 

Upon completion, the program itself will be able to:

* Save/Load multiple save files
* Generate missing .txt files if not present
* Employ a hashtable to quickly search through a potentially very large list of heroes or enemies
* Have weight-based decision making so that the AI attempts to look smart
* Generate enemies based on a chosen "region"
* Emulate the entirety of the combat shown above
* Work

Currently, as of 8/6/2023, the program can:

* Load character data from .txt files
* Generate new (incomplete) save files
* Display heroes and their stats
* Choose heroes when starting a new game
* Save this selection
