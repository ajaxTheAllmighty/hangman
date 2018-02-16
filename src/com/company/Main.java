package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.Date;
import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.System.exit;

public class Main {

    private static String word;
    private static int count = 0;
    private static String asterisk;
    private static Date date = new Date();
    private static File log = new File("./log");
    private static File wordFile = new File("./words");

    public static void main(String[] args) throws IOException {
        showHistory();
        Scanner choice = new Scanner(System.in);
        System.out.println("To play HANGMAN press y");
        if (choice.next().equals("y")){
            Scanner sc = new Scanner(System.in);
            Scanner fileScan = new Scanner(wordFile);
            String input = "";                          //========================
            while (fileScan.hasNextLine()) {            //
                input += fileScan.next();               //Loading words from file
                input += "\n";                          //
            }                                           //
            String[] words = input.split("\n");  //Separating and getting rid of enters
            int randomNum = ThreadLocalRandom.current().nextInt(0, words.length);       //Select random word
            word = words[randomNum];                                                           //
            asterisk = new String(new char[word.length()]).replace("\0", "*");//Create same size word which contains *s
            System.out.println(word);
            System.out.println(asterisk);
            while (count < 10 && asterisk.contains("*")) {      //Still attempts remaining and word is not yet guessed
                System.out.println("Guess any letter in the word");
                System.out.println(asterisk);
                String guess = sc.next();
                hang(guess);
            }
            Files.write(Paths.get("./log"),(date.toString() + " "+ asterisk + " LOST"+"\n\r").getBytes(), StandardOpenOption.APPEND);//Save history
            System.out.println("You lose! You get nothing! Good day sir!");
        }
        else exit(0);
    }

    static void hang(String guess){
        String newasterisk = "";        //For checking which letters are already guessed
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == guess.charAt(0)) {    //entered letter is in our word
                newasterisk += guess.charAt(0);         //
            } else if (asterisk.charAt(i) != '*') {     //current letter already guessed
                newasterisk += word.charAt(i);          //
            } else {                                    //not guessed
                newasterisk += "*";
            }
        }

        if (asterisk.equals(newasterisk)) {             //no letters were guessed this turn
            count++;                                    //next turn
            System.out.println("Wrong guess! Attempts remaining " + (10 - count) + " ");
        } else {
            asterisk = newasterisk;                     //something was guessed
        }
        if (asterisk.equals(word)) {                    //whole word guessed
            System.out.println("Correct! You win! The word was " + word);
            try {
                Files.write(Paths.get("./log"), (date.toString() + " " + word + " Won" + "\n\r").getBytes(), StandardOpenOption.APPEND); //Save history
            }
            catch (IOException e){
                System.out.println("Failed to write history:\n\r");
                e.printStackTrace();
            }
                finally{
                exit(0);
            }
        }
    }
    static void showHistory() throws IOException{
        Scanner scanner = new Scanner(log);
        System.out.println("Play history:");
        while (scanner.hasNextLine()){
            System.out.println(scanner.nextLine());
        }
    }
}