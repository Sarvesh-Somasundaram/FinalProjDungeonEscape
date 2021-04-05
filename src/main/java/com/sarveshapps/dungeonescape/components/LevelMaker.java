package com.sarveshapps.dungeonescape.components;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * This class generates 5 random mazes in 5 text files
 *
 *  @author Sarvesh Somasundaram
 *  @version 1.0
 *
 *  @see <a href=" https://github.com/Sarvesh1234567/FinalProjDungeonEscape ">Github Link to Project Repo</a>
 *  @see <a href=" https://github.com/AlmasB/FXGL ">JavaFX Game Library</a>
 *  @see <a href=" https://opengameart.org/ ">Textures, animations and backgrounds</a>
 *
 */
public class LevelMaker {

    /**
     * This method generates 5 random mazes in 5 text files
     */
    public static void LevelGen() {
        Random rand = new Random();

        /*
            Here, all the variables used in the method are declared and initialized
         */
        int upperBound = 3;
        int lowerBound = 2;
        int maxEnemies = 3;
        int enemyCounter = 0;
        int numWalls = 5;
        int wallCounter = 0;

        for (int k = 0; k < 5; k++) { // this loop repeats 5 times to make 5 different text files and 5 different mazes

            File f = new File("src" + File.separator + "main" + File.separator + "resources" + File.separator +
                    "assets" + File.separator + "levels" + File.separator + "levels" + k + ".txt"); // creates the file

            try {
                if (f.createNewFile()) { // creating the actual file
                    System.out.println("File created: " + f.getName());
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }


            try {
                FileWriter myWriter = new FileWriter(f); // opening the file in a FileWriter

                /*
                    Here is the algorithm/logic I wrote to generate the maze

                    first the outside edges on the top and bottom have all 1's to indicate that the outside edges are
                    walled, except for one spot in the center which has the exit at the top and the player at the bottom

                    Then, for all the rows in between the first and last row, a random number between 0 and 2 is
                    selected, if a 2 is selected then an enemy is spawned and the enemy counter goes up by 1, if a 1 is
                    selected then a wall is spawned and the wall counter increases by 1. If a 0 is selected then that
                    spot is a floor.

                    The wall counter is used to make sure that no row has more than 5 walls, this is to ensure that the
                    maze is completable with the help of teleportation. The enemy counter is used to ensure that each
                    row only has 3 enemies max so that the maze isn't too hard.

                    The algorithm also makes sure that the three spots in front of the exit and entrance are walkable
                    floor blocks or enemies so that the player has to defeat enemies to leave the dungeon they are in and
                    to ensure that they aren't blocked in by blocks as soon as they spawn in.
                 */

                for (int i = 1; i < 18; i++) { // this outer loop goes through every row in the maze
                    if (i == 1) { // if it is the first row, then all the spots are made into walls except the middle, which is an exit
                        for (int w = 1; w < 18; w++) {
                            if (w == 8) {
                                myWriter.write("F");
                            } else {
                                myWriter.write("1");
                            }
                        }
                        myWriter.write("\n");
                    } else if (i == 17) { // if it is the last row, then all the spots are made into walls except the middle, which is the player
                        for (int w = 1; w < 18; w++) {
                            if (w == 8) {
                                myWriter.write("P");
                            } else {
                                myWriter.write("1");
                            }
                        }
                    } else {
                        myWriter.write("1"); // for each row it starts by spawning a wall so that the outside edges are always walls

                        for (int j = 2; j < 17; j++) { // nested loop goes through the columns in the row
                            if (i == 2 && (j == 7 || j == 8 || j == 9)) { // this spawns enemies in front of the exit
                                myWriter.write("E");
                                continue;
                            } else if (i == 16 && (j == 7 || j == 8 || j == 9)) { // this spawns floors in front of the player spawn
                                myWriter.write("0");
                                continue;
                            }

                            int randNum = rand.nextInt(upperBound);

                            if (enemyCounter == maxEnemies) { // if the max number of enemies is reached then the random number is set to the lower bound
                                randNum = rand.nextInt(lowerBound);
                                if (wallCounter == numWalls) { // if the max number of walls is reached then a floor is spawned in instead of a wall
                                    myWriter.write("0");
                                } else if (randNum == 1) { // spawns a wall and increments the wall counter
                                    wallCounter++;
                                    myWriter.write(String.valueOf(randNum));
                                } else {
                                    myWriter.write(String.valueOf(randNum));
                                }
                            } else {
                                if (randNum == 2) { // if a 2 is selected then an enemy is spawned and enemy counter is inccremented
                                    myWriter.write("E");
                                    enemyCounter++;
                                } else {
                                    randNum = rand.nextInt(lowerBound); // if it's not a 2 then the same algorithm as the previous statement is used
                                    if (wallCounter == numWalls) {
                                        myWriter.write("0");
                                    } else if (randNum == 1) {
                                        wallCounter++;
                                        myWriter.write(String.valueOf(randNum));
                                    } else {
                                        myWriter.write(String.valueOf(randNum));
                                    }
                                }
                            }
                        }
                        myWriter.write("1"); // the end of the row always has a wall and a new line
                        myWriter.write("\n");
                        enemyCounter = 0; // reset the counters for the next row
                        wallCounter = 0;
                    }
                }
                myWriter.close(); // close the file and save
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }
}
