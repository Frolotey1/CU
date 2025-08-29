package com.console.application;
import java.io.*;
import java.util.*;

public class Main {
    private static final String
            ESC = "\u001B",
            GREEN = ESC + "[32m",
            YELLOW = ESC + "[33m",
            RED = ESC + "[31m",
            RESET = ESC + "[0m";
    public static void main(String[] args) throws IOException {
        Console console = System.console();
        Scanner loginOrRegister = new Scanner(System.in);
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("Select for enter to system. Write 1 or 2: ");
        int choose = loginOrRegister.nextInt();
        if(choose == 1) {
            char[] password = console.readPassword("Write the password: ");
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
                System.out.println("Write the password: ");
                char[]password = console.readPassword();
                try(BufferedWriter write = new BufferedWriter(new FileWriter(saveThePassword))) {
                    write.write(password);
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
        System.out.println("Which language do you want to use for working with ConsoleUtility. Write number 1 or 2: ");
        System.out.println("1. Deutsch | 2. English");
        int chooseLanguage = includeLanguage.nextInt();
        switch(chooseLanguage) {
            case 1 -> System.out.println("Ein weg zu Console Utility's deutsch version: "
                    + GREEN + "src\\KonsoleDienstProgramm.java" + RESET + // deutsch version fur konsole dienstprogramm
                    YELLOW + "\nFur eines einfuhrung mit deutsches befehlen - schreiben --helfen oder --hel / fur befehlens syntax: --tldr oder --tld\n" + RESET);
            case 2 -> System.out.println("The path to Console Utility's english version: "
                    + GREEN + "src\\ConsoleUtilityItself.java" + RESET +  // english version for console utility
                    YELLOW + "\nFor familiarization with english commands - write --help or --h / for syntax of commands: --tldr or --tl" + RESET);
            default -> System.err.println(RED + "This language is not supported in the console utility" + RESET);
        }
    }
}
