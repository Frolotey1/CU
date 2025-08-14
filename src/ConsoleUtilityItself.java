import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.jar.*;
import java.util.logging.*;
import java.util.stream.Stream;
import java.util.zip.*;

public class ConsoleUtilityItself {
    private static final Path HistoricalFile = Path.of(System.getProperty("user.home"), ".consoleutility_history");
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
        List<String> historyOFCommands = loadHistory();
        int index = historyOFCommands.size();
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
                        System.out.println("Write the new name for the file:");
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
                case "--jar", "--jr","--zip","--zp" -> {
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
                        System.out.println("Write the name for zip file: ");
                        String fileName = operation.nextLine();
                        String zipName = "";
                        if(Files.exists(Path.of(fileName))) {
                            name = fileName;
                            System.out.println("Write the file which you want to include to the zip");
                            zipName = operation.nextLine();
                        } else {
                            System.out.println("This zip file doesn't exist. Create the new: ");
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
                    if(!directory.startsWith("C:\\") || !directory.startsWith("/")) {
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
                case null, default -> System.err.println(RED + "This operation doesn't exist" + RESET);
            }
        }
    }
    public static void allCommands() {
        new LinkedList<>(
                List.of("--help       / --hp = familiarization with commands of the programm",
                        "--add        / --ad = add file and write information in computer system",
                        "--read       / --rd = read the information from the file",
                        "--delete     / --de = delete the file from computer",
                        "--copy       / --cp = copy the data from one file to other file",
                        "--move       / --mv = move the file from one disk to other disk",
                        "--newname    / --nn = rename the file",
                        "--taskmgr    / --tm = include the Taskmgr.exe from the system files in Windows",
                        "--stopgap    / --sg = create the stopgap (temporary) file",
                        "--GUI        / --gi = GUI's version of the Console Utility",
                        "--jar        / --jr = creation of the jar files",
                        "--zip        / --zp = creation of the zip files",
                        "--write      / --wt = write the text or data to the definite file",
                        "--grep       / --gp = find the text or word in the file",
                        "--history    / --hi = show the history of the commands which were used in the ConsoleUtility",
                        "--find       / --fd = find the files with the definite extension (exclude HistoryFile.txt and PasswordManager.txt if user wants to find the files with .txt extensions)"
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
