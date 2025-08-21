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

public class KonsoleDienstProgramm {
    private static final
    Path
            HistorischDatei = Path.of(System.getProperty("user.home"), ".consoleutility_history"),
            DirektoreiDatei = Path.of("Directory.txt");
    private static void hinzufugenWegDirectoreis(String direktoreiName) throws IOException {
        Files.writeString(DirektoreiDatei, direktoreiName + System.lineSeparator(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
    private static ArrayList<String> loadenWegDirectoreis() throws IOException {
        return new ArrayList<>(Files.readAllLines(DirektoreiDatei));
    }
    private static List<String> loadenGeschichte() throws IOException {
        if (Files.exists(HistorischDatei)) {
            return new ArrayList<>(Files.readAllLines(HistorischDatei));
        }
        return new ArrayList<>();
    }
    private static void hinzufugenGeschichte(String befehle) throws IOException {
        Files.writeString(HistorischDatei, befehle + System.lineSeparator(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
    private static final String
            ESC = "\u001B",
            GREEN = ESC + "[32m",
            RED = ESC + "[31m",
            RESET = ESC + "[0m";
    public static void main(String[] args) throws IOException {
        List<String> geschichteBefehlen = loadenGeschichte();
        int index = geschichteBefehlen.size(), indexWeg = loadenWegDirectoreis().size();
        File datei;
        Scanner operation = new Scanner(System.in);
        Logger loggerFurProgramm = Logger.getLogger(KonsoleDienstProgramm.class.getName());
        FileHandler programmFehler = new FileHandler("DateiLogFehler.log",true);
        programmFehler.setFormatter(new SimpleFormatter());
        loggerFurProgramm.addHandler(programmFehler);
        if(args.length == 0) {
            loggerFurProgramm.log(Level.WARNING, RED + "Sie mussel haben mehr als 0 argumenten ins Konsole DienstProgramm" + RESET);
        }
        for (String arg : args) {
            switch (arg) {
                case "--helfen", "--hel" -> alleBefehlen();
                case "--hinzufugen", "--hin" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben eine name fur datei: ");
                    String dateiName = operation.nextLine();
                    System.out.println("Schreiben ein text fur datei: ");
                    String text = operation.nextLine();
                    datei = new File(dateiName);
                    try (BufferedWriter schreiben = new BufferedWriter(new FileWriter(datei))) {
                        schreiben.write(text);
                    } catch (IOException e) {
                        loggerFurProgramm.log(Level.WARNING, RED + "Das ist fehler fur schreiben eines text zu datei" + RESET);
                    }
                }
                case "--lesen", "--les" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben eine dateiName fur prufung es existieren: ");
                    String dateiName = operation.nextLine();
                    if (Files.exists(Path.of(dateiName))) {
                        try (BufferedReader bekommen = new BufferedReader(new FileReader(dateiName))) {
                            System.out.println(bekommen.readLine());
                        } catch (IOException e) {
                            loggerFurProgramm.log(Level.WARNING, RED + "Das ist fehler fur schreiben eines text zu datei" + RESET);
                        }
                    } else {
                        System.err.println(RED + "Diese datei existiert nicht" + RESET);
                    }
                }
                case "--loschen", "--los" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben eine dateiName fur prufung es existieren: ");
                    String dateiName = operation.nextLine();
                    datei = new File(dateiName);
                    if (Files.exists(datei.toPath())) {
                        System.out.println(GREEN + "Diese datei: " + datei.toPath() + " war loscht erfolgreich" + RESET);
                        Files.delete(datei.toPath());
                    } else {
                        System.err.println(RED + "Diese datei existiert nicht" + RESET);
                    }
                }
                case "--kopieren", "--kop" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben eine dateiName fur prufung es existieren: ");
                    String dateiName = operation.nextLine();
                    System.out.println("Schreiben eine neue datei: ");
                    String neueDatei = operation.nextLine();
                    datei = new File(dateiName);
                    if (Files.exists(datei.toPath())) {
                        System.out.println("Kopierung einen datei zu andere datei ist erfolgreich: ");
                        Files.copy(Path.of(neueDatei), datei.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        System.err.println(RED + "Diese datei existiert nicht" + RESET);
                    }
                }
                case "--bewegen", "--bew" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben eine dateiName fur prufung es existieren: ");
                    String dateiName = operation.nextLine();
                    System.out.println("Schreiben eine neue laufwerk: ");
                    String neueWerk = operation.nextLine();
                    datei = new File(dateiName);
                    if (Files.exists(datei.toPath())) {
                        System.out.println("Diese datei was zu andere LaufWerk bewegt");
                        Files.move(datei.toPath(), Path.of(neueWerk.charAt(0) + ":\\", datei.getName()), StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        System.err.println(RED + "Diese datei existiert nicht" + RESET);
                    }
                }
                case "--umbenennen", "--umb" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben ein datei, welche wollen sie umbenennen: ");
                    String einDatei = operation.nextLine();
                    datei = new File(einDatei);
                    if (Files.exists(datei.toPath())) {
                        System.out.println("Schreiben eine neue name fur datei:");
                        String neueName = operation.nextLine();
                        String[] datenSpreichern = new String[2];
                        datenSpreichern[0] = datei.getName();
                        try (BufferedReader lesenDatenVomDatei = new BufferedReader(new FileReader(datei))) {
                            datenSpreichern[1] = lesenDatenVomDatei.readLine();
                            datenSpreichern[0] = neueName;
                        }
                        Files.delete(datei.toPath());
                        datei = new File(datenSpreichern[0]);
                        try (BufferedWriter schreibenDatenVomVergangenheitischDatei = new BufferedWriter(new FileWriter(datei))) {
                            schreibenDatenVomVergangenheitischDatei.write(datenSpreichern[1]);
                        } catch (IOException e) {
                            System.err.println(RED + "Das ist ein fehler mit eingang/ausgang operationen" + RESET);
                        }
                    }
                }
                case "--taskmanageren", "--tas" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    if(Desktop.isDesktopSupported()) {
                        datei = new File("C:\\Windows\\System32\\Taskmgr.exe");
                        Desktop desktop = Desktop.getDesktop();
                        desktop.open(datei);
                    } else {
                        System.err.println(RED + "System doesn't support including the taskmgr" + RESET);
                    }
                }
                case "--zeitweiligen", "--zei" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben eine name fur zeitwiliger datei: ");
                    String zeitweiligeNameFurDatei = operation.nextLine();
                    System.out.println("Schreiben eine verbreitenung fur dieser datei: ");
                    String verbreitenung = operation.nextLine();
                    try {
                        Path zeitwiligWeg = Files.createTempFile(zeitweiligeNameFurDatei,verbreitenung);
                        System.out.println(GREEN + "Diese zeitwilige datei war erfolgreich einstellt: " +
                                zeitwiligWeg.toAbsolutePath() + RESET);
                        Files.writeString(zeitwiligWeg,"Diese zeitwilige datei war erfolgreich einstellt | " + LocalDateTime.now());
                    } catch (FileSystemException e) {
                        throw new RuntimeException(e.getLocalizedMessage());
                    }
                }
                case "--GBS", "--gbs" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println(new KonsoleDienstProgrammsGBS());
                }
                case "--jarchiv", "--jar","--pfeifen","--pfe","--tar.gz","--tar" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    String name;
                    if(Objects.equals(arg,"--jarchiv") || Objects.equals(arg,"--jar")) {
                        System.out.println("Schreiben eine name fur jar datei: ");
                        String dateiName = operation.nextLine();
                        String jarName = "";
                        if(Files.exists(Path.of(dateiName))) {
                            name = dateiName;
                            System.out.println("Schreiben eine datei welche wollen sie zu jar hinzufugen: ");
                            jarName = operation.nextLine();
                        } else {
                            System.out.println("Dieser jar datei existiert nicht. Einstellen sie neue: ");
                            name = operation.nextLine();
                        }
                        try (JarOutputStream zuDatei = new JarOutputStream(new FileOutputStream(name))) {
                            JarEntry entry = new JarEntry(jarName);
                            zuDatei.putNextEntry(entry);
                            zuDatei.setComment("Zeit fur einstellen datei: " + LocalDateTime.now());
                            System.out.println(GREEN + "Jar datei war erfolgreich hinzufugt: " + new File(name).getAbsolutePath() + RESET);
                            zuDatei.closeEntry();
                        }
                    } else {
                        System.out.println("Schreiben eine name fur zip datei: ");
                        String dateiName = operation.nextLine();
                        String zipDatei = "";
                        if(Files.exists(Path.of(dateiName))) {
                            name = dateiName;
                            System.out.println("Schreiben eine datei welche wollen sie zu zip hinzufugen: ");
                            zipDatei = operation.nextLine();
                        } else {
                            System.out.println("Dieser datei existiert nicht. Einstellen sie neue: ");
                            name = operation.nextLine();
                        }
                        try (ZipOutputStream zuDatei = new ZipOutputStream(new FileOutputStream(name))) {
                            ZipEntry entry = new ZipEntry(zipDatei);
                            zuDatei.putNextEntry(entry);
                            zuDatei.setComment("Zeit fur einstellen zip: " + LocalDateTime.now());
                            System.out.println(GREEN + "Zip datei war erfolgreich hinzufugt: " + new File(name).getAbsolutePath() + RESET);
                            zuDatei.closeEntry();
                        }
                    }
                }
                case "--schreiben","--sch" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben ein text oder daten: ");
                    String daten = operation.nextLine();
                    System.out.println("Schreiben eine name fur datei wohin wollen Sie eine daten integrieren: ");
                    String dateiname = operation.nextLine();
                    String datei_;
                    if(Files.exists(Path.of(dateiname))) {
                        datei_ = dateiname;
                    } else {
                        System.out.println("This file doesn't exist. Create the new: ");
                        datei_ = operation.nextLine();
                    }
                    try(FileOutputStream fos = new FileOutputStream(datei_)) {
                        fos.write(daten.getBytes());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex.getLocalizedMessage());
                    }
                }
                case "--grep","--gre" -> {
                    hinzufugenGeschichte((index++) + " | " + arg);
                    System.out.println("Schreiben eine datei wo wollen Sie eine text bekommen: ");
                    String dateiname = operation.nextLine();
                    String name, daten;
                    if(Files.exists(Path.of(dateiname))) {
                        name = dateiname;
                    } else {
                        System.out.println("Diese datei existiert nicht. Erstellen Sie neue");
                        name = operation.nextLine();
                        System.out.println("Schreiben eine text in datei: ");
                        daten = operation.nextLine();
                        try(BufferedWriter schreibenZu = new BufferedWriter(new FileWriter(name))) {
                            schreibenZu.write(daten);
                        } catch (IOException aus) {
                            throw new RuntimeException(aus.getLocalizedMessage());
                        }
                    }
                    System.out.println("Schreiben ein wort oder text welche wollen Sie vom datei bekommen: ");
                    String text = operation.nextLine();
                    String textVom;
                    int zahleWorten;
                    try(BufferedReader lesenVom = new BufferedReader(new FileReader(name))) {
                        textVom = lesenVom.readLine();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex.getLocalizedMessage());
                    }
                    StringTokenizer token = new StringTokenizer(textVom);
                    List<String> spreichernTokens = new LinkedList<>();
                    while(token.hasMoreTokens()) {
                        spreichernTokens.add(token.nextToken());
                    }
                    zahleWorten = (int) spreichernTokens.stream().filter(object -> Objects.equals(object,text)).count();
                    for(int findenText = 0; findenText < spreichernTokens.size(); ++findenText) {
                        if(Objects.equals(spreichernTokens.get(findenText), text)) {
                            spreichernTokens.set(findenText,RED + text + RESET);
                        }
                    }
                    spreichernTokens.add("| " + zahleWorten);
                    for(String ergebnis : spreichernTokens) {
                        System.out.printf("%s ",ergebnis);
                    }
                    System.out.println("\n");
                    spreichernTokens.clear();
                }
                case "--geschichte","--ges" -> {
                    hinzufugenGeschichte((index++) + " | " + arg);
                    loadenGeschichte().forEach(System.out::println);
                }
                case "--finden","--fin" -> {
                    hinzufugenGeschichte((index++) + " | " + arg);
                    System.out.println("Schreiben eine direktorei wo wollen Sie dateis finden: ");
                    String directorei = operation.nextLine();
                    if(!directorei.startsWith("C:\\") || !directorei.startsWith("/")) {
                        System.err.println("Directoreis mussen mit C:\\ (fur Windows) oder / (fur Linux) aktiv sein");
                    } else {
                        Path analyzieren = Path.of(directorei);
                        System.out.println("Schreiben eine dateis erweiterung fur suchen: ");
                        String erweiterung = operation.nextLine();
                        if(!erweiterung.startsWith(".")) {
                            System.err.println(RED + "Erweiterungen mussen mit '.' aktiv sein" + RESET);
                        } else {
                            try(Stream<Path> paths = Files.walk(analyzieren)) {
                                paths.filter(Files::isRegularFile).filter(isTxt -> isTxt.toString().endsWith(erweiterung) &&
                                        !Objects.equals(isTxt.toString(),new File("HistoryFile.txt").getAbsolutePath())
                                        && !Objects.equals(isTxt.toString(),new File("PasswordManager.txt").getAbsolutePath())).forEach(System.out::println);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex.getLocalizedMessage());
                            }
                        }
                    }
                }
                case "--lstkat", "--lst" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben Sie direktorei wo wollen Sie eine katalogien sehen: ");
                    String direktorei = operation.nextLine();
                    if(!direktorei.startsWith("C:\\") && !direktorei.startsWith("/")) {
                        System.err.println("Direktoreis mussen mit C:\\ (fur Windows) oder / (fur Linux} starten sein: ");
                    } else {
                        try(Stream<Path> wegs = Files.walk(Path.of(direktorei))) {
                            wegs.forEach(System.out::println);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex.getLocalizedMessage());
                        }
                    }
                }
                case "--ersetzen","--ers" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben Sie eine datei welche hat einen text und Sie wollen in ihnen eine zeichnen anderen: ");
                    String dateiName = operation.nextLine();
                    String name, daten = "", ersteZeichne, zweiteZeichne, neueDaten;
                    if(Files.exists(Path.of(dateiName))) {
                        name = dateiName;
                    } else {
                        System.out.println("Diese datei existiert nicht. Erstellen Sie eine neue: ");
                        name = operation.nextLine();
                    }
                    System.out.println("Schreiben Sie eine erste zeichne welche wollen Sie anderen: ");
                    ersteZeichne = operation.nextLine();
                    System.out.println("Schreiben Sie eine zweite zeichne fur anderung: ");
                    zweiteZeichne = operation.nextLine();
                    try(BufferedReader lesenVom = new BufferedReader(new FileReader(name))) {
                        String line = lesenVom.readLine();
                        if(line.isEmpty()) {
                            System.err.println(RED + "Es gibt keinen string fur erstetzenung" + RESET);
                        } else {
                            daten = line;
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex.getLocalizedMessage());
                    }
                    char erste = ersteZeichne.charAt(0), zweite = zweiteZeichne.charAt(0);
                    neueDaten = daten.replace(erste,zweite);
                    System.out.println(GREEN + neueDaten + RESET);
                    try(BufferedWriter schreibenNeueDaten = new BufferedWriter(new FileWriter(name))) {
                        schreibenNeueDaten.write(neueDaten);
                    } catch (IOException aus) {
                        throw new RuntimeException(aus.getLocalizedMessage());
                    }
                }
                case "--ertdir", "--ert" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben ein name fur direktorei: ");
                    String direktorei = operation.nextLine();
                    System.out.println("Schreiben eine haupte direktorei (С:\\Users\\...) oder (/home/...): ");
                    String hauptDirectorei = operation.nextLine();
                    if(!hauptDirectorei.startsWith("C:\\") && !hauptDirectorei.startsWith("/")) {
                        System.err.println(RED + "Directoreis mussen mit C:\\ (fur Windows) oder / (fur Linux} beginnen sein: " + RESET);
                    } else {
                        if(!hauptDirectorei.endsWith("/")) {
                            hauptDirectorei = hauptDirectorei + "/";
                        }
                        String erstellenDir = hauptDirectorei + direktorei;
                        hinzufugenWegDirectoreis(indexWeg + " | " + erstellenDir);
                        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxr-xr-x");
                        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
                        Path weg = Path.of(erstellenDir);
                        Files.createDirectory(weg,attr);
                        System.out.println(GREEN + "Direktorei war hinzufugt: " + weg.toAbsolutePath() + RESET);
                    }
                }
                case "--lshdir", "--lsh" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben Sie eine direktorei welche wollen Sie loschen: ");
                    String findenDirektorei = operation.nextLine();
                    if(!findenDirektorei.startsWith("C:\\") && !findenDirektorei.startsWith("/")) {
                        System.err.println(RED + "Direktoreis mussen mit C:\\ (fur Windows) oder / (fur Linux} starten sein: " + RESET);
                    } else {
                        Path weg = Path.of(findenDirektorei);
                        Files.deleteIfExists(weg);
                        if(!Files.isDirectory(weg)) {
                            Path vomDatei = Paths.get("Directory.txt");
                            List<String> loschenDirektorei = new ArrayList<>(Files.readAllLines(vomDatei));
                            loschenDirektorei.remove(findenDirektorei);
                            Files.delete(vomDatei);
                            for(String schreibenWiderAlleDirektoreis : loschenDirektorei) {
                                hinzufugenWegDirectoreis(schreibenWiderAlleDirektoreis);
                            }
                            System.out.println(GREEN + "Direktorei '" + findenDirektorei + "' war erfolgreich loscht" + RESET);
                        } else {
                            System.out.println("Direktorei'" + findenDirektorei + "' existiert noch");
                        }
                    }
                }
                case "--exstdirs", "--exs" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    loadenWegDirectoreis().forEach(System.out::println);
                }
                case null, default -> System.err.println(RED + "Diese befehle existiert nicht" + RESET);
            }
        }
    }
    public static void alleBefehlen() {
        new LinkedList<>(
                List.of("--helfen         / --hel = eine manual mit allem konsoleDienstprogramms befehlen",
                        "--hinzufugen     / --hin = hinzufugen eine datei und schreiben eine information zu Komputers system",
                        "--lesen          / --les = lesen eine daten vom datei",
                        "--loschen        / --los = loschen eine datei vom komputer",
                        "--kopieren       / --kop = kopieren daten von eines datei zu andere datei",
                        "--bewegen        / --bew = bewegen ein datei von eines LaufWerk zu andere LaufWerk",
                        "--umbenennen     / --umb = umbenennen einen datei",
                        "--taskmanageren  / --tas = anmachen Taskmgr.exe in system datei in Windows",
                        "--zeitweiligen   / --zei = einstellen eine zeitweilige datei",
                        "--GBS            / --gbs = GBS (oder Grafik Benutzer Schnittstelle) ist KonsoleDienstProgramms grafik version",
                        "--jarchiv        / --jar = eine jar datei erstellen",
                        "--pfeifen        / --pfe = eine zip datei erstellen (tar.gz format fur Linux system)",
                        "--schreiben      / --sch = schreiben eine daten zu datei",
                        "--grep           / --gre = finden einen teil von text in datei",
                        "--geschichte     / --ges = analyzieren eine geschichte von befehlen welche haben Sie fruher benutzen",
                        "--finden         / --fin = finden eine datei mit erweiterung welche wollen Sie finden",
                        "--lstkat         / --lst = analysieren und sehen alle katalogien in definischen direktorei",
                        "--ersetzen       / --ers = ersetzen eine zeichne zu andere zeichne in text vom datei",
                        "--ertdir         / --ert = erstellen direktorei in System-Explorer",
                        "--lshdir         / --lsh = loschen direktorei vom System-Explorer",
                        "--exstdirs       / --exs = analysieren alle direktoreis welche hat ein benutzer erstellt"
                )).forEach(System.out::println);
    }
    private static class KonsoleDienstProgrammsGBS extends JFrame {
        private final JLabel wahlEtikett;
        public KonsoleDienstProgrammsGBS() {
            super("KonsoleDienstProgramms GBS");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            JButton spreichernDatei = new JButton("Spreichern eine datei: "),
                    offnenDirektorei = new JButton("Offnen eine direktorei: ");
            JPanel panel = new JPanel();
            panel.add(spreichernDatei);
            panel.add(offnenDirektorei);
            setContentPane(panel);
            wahlEtikett = new JLabel("Eine datei oder eine direktorei war wahlt nicht.");
            pack();
            setLocationRelativeTo(null);
            spreichernDatei.addActionListener(this::actionPerformed);
            offnenDirektorei.addActionListener(this::actionPerformed2);
            setVisible(true);
        }
        private void spreichernEineDateiWahler() {
            JFileChooser dateiWahler = new JFileChooser();
            dateiWahler.setCurrentDirectory(new File(System.getProperty("user.home")));
            int ergebnis = dateiWahler.showSaveDialog(this);
            if(ergebnis == JFileChooser.APPROVE_OPTION) {
                wahlEtikett.setText("Ausgewahlte datei: " + dateiWahler.getSelectedFile().getAbsolutePath());
                System.out.println("Ausgewahlte datei: " + dateiWahler.getSelectedFile().getAbsolutePath());
            } else {
                if(ergebnis == JFileChooser.CANCEL_OPTION) {
                    System.out.println("Befehle funktioniert nicht");
                }
            }
        }
        private void offnenDirektoreiWahler() {
            JFileChooser dateiWahler = new JFileChooser();
            dateiWahler.setCurrentDirectory(new File(System.getProperty("user.home")));
            int ergebnis = dateiWahler.showSaveDialog(this);
            if(ergebnis == JFileChooser.APPROVE_OPTION) {
                wahlEtikett.setText("Ausgewahlte datei: " + dateiWahler.getSelectedFile().getAbsolutePath());
                System.out.println("Ausgewahlte datei: " + dateiWahler.getSelectedFile().getAbsolutePath());
            } else {
                if(ergebnis == JFileChooser.CANCEL_OPTION) {
                    System.out.println("Befehle funktioniert nicht");
                }
            }
        }

        private void actionPerformed(ActionEvent wahler) {
            spreichernEineDateiWahler();
        }

        private void actionPerformed2(ActionEvent wahler) {
            offnenDirektoreiWahler();
        }
    }
}
