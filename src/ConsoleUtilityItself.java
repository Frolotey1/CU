import javax.management.RuntimeOperationsException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.lang.management.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.security.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.jar.*;
import java.util.stream.Stream;
import java.util.zip.*;
import javax.xml.stream.*;

public class ConsoleUtilityItself {
    private static final
    Path HistoricalFile = Path.of(System.getProperty("user.home"), ".consoleutility_history"),
            DirectoryFile = Paths.get("Directory.txt");
    private static void appendPathOfDirectory(String directoryName) throws IOException {
        Files.writeString(DirectoryFile, directoryName + System.lineSeparator(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
    private static ArrayList<String> loadPathsOfDirectory() throws IOException {
        return new ArrayList<>(Files.readAllLines(DirectoryFile));
    }
    private static List<String> loadHistory() throws IOException {
        if (Files.exists(HistoricalFile)) {
            return new ArrayList<>(Files.readAllLines(HistoricalFile));
        }
        return new ArrayList<>();
    }
    private static void appendHistory(String command) throws IOException {
        Files.writeString(HistoricalFile, command + System.lineSeparator(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
    private static final String
            ESC = "\u001B",
            GREEN = ESC + "[32m",
            RED = ESC + "[31m",
            YELLOW = ESC + "[33m",
            RESET = ESC + "[0m";
    public static void main(String[] args) throws IOException, XMLStreamException {
        List<String> rights = new ArrayList<>(List.of(
                "---", "--x", "-w-", "-wx", "r--", "r-x", "rw-", "rwx"));
        List<Character> numberRights = new ArrayList<>();
        List<String> resultRights = new ArrayList<>();
        List<String> historyOFCommands = loadHistory();
        String isLogoutValue = "";
        try(BufferedReader readisLogout = new BufferedReader(new FileReader("IsLogout.txt"))) {
            isLogoutValue = readisLogout.readLine();
        } catch (IOException exc) {
            throw new RuntimeException(exc.getLocalizedMessage());
        }
        int index = historyOFCommands.size(), indexPath = loadPathsOfDirectory().size();
        File file;
        String directory;
        Scanner operation = new Scanner(System.in);
        if (args.length == 0) {
            System.out.println(YELLOW + "Maybe you wanted to put: " + RESET);
            List<String> prompt = new ArrayList<>(List.of(
                    "--help or --hp", "--add or --ad",
                    "--read or --rd", "--delete or --dt", "--copy or --cp",
                    "--move or --mv", "--newname or --nn",
                    "--stopgap or --sg", "--GUI or --gi", "--jar or --jr",
                    "--zip or -zp (Windows) / --tar.gz or -tr (Linux)",
                    "--write or --wt", "--grep or --gp",
                    "--history or --hi", "--find or --fd",
                    "--lstcat or --ls", "--replace or --re", "--crtdir or --cr",
                    "--candir or --ca", "--exstdirs or --ex",
                    "--tldr or --tl", "--chgrits or --cs",
                    "--chgextn or --cx", "--symlink or --sl",
                    "--empty or --em", "--sort or --st",
                    "--reverse or --rv", "--remall or --ra",
                    "--remove or --rm", "--integrate or --ig",
                    "--sizfls or --sf", "--edit or --et", "--symcnt or --sc",
                    "--resize or --rs", "--version or --vs",
                    "--backup,--bp", "--xexport,--xp", "--ximport or --xm",
                    "--restore or --rt", "--stats or --ss", "--search or --sh",
                    "--hostinfo or --ho", "--shutdown or sd", "--restart or --rr", "--fmem or --fm",
                    "--clean or --cn", "--ping or --pg", "--intproc or --ip", "--interrupt or --ir",
                    "--filter or --fr", "--md5gen or --mg", "--sha256gen or --sn", "--freeze or --fe",
                    "--unique or --uq", "--stat or --sa", "--split or --sp", "--rsync or --rc",
                    "----cmp or --cp", "--sysinfo or --si", "--recent or --rn", "--active or --ae",
                    "--username or --un", "--preview or --pw", "--cut or --ct", "--chgpass or --ps",
                    "--logout or --lg","--fcmd or --fc","--gzip or --gz","--fdir or --fi",
                    "--erase or --es","--mirror or --mr","--update or --ue"
            ));
            for (String all : prompt) {
                System.out.println(all);
            }
        }
        for (String arg : args) {
            final Path fromFile = Path.of("Directory.txt");
            final Path source = Path.of("ReserveCopy.bin");
            if(Objects.equals(isLogoutValue, "true")) {
                System.err.println(RED + "You can't perform this command. You logged out" + RESET);
                System.exit(0);
            }
            switch (arg) {
                case "--help", "--hp" -> {
                    appendHistory((index++) + " | " + arg);
                    allCommands();
                }
                case "--add", "--ad" -> {
                    appendHistory((index++) + " | " + arg);
                    System.out.println("Write the name for file: ");
                    String nameFile = operation.nextLine();
                    String text = "";
                    Files.writeString(Path.of(nameFile), text + System.lineSeparator(),
                            StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                    System.out.println(GREEN + "File was added successfully" + RESET);
                }
                case "--read", "--rd" -> {
                    appendHistory((index++) + " | " + arg);
                    System.out.println("Write the filename for checking his existence: ");
                    String nameFile = operation.nextLine();
                    Path path = Path.of(nameFile);
                    if (Files.exists(path)) {
                        List<String> readStrings = new ArrayList<>(Files.readAllLines(path));
                        readStrings.forEach(System.out::println);
                        readStrings.clear();
                    } else {
                        System.err.println(RED + "This file doesn't exist" + RESET);
                    }
                }
                case "--delete", "--de" -> {
                    appendHistory((index++) + " | " + arg);
                    System.out.println("Write the name of the file for checking existence: ");
                    String nameFile = operation.nextLine();
                    file = new File(nameFile);
                    Files.deleteIfExists(file.toPath());
                    System.out.println(GREEN + "This file: " + file.toPath() + " was deleted successfully" + RESET);
                }
                case "--copy", "--cp" -> {
                    appendHistory((index++) + " | " + arg);
                    System.out.println("Write the name of the file for checking existence: ");
                    String nameFile = operation.nextLine();
                    System.out.println("Write the name of the new file, which will added the information from the past file in: ");
                    String newFile = operation.nextLine();
                    file = new File(nameFile);
                    if (Files.exists(file.toPath())) {
                        System.out.println(GREEN + "Copy from one file to other file is successfully: " + RESET);
                        Files.copy(Path.of(newFile), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        System.err.println(RED + "This file doesn't exist" + RESET);
                    }
                }
                case "--move", "--mv" -> {
                    appendHistory((index++) + " | " + arg);
                    System.out.println("Write the name of the file for checking existence: ");
                    String nameFile = operation.nextLine();
                    System.out.println("Write the new disk: ");
                    char newDisk = operation.nextLine().charAt(0);
                    file = new File(nameFile);
                    if (Files.exists(file.toPath())) {
                        if (newDisk == 'C') {
                            Files.move(file.toPath(), Path.of(newDisk + ":\\", file.getName()), StandardCopyOption.REPLACE_EXISTING);
                        } else if (newDisk == '/') {
                            Files.move(file.toPath(), Path.of(newDisk + "home/", file.getName()), StandardCopyOption.REPLACE_EXISTING);
                        } else {
                            System.err.println(RED + "This type of disk doesn't exist" + RESET);
                            System.exit(0);
                        }
                        System.out.println(GREEN + "This file was moved to other disk successfully" + RESET);
                    }
                }
                case "--newname", "--nn" -> {
                    appendHistory((index++) + " | " + arg);
                    System.out.println("Write the file, which you want to rename: ");
                    String theFile = operation.nextLine();
                    file = new File(theFile);
                    if (Files.exists(file.toPath())) {
                        System.out.println("Write the new name for the file: ");
                        String newName = operation.nextLine();
                        String[] saveTheData = new String[2];
                        saveTheData[0] = file.getName();
                        try (BufferedReader readTheDataFromFile = new BufferedReader(new FileReader(file))) {
                            saveTheData[1] = readTheDataFromFile.readLine();
                            saveTheData[0] = newName;
                        }
                        Files.delete(file.toPath());
                        file = new File(saveTheData[0]);
                        try (BufferedWriter writeTheDataFromThePastFile = new BufferedWriter(new FileWriter(file))) {
                            writeTheDataFromThePastFile.write(saveTheData[1]);
                        } catch (IOException e) {
                            System.err.println(RED + "This is the error of the input/output operations" + RESET);
                        }
                    }
                }
                case "--stopgap", "--sg" -> {
                    appendHistory((index++) + " | " + arg);
                    System.out.println("Write the name for the stopgap file: ");
                    String stopGapNameForFile = operation.nextLine();
                    System.out.println("Write the extension for the file: ");
                    String extension = operation.nextLine();
                    try {
                        Path stopGapPath  = Files.createTempFile(stopGapNameForFile, extension);
                        System.out.println(GREEN + "The stopgap file was created successfully: " +
                                stopGapPath.toAbsolutePath() + RESET);
                        Files.writeString(stopGapPath, "The stopgap file was created successfully | " + LocalDateTime.now());
                    } catch (FileSystemException e) {
                        throw new RuntimeException(e.getLocalizedMessage());
                    }
                }
                case "--GUI", "--gi" -> {
                    appendHistory((index++) + " | " + arg);
                    System.out.println(new ConsoleUtilitysGUI());
                }
                case "--jar", "--jr", "--zip", "--zp", "--tar.gz", "--tr" -> {
                    appendHistory((index++) + " | " + arg);
                    String name;
                    if (Objects.equals(arg, "--jar") || Objects.equals(arg, "--j")) {
                        System.out.println("Write the name for jar file: ");
                        String fileName = operation.nextLine();
                        String jarName = "";
                        if (Files.exists(Path.of(fileName))) {
                            name = fileName;
                            System.out.println("Write the file which you want to include to the jar: ");
                            jarName = operation.nextLine();
                        } else {
                            System.out.println("This jar file doesn't exist. Create the new: ");
                            name = operation.nextLine();
                        }
                        try (JarOutputStream toTheFile = new JarOutputStream(new FileOutputStream(name))) {
                            JarEntry entry = new JarEntry(jarName);
                            toTheFile.putNextEntry(entry);
                            toTheFile.setComment("Time of the creation jar: " + LocalDateTime.now());
                            System.out.println(GREEN + "Jar file was created successfully: " + new File(name).getAbsolutePath() + RESET);
                            toTheFile.closeEntry();
                        }
                    } else {
                        System.out.println("Write the name for zip (or tar.gz for Linux) file: ");
                        String fileName = operation.nextLine();
                        String zipName = "";
                        if (Files.exists(Path.of(fileName))) {
                            name = fileName;
                            System.out.println("Write the file which you want to include to the zip (or tar.gz): ");
                            zipName = operation.nextLine();
                        } else {
                            System.out.println("This zip (tar.gz) file doesn't exist. Create the new: ");
                            name = operation.nextLine();
                        }
                        try (ZipOutputStream toTheFile = new ZipOutputStream(new FileOutputStream(name))) {
                            ZipEntry entry = new ZipEntry(zipName);
                            toTheFile.putNextEntry(entry);
                            toTheFile.setComment("Time of the creation zip: " + LocalDateTime.now());
                            System.out.println(GREEN + "Zip file was created successfully: " + new File(name).getAbsolutePath() + RESET);
                            toTheFile.closeEntry();
                        }
                    }
                }
                case "--write", "--wt" -> {
                    appendHistory((index++) + " | " + arg);
                    System.out.println("Write the text or data: ");
                    String data = operation.nextLine();
                    System.out.println("Write the filename where you want to write the data in: ");
                    String filename = operation.nextLine();
                    String name;
                    if (Files.exists(Path.of(filename))) {
                        name = filename;
                    } else {
                        System.out.println("This file doesn't exist. Create the new: ");
                        name = operation.nextLine();
                    }
                    Files.writeString(Path.of(name), data + System.lineSeparator(),
                            StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                }
                case "--grep", "--gp" -> {
                    appendHistory((index++) + " | " + arg);
                    ArrayList<String> saveTheTokens = new ArrayList<>();
                    int countTheWords;
                    StringTokenizer token;
                    System.out.println("Write the file or directory: ");
                    String fileOrDirectory = operation.nextLine();
                    String finalName, data;
                    Path start = Path.of(fileOrDirectory);
                    if (new File(fileOrDirectory).isDirectory()) {
                        System.out.println("Write the name of file or extension for him: ");
                        finalName = operation.nextLine();
                        List<Path> allFiles;
                        try (Stream<Path> streamPath = Files.walk(start)) {
                            allFiles = streamPath.filter(comparePath -> String.valueOf(comparePath).endsWith(finalName)).toList();
                        } catch (IOException exc) {
                            throw new RuntimeException(exc.getLocalizedMessage());
                        }
                        for (Path addAllPaths : allFiles) {
                            saveTheTokens.add(addAllPaths.toString());
                        }
                        for (String findGrepElem : saveTheTokens) {
                            if (Objects.equals(findGrepElem, finalName)) {
                                saveTheTokens.set(saveTheTokens.indexOf(findGrepElem), RED + findGrepElem + RESET);
                            }
                        }
                        saveTheTokens.forEach(System.out::println);
                        saveTheTokens.clear();
                    } else if (new File(fileOrDirectory).isFile()) {
                        if (Files.exists(start)) {
                            finalName = fileOrDirectory;
                        } else {
                            System.out.println("This file doesn't exist. Create the new");
                            finalName = operation.nextLine();
                            System.out.println("Write the text in the file: ");
                            data = operation.nextLine();
                            try (BufferedWriter writeTo = new BufferedWriter(new FileWriter(finalName))) {
                                writeTo.write(data);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex.getLocalizedMessage());
                            }
                        }
                        System.out.println("Write the word or text which you want to become from the file: ");
                        String text = operation.nextLine();
                        String textFrom;
                        try (BufferedReader readFrom = new BufferedReader(new FileReader(finalName))) {
                            textFrom = readFrom.readLine();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex.getLocalizedMessage());
                        }
                        token = new StringTokenizer(textFrom);
                        while (token.hasMoreTokens()) {
                            saveTheTokens.add(token.nextToken());
                        }
                        countTheWords = (int) saveTheTokens.stream().filter(object -> Objects.equals(object, text)).count();
                        for (int findText = 0; findText < saveTheTokens.size(); ++findText) {
                            if (Objects.equals(saveTheTokens.get(findText), text)) {
                                saveTheTokens.set(findText, RED + text + RESET);
                            }
                        }
                        saveTheTokens.add("| " + countTheWords);
                        saveTheTokens.forEach(System.out::println);
                        saveTheTokens.clear();
                    } else {
                        System.err.println(RED + "Error naming file or directory" + RESET);
                    }
                }
                case "--history", "--hi" -> {
                    appendHistory((index++) + " | " + arg);
                    System.out.println("Write your username: ");
                    String username = operation.nextLine();
                    if (Objects.equals(Files.readString(Path.of("Username.txt")), username)) {
                        for (int readAllCommands = 1; readAllCommands < loadHistory().size(); ++readAllCommands) {
                            System.out.println(loadHistory().get(readAllCommands));
                        }
                    } else {
                        System.err.println(RED + "This username doesn't exist" + RESET);
                    }
                }
                case "--find", "--fd" -> {
                    appendHistory((index++) + " | " + arg);
                    System.out.println("Write the directory where you want to find the files: ");
                    directory = operation.nextLine();
                    if (!directory.startsWith("C:\\") && !directory.startsWith("/")) {
                        System.err.println("Directories must be started with C:\\ (for Windows) or / (for Linux)");
                    } else {
                        Path analysis = Path.of(directory);
                        System.out.println("Write the extension of the files for searching: ");
                        String extension = operation.nextLine();
                        if (!extension.startsWith(".")) {
                            System.err.println(RED + "Extensions must be started with '.'" + RESET);
                        } else {
                            try (Stream<Path> paths = Files.walk(analysis)) {
                                paths.filter(Files::isRegularFile).filter(isTxt -> isTxt.toString().endsWith(extension) &&
                                        !Objects.equals(isTxt.toString(), new File("HistoryFile.txt").getAbsolutePath())
                                        && !Objects.equals(isTxt.toString(), new File("PasswordManager.txt").getAbsolutePath())).forEach(System.out::println);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex.getLocalizedMessage());
                            }
                        }
                    }
                }
                case "--lstcat", "--ls" -> {
                    appendHistory((index) + " | " + arg);
                    System.out.println("Write the directory where want you to see the catalogs: ");
                    directory = operation.nextLine();
                    if (!directory.startsWith("C:\\") && !directory.startsWith("/")) {
                        System.err.println("The directories must started with C:\\ (for Windows) or / (for Linux}: ");
                    } else {
                        try (Stream<Path> paths = Files.walk(Path.of(directory))) {
                            paths.forEach(System.out::println);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex.getLocalizedMessage());
                        }
                    }
                }
                case "--replace", "--re" -> {
                    appendHistory((index) + " | " + arg);
                    System.out.println("Write the file where will you change the chars in: ");
                    String filename = operation.nextLine();
                    String name, data = "", firstChar, secondChar, newData;
                    if (Files.exists(Path.of(filename))) {
                        name = filename;
                    } else {
                        System.out.println("This file doesn't exist. Create the new: ");
                        name = operation.nextLine();
                    }
                    System.out.println("Write the first char which you want to change: ");
                    firstChar = operation.nextLine();
                    System.out.println("Write the second char for changing: ");
                    secondChar = operation.nextLine();
                    try (BufferedReader readFrom = new BufferedReader(new FileReader(name))) {
                        String line = readFrom.readLine();
                        if (line.isEmpty()) {
                            System.err.println(RED + "There is not string for replacing" + RESET);
                        } else {
                            data = line;
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex.getLocalizedMessage());
                    }
                    char first = firstChar.charAt(0), second = secondChar.charAt(0);
                    newData = data.replace(first, second);
                    System.out.println(GREEN + newData + RESET);
                    try (BufferedWriter writeNewData = new BufferedWriter(new FileWriter(name))) {
                        writeNewData.write(newData);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex.getLocalizedMessage());
                    }
                }
                case "--crtdir", "--cr" -> {
                    appendHistory((index) + " | " + arg);
                    System.out.println("Write the name for the directory: ");
                    directory = operation.nextLine();
                    System.out.println("Write the main directory: ");
                    String mainDirectory = operation.nextLine();
                    if (!mainDirectory.startsWith("C:\\") && !mainDirectory.startsWith("/")) {
                        System.err.println(RED + "The directories must started with C:\\ (for Windows) or / (for Linux}: " + RESET);
                    } else {
                        if (!mainDirectory.endsWith("/")) {
                            mainDirectory = mainDirectory + "/";
                        }
                        String createDir = mainDirectory + directory;
                        appendPathOfDirectory(indexPath + " | " + createDir);
                        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxr-xr-x");
                        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
                        Path path = Path.of(createDir);
                        Files.createDirectory(path, attr);
                        System.out.println(GREEN + "The directory was created: " + path.toAbsolutePath() + RESET);
                    }
                }
                case "--candir", "--ca" -> {
                    appendHistory((index++) + " | " + arg);
                    System.out.println("Write the directory which you want to delete: ");
                    String findDirectory = operation.nextLine();
                    if (!findDirectory.startsWith("C:\\") && !findDirectory.startsWith("/")) {
                        System.err.println(RED + "The directories must started with C:\\ (for Windows) or / (for Linux}: " + RESET);
                    } else {
                        Path path = Path.of(findDirectory);
                        Files.deleteIfExists(path);
                        if (!Files.isDirectory(path)) {
                            List<String> removeTheDirectory = new ArrayList<>(Files.readAllLines(fromFile));
                            removeTheDirectory.remove(findDirectory);
                            Files.delete(fromFile);
                            for (String rewriteAllDirectories : removeTheDirectory) {
                                appendPathOfDirectory(rewriteAllDirectories);
                            }
                            System.out.println(GREEN + "The directory '" + findDirectory + "' was deleted successfully" + RESET);
                        } else {
                            System.out.println("The directory '" + findDirectory + "' exists yet");
                        }
                    }
                }
                case "--tldr", "--tl" -> {
                    appendHistory(index + " | " + arg);
                    String[] allCommandsInstruction =
                            {"",
                                    "add", "read", "delete", "copy",
                                    "move", "newname", "stopgap",
                                    "GUI", "jar", "zip / tar.gz", "write",
                                    "grep", "history", "find", "lstcat",
                                    "replace", "crtdir", "candir", "exstdirs",
                                    "chgrits", "chgextn", "symlink", "empty",
                                    "sort", "reverse", "remall", "remove",
                                    "integrate", "sizfls", "edit", "symcnt",
                                    "resize", "version", "backup", "xexport",
                                    "ximport", "restore", "stats", "search",
                                    "hostinfo", "shutdown", "restart", "fmem",
                                    "clean", "ping", "intproc",
                                    "interrupt", "filter", "md5gen", "sha256gen",
                                    "freeze", "unique", "stat", "split", "rsync",
                                    "cmp", "sysinfo", "recent", "active",
                                    "username", "preview", "cut", "chgpass",
                                    "logout","--fcmd or --fc","--gzip or --gz",
                                    "--fdir or --fi", "--erase or --es"
                            };
                    for (int i = 1; i < allCommandsInstruction.length; ++i) {
                        System.out.println(i + ") " + allCommandsInstruction[i]);
                    }
                    System.out.println("Write the command (1,2,3,4....) for which you want to see the instruction how use: ");
                    int commandChoose = operation.nextInt();
                    switch (commandChoose) {
                        case 1 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--add / --ad [ENTER] -> " +
                                "Write the name for file: -> (name for your file. Example: test.txt or another) -> [ENTER] ->" +
                                "Write the text for file: -> (text for your file. Example: Hello_World) -> [ENTER]" +
                                "{IF SUCCESS} -> text Hello_World was written in the test.txt");
                        case 2 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--read / --rd [ENTER] -> " +
                                "Write the dateiName fur checking his existence: " +
                                "-> (your file which you created with --add) [ENTER] -> " +
                                "{IF SUCCESS} -> <EXAMPLE> Hello_World (text from test.txt was read)");
                        case 3 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--delete / --dt [ENTER] -> " +
                                "Write the name of the file for checking existence: " +
                                "-> (your file which you created with --add) [ENTER] -> " +
                                "{IF SUCCESS} -> <MESSAGE> 'This file: (you file) was deleted successfully'");
                        case 4 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--copy / --cp [ENTER] -> " +
                                "Write the name of the file for checking existence: " +
                                "-> (your file which you created with --add) [ENTER] -> " +
                                "Write the name of the new file, which will added the information from the past file in: " +
                                "-> (the name for the new file where will saved your information (Example: copy.txt) [ENTER] -> " +
                                "{IF SUCCESS} -> <MESSAGE> Copy from one file to other file is successfully: (path to the new file for copy)");
                        case 5 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--move / --mv [ENTER] -> " +
                                "Write the name of the file for checking existence: " +
                                "-> (your file which you created with --add) [ENTER] -> " +
                                "Write the new disk: -> (system disk where you want to save your file or share) -> [ENTER] -> " +
                                "{IF SUCCESS} -> <MESSAGE> This file was moved to other disk successfully");
                        case 6 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--newname / --nn [ENTER] -> " +
                                "Write the file, which you want to rename: " +
                                "-> (your file which you created with --add) [ENTER] -> " +
                                "Write the new name for the file: -> (new name for your file) -> [ENTER] -> " +
                                "{IF SUCCESS} -> (your file got the new name)");
                        case 7 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--stopgap / --sg [ENTER] -> " +
                                "Write the name for the stopgap file: -> (name for the temp file) -> [ENTER] -> " +
                                "Write the extension for the file: -> (extension for your file. Example: .txt,.bin and etc.) -> [ENTER} " +
                                "-> <MESSAGE> 'The stopgap file was created successfully: (path to your temp file)'");
                        case 8 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--GUI / --gi [ENTER] -> (GUI's version for ConsoleUtility. Work in Windows and Linux)");
                        case 9 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--jar / --jr [ENTER] -> " +
                                "Write the name for jar file: -> (name for the jar file. Example: java.jar) -> [ENTER] -> " +
                                "{IF JAR FILE EXISTS} -> Write the file which you want to include to the jar: " +
                                "(your file. Example: test.txt) {ELSE} This jar file doesn't exist. Create the new: " +
                                "-> (the new jar file) -> [ENTER] -> " +
                                "<MESSAGE> Jar file was created successfully: (path to your jar file)");
                        case 10 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--zip,--zp (for Windows) / --tar.gz,--tr [ENTER] -> " +
                                "Write the name for zip (or tar.gz for Linux) file: -> [ENTER] -> " +
                                "Write the file which you want to include to the zip (or tar.gz): -> (your file. Example: test.txt) " +
                                "-> [ENTER] -> {IF YOUR FILE EXISTS} -> <MESSAGE> Zip file was created successfully: (path to your zip file) -> " +
                                "{ELSE} This zip (tar.gz) file doesn't exist. Create the new: -> (your file with zip,tar.gz)");
                        case 11 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--write,--wt -> [ENTER] -> " +
                                "Write the text or data: -> (your text) -> [ENTER] -> " +
                                "Write the filename where you want to write the data in: -> (name for your file) -> [ENTER] -> " +
                                "{IF EXISTS} -> (your text was written to the file) {ELSE} -> This file doesn't exist. Create the new: ");
                        case 12 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--grep,--gp -> [ENTER] -> " +
                                "Write the file where you want to get the text from: -> (your file. Example: test.txt) -> [ENTER] -> " +
                                "{IF EXISTS} -> Write the word or text which you want to become from the file: (word from your text) {ELSE} -> " +
                                "This file doesn't exist. Create the new -> (create the new file) -> [ENTER] -> Write the text in the file: -> " +
                                "(text to the file)");
                        case 13 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--history / --hi -> [ENTER] -> (will showed the list of the commands which you used in the ConsoleUtility)");
                        case 14 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--find / --fd -> [ENTER] -> " +
                                "Write the directory where you want to find the files: -> (your directory) -> [ENTER] -> " +
                                "Write the extension of the files for searching: -> (your extension for files: Example: .txt,.bin) -> [ENTER] -> " +
                                "(will showed all files / catalogs with definite extension)");
                        case 15 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--lstcat / --ls -> [ENTER] -> " +
                                "Write the directory where want you to see the catalogs: -> (your directory) -> [ENTER] -> " +
                                "(will showed all the files / catalogs in the definite directory)");
                        case 16 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--replace / --re -> [ENTER] -> " +
                                "Write the file where will you change the chars in: -> (your file. Example: test.txt) -> [ENTER] -> " +
                                "{IF EXISTS} -> Write the first char which you want to change: (first char. Example: c) -> [ENTER] -> " +
                                "Write the second char for changing: -> (second char. Example: i) -> [ENTER] -> (will replaced first char to second char)");
                        case 17 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--crtdir / --cr -> [ENTER] -> " +
                                "Write the name for the directory: -> (your directory) -> [ENTER] -> " +
                                "Write the main directory: (your main directory) (Example: C:\\Users\\... or /home/...) -> [ENTER] -> " +
                                "<MESSAGE> 'The directory was created: (your created directory)'");
                        case 18 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--candir / --ca -> [ENTER] -> " +
                                "Write the directory which you want to delete: (your directory for deleting) -> [ENTER] -> " +
                                "The directory (your directory) was deleted successfully");
                        case 19 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--exstdirs / --ex -> [ENTER] -> " +
                                "(will showed all directories which were created with crtdir)");
                        case 20 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--chgrits / --cs -> [ENTER] -> " +
                                "Write the file which you want to change the rights for using this file in: -> (your file with name) -> [ENTER] -> " +
                                "Write the rights in the view of the octal system (Example: 700): (rights for your file. Example: 700) -> [ENTER] -> " +
                                "{IF SUCCESS} -> <MESSAGE> The rights for the file: '(name for your saving file)' were changed successfully");
                        case 21 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--chgextn / --cx -> [ENTER] -> " +
                                "Write your file without extension: (your file without extension) -> [ENTER] -> " +
                                "Write the extension for your file: (extension for your file) -> [ENTER] -> " +
                                "Write the new extension for your file: (new extension for your file) -> [ENTER] -> " +
                                "{IF SUCCESS} -> <MESSAGE> The file's extension was changed successfully");
                        case 22 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--symlink / --sl -> [ENTER] -> " +
                                "Write the directory for which you want to create the symbolic link: (your directory) -> [ENTER] -> " +
                                "Write the symbolic link: (symbolic link for your file) -> [ENTER] -> " +
                                "{IF SUCCESS} -> <MESSAGE> The symbolic link was created successfully: (path to your link directory)");
                        case 23 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--empty / --em -> [ENTER] -> " +
                                "Write the file which you want to delete the text from: (your file. Example: test.txt) -> [ENTER] -> " +
                                "{IF SUCCESS} -> <MESSAGE> Text from the file was deleted successfully");
                        case 24 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--sort / --st -> [ENTER] -> " +
                                "Write your file: -> (your file. Example: test.txt) " +
                                "Write your directory: -> (your directory) -> [ENTER]");
                        case 25 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--reverse / --rv -> [ENTER] -> " +
                                "Write the file which you want to read data from: (your file. Example: test.txt) -> [ENTER]");
                        case 26 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--remall / --ra -> [ENTER] -> " +
                                "Write your directory: -> (your directory) -> [ENTER] -> " +
                                "{IF SUCCESS} -> <MESSAGE> All catalogies and files in (your directory) were deleted successfully");
                        case 27 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--remove / --rm -> [ENTER] -> " +
                                "Write name of the file: -> (your file. Example: test.txt) -> [ENTER] -> " +
                                "Write your directory: -> (your directory) -> [ENTER] -> " +
                                "{IF SUCCESS} -> <MESSAGE> File (your file) was deleted from directory (your directory) successfully");
                        case 28 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--integrate / --ig -> [ENTER] -> " +
                                "Write your file: -> (your file. Example: test.txt) -> [ENTER] -> " +
                                "Write your directory: -> (your directory) -> [ENTER] -> " +
                                "{IF SUCCESS} -> <MESSAGE> File (your file) was added to directory (your directory) successfully");
                        case 29 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--sizfls / --sf -> [ENTER] ->  " +
                                "Write the directory (for analysis all files) or file: (your directory or file) -> [ENTER] -> " +
                                "{IF YOU WRITE DIRECTORY} -> (all files will analysed with size in bytes) " +
                                "<> {ELSE IF YOU WRITE PATH TO FILE} -> Write the path to your file: (your path to file) -> [ENTER] ->" +
                                "(the definite file was analysed with size in bytes)");
                        case 30 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--edit / --et -> [ENTER] -> " +
                                "(will shown the file in GUI version. You can write all what you want with possible save and open file itself)"
                        );
                        case 31 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--wrdcnt / --wc -> [ENTER] -> " +
                                "{IF SUCCESS} -> (will counted all symbols in rou date from your file)");
                        case 32 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--resize / --rs -> [ENTER] -> " +
                                "Write a name for your file: (name for your file) -> [ENTER] -> " +
                                "Write a new size for your file: (new size for your file) -> [ENTER] -> " +
                                "{IF SUCCESS} -> <MESSAGE> File size was created successfully");
                        case 33 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--version / --vs -> [ENTER] -> " +
                                "(The current version for your utility)");
                        case 34 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--backup / --bp -> [ENTER] -> " +
                                "Write your file which contains a data: " +
                                "{IF SUCCESS} <MESSAGE> To save data from the file ReserveCopy.bin file was created");
                        case 35 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--xexport / --xp -> [ENTER] -> " +
                                "Write a data: (your data) -> [ENTER] -> " +
                                "{IF SUCCESS} <MESSAGE> XML file with data was successfully created");
                        case 36 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--ximport / --xm -> [ENTER] -> " +
                                "(Will showed your data from XML file)");
                        case 37 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--restore / --rt -> [ENTER] -> " +
                                "<MESSAGE> (Data from XML to simple file were shared successfully)");
                        case 38 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--stats / --ss -> [ENTER] -> " +
                                "(Will showed your time of using this project)");
                        case 39 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--search / --sh -> [ENTER] -> " +
                                "Write which command you find: -> (command which you want to find) -> [ENTER] ->" +
                                "(Will showed the list of commands under your filtration");
                        case 40 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--hostinfo / --ho -> [ENTER] -> " +
                                "(data about your host settings)");
                        case 41 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--shutdown / --sd -> [ENTER] -> " +
                                "(your computer'll turn off and stop working)");
                        case 42 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--restart / --rr -> [ENTER] -> " +
                                "(your computer'll restart and update his working)");
                        case 43 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--fmem / --fm -> [ENTER] -> " +
                                "(utility'll show you the info about your free memory in bytes)");
                        case 44 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--clean / --cn -> [ENTER] -> " +
                                "(utility'll clean the history of commands which you used)");
                        case 45 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--ping / --pg -> [ENTER] -> " +
                                "Write the IP or DNS which you want to ping: (your IP or DNS) -> " +
                                "Write how many time you want to ping: (count times for pinging IP or DNS) -> " +
                                "{IF SUCCESS} -> (you'll get the information about pinging your IP or DNS)");
                        case 46 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--intproc / --ip -> [ENTER] -> " + "(you'll get the information about your IP)");
                        case 47 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--interrupt / --ir -> [ENTER] -> " + "Write the id of your process: (id of your process) -> " +
                                "(id of the definite process will interrupt and process will end his work)");
                        case 48 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--filter / --fr -> [ENTER] -> " + "Write the directory: (your directory) -> " +
                                "Write the size for filtering files in directory: (size of files in directory) -> " +
                                "Write the compare operator for searching files with definite pattern: (operator > < = !) -> " +
                                "{IF SUCCESS} -> (you can see the files with your definite pattern)");
                        case 50 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--md5gen / --mg -> [ENTER] -> " +
                                "Write your file for analysing data in md5 algorithm: (your file) -> [ENTER] -> " +
                                "(will show data from file in byte-hex with md5 algorithm");
                        case 51 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--sha256gen / --sn -> [ENTER] -> " +
                                "Write your file for analysing data in sha256 algorithm: (your file) -> [ENTER] -> " +
                                "(will show data from file in byte-hex with sha256 algorithm");
                        case 52 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--freeze / --fe -> [ENTER] -> " +
                                "Write the id of your process: (your id for process) -> [ENTER] -> " +
                                "(will freezed on time your process with definite id)");
                        case 53 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--unique / --uq -> [ENTER] -> " +
                                "Write your file: (your file) -> [ENTER] -> " +
                                "(will showed unique and sorted strings)");
                        case 54 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--stat / --sa -> [ENTER] -> " +
                                "Write the name for your file: (your file) -> [ENTER] -> " +
                                "(will show description about your file)");
                        case 55 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--split / --sp -> [ENTER] -> " +
                                "Write the name for your file: (your file) -> [ENTER] -> {IF SUCCESS} <MESSAGE> " +
                                "Spliting of the file (your file) is successful"
                        );
                        case 56 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--rsync / --rc -> [ENTER] -> " +
                                "Write your source file: (your source file) -> [ENTER] -> Write your destination file: (your destination file) -> [ENTER] -> " +
                                "{WENN SUCCESS} <MESSAGE> Byte data with size: (size for your file)  were synchronized to new file successfully");
                        case 57 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--cmp / --cm -> [ENTER] -> " +
                                "Write your first File: (your first file) -> [ENTER] -> Write your5 second file: (your second file) -> [ENTER] -> " +
                                "(will analyse the strings of data from your two files)"
                        );
                        case 58 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--sysinfo / --si -> [ENTER] -> " + "will show the information about system on user's computer");
                        case 59 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--recent / --rn -> [ENTER] -> " + "will show the information about recent enter by login or register in utility");
                        case 60 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--active / --ae -> [ENTER] -> " + "will show the information about user in system and his active from time of login or register in utility");
                        case 61 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--username / --un -> [ENTER] -> " + "will show the unique random nickname for user in system");
                        case 62 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--preview / --pw -> [ENTER] -> " + "will show the list of the future commands which will integrate in utility");
                        case 63 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--cut / --cut -> [ENTER] -> " + "(next you'll get the variants for working with cutting strings. Cutting strings with indexes or with delimiter)");
                        case 64 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--chgpass / --ps -> [ENTER] -> " + "Change your password: (your current password) -> {IF SUCCESS} -> Write new password (new password) -> {IF SUCCESS}" +
                                " -> <MESSAGE> Password was changed successfully");
                        case 65 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--logout / --lg -> [ENTER] -> " + "<MESSAGE> Deleting username and password are successful");
                        case 66 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--fcmd / --fc -> [ENTER] -> " + "Write the name of command from history: (name of command from history) -> (will showed the history of commands  by regex in utility)");
                        case 67 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--gzip / --gz -> [ENTER] -> " + "Write the file's name for reading data: (name of file for reading data) -> " +
                                "Select the option: 1. Compress file 2. Uncompress file (you can select one of two variants for working with data from file) -> " +
                                "{IF SUCCESS} -> Original size: (data's size) Compress file: (file's compress data)");
                        case 68 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--fdir / --fi -> [ENTER] -> " + "Write the name of your directory: (name for your directory) -> (will showed the list of directories by regex in utility");
                        case 69 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--erase / --es -> [ENTER] -> " + "<MESSAGE> List of directories was erased successfully");
                        case 70 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--mirror / --mr -> [ENTER] -> " + "Write the name of your file: (your file) -> [ENTER] -> {IF SUCCESS} -> Data from file were changed successfully");
                        case 71 -> System.out.println("your path (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--update / --ue -> [ENTER] -> " + "<MESSAGE> Configuration files for Utility were updated successfully");
                        default -> System.err.println("This command doesn't exist or not the standard yet");
                    }
                }
                case "--exstdirs", "--ex" -> {
                    appendHistory((index) + " | " + arg);
                    loadPathsOfDirectory().forEach(System.out::println);
                }
                case "--chgrits", "--cs" -> {
                    appendHistory((index) + " | " + arg);
                    System.out.println("Write the file which you want to change the rights for using this file in: ");
                    String saveFileName = operation.nextLine();
                    Path path = Path.of(saveFileName);
                    if (Files.exists(path)) {
                        String line;
                        try (BufferedReader readFromFileDate = new BufferedReader(new FileReader(saveFileName))) {
                            line = readFromFileDate.readLine();
                            if (line.isEmpty()) {
                                line = "";
                            }
                        } catch (IOException ex) {
                            throw new RuntimeException(ex.getLocalizedMessage());
                        }
                        Files.delete(path);
                        System.out.println("Write the rights in the view of the octal system (Example: 700): ");
                        String octalSystem = operation.nextLine();
                        if (octalSystem.length() > 3) {
                            System.err.println(RED + "The rights must be not more than 3 numbers" + RESET);
                        } else {
                            for (Character charNumbers : octalSystem.toCharArray()) {
                                numberRights.add(charNumbers);
                            }
                            List<Integer> numbers = new ArrayList<>();
                            for (int i = 0; i < octalSystem.length(); ++i) {
                                int toNumber = octalSystem.charAt(i) - 48;
                                numbers.add(toNumber);
                            }
                            for (Integer number : numbers) {
                                resultRights.add(rights.get(number));
                            }
                            String allRights = resultRights.getFirst() + resultRights.get(1) + resultRights.getLast();
                            Set<PosixFilePermission> perms = PosixFilePermissions.fromString(allRights);
                            FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
                            Files.createFile(path, attr);
                            try (BufferedWriter writeBackToFile = new BufferedWriter(new FileWriter(saveFileName))) {
                                writeBackToFile.write(line);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex.getLocalizedMessage());
                            }
                            System.out.println(GREEN + "The rights for the file: '" + saveFileName + "' were changed successfully" + RESET);
                            numberRights.clear();
                        }
                    }
                }
                case "--chgextn", "--cx" -> {
                    appendHistory((index) + " | " + arg);
                    String filename, newFileName = "", extension, newExtension, isExists, data = "";
                    System.out.println("Write your file without extension: ");
                    filename = operation.nextLine();
                    System.out.println("Write the extension for your file: ");
                    extension = operation.nextLine();
                    isExists = filename + extension;
                    Path path = Path.of(isExists);
                    if (Files.exists(path)) {
                        try (BufferedReader readFromFile = new BufferedReader(new FileReader(isExists))) {
                            String line = readFromFile.readLine();
                            if (line == null || line.isEmpty()) {
                                data = " ";
                            } else {
                                data = line;
                            }
                        } catch (IOException ex) {
                            throw new RuntimeException(ex.getLocalizedMessage());
                        }
                    } else {
                        System.err.println(RED + "This file doesn't exist" + RESET);
                        System.exit(0);
                    }
                    Files.deleteIfExists(path);
                    System.out.println("Write the new extension for your file: ");
                    newExtension = operation.nextLine();
                    if (!newExtension.startsWith(".")) {
                        System.err.println(RED + "Extensions must be started with ." + RESET);
                    } else {
                        newFileName = filename + newExtension;
                    }
                    try (BufferedWriter writeBackToFile = new BufferedWriter(new FileWriter(newFileName))) {
                        writeBackToFile.write(data);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex.getLocalizedMessage());
                    }
                    System.out.println(GREEN + "The file's extension was changed successfully" + RESET);
                }
                case "--symlink", "--sl" -> {
                    appendHistory((index) + " | " + arg);
                    String symlink;
                    System.out.println("Write the directory for which you want to create the symbolic link: ");
                    directory = operation.nextLine();
                    List<String> findDirectory = new ArrayList<>(Files.readAllLines(fromFile));
                    String foundDirectory = findDirectory.get(findDirectory.indexOf(directory) + 2);
                    System.out.println(foundDirectory);
                    if (foundDirectory.isEmpty()) {
                        System.err.println(RED + "This directory doesn't exist" + RESET);
                    } else {
                        Path target = Path.of(foundDirectory);
                        System.out.println("Write the symbolic link: ");
                        symlink = operation.nextLine();
                        Files.createSymbolicLink(Path.of(symlink), target);
                        System.out.println(GREEN + "The symbolic link was created successfully: " + new File(symlink).getAbsolutePath() + RESET);
                    }
                }
                case "--empty", "--em" -> {
                    appendHistory((index) + " | " + arg);
                    System.out.println("Write the file which you want to delete the text from: ");
                    String fileName = operation.nextLine();
                    String name = "", newFile;
                    if (Files.exists(Path.of(fileName))) {
                        name = fileName;
                    } else {
                        System.out.println(RED + "This file doesn't exist " + RESET);
                    }
                    try (BufferedReader checkTheEmptyData = new BufferedReader(new FileReader(name))) {
                        String check = checkTheEmptyData.readLine();
                        if (check == null || check.isEmpty()) {
                            System.out.println(YELLOW + "The file is already empty" + RESET);
                        }
                    }
                    newFile = name;
                    Files.deleteIfExists(Path.of(name));
                    Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxr-xr-x");
                    FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
                    Files.createFile(Path.of(newFile), attr);
                    System.out.println(GREEN + "Text from the file was deleted successfully" + RESET);
                }
                case "--sort", "--st" -> {
                    appendHistory((index) + " | " + arg);
                    System.out.println("Write your file: ");
                    String file_ = operation.nextLine();
                    System.out.println("Write your directory: ");
                    directory = operation.nextLine();
                    String fullPath = String.format("%s/%s", directory, file_);
                    List<String> allCatalogies = Files.readAllLines(Path.of(fullPath));
                    Collections.sort(allCatalogies);
                    allCatalogies.forEach(System.out::println);
                    allCatalogies.clear();
                }
                case "--reverse", "--rv" -> {
                    appendHistory((index) + " | " + arg);
                    System.out.println("Write the file which you want to read data from: ");
                    String fileName = operation.nextLine();
                    List<String> strings = new ArrayList<>();
                    String line;
                    try (BufferedReader readFrom = new BufferedReader(new FileReader(fileName))) {
                        line = readFrom.readLine();
                        if (line == null || line.isEmpty()) {
                            System.err.println(RED + "Data of the file is null" + RESET);
                        }
                    }
                    StringTokenizer token = new StringTokenizer(line);
                    while (token.hasMoreTokens()) {
                        strings.add(token.nextToken());
                    }
                    Collections.reverse(strings);
                    strings.forEach(System.out::println);
                    strings.clear();
                }
                case "--remall", "--ra" -> {
                    appendHistory((index) + " | " + arg);
                    System.out.println("Write your directory: ");
                    directory = operation.nextLine();
                    if (!directory.startsWith("C:\\") && !directory.startsWith("/")) {
                        System.err.println(RED + "Directories must be started with C:\\ (for Windows) or / (Linux)" + RESET);
                    } else {
                        try (Stream<Path> paths = Files.walk(Path.of(directory))) {
                            paths.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(file_ -> {
                                try {
                                    Files.delete(file_.toPath());
                                } catch (IOException e) {
                                    System.err.println(RED + "Deleting file error: " + file_ + RESET);
                                }
                            });
                        }
                        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxrwxrwx");
                        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
                        Files.createDirectory(Path.of(directory), attr);
                        System.out.println(GREEN + "All catalogies and files in " + directory + " were deleted successfully" + RESET);
                    }
                }
                case "--remove", "--rm" -> {
                    appendHistory((index) + " | " + arg);
                    System.out.println("Write name of the file: ");
                    String fileName = operation.nextLine();
                    System.out.println("Write your directory: ");
                    directory = operation.nextLine();
                    if (!directory.startsWith("C:\\") && !directory.startsWith("/")) {
                        System.err.println(RED + "Directories must be started with C:\\ (for Windows) or / (Linux)" + RESET);
                    } else {
                        String fullPath = String.format("%s/%s", directory, fileName);
                        Files.deleteIfExists(Path.of(fullPath));
                        System.out.println(GREEN + "File '" + fileName + "' was deleted from directory '" + directory + "' successfully" + RESET);
                    }
                }
                case "--integrate", "--ig" -> {
                    appendHistory((index) + " | " + arg);
                    System.out.println("Write your file: ");
                    String newFile = operation.nextLine();
                    System.out.println("Write your directory: ");
                    directory = operation.nextLine();
                    if (!directory.startsWith("C:\\") && !directory.startsWith("/")) {
                        System.err.println(RED + "Directories must be started with C:\\ (for Windows) or / (Linux)" + RESET);
                    } else {
                        String fullPath = String.format("%s/%s", directory, newFile);
                        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxrwxrwx");
                        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
                        Files.createFile(Path.of(fullPath), attr);
                        System.out.println(GREEN + "File '" + newFile + "' was added to directory '" + directory + "' successfully" + RESET);
                    }
                }
                case "--sizfls", "--sf" -> {
                    appendHistory((index) + " | " + arg);
                    System.out.println("Write the directory (for analysis all files) or file: ");
                    String allOrDefinite = operation.nextLine();
                    if (new File(allOrDefinite).isDirectory()) {
                        if (!allOrDefinite.startsWith("C:\\") && !allOrDefinite.startsWith("/")) {
                            System.err.println(RED + "Directories must be started with C:\\ (for Windows) or / (Linux)" + RESET);
                        } else {
                            try (Stream<Path> paths = Files.walk(Path.of(allOrDefinite))) {
                                paths.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach
                                        (file_ -> System.out.println(file_ + " = " + YELLOW + file_.length() + RESET + " bytes"));
                            }
                        }
                    } else {
                        System.out.println("Write the path to your file: ");
                        allOrDefinite = operation.nextLine();
                        if (Files.exists(Path.of(allOrDefinite))) {
                            System.out.println(allOrDefinite + " = " + YELLOW + new File(allOrDefinite).length() + RESET + " bytes");
                        } else {
                            System.err.println(RED + "This file doesn't exist" + RESET);
                        }
                    }
                }
                case "--edit", "--et" -> {
                    appendHistory((index) + " | " + arg);
                    SwingUtilities.invokeLater(() -> new TextEditor().setVisible(true));
                }
                case "--symcnt", "--wc" -> {
                    appendHistory((index) + " | " + arg);
                    System.out.println("Write name of the file: ");
                    String fileName = operation.nextLine();
                    if (Files.exists(Path.of(fileName))) {
                        try (BufferedReader symbolsCount = new BufferedReader(new FileReader(fileName))) {
                            String line = symbolsCount.readLine();
                            if (line == null || line.isEmpty()) {
                                System.out.println(GREEN + "Symbols: " + 0 + RESET);
                            } else {
                                System.out.println(GREEN + "Symbols: " + line.length() + RESET);
                            }
                        }
                    } else {
                        System.err.println(RED + "This file doesn't exist" + RESET);
                    }
                }
                case "--resize", "--rs" -> {
                    appendHistory((index) + " | " + arg);
                    System.out.println("Write name of your file: ");
                    String filename = operation.nextLine();
                    Path path = Path.of(filename);
                    if (Files.exists(path)) {
                        System.out.println("Write the new size for your file: ");
                        long newsize = operation.nextLong();
                        String[] fileData = new String[2];
                        try (BufferedReader readFrom = new BufferedReader(new FileReader(filename))) {
                            String line = readFrom.readLine();
                            if (line == null || line.isEmpty()) {
                                line = "";
                            }
                            fileData[0] = line;
                        }
                        fileData[1] = filename;
                        Files.deleteIfExists(path);
                        try (RandomAccessFile recreateAndResize = new RandomAccessFile(fileData[1], "rw")) {
                            recreateAndResize.setLength(newsize);
                        }
                        try (BufferedWriter writeAgainBack = new BufferedWriter(new FileWriter(fileData[1]))) {
                            writeAgainBack.write(fileData[0]);
                        } catch (IOException exc) {
                            throw new RuntimeException(exc.getLocalizedMessage());
                        }
                        System.out.println(GREEN + "File's size was changed successfully" + RESET);
                    } else {
                        System.err.println(RED + "This file doesn't exist" + RESET);
                    }
                }
                case "--version", "--vs" -> {
                    appendHistory((index) + " | " + arg);
                    System.out.println("3.3.0");
                }
                case "--backup", "--bp" -> {
                    appendHistory((index) + " | " + arg);
                    System.out.println("Write your file which saved your data: ");
                    String filename = operation.nextLine();
                    Path path = Path.of(filename);
                    if (Files.exists(path)) {
                        try (BufferedReader checkDataInFIle = new BufferedReader(new FileReader(filename))) {
                            String data = checkDataInFIle.readLine();
                            if (data == null || data.isEmpty()) {
                                System.err.println(RED + "Data in the file is empty" + RESET);
                            }
                        }
                        try {
                            Files.copy(path, source, StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException exc) {
                            throw new RuntimeException(exc.getLocalizedMessage());
                        }
                        System.out.println(GREEN + "For saving data from file was created ReserveCopy.bin file" + RESET);
                    } else {
                        System.err.println(RED + "This file doesn't exist" + RESET);
                    }
                }
                case "--xexport", "--xp" -> {
                    appendHistory((index) + " | " + arg);
                    System.out.println("Write your data: ");
                    String data = operation.nextLine();
                    XMLOutputFactory factory = XMLOutputFactory.newInstance();
                    XMLStreamWriter writeToXML = factory.createXMLStreamWriter(new FileWriter("XMLFormat.xml"));
                    writeToXML.writeStartDocument("UTF-8", "1.0");
                    writeToXML.writeStartElement("Message");
                    writeToXML.writeCData(data);
                    writeToXML.writeEndElement();
                    writeToXML.writeEndDocument();
                    writeToXML.close();
                    System.out.println(GREEN + "XML File with date was created successfully" + RESET);
                }
                case "--ximport", "--xm" -> {
                    appendHistory((index) + " | " + arg);
                    XMLInputFactory factory = XMLInputFactory.newInstance();
                    XMLStreamReader readFromXML = factory.createXMLStreamReader(new FileReader("XMLFormat.xml"));
                    if (readFromXML.hasNext()) {
                        readFromXML.next();
                    }
                    String[] dataFromXML = new String[4];
                    dataFromXML[0] = "Name: " + readFromXML.getName();
                    dataFromXML[1] = "Encoding: " + readFromXML.getEncoding();
                    dataFromXML[2] = "Version: " + readFromXML.getVersion();
                    dataFromXML[3] = "Text " + readFromXML.getElementText();
                    for (String info : dataFromXML) {
                        System.out.println(info);
                    }
                    readFromXML.close();
                }
                case "--restore", "--rt" -> {
                    appendHistory((index) + " | " + arg);
                    try {
                        Files.copy(source, Path.of("FromReserve.txt"), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOError exc) {
                        throw new RuntimeException(exc.getLocalizedMessage());
                    }
                    try (BufferedReader dataFromReserve = new BufferedReader(new FileReader("FromReserve.txt"))) {
                        System.out.println(dataFromReserve.readLine());
                    } catch (IOException exc) {
                        throw new RuntimeException(exc.getLocalizedMessage());
                    }
                    System.out.println(GREEN + "Data from XML to simple file were shared successfully" + RESET);
                }
                case "--stats", "--ss" -> {
                    appendHistory((index) + " | " + arg);
                    List<Integer> HourMinuteSecond = getIntegers();
                    ArrayList<String> countUsedCommands = new ArrayList<>(Files.readAllLines(HistoricalFile));
                    System.out.println("Used commands: " + countUsedCommands.size() + " | Uptime: " + (LocalTime.now().getHour() - HourMinuteSecond.getFirst())
                            + ":" + (LocalTime.now().getMinute() - HourMinuteSecond.get(1))
                            + ":" + (Math.abs(LocalTime.now().getSecond() - HourMinuteSecond.getLast())));
                }
                case "--search", "--sh" -> {
                    appendHistory((index) + " | " + arg);
                    List<String> search = new ArrayList<>(List.of(
                            "--help or --hp", "--add or --ad",
                            "--read or --rd", "--delete or --dt", "--copy or --cp",
                            "--move or --mv", "--newname or --nn",
                            "--stopgap or --sg", "--GUI or --gi", "--jar or --jr",
                            "--zip or -zp (Windows) / --tar.gz or -tr (Linux)",
                            "--write or --wt", "--grep or --gp",
                            "--history or --hi", "--find or --fd",
                            "--lstcat or --ls", "--replace or --re", "--crtdir or --cr",
                            "--candir or --ca", "--exstdirs or --ex",
                            "--tldr or --tl", "--chgrits or --cs",
                            "--chgextn or --cx", "--symlink or --sl",
                            "--empty or --em", "--sort or --st",
                            "--reverse or --rv", "--remall or --ra",
                            "--remove or --rm", "--integrate or --ig",
                            "--sizfls or --sf", "--edit or --et", "--symcnt or --sc",
                            "--resize or --rs", "--version or --vs",
                            "--backup,--bp", "--xexport,--xp", "--ximport or --xm",
                            "--restore or --rt", "--stats or --ss", "--search or --sh",
                            "--hostinfo or --ho", "--shutdown or sd", "--restart or --rr", "--fmem or --fm",
                            "--clean or --cn", "--ping or --pg", "--intproc or --ip", "--interrupt or --ir",
                            "--filter or --fr", "--md5gen or --mg", "--sha256gen or --sn", "--freeze or --fe",
                            "--unique or --uq", "--stat or --sa", "--split or --sp", "--rsync or --rc",
                            "----cmp or --cp", "--sysinfo or --si", "--recent or --rn", "--active or --ae",
                            "--username or --un", "--preview or --pw", "--cut or --ct", "--chgpass or --ps",
                            "--logout or --lg","--fcmd or --fc","--gzip or --gz","--fdir or --fi",
                            "--erase or --es","--mirror or --mr","--update or --ue"
                    ));
                    System.out.println("Write which command you find: ");
                    String command = operation.nextLine();
                    search.stream().filter(foundCommand -> foundCommand.startsWith(command)).forEach(System.out::println);
                }
                case "--hostinfo", "--ho" -> {
                    appendHistory((index) + " | " + arg);
                    String[] hostInfo = new String[]
                            {
                                    "Host name: " + Inet4Address.getLocalHost().getHostName(),
                                    "Host address: " + InetAddress.getLocalHost().getHostAddress(),
                                    "Canonical host name: " + InetAddress.getLocalHost().getCanonicalHostName()
                            };
                    for (String info : hostInfo) {
                        System.out.println(info);
                    }
                }
                case "--shutdown", "--sd" -> {
                    String getOsName = System.getProperty("os.name");
                    if (Objects.equals(getOsName, "Linux") || Objects.equals(getOsName, "Windows")) {
                        String[] shutdown = new String[]{"shutdown -h now"};
                        try {
                            Process offProcess = Runtime.getRuntime().exec(shutdown);
                            System.out.println(offProcess);
                            System.exit(0);
                        } catch (RuntimeOperationsException exc) {
                            throw new RuntimeException(exc.getLocalizedMessage());
                        }
                    } else {
                        System.out.println(RED + "Console Utility doesn't support this OS" + RESET);
                    }
                }
                case "--restart", "--rr" -> {
                    String[] OSCommands = new String[1];
                    String getOsName = System.getProperty("os.name");
                    if (Objects.equals(getOsName, "Linux")) {
                        OSCommands[0] = "reboot";
                    } else if (Objects.equals(getOsName, "Windows")) {
                        OSCommands[0] = "restart-computer";
                    }
                    try {
                        Process restartProcess = Runtime.getRuntime().exec(OSCommands);
                        System.out.println(restartProcess);
                        System.exit(0);
                    } catch (RuntimeOperationsException exc) {
                        throw new RuntimeException(exc.getLocalizedMessage());
                    }
                }
                case "--fmem", "--fm" -> {
                    appendHistory((index) + " | " + arg);
                    System.out.println(Runtime.getRuntime().freeMemory());
                }
                case "--clean", "--cn" -> {
                    Files.deleteIfExists(HistoricalFile);
                    System.out.println(GREEN + "History of commands was deleted successfully" + RESET);
                }
                case "--ping", "--pg" -> {
                    appendHistory((index) + " | " + arg);
                    String IPorDNS;
                    int ping_times;
                    System.out.println("Write the IP or DNS which you want to ping: ");
                    IPorDNS = operation.nextLine();
                    System.out.println("Write how many time you want to ping: ");
                    ping_times = operation.nextInt();
                    String[] command = new String[0];
                    String OSName = System.getProperty("os.name");
                    if (Objects.equals(OSName, "Windows")) {
                        command = new String[]{"ping", "-n", String.valueOf(ping_times), IPorDNS};
                    } else if (Objects.equals(OSName, "Linux")) {
                        command = new String[]{"ping", "-c", String.valueOf(ping_times), IPorDNS};
                    } else {
                        System.err.println("Utility doesn't support this OS");
                    }
                    try {
                        Process process = Runtime.getRuntime().exec(command);
                        BufferedReader readInputStream = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String line;
                        while ((line = readInputStream.readLine()) != null) {
                            System.out.println(line);
                        }
                        int exitCode = process.waitFor();
                        System.out.println(exitCode);
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "--intproc", "--ip" -> {
                    appendHistory((index) + " | " + arg);
                    String OSName = System.getProperty("os.name");
                    String[] command = new String[0];
                    if (Objects.equals(OSName, "Linux")) {
                        command = new String[]{"ip", "a"};
                    } else if (Objects.equals(OSName, "Windows")) {
                        command = new String[]{"ipconfig"};
                    } else {
                        System.err.println("Utility doesn't support this OS");
                    }
                    try {
                        Process showIP = Runtime.getRuntime().exec(command);
                        BufferedReader readIPInText = new BufferedReader(new InputStreamReader(showIP.getInputStream()));
                        String line;
                        while ((line = readIPInText.readLine()) != null) {
                            System.out.println(line);
                        }
                        System.out.println(showIP);
                    } catch (IOException exc) {
                        throw new RuntimeException(exc.getLocalizedMessage());
                    }
                }
                case "--interrupt", "--ir" -> {
                    appendHistory((index) + " | " + arg);
                    long process_id;
                    System.out.println("Write the id of your process: ");
                    process_id = operation.nextLong();
                    String[] command = new String[0];
                    String OSName = System.getProperty("os.name");
                    if (Objects.equals(OSName, "Linux")) {
                        command = new String[]{"kill", String.valueOf(process_id)};
                    } else if (Objects.equals(OSName, "Windows")) {
                        command = new String[]{"taskkill", "/PID", String.valueOf(process_id), "/F"};
                    } else {
                        System.err.println("Utility doesn't support this OS");
                    }
                    try {
                        Process interruptProcess = Runtime.getRuntime().exec(command);
                        System.out.println(interruptProcess);
                    } catch (IOException exc) {
                        throw new RuntimeException(exc.getLocalizedMessage());
                    }
                }
                case "--filter", "--fr" -> {
                    appendHistory((index) + " | " + arg);
                    System.out.println("Write the directory: ");
                    String directory_ = operation.nextLine();
                    System.out.println("Write the size for filtering files in directory:");
                    int size = operation.nextInt();
                    operation.nextLine();
                    System.out.println("Write the compare operator for searching files with definite pattern: ");
                    String compare = operation.nextLine();
                    if (!directory_.startsWith("C:\\") && !directory_.startsWith("/")) {
                        System.err.println(RED + "Directories must be started with C:\\ (for Windows) or / (Linux)" + RESET);
                    } else {
                        try (Stream<Path> paths = Files.walk(Path.of(directory_))) {
                            Stream<Path> onlyFiles = paths.filter(Files::isRegularFile);
                            switch (compare.charAt(0)) {
                                case '<' -> onlyFiles.filter(sizeFile -> {
                                    try {
                                        return Files.size(sizeFile) < size;
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach
                                        (file_ -> System.out.println(file_ + " = " + YELLOW + file_.length() + RESET + " bytes"));
                                case '>' -> onlyFiles.filter(sizeFile -> {
                                    try {
                                        return Files.size(sizeFile) > size;
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach
                                        (file_ -> System.out.println(file_ + " = " + YELLOW + file_.length() + RESET + " bytes"));
                                case '=' -> onlyFiles.filter(sizeFile -> {
                                    try {
                                        return Files.size(sizeFile) == size;
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach
                                        (file_ -> System.out.println(file_ + " = " + YELLOW + file_.length() + RESET + " bytes"));
                                case '!' -> onlyFiles.filter(sizeFile -> {
                                    try {
                                        return Files.size(sizeFile) != size;
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach
                                        (file_ -> System.out.println(file_ + " = " + YELLOW + file_.length() + RESET + " bytes"));
                            }
                        }
                    }
                }
                case "--md5gen", "--mg" -> {
                    appendHistory((index) + " | " + arg);
                    System.out.println("Write your file for analysing data in md5 algorithm: ");
                    String getFileForMd5 = operation.nextLine();
                    Path path = Path.of(getFileForMd5);
                    if (Files.exists(path)) {
                        String getInputData = Files.readString(path, StandardCharsets.UTF_8);
                        if (getInputData == null || getInputData.isEmpty()) {
                            getInputData = "0";
                        }
                        MessageDigest md5;
                        try {
                            md5 = MessageDigest.getInstance("MD5");
                        } catch (NoSuchAlgorithmException exc) {
                            throw new RuntimeException(exc.getLocalizedMessage());
                        }
                        byte[] byteHex = md5.digest(getInputData.getBytes(StandardCharsets.UTF_8));
                        for (byte getByteInHex : byteHex) {
                            System.out.printf("%s", Integer.toHexString(0xFF & getByteInHex));
                        }
                        System.out.println("\n");
                    }
                }
                case "--sha256gen", "--sn" -> {
                    appendHistory((index) + " | " + arg);
                    System.out.println("Write your file for analysing data in sha256 algorithm: ");
                    String getFileForSha256 = operation.nextLine();
                    Path path = Path.of(getFileForSha256);
                    if (Files.exists(path)) {
                        String getInputData = Files.readString(path, StandardCharsets.UTF_8);
                        if (getInputData == null || getInputData.isEmpty()) {
                            getInputData = "0";
                        }
                        MessageDigest sha256;
                        try {
                            sha256 = MessageDigest.getInstance("SHA-256");
                        } catch (NoSuchAlgorithmException exc) {
                            throw new RuntimeException(exc.getLocalizedMessage());
                        }
                        byte[] byteHex = sha256.digest(getInputData.getBytes(StandardCharsets.UTF_8));
                        for (byte getByteInHex : byteHex) {
                            System.out.printf("%s", Integer.toHexString(0xFF & getByteInHex));
                        }
                        System.out.println("\n");
                    }
                }
                case "--freeze", "--fe" -> {
                    appendHistory((index) + " | " + arg);
                    long process_id;
                    System.out.println("Write the id of your process: ");
                    process_id = operation.nextLong();
                    String[] command = new String[0];
                    String OSName = System.getProperty("os.name");
                    if (Objects.equals(OSName, "Linux")) {
                        command = new String[]{"kill", "-STOP", String.valueOf(process_id)};
                    } else if (Objects.equals(OSName, "Windows")) {
                        command = new String[]{"taskkill", "/PID", String.valueOf(process_id), "/F"};
                    } else {
                        System.err.println("Utility doesn't support this OS");
                    }
                    try {
                        Process freezeProcess = Runtime.getRuntime().exec(command);
                        System.out.println(freezeProcess);
                    } catch (IOException exc) {
                        throw new RuntimeException(exc.getLocalizedMessage());
                    }
                }
                case "--unique", "--uq" -> {
                    System.out.println("Write your file: ");
                    String getFile = operation.nextLine();
                    Path path = Path.of(getFile);
                    if (Files.exists(path)) {
                        List<String> getAllStrings = new ArrayList<>(Files.readAllLines(path));
                        Set<String> uniqueStrings = new LinkedHashSet<>(getAllStrings);
                        getAllStrings.clear();
                        uniqueStrings.forEach(System.out::println);
                        uniqueStrings.clear();
                    } else {
                        System.err.println(RED + "This file doesn't exist" + RESET);
                    }
                }
                case "--stat", "--sa" -> {
                    System.out.println("Write the name for your file: ");
                    String fileName = operation.nextLine();
                    Path path = Path.of(fileName);
                    if (Files.exists(path)) {
                        System.out.println("File's name: " + new File(fileName).getName());
                        System.out.println("File's size: " + new File((fileName)).length());
                        BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
                        FileTime creationTime = attributes.creationTime();
                        FileTime modifiedTime = attributes.lastModifiedTime();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd");
                        LocalDateTime creationDateTime = LocalDateTime.ofInstant(creationTime.toInstant(), ZoneId.systemDefault());
                        LocalDateTime creationModifiedDateTime = LocalDateTime.ofInstant(modifiedTime.toInstant(), ZoneId.systemDefault());
                        System.out.println("Creation file's time: " + creationDateTime.format(formatter));
                        System.out.println("Last Modified file's time: " + creationModifiedDateTime.format(formatter));
                    } else {
                        System.err.println(RED + "This file doesn't exist" + RESET);
                    }
                }
                case "--split", "--sp" -> {
                    appendHistory((index) + " | " + arg);
                    System.out.println("Write the name for your file: ");
                    String fileName = operation.nextLine();
                    Path path1 = Path.of(fileName);
                    if (Files.exists(path1)) {
                        String dataFromFile;
                        try (BufferedReader readData = new BufferedReader(new FileReader(fileName))) {
                            dataFromFile = readData.readLine();
                            if (dataFromFile == null || dataFromFile.isEmpty()) {
                                dataFromFile = " ";
                            }
                        }
                        byte[] byteData = Files.readAllBytes(path1);
                        byte[] firstHalfBytes = new byte[byteData.length / 2];
                        byte[] secondHalfBytes = new byte[byteData.length];
                        if (byteData.length >= 0) System.arraycopy(byteData, 0, secondHalfBytes, 0, byteData.length);
                        if (byteData.length / 2 >= 0)
                            System.arraycopy(byteData, 0, firstHalfBytes, 0, byteData.length / 2);
                        Files.deleteIfExists(path1);
                        String new_ = "New";
                        String file_ = fileName.substring(0, fileName.indexOf('.'));
                        String extension = fileName.substring(fileName.indexOf('.'));
                        File newFile = new File(new_ + file_ + extension);
                        File newFile2 = new File(new_ + file_ + "2" + extension);
                        try {
                            Files.write(newFile.toPath(), firstHalfBytes, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                        } catch (IOException exc) {
                            throw new RuntimeException(exc.getLocalizedMessage());
                        }
                        try {
                            Files.write(newFile2.toPath(), secondHalfBytes, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                        } catch (IOException exc) {
                            throw new RuntimeException(exc.getLocalizedMessage());
                        }
                        System.out.println(GREEN + "Spliting of the file " + fileName + " is successful" + RESET);
                    } else {
                        System.err.println(RED + "This file for spliting doesn't exist" + RESET);
                    }
                }
                case "--rsync", "--rc" -> {
                    appendHistory((index) + " | " + arg);
                    System.out.println("Write your source file: ");
                    String SourceFile = operation.nextLine();
                    System.out.println("Write your destination file: ");
                    String DestinationFile = operation.nextLine();
                    Path path = Path.of(SourceFile);
                    if (Files.exists(path)) {
                        Path path1 = Path.of(DestinationFile);
                        if (Files.exists(path1)) {
                            byte[] byteData = Files.readAllBytes(path);
                            try {
                                Files.write(path1, byteData, StandardOpenOption.APPEND);
                            } catch (IOException exc) {
                                throw new RuntimeException(exc.getLocalizedMessage());
                            }
                            System.out.println(GREEN + "Byte data with size: " + byteData.length + " were synchronized to new file successfully" + RESET);
                            System.out.println(GREEN + "Operation was completed in " + LocalDateTime.now() + RESET);
                        } else {
                            System.err.println(RED + "Error. It's impossible to write data from source file to unknown destination. Create the destination file" + RESET);
                        }
                    } else {
                        System.err.println(RED + "Error. It's impossible to write data from unknown source file to destination file. Create the new source file and write data in this file" + RESET);
                    }
                }
                case "--cmp", "--cm" -> {
                    appendHistory((index) + " | " + arg);
                    String firstFile, secondFile, firstData = "", secondData = "";
                    System.out.println("Write the first file: ");
                    firstFile = operation.nextLine();
                    System.out.println("Write the second file : ");
                    secondFile = operation.nextLine();
                    Path path = Path.of(firstFile);
                    Path path4 = Path.of(secondFile);
                    if (!Files.exists(path) || !Files.exists(path4)) {
                        System.err.println(RED + "One of two is not detected" + RESET);
                    } else {
                        List<String> checkSorted1 = new ArrayList<>(Files.readAllLines(path));
                        List<String> CheckSorted2 = new ArrayList<>(Files.readAllLines(path4));
                        if (checkSorted1.stream().sorted().isParallel() || CheckSorted2.stream().sorted().isParallel()) {
                            firstData = String.valueOf(Files.readAllLines(path));
                            secondData = String.valueOf(Files.readAllLines(path4));
                        }
                        if (Objects.equals(firstData, secondData)) {
                            System.out.println(firstData);
                        } else {
                            System.out.println(firstData + " | " + secondData);
                        }
                    }
                }
                case "--sysinfo", "--si" -> {
                    appendHistory((index) + " | " + arg);
                    System.out.println("System Information: ");
                    System.out.println(GREEN + "CPU >> " + RESET);
                    for (String partInfo : getCpuInfo()) {
                        System.out.println(partInfo);
                    }
                    System.out.println(GREEN + "Disk >> " + RESET);
                    File[] roots = File.listRoots();
                    for (File diskInfo : roots) {
                        System.out.println(YELLOW + "Disk > " + RESET + diskInfo.getAbsolutePath());
                        System.out.println(YELLOW + "Total Memory > " + RESET + diskInfo.getTotalSpace());
                        System.out.println(YELLOW + "Usable Memory > " + RESET + diskInfo.getUsableSpace());
                        System.out.println(YELLOW + "Free Memory > " + RESET + diskInfo.getFreeSpace());
                    }
                    System.out.println(YELLOW + "Heap >> " + RESET);
                    for (String heapInfo : getHeapInfo()) {
                        System.out.println(heapInfo);
                    }
                }
                case "--recent", "--rn" -> {
                    appendHistory((index) + " | " + arg);
                    Path allRecentActive = Path.of("Recent.txt");
                    new ArrayList<>(Files.readAllLines(allRecentActive)).forEach(System.out::println);
                }
                case "--active", "--ae" -> {
                    appendHistory((index++) + " | " + arg);
                    String username, activefrom;
                    if (Files.exists(Path.of("Username.txt"))) {
                        try (BufferedReader readUserName = new BufferedReader(new FileReader("Username.txt"))) {
                            username = readUserName.readLine();
                            if (username == null || username.isEmpty()) {
                                username = "";
                            }
                        } catch (IOException exc) {
                            throw new RuntimeException(exc.getLocalizedMessage());
                        }
                        try (BufferedReader readTime = new BufferedReader(new FileReader("UtilityStatistic.txt"))) {
                            activefrom = readTime.readLine();
                            if (activefrom == null || activefrom.isEmpty()) {
                                activefrom = "";
                            }
                        } catch (IOException exc) {
                            throw new RuntimeException(exc.getLocalizedMessage());
                        }
                        StringTokenizer divideTimeData = new StringTokenizer(activefrom);
                        List<String> timeIntervals = new ArrayList<>();
                        while (divideTimeData.hasMoreTokens()) {
                            timeIntervals.add(divideTimeData.nextToken());
                        }
                        String formatTime = timeIntervals.getFirst() + ":" + timeIntervals.get(1) + ":" + timeIntervals.getLast();
                        timeIntervals.clear();
                        System.out.println("USERNAME: " + username + " | ACTIVE FROM: " + formatTime + " | TIME NOW: " + LocalDateTime.now());
                    } else {
                        System.err.println(RED + "You logged out from Utility's system." + RESET);
                    }
                }
                case "--username", "--un" -> {
                    appendHistory((index++) + " | " + arg);
                    String username;
                    Console readPassword = System.console();
                    char[] password = readPassword.readPassword("Write the password: ");
                    try (BufferedReader checkThePassword = new BufferedReader(new FileReader("PasswordManager.txt"))) {
                        if (Arrays.equals(password, checkThePassword.readLine().toCharArray())) {
                            try (BufferedReader readUserName = new BufferedReader(new FileReader("Username.txt"))) {
                                username = readUserName.readLine();
                                if (username == null || username.isEmpty()) {
                                    username = "";
                                }
                            }
                            System.out.println(GREEN + username + RESET);
                        } else {
                            System.err.println(RED + "This password is false or was log out from Utility" + RESET);
                            System.exit(0);
                        }
                    } catch (IOException exc) {
                        throw new RuntimeException(exc.getLocalizedMessage());
                    }
                }
                case "--preview", "--pw" -> {
                    appendHistory((index++) + " | " + arg);
                    Preview show = new Preview();
                    List<String> showAllFutureCommands = show.commandList();
                    for (String futureCommand : showAllFutureCommands) {
                        System.out.println(showAllFutureCommands.indexOf(futureCommand) + 1 + "> " + futureCommand);
                    }
                }
                case "--cut", "--ct" -> {
                    appendHistory((index++) + " | " + arg);
                    int startIndex, endIndex, select;
                    System.out.println("Write the name for your file: ");
                    String filename = operation.nextLine();
                    String data;
                    if (Files.exists(Path.of(filename))) {
                        try (BufferedReader readData = new BufferedReader(new FileReader(filename))) {
                            data = readData.readLine();
                            if (data == null || data.isEmpty()) {
                                data = "";
                            }
                        }
                        System.out.println("Select the variant for cutting the data from file: ");
                        System.out.println("1. Cut string with indexes");
                        System.out.println("2. Cut string for delimiter: ");
                        select = operation.nextInt();
                        if (select == 1) {
                            System.out.println("Write first index: ");
                            startIndex = operation.nextInt();
                            System.out.println("Write second index: ");
                            endIndex = operation.nextInt();
                            if (startIndex < 0) {
                                startIndex = 0;
                            }
                            if (endIndex >= data.length()) {
                                endIndex = data.length();
                            }
                            System.out.println(data.substring(startIndex, endIndex));
                        } else if (select == 2) {
                            StringTokenizer divideStringForTokens = new StringTokenizer(data);
                            while (divideStringForTokens.hasMoreTokens()) {
                                System.out.println(divideStringForTokens.nextToken());
                            }
                        }
                    } else {
                        System.err.println(RED + "This file doesn't exist" + RESET);
                    }
                }
                case "--chgpass", "--ps" -> {
                    appendHistory((index++) + " | " + arg);
                    File saveThePassword = new File("PasswordManager.txt");
                    Console ownPassword = System.console(), changePassword = System.console();
                    char[] currentPassword = ownPassword.readPassword("Write your password: ");
                    try (BufferedReader read = new BufferedReader(new FileReader(saveThePassword))) {
                        if (Arrays.equals(currentPassword, read.readLine().toCharArray())) {
                            char[] newPassword = changePassword.readPassword("Change the password: ");
                            if (newPassword.length < 8) {
                                System.err.println(RED + "Size for the password must be greater or equal 8 symbols" + RESET);
                            } else {
                                try (BufferedWriter writeTheNewPassword = new BufferedWriter(new FileWriter(saveThePassword))) {
                                    writeTheNewPassword.write(newPassword);
                                } catch (IOException exc) {
                                    throw new RuntimeException(exc.getLocalizedMessage());
                                }
                                System.out.println(GREEN + "Password was changes successfully" + RESET);
                            }
                        } else {
                            System.err.println(RED + "Error. This password doesn't exist or was log out from Utility" + RESET);
                        }
                    } catch (IOException exc) {
                        throw new RuntimeException(exc.getLocalizedMessage());
                    }
                }
                case "--logout", "--lg" -> {
                    Files.deleteIfExists(Path.of("Username.txt"));
                    Files.deleteIfExists(Path.of("PasswordManager.txt"));
                    try(BufferedWriter isLogout = new BufferedWriter(new FileWriter("IsLogout.txt"))) {
                        isLogout.write("true");
                    } catch (IOException exc) {
                        throw new RuntimeException(exc.getLocalizedMessage());
                    }
                    System.out.println(GREEN + "Deleting username and password are successful" + RESET);
                }
                case "--fcmd", "--fc" -> {
                    System.out.println("Write the name of command from history: ");
                    String commandName = operation.nextLine();
                    loadHistory().stream().filter(findCommand -> findCommand.endsWith(commandName)).forEach(System.out::println);
                }
                case "--gzip", "--gz" -> {
                    List<byte[]> bytes = new ArrayList<>();
                    List<String> files = new ArrayList<>();
                    System.out.println("Write the file's name for reading data:");
                    String fileName = operation.nextLine();
                    if (Files.exists(Path.of(fileName))) {
                        files.add(fileName);
                        String data;
                        try (BufferedReader readDataString = new BufferedReader(new FileReader(fileName))) {
                            data = readDataString.readLine();
                            if (data == null || data.isEmpty()) {
                                data = "";
                            }
                        }
                        System.out.println("Select the option: ");
                        System.out.println("1. Compress file");
                        System.out.println("2. Uncompress file");
                        int option = operation.nextInt();
                        if (option < 1 || option > 2) {
                            System.err.println(RED + "Error selecting option" + RESET);
                        } else if (option == 1) {
                            ByteArrayOutputStream OutPutByteString = new ByteArrayOutputStream();
                            try (GZIPOutputStream writeStringBytes = new GZIPOutputStream(OutPutByteString)) {
                                writeStringBytes.write(fileName.getBytes());
                            } catch (IOException exc) {
                                throw new RuntimeException(exc.getLocalizedMessage());
                            }
                            System.out.println(GREEN + "Original size: " + data.length() + RESET);
                            System.out.println(GREEN + "Compressed size: " + OutPutByteString.size() + RESET);
                            byte[] getBytesString = Base64.getEncoder().encodeToString(OutPutByteString.toByteArray()).getBytes();
                            bytes.add(getBytesString);
                        } else {
                            ByteArrayInputStream InPutByteString = new ByteArrayInputStream(bytes.get(files.indexOf(fileName)));
                            String decompressedString = getStringBuilder(InPutByteString);
                            System.out.println(GREEN + "Decompressed data: " + decompressedString + RESET);
                            System.out.println(GREEN + "Decompressed size: " + decompressedString.length() + RESET);
                        }
                    }
                }
                case "--fdir", "--fi" -> {
                    System.out.println("Write the name of your directory: ");
                    String findDirectory = operation.nextLine();
                    loadPathsOfDirectory().stream().filter(findDir -> findDir.endsWith(findDirectory)).forEach(System.out::println);
                }
                case "--erase", "--es" -> {
                    Files.deleteIfExists(DirectoryFile);
                    try (BufferedWriter restore = new BufferedWriter(new FileWriter(DirectoryFile.toFile()))) {
                        restore.write("");
                    } catch (IOException exc) {
                        throw new RuntimeException(exc.getLocalizedMessage());
                    }
                    System.out.println(GREEN + "List of directories was erased successfully" + RESET);
                }
                case "--mirror","--mr" -> {
                    System.out.println("Write the name of your file: ");
                    String nameFile = operation.nextLine();
                    Path path = Path.of(nameFile);
                    if(Files.exists(path)) {
                        List<String> getStrings = Files.readAllLines(path);
                        Files.delete(path);
                        StringBuilder createString = new StringBuilder();
                        while(!getStrings.isEmpty()) {
                            createString.append(getStrings.getFirst());
                            createString.append(" ");
                            getStrings.remove(getStrings.getFirst());
                        }
                        String forByteString = createString.toString();
                        String reversedString = new StringBuilder(forByteString).reverse().toString();
                        byte[] getBytes = reversedString.getBytes();
                        Files.write(path,getBytes,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
                        System.out.println(GREEN + "Data from file were changed successfully" + RESET);
                        getStrings.clear();
                    } else {
                        System.err.println(RED + "This file doesn't exist" + RESET);
                    }
                }
                case "--update","--ue" -> {
                    Path utilityStatistic = Path.of("utilityStatistic.txt"),
                            fromReserve = Path.of("FromReserve.txt"),
                            getDirectory = Path.of("Directory.txt"),
                            recent = Path.of("Recent.txt"),
                            reserveCopy = Path.of("ReserveCopy.bin"),
                            islogout = Path.of("IsLogout.txt");
                    Files.deleteIfExists(utilityStatistic);
                    Files.deleteIfExists(fromReserve);
                    Files.deleteIfExists(getDirectory);
                    Files.deleteIfExists(recent);
                    Files.deleteIfExists(reserveCopy);
                    Files.deleteIfExists(islogout);
                    Files.writeString(utilityStatistic," " + System.lineSeparator(),StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
                    Files.writeString(fromReserve," " + System.lineSeparator(),StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
                    Files.writeString(getDirectory," " + System.lineSeparator(),StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
                    Files.writeString(recent," " + System.lineSeparator(),StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
                    Files.writeString(reserveCopy," " + System.lineSeparator(),StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
                    Files.writeString(islogout," " + System.lineSeparator(),StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
                    System.out.println(GREEN + "Configuration files for Utility were updated successfully" + RESET);
                }
                case null, default -> System.err.println(RED + "This operation doesn't exist" + RESET);
            }
        }
    }

    private static String getStringBuilder(ByteArrayInputStream InPutByteString) throws IOException {
        StringBuilder decompressedString = new StringBuilder();
        try(GZIPInputStream readStringBytes = new GZIPInputStream(InPutByteString)) {
            byte[] byteBuffer = new byte[1024];
            int len;
            while((len = readStringBytes.read(byteBuffer)) > 0) {
                decompressedString.append(new String(byteBuffer,0,len));
            }
        }
        return decompressedString.toString();
    }

    private static List<Integer> getIntegers() {
        String getTime;
        try(BufferedReader readTheTime = new BufferedReader(new FileReader("UtilityStatistic.txt"))) {
            getTime = readTheTime.readLine();
            if(getTime == null || getTime.isEmpty()) {
                getTime = "";
            }
        } catch (IOException exc) {
            throw new RuntimeException(exc.getLocalizedMessage());
        }
        List<Integer> HourMinuteSecond = new ArrayList<>();
        StringTokenizer divideTime = new StringTokenizer(getTime);
        while(divideTime.hasMoreTokens()) {
            HourMinuteSecond.add(Integer.parseInt(divideTime.nextToken()));
        }
        return HourMinuteSecond;
    }

    private static String[] getCpuInfo() {
        OperatingSystemMXBean sysinfo = ManagementFactory.getOperatingSystemMXBean();
        return new String[]{
                YELLOW + "Name > " + RESET + sysinfo.getName(),
                YELLOW + "Architecture > "  + RESET + sysinfo.getArch(),
                YELLOW + "System's version > " + RESET + sysinfo.getVersion(),
                YELLOW + "CPU Threads > " + RESET + sysinfo.getAvailableProcessors(),
                YELLOW + "Load Average > " + RESET + sysinfo.getSystemLoadAverage()
        };
    }
    private static String[] getHeapInfo() {
        Runtime getHeapInfo = Runtime.getRuntime();
        return new String[]{
                YELLOW + "Total Heap > " + RESET + getHeapInfo.totalMemory(),
                YELLOW + "Max Heap > " + RESET + getHeapInfo.maxMemory(),
                YELLOW + "Free Heap > " + RESET + getHeapInfo.freeMemory(),
                YELLOW + "Usable Heap > " + RESET + (getHeapInfo.maxMemory() - getHeapInfo.freeMemory())
        };
    }

    public static void allCommands() {
        new LinkedList<>(
                List.of("--help      / --hp = familiarization with commands of the programm",
                        "--add          /       --ad = add file and write information in computer system",
                        "--read         /       --rd = read the information from the file",
                        "--delete       /       --de = delete the file from computer",
                        "--copy         /       --cp = copy the data from one file to other file",
                        "--move         /       --mv = move the file from one disk to other disk",
                        "--newname      /       --nn = rename the file",
                        "--stopgap      /       --sg = create the stopgap (temporary) file",
                        "--GUI          /       --gi = GUI's version of the Console Utility",
                        "--jar          /       --jr = creation of the jar files",
                        "--zip,--tar.gz / --zp,--tar = creation of the zip files in Windows (tar.gz in Linux)",
                        "--write        /       --wt = write the text or data to the definite file",
                        "--grep         /       --gp = find the text or word in the file",
                        "--history      /       --hi = show the history of the commands which were used in the ConsoleUtility",
                        "--find         /       --fd = find the files with the definite extension",
                        "--lstcat       /       --ls = analyse and read all files from the definite catalog",
                        "--replace      /       --re = replace the char symbol in the text of the file to another",
                        "--crtdir       /       --cr = create the directory in the system's explorer",
                        "--candir       /       --ca = cancel the directory from the system's explorer",
                        "--exstdirs     /       --ex = read all directories which the user created und exist",
                        "--tldr         /       --tl = instruction how use every command in ConsoleUtility with syntax.",
                        "--chgrits      /       --cs = change the rights for your file with octal system",
                        "--chgextn      /       --cx = change the extension for your file",
                        "--symlink      /       --sl = symbol link for directories",
                        "--empty        /       --em = delete the date from the file",
                        "--sort         /       --st = sort the data from the file",
                        "--reverse      /       --rv = read the data from end to begin",
                        "--remall       /       --ra = remove all catalogies in the directory",
                        "--remove       /       --rm = remove the definite catalog in the directory",
                        "--integrate    /       --ig = integrate the catalog to the directory",
                        "--sizfls       /       --sf = analysis all files in the directory or definite file with size in bytes",
                        "--edit         /       --et = edit the file in GUI view when user wants to write all text data in file.",
                        "--symcnt       /       --sc = count all symbols from text of your file",
                        "--resize       /       --rs = change the size of the file",
                        "--version      /       --vs = show the version of your ConsoleUtility",
                        "--backup       /       --bp = create the reserve copy for saving data of your file",
                        "--xexport      /       --xp = export the data in XML file",
                        "--ximport      /       --xm = import the data from XML file",
                        "--restore      /       --rt = return the data from backup file",
                        "--stats        /       --ss = time of using this utility",
                        "--search       /       --sh = search the definite command",
                        "--hostinfo     /       --ho = information about your host settings",
                        "--shutdown     /       --sd = stop the working of your computer",
                        "--restart      /       --rr = restart your computer",
                        "--fmem         /       --fm = check the information about your free memory in bytes",
                        "--clean        /       --cn = clean the history of the commands",
                        "--ping         /       --pg = ping the IP, DNS in your computer",
                        "--intproc      /       --ip = show the user's own IP in his computer or another device",
                        "--interrupt    /       --ir = interrupt thw working of the definite process in your computer",
                        "--filter       /       --fr = filter the files in directory with definite size",
                        "--sha256gen    /       --sn = get data from file in hex-byte system with sha256 algorithm",
                        "--md5gen       /       --mg = get data from file in hex-byte system with md5 algorithm",
                        "--freeze       /       --fe = freeze the process with id",
                        "--unique       /       --uq = get the unique and sorted data from files",
                        "--stat         /       --sa = description for file",
                        "--split        /       --sp = split one file on two files",
                        "--rsync        /       --rc = synchronize data from one file to other file",
                        "--cmp          /       --cm = compare data between two sorted files",
                        "--sysinfo      /       --si = system information about user's computer",
                        "--recent       /       --rn = show the information about recent enter for register or login in utility",
                        "--active       /       --ae = show the information about username which actived in utility",
                        "--username     /       --un = show the username for user",
                        "--preview      /       --pw = show the list with future commands which will integrate in utility",
                        "--cut          /       --ct = cut the part of data from file",
                        "--chgpass      /       --ps = change the password in Utility's system",
                        "--logout       /       --lg = log out from Utility. Deleting username and password",
                        "--fcmd         /       --fc = find command from history of commands",
                        "--gzip         /       --gz = compress and uncompress data from file with bytes",
                        "--fdir         /       --fi = find directory from list of directories",
                        "--erase        /       --es = erase the list of directories",
                        "--mirror       /       --mr = reverse data from the file with bytes",
                        "--update       /       --ue = update the configuration files for utility"
                )).forEach(System.out::println);
    }
    private static class ConsoleUtilitysGUI extends JFrame {
        private final JLabel selectedLabel;
        public ConsoleUtilitysGUI() {
            super("Console Utility's GUI");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            JButton saveFile = new JButton("Save the file: "),
                    openDirectory = new JButton("Open the directory: ");
            JPanel panel = new JPanel();
            panel.add(saveFile);
            panel.add(openDirectory);
            setContentPane(panel);
            selectedLabel = new JLabel("No file/directory selected.");
            pack();
            setLocationRelativeTo(null);
            saveFile.addActionListener(this::actionPerformed);
            openDirectory.addActionListener(this::actionPerformed2);
            setVisible(true);
        }
        private void saveTheFileChooser() {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showSaveDialog(this);
            if(result == JFileChooser.APPROVE_OPTION) {
                selectedLabel.setText("Selected file: " + fileChooser.getSelectedFile().getAbsolutePath());
                System.out.println("Selected file: " + fileChooser.getSelectedFile().getAbsolutePath());
            } else {
                if(result == JFileChooser.CANCEL_OPTION) {
                    System.out.println("Command is cancelled");
                }
            }
        }
        private void openDirectoryChooser() {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showSaveDialog(this);
            if(result == JFileChooser.APPROVE_OPTION) {
                selectedLabel.setText("Selected file: " + fileChooser.getSelectedFile().getAbsolutePath());
                System.out.println("Directory: " + fileChooser.getSelectedFile().getAbsolutePath());
            } else {
                if(result == JFileChooser.CANCEL_OPTION) {
                    System.out.println("Command is cancelled");
                }
            }
        }
        private void actionPerformed(ActionEvent chooser) {
            saveTheFileChooser();
        }
        private void actionPerformed2(ActionEvent choose) {
            openDirectoryChooser();
        }
    }

    private static class TextEditor extends JFrame {
        private final JTextArea textArea;
        private final JFileChooser fileChooser;

        public TextEditor() {
            setTitle("File editor");
            setSize(800, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            textArea = new JTextArea();
            JScrollPane scrollPane = new JScrollPane(textArea);
            add(scrollPane, BorderLayout.CENTER);
            fileChooser = new JFileChooser();
            initMenu();
        }

        private void initMenu() {
            JMenuBar menuBar = new JMenuBar();
            JMenu fileMenu = new JMenu("File");
            fileMenu.setSize(300, 300);
            JMenuItem open = new JMenuItem("Open");
            open.addActionListener(e -> openFile());
            JMenuItem save = new JMenuItem("Save");
            save.addActionListener(e -> saveFile());
            fileMenu.add(open);
            fileMenu.add(save);
            menuBar.add(fileMenu);
            setJMenuBar(menuBar);
        }

        private void openFile() {
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    StringBuilder content = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    textArea.setText(content.toString());
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, RED + "Read file error" + RESET);
                }
            }
        }

        private void saveFile() {
            int returnVal = fileChooser.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(textArea.getText());
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, RED + "Save file error" + RESET);
                }
            }
        }
    }
    private static class Preview {
        public List<String> commandList() {
            return new ArrayList<>(List.of("chgatr (change attributes for files)"));
        }
    }
}
