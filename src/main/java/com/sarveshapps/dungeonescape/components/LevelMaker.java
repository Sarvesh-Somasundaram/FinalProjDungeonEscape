package com.sarveshapps.dungeonescape.components;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class LevelMaker {

    public static void LevelGen() {
        Random rand = new Random();

        int upperBound = 3;
        int lowerBound = 2;
        int maxEnemies = 3;
        int enemyCounter = 0;
        int numWalls = 5;
        int wallCounter = 0;

        for (int k = 0; k < 5; k++) {
            File f = new File("src" + File.separator + "main" + File.separator + "resources" + File.separator +
                    "assets" + File.separator + "levels" + File.separator + "levels" + k + ".txt");

            try {
                if (f.createNewFile()) {
                    System.out.println("File created: " + f.getName());
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }


            try {
                FileWriter myWriter = new FileWriter(f);
                for (int i = 1; i < 18; i++) {
                    if (i == 1) {
                        for (int w = 1; w < 18; w++) {
                            if (w == 8) {
                                myWriter.write("F");
                            } else {
                                myWriter.write("1");
                            }
                        }
                        myWriter.write("\n");
                    } else if (i == 17) {
                        for (int w = 1; w < 18; w++) {
                            if (w == 8) {
                                myWriter.write("P");
                            } else {
                                myWriter.write("1");
                            }
                        }
                    } else {
                        myWriter.write("1");

                        for (int j = 2; j < 17; j++) {
                            if (i == 2 && (j == 7 || j == 8 || j == 9)) {
                                myWriter.write("E");
                                continue;
                            } else if (i == 16 && (j == 7 || j == 8 || j == 9)) {
                                myWriter.write("0");
                                continue;
                            }

                            int randNum = rand.nextInt(upperBound);

                            if (enemyCounter == maxEnemies) {
                                randNum = rand.nextInt(lowerBound);
                                if (wallCounter == numWalls) {
                                    myWriter.write("0");
                                } else if (randNum == 1) {
                                    wallCounter++;
                                    myWriter.write(String.valueOf(randNum));
                                } else {
                                    myWriter.write(String.valueOf(randNum));
                                }
                            } else {
                                if (randNum == 2) {
                                    myWriter.write("E");
                                    enemyCounter++;
                                } else {
                                    randNum = rand.nextInt(lowerBound);
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
                        myWriter.write("1");
                        myWriter.write("\n");
                        enemyCounter = 0;
                        wallCounter = 0;
                    }
                }
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }

    public static void BossLevelGen() {

    }
}
