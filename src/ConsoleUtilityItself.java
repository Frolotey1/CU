import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.jar.*;
import java.util.logging.*;
import java.util.stream.Stream;
import java.util.zip.*;

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
            RESET = ESC + "[0m";
    public static void main(String[] args) throws IOException {
        List<String> rights = new ArrayList<>(List.of(
                "---","--x","-w-","-wx","r--","r-x","rw-","rwx"));
        List<Character> numberRights = new ArrayList<>();
        List<String>resultRights = new ArrayList<>();
        List<String> historyOFCommands = loadHistory();
        int index = historyOFCommands.size(), indexPath = loadPathsOfDirectory().size();
        Logger loggerForProgramm = Logger.getLogger(ConsoleUtilityItself.class.getName());
        FileHandler programmError = new FileHandler("LogFileWithErrors.log",true);
        programmError.setFormatter(new SimpleFormatter());
        loggerForProgramm.addHandler(programmError);
        File file;
        Scanner operation = new Scanner(System.in);
        if(args.length == 0) {
            loggerForProgramm.log(Level.WARNING, RED + "You must have not less than 2 arguments in this console utility" + RESET);
        }
        for (String arg : args) {
            switch (arg) {
                case "--help", "--hp" -> {
                    appendHistory((index++) + " | " + arg);
                    allCommands();
                }
                case "--add", "--ad" -> {
                    appendHistory((index++) + " | " + arg);
                    System.out.println("Write the name for file: ");
                    String nameFile = operation.nextLine();
                    System.out.println("Write the text for file: ");
                    String text = operation.nextLine();
                    file = new File(nameFile);
                    try (BufferedWriter write = new BufferedWriter(new FileWriter(file))) {
                        write.write(text);
                    } catch (IOException e) {
                        loggerForProgramm.log(Level.WARNING, RED + "This is error for writing a text in file" + RESET);
                    }
                }
                case "--read", "--rd" -> {
                    appendHistory((index++) + " | " + arg);
                    System.out.println("Write the dateiName fur checking his existence: ");
                    String nameFile = operation.nextLine();
                    if (Files.exists(Path.of(nameFile))) {
                        try (BufferedReader bekommen = new BufferedReader(new FileReader(nameFile))) {
                            System.out.println(bekommen.readLine());
                        } catch (IOException e) {
                            loggerForProgramm.log(Level.WARNING, RED + "This is error for writing a text in file" + RESET);
                        }
                    } else {
                        System.err.println(RED + "This file doesn't exist" + RESET);
                    }
                }
                case "--delete", "--de" -> {
                    appendHistory((index++) + " | " + arg);
                    System.out.println("Write the name of the file for checking existence: ");
                    String nameFile = operation.nextLine();
                    file = new File(nameFile);
                    if (Files.exists(file.toPath())) {
                        System.out.println(GREEN + "This file: " + file.toPath() + " was deleted successfully" + RESET);
                        Files.delete(file.toPath());
                    } else {
                        System.err.println(RED + "This file doesn't exist" + RESET);
                    }
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
                        System.out.println(GREEN + "This file was moved to other disk successfully" + RESET);
                        Files.move(file.toPath(), Path.of(newDisk + ":\\",file.getName()), StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        System.err.println(RED + "This file doesn't exist" + RESET);
                    }
                }
                case "--newname", "--nn" -> {
                    appendHistory((index++) + " | " + arg);
                    System.out.println("Write the file, which you want to rename: ");
                    String theFile = operation.nextLine();
                    file = new File(theFile);
                    if(Files.exists(file.toPath())) {
                        System.out.println("Write the new name for the file: ");
                        String newName = operation.nextLine();
                        String[] saveTheData = new String[2]; saveTheData[0] = file.getName();
                        try(BufferedReader readTheDataFromFile = new BufferedReader(new FileReader(file))) {
                            saveTheData[1] = readTheDataFromFile.readLine();
                            saveTheData[0] = newName;
                        }
                        Files.delete(file.toPath());
                        file = new File(saveTheData[0]);
                        try(BufferedWriter writeTheDataFromThePastFile = new BufferedWriter(new FileWriter(file))) {
                            writeTheDataFromThePastFile.write(saveTheData[1]);
                        } catch (IOException e) {
                            System.err.println(RED + "This is the error of the input/output operations" + RESET);
                        }
                    }
                }
                case "--taskmgr", "--tm" -> {
                    appendHistory((index++) + " | " + arg);
                    if(Desktop.isDesktopSupported()) {
                        file = new File("C:\\Windows\\System32\\Taskmgr.exe");
                        Desktop desktop = Desktop.getDesktop();
                        desktop.open(file);
                    } else {
                        System.err.println(RED + "System doesn't support including the taskmgr" + RESET);
                    }
                }
                case "--stopgap", "--sg" -> {
                    appendHistory((index++) + " | " + arg);
                    System.out.println("Write the name for the stopgap file: ");
                    String stopGapNameForFile = operation.nextLine();
                    System.out.println("Write the extension for the file: ");
                    String extension = operation.nextLine();
                    try {
                        Path stopGapPath = Files.createTempFile(stopGapNameForFile,extension);
                        System.out.println(GREEN + "The stopgap file was created successfully: " +
                                stopGapPath.toAbsolutePath() + RESET);
                        Files.writeString(stopGapPath,"The stopgap file was created successfully | " + LocalDateTime.now());
                    } catch (FileSystemException e) {
                        throw new RuntimeException(e.getLocalizedMessage());
                    }
                }
                case "--GUI", "--gi" -> {
                    appendHistory((index++) + " | " + arg);
                    System.out.println(new ConsoleUtilitysGUI());
                }
                case "--jar", "--jr","--zip","--zp","--tar.gz","--tr" -> {
                    appendHistory((index++) + " | " + arg);
                    String name;
                    if(Objects.equals(arg,"--jar") || Objects.equals(arg, "--j")) {
                        System.out.println("Write the name for jar file: ");
                        String fileName = operation.nextLine();
                        String jarName = "";
                        if(Files.exists(Path.of(fileName))) {
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
                        if(Files.exists(Path.of(fileName))) {
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
                case "--write","--wt" -> {
                    appendHistory((index++) + " | " + arg);
                    System.out.println("Write the text or data: ");
                    String data = operation.nextLine();
                    System.out.println("Write the filename where you want to write the data in: ");
                    String filename = operation.nextLine();
                    String name;
                    if(Files.exists(Path.of(filename))) {
                        name = filename;
                    } else {
                        System.out.println("This file doesn't exist. Create the new: ");
                        name = operation.nextLine();
                    }
                    try(FileOutputStream fos = new FileOutputStream(name)) {
                        fos.write(data.getBytes());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex.getLocalizedMessage());
                    }
                }
                case "--grep","--gp" -> {
                    appendHistory((index++) + " | " + arg);
                    System.out.println("Write the file where you want to become the text from: ");
                    String filename = operation.nextLine();
                    String name, data;
                    if(Files.exists(Path.of(filename))) {
                        name = filename;
                    } else {
                        System.out.println("This file doesn't exist. Create the new");
                        name = operation.nextLine();
                        System.out.println("Write the text in the file: ");
                        data = operation.nextLine();
                        try(BufferedWriter writeTo = new BufferedWriter(new FileWriter(name))) {
                            writeTo.write(data);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex.getLocalizedMessage());
                        }
                    }
                    System.out.println("Write the word or text which you want to become from the file: ");
                    String text = operation.nextLine();
                    String textFrom;
                    int countTheWords;
                    try(BufferedReader readFrom = new BufferedReader(new FileReader(name))) {
                        textFrom = readFrom.readLine();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex.getLocalizedMessage());
                    }
                    StringTokenizer token = new StringTokenizer(textFrom);
                    List<String> saveTheTokens = new LinkedList<>();
                    while(token.hasMoreTokens()) {
                        saveTheTokens.add(token.nextToken());
                    }
                    countTheWords = (int) saveTheTokens.stream().filter(object -> Objects.equals(object,text)).count();
                    for(int findText = 0; findText < saveTheTokens.size(); ++findText) {
                        if(Objects.equals(saveTheTokens.get(findText), text)) {
                            saveTheTokens.set(findText,RED + text + RESET);
                        }
                    }
                    saveTheTokens.add("| " + countTheWords);
                    for(String result : saveTheTokens) {
                        System.out.printf("%s ",result);
                    }
                    System.out.println("\n");
                    saveTheTokens.clear();
                }
                case "--history","--hi" -> {
                    appendHistory((index++) + " | " + arg);
                    loadHistory().forEach(System.out::println);
                }
                case "--find","--fd" -> {
                    appendHistory((index++) + " | " + arg);
                    System.out.println("Write the directory where you want to find the files: ");
                    String directory = operation.nextLine();
                    if(!directory.startsWith("C:\\") && !directory.startsWith("/")) {
                        System.err.println("Directories must be started with C:\\ (for Windows) or / (for Linux)");
                    } else {
                        Path analysis = Path.of(directory);
                        System.out.println("Write the extension of the files for searching: ");
                        String extension = operation.nextLine();
                        if(!extension.startsWith(".")) {
                            System.err.println(RED + "Extensions must be started with '.'" + RESET);
                        } else {
                            try(Stream<Path> paths = Files.walk(analysis)) {
                                paths.filter(Files::isRegularFile).filter(isTxt -> isTxt.toString().endsWith(extension) &&
                                        !Objects.equals(isTxt.toString(),new File("HistoryFile.txt").getAbsolutePath())
                                        && !Objects.equals(isTxt.toString(),new File("PasswordManager.txt").getAbsolutePath())).forEach(System.out::println);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex.getLocalizedMessage());
                            }
                        }
                    }
                }
                case "--lstcat", "--ls" -> {
                    appendHistory((index) + " | " + arg);
                    System.out.println("Write the directory where want you to see the catalogs: ");
                    String directory = operation.nextLine();
                    if(!directory.startsWith("C:\\") && !directory.startsWith("/")) {
                        System.err.println("The directories must started with C:\\ (for Windows) or / (for Linux}: ");
                    } else {
                        try(Stream<Path> paths = Files.walk(Path.of(directory))) {
                            paths.forEach(System.out::println);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex.getLocalizedMessage());
                        }
                    }
                }
                case "--replace","--re" -> {
                    appendHistory((index) + " | " + arg);
                    System.out.println("Write the file where will you change the chars in: ");
                    String filename = operation.nextLine();
                    String name, data = "", firstChar, secondChar, newData;
                    if(Files.exists(Path.of(filename))) {
                        name = filename;
                    } else {
                        System.out.println("This file doesn't exist. Create the new: ");
                        name = operation.nextLine();
                    }
                    System.out.println("Write the first char which you want to change: ");
                    firstChar = operation.nextLine();
                    System.out.println("Write the second char for changing: ");
                    secondChar = operation.nextLine();
                    try(BufferedReader readFrom = new BufferedReader(new FileReader(name))) {
                        String line = readFrom.readLine();
                        if(line.isEmpty()) {
                            System.err.println(RED + "There is not string for replacing" + RESET);
                        } else {
                            data = line;
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex.getLocalizedMessage());
                    }
                    char first = firstChar.charAt(0), second = secondChar.charAt(0);
                    newData = data.replace(first,second);
                    System.out.println(GREEN + newData + RESET);
                    try(BufferedWriter writeNewData = new BufferedWriter(new FileWriter(name))) {
                        writeNewData.write(newData);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex.getLocalizedMessage());
                    }
                }
                case "--crtdir", "--cr" -> {
                    appendHistory((index) + " | " + arg);
                    System.out.println("Write the name for the directory: ");
                    String directory = operation.nextLine();
                    System.out.println("Write the main directory: ");
                    String mainDirectory = operation.nextLine();
                    if(!mainDirectory.startsWith("C:\\") && !mainDirectory.startsWith("/")) {
                        System.err.println(RED + "The directories must started with C:\\ (for Windows) or / (for Linux}: " + RESET);
                    } else {
                        if(!mainDirectory.endsWith("/")) {
                            mainDirectory = mainDirectory + "/";
                        }
                        String createDir = mainDirectory + directory;
                        appendPathOfDirectory(indexPath + " | " + createDir);
                        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxr-xr-x");
                        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
                        Path path = Path.of(createDir);
                        Files.createDirectory(path,attr);
                        System.out.println(GREEN + "The directory was created: " + path.toAbsolutePath() + RESET);
                    }
                }
                case "--candir", "--ca" -> {
                    appendHistory((index++) + " | " + arg);
                    System.out.println("Write the directory which you want to delete: ");
                    String findDirectory = operation.nextLine();
                    if(!findDirectory.startsWith("C:\\") && !findDirectory.startsWith("/")) {
                        System.err.println(RED + "The directories must started with C:\\ (for Windows) or / (for Linux}: " + RESET);
                    } else {
                        Path path = Path.of(findDirectory);
                        Files.deleteIfExists(path);
                        if(!Files.isDirectory(path)) {
                            Path fromFile = Paths.get("Directory.txt");
                            List<String> removeTheDirectory = new ArrayList<>(Files.readAllLines(fromFile));
                            removeTheDirectory.remove(findDirectory);
                            Files.delete(fromFile);
                            for(String rewriteAllDirectories : removeTheDirectory) {
                                appendPathOfDirectory(rewriteAllDirectories);
                            }
                            System.out.println(GREEN + "The directory '" + findDirectory + "' was deleted successfully" + RESET);
                        } else {
                            System.out.println("The directory '" + findDirectory + "' exists yet");
                        }
                    }
                }
                case "--tldr","--tl" -> {
                    appendHistory(index + " | " + arg);
                    String []allCommandsInstruction =
                            {"",
                                    "add","read","delete","copy",
                                    "move","newname","taskmgr","stopgap",
                                    "GUI","jar","zip / tar.gz","write",
                                    "grep","history","find","lstcat",
                                    "replace","crtdir","candir","exstdirs"
                            };
                    for(int i = 1; i < allCommandsInstruction.length; ++i) {
                        System.out.println(i + ") " + allCommandsInstruction[i]);
                    }
                    System.out.println("Write the command (1,2,3,4....) for which you want to see the instruction how use: ");
                    int commandChoose = operation.nextInt();
                    switch(commandChoose) {
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
                                "--taskmgr / --tm [ENTER] (for Windows System) -> " +
                                "(in Windows will started the taskmgr window with tasks which work in the Windows system). " +
                                "In Linux system this command will not started)");
                        case 8 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--stopgap / --sg [ENTER] -> " +
                                "Write the name for the stopgap file: -> (name for the temp file) -> [ENTER] -> " +
                                "Write the extension for the file: -> (extension for your file. Example: .txt,.bin and etc.) -> [ENTER} " +
                                "-> <MESSAGE> 'The stopgap file was created successfully: (path to your temp file)'");
                        case 9 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                        "java src\\ConsoleUtilityItself.java (Windows) " +
                                        "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                        "--GUI / --gi [ENTER] -> (GUI's version for ConsoleUtility. Work in Windows and Linux)");
                        case 10 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--jar / --jr [ENTER] -> " +
                                "Write the name for jar file: -> (name for the jar file. Example: java.jar) -> [ENTER] -> " +
                                "{IF JAR FILE EXISTS} -> Write the file which you want to include to the jar: " +
                                        "(your file. Example: test.txt) {ELSE} This jar file doesn't exist. Create the new: " +
                                        "-> (the new jar file) -> [ENTER] -> " +
                                "<MESSAGE> Jar file was created successfully: (path to your jar file)");
                        case 11 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--zip,--zp (for Windows) / --tar.gz,--tr [ENTER] -> " +
                                "Write the name for zip (or tar.gz for Linux) file: -> [ENTER] -> " +
                                "Write the file which you want to include to the zip (or tar.gz): -> (your file. Example: test.txt) " +
                                        "-> [ENTER] -> {IF YOUR FILE EXISTS} -> <MESSAGE> Zip file was created successfully: (path to your zip file) -> " +
                                "{ELSE} This zip (tar.gz) file doesn't exist. Create the new: -> (your file with zip,tar.gz)");
                        case 12 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                        "java src\\ConsoleUtilityItself.java (Windows) " +
                                        "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                        "--write,--wt -> [ENTER] -> " +
                                        "Write the text or data: -> (your text) -> [ENTER] -> " +
                                        "Write the filename where you want to write the data in: -> (name for your file) -> [ENTER] -> " +
                                        "{IF EXISTS} -> (your text was written to the file) {ELSE} -> This file doesn't exist. Create the new: ");
                        case 13 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--grep,--gp -> [ENTER] -> " +
                                "Write the file where you want to get the text from: -> (your file. Example: test.txt) -> [ENTER] -> " +
                                "{IF EXISTS} -> Write the word or text which you want to become from the file: (word from your text) {ELSE} -> " +
                                "This file doesn't exist. Create the new -> (create the new file) -> [ENTER] -> Write the text in the file: -> " +
                                "(text to the file)");
                        case 14 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--history / --hi -> [ENTER] -> (will showed the list of the commands which you used in the ConsoleUtility)");
                        case 15 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--find / --fd -> [ENTER] -> " +
                                "Write the directory where you want to find the files: -> (your directory) -> [ENTER] -> " +
                                "Write the extension of the files for searching: -> (your extension for files: Example: .txt,.bin) -> [ENTER] -> " +
                                "(will showed all files / catalogs with definite extension)");
                        case 16 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--lstcat / --ls -> [ENTER] -> " +
                                "Write the directory where want you to see the catalogs: -> (your directory) -> [ENTER] -> " +
                                "(will showed all the files / catalogs in the definite directory)");
                        case 17 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--replace / --re -> [ENTER] -> " +
                                "Write the file where will you change the chars in: -> (your file. Example: test.txt) -> [ENTER] -> " +
                                "{IF EXISTS} -> Write the first char which you want to change: (first char. Example: c) -> [ENTER] -> " +
                                "Write the second char for changing: -> (second char. Example: i) -> [ENTER] -> (will replaced first char to second char)");
                        case 18 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--crtdir / --cr -> [ENTER] -> " +
                                "Write the name for the directory: -> (your directory) -> [ENTER] -> " +
                                "Write the main directory: (your main directory) (Example: C:\\Users\\... or /home/...) -> [ENTER] -> " +
                                "<MESSAGE> 'The directory was created: (your created directory)'");
                        case 19 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--candir / --ca -> [ENTER] -> " +
                                "Write the directory which you want to delete: (your directory for deleting) -> [ENTER] -> " +
                                "The directory (your directory) was deleted successfully");
                        case 20 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--exstdirs / --ex -> [ENTER] -> " +
                                "(will showed all directories which were created with crtdir)");
                        case 21 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--chgrits / --cs -> [ENTER] -> " +
                                "Write the file which you want to change the rights for using this file in: -> (your file with name) -> [ENTER] -> " +
                                "Write the rights in the view of the octal system (Example: 700): (rights for your file. Example: 700) -> [ENTER] -> " +
                                "{IF SUCCESS} -> <MESSAGE> The rights for the file: '(name for your saving file)' were changed successfully");
                        case 22 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--chgextn / --cx -> [ENTER] -> " +
                                "Write your file without extension: (your file without extension) -> [ENTER] -> " +
                                "Write the extension for your file: (extension for your file) -> [ENTER] -> " +
                                "Write the new extension for your file: (new extension for your file) -> [ENTER] -> " +
                                "{IF SUCCESS} -> <MESSAGE> The file's extension was changed successfully");
                        case 23 -> System.out.println("your path (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "or /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--symlink / --sy -> [ENTER] -> " +
                                "Write the directory for which you want to create the symbolic link: (your directory) -> [ENTER] -> " +
                                "Write the symbolic link: (symbolic link for your file) -> [ENTER] -> " +
                                "{IF SUCCESS} -> <MESSAGE> The symbolic link was created successfully: (path to your link directory)"
                                );
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
                    String filename = operation.nextLine();
                    Path path = Path.of(filename);
                    if(Files.exists(path)) {
                        String saveFileName = filename, line;
                        try(BufferedReader readFromFileDate = new BufferedReader(new FileReader(filename))) {
                            line = readFromFileDate.readLine();
                            if(line.isEmpty()) {
                                line = "";
                            }
                        } catch (IOException ex) {
                            throw new RuntimeException(ex.getLocalizedMessage());
                        }
                        Files.delete(path);
                        System.out.println("Write the rights in the view of the octal system (Example: 700): ");
                        String octalSystem = operation.nextLine();
                        if(octalSystem.length() > 3) {
                            System.err.println(RED + "The rights must be not more than 3 numbers" + RESET);
                        } else {
                            for(Character charNumbers : octalSystem.toCharArray()) {
                                numberRights.add(charNumbers);
                            }
                            List<Integer> numbers = new ArrayList<>();
                            for(int i = 0; i < octalSystem.length(); ++i) {
                                int toNumber = octalSystem.charAt(i) - 48;
                                numbers.add(toNumber);
                            }
                            for (Integer number : numbers) {
                                resultRights.add(rights.get(number));
                            }
                            String allRights = resultRights.getFirst() + resultRights.get(1) + resultRights.getLast();
                            Set<PosixFilePermission> perms = PosixFilePermissions.fromString(allRights);
                            FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
                            Files.createFile(Path.of(saveFileName),attr);
                            try(BufferedWriter writeBackToFile = new BufferedWriter(new FileWriter(saveFileName))) {
                                writeBackToFile.write(line);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex.getLocalizedMessage());
                            }
                            System.out.println(GREEN + "The rights for the file: '" + saveFileName + "' were changed successfully" + RESET);
                        }
                    }
                }
                case "--chgextn", "--cx" -> {
                    appendHistory((index) + " | " + arg);
                    String filename,newFileName = "",extension,newExtension,isExists,data = "";
                    System.out.println("Write your file without extension: ");
                    filename = operation.nextLine();
                    System.out.println("Write the extension for your file: ");
                    extension = operation.nextLine();
                    isExists = filename + extension;
                    Path path = Path.of(isExists);
                    if(Files.exists(path)) {
                        try(BufferedReader readFromFile = new BufferedReader(new FileReader(isExists))) {
                            String line = readFromFile.readLine();
                            if(line == null || line.isEmpty()) {
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
                    if(!newExtension.startsWith(".")) {
                        System.err.println(RED + "Extensions must be started with ." + RESET);
                    } else {
                        newFileName = filename + newExtension;
                    }
                    try(BufferedWriter writeBackToFile = new BufferedWriter(new FileWriter(newFileName))) {
                        writeBackToFile.write(data);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex.getLocalizedMessage());
                    }
                    System.out.println(GREEN + "The file's extension was changed successfully" + RESET);
                }
                case "--symlink","--sl" -> {
                    appendHistory((index) + " | " + arg);
                    String symlink, directory;
                    System.out.println("Write the directory for which you want to create the symbolic link: ");
                    directory = operation.nextLine();
                    Path path = Path.of("Directory.txt");
                    List<String> findDirectory = new ArrayList<>(Files.readAllLines(path));
                    String foundDirectory = findDirectory.get(findDirectory.indexOf(directory) + 2);
                    System.out.println(foundDirectory);
                    if(foundDirectory.isEmpty()) {
                        System.err.println(RED + "This directory doesn't exist" + RESET);
                    } else {
                        Path target = Path.of(foundDirectory);
                        System.out.println("Write the symbolic link: ");
                        symlink = operation.nextLine();
                        Files.createSymbolicLink(Path.of(symlink),target);
                        System.out.println(GREEN + "The symbolic link was created successfully: " + new File(symlink).getAbsolutePath() + RESET);
                    }
                }
                case null, default -> System.err.println(RED + "This operation doesn't exist" + RESET);
            }
        }
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
                        "--taskmgr      /       --tm = include the Taskmgr.exe from the system files in Windows",
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
                        "--symlink      /       --sl = symbol link for directories"
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
}
