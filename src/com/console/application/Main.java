package com.console.application;
import java.io.*;
import java.time.LocalTime;
import java.util.*;

public class Main {
    private static final String
            ESC = "\u001B",
            GREEN = ESC + "[32m",
            YELLOW = ESC + "[33m",
            RED = ESC + "[31m",
            PURPLE = ESC + "[35m",
            RESET = ESC + "[0m";
    public static void main(String[] args) throws IOException {
        try(BufferedWriter writeTheTime = new BufferedWriter(new FileWriter("UtilityStatistic.txt"))) {
            writeTheTime.write(LocalTime.now().getHour() + " " + LocalTime.now().getMinute() + " " + LocalTime.now().getSecond());
        }
        Console console = System.console();
        Scanner loginOrRegister = new Scanner(System.in);
        System.out.println(PURPLE + "Select for enter to system. Write 1 or 2: " + RESET);
        System.out.println(PURPLE + "1. Login" + RESET);
        System.out.println(PURPLE + "2. Register" + RESET);
        int choose = loginOrRegister.nextInt();
        if(choose == 1) {
            char[] password = console.readPassword(PURPLE + "Write the password: " + RESET);
            try(BufferedReader read = new BufferedReader(new FileReader("PasswordManager.txt"))) {
                if(Arrays.equals(password, read.readLine().toCharArray())) {
                    System.out.println(GREEN + "You completed the system. Welcome to Console Utility" + RESET);
                } else {
                    System.err.println(RED + "This password is false" + RESET);
                    System.exit(0);
                }
            }
        } else if(choose == 2) {
            File saveThePassword = new File("PasswordManager.txt");
            if(console != null) {
                System.out.println(PURPLE + "Write the password: " + RESET);
                char[]password = console.readPassword();
                try(BufferedWriter write = new BufferedWriter(new FileWriter(saveThePassword))) {
                    write.write(password);
                    System.out.println(GREEN + "Your password is created and saved. Welcome to Console Utility" + RESET);
                } catch (IOException e) {
                    System.err.println(RED + "This is error for writing the password." + RESET);
                }
            } else {
                System.err.println(RED + "Console doesn't support the IDE, which you use" + RESET);
            }
        } else {
            System.err.println(RED + "This variant doesn't exist" + RESET);
            System.exit(0);
        }
        Scanner includeLanguage = new Scanner(System.in);
        System.out.println(PURPLE + "Which language do you want to use for working with ConsoleUtility. Write number 1 or 2: " + RESET);
        System.out.println(YELLOW + "1. Deutsch | 2. English" + RESET);
        int chooseLanguage = includeLanguage.nextInt();
        switch(chooseLanguage) {
            case 1 -> System.out.println(PURPLE + "Ein weg zu Console Utility's deutsch version: " + RESET +
                    GREEN + "src\\KonsoleDienstProgramm.java" + RESET + // deutsch version fur konsole dienstprogramm
                    YELLOW + "\nFur eines einfuhrung mit deutsches befehlen - schreiben --helfen oder --hel / fur syntax: --tldr oder --tld. Wenn schreibst du nichts, Dienst Programm will zeigt dir alle befehlen fur benutzerung\n" + RESET);
            case 2 -> System.out.println(PURPLE + "The path to Console Utility's english version: " + RESET
                    + GREEN + "src\\ConsoleUtilityItself.java" + RESET +  // english version for console utility
                    YELLOW + "\nFor familiarization with english commands - write --help or --h / for syntax: --tldr or --tl. Or if you don't write anything. Utility'll show you the list of commands for using for special prompt" + RESET);
            default -> System.err.println(RED + "This language is not supported in the console utility" + RESET);
        }
    }
}
