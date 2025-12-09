import javax.management.RuntimeOperationsException;
import javax.swing.*;
import javax.xml.stream.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
            DirektoreiDatei = Paths.get("Directory.txt");
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
            GRUN = ESC + "[32m",
            ROT = ESC + "[31m",
            GELB = ESC + "[33m",
            RESET = ESC + "[0m";
    public static void main(String[] args) throws IOException, XMLStreamException {
        List<String> richten = new ArrayList<>(List.of(
                "---","--x","-w-","-wx","r--","r-x","rw-","rwx"));
        List<Character> nummerRichten = new ArrayList<>();
        List<String>ergebnisRichten = new ArrayList<>();
        List<String> geschichteBefehlen = loadenGeschichte();
        String istLogoutValue = "";
        int index = geschichteBefehlen.size(), indexWeg = loadenWegDirectoreis().size();
        try(BufferedReader lesenIsLogout = new BufferedReader(new FileReader("IsLogout.txt"))) {
            istLogoutValue = lesenIsLogout.readLine();
        } catch (IOException exc) {
            throw new RuntimeException(exc.getLocalizedMessage());
        }
        File datei;
        List<String> DienstProgrammBefehlen = alleBefehlen();
        Scanner operation = new Scanner(System.in);
        if(args.length == 0) {
            System.out.println(GELB + "Maybe you wanted to put: " + RESET);
            List<String> prompt = new ArrayList<>(List.of(
                    "--hilfen oder --hil","--hinzufugen oder --hin",
                    "--lesen oder --les", "--loschen oder --los","--kopieren oder --kop",
                    "--bewegen oder --bew","--umbenennen oder --umb",
                    "--zeitweiligen oder --zei", "--GBS oder --gbs","--jarchiv oder --jar",
                    "--pfeifen oder -pfe (Windows) / --tar.gz oder -tr (Linux)",
                    "--schreiben oder --sch","--grep oder --gre",
                    "--geschichte oder --ges","--finden oder --fin",
                    "--lstkat oder --lst","--ersetzen oder --ers","--ertdir oder --ert",
                    "--lshdir oder --lsh","--exstdirs oder --exs",
                    "--tldr oder --tld","--andriten oder --and",
                    "--adverungen oder --adv","--symlink oder --sym",
                    "--leer oder --lee","--sortieren oder --sor",
                    "--umkehren oder --umk","--entAlle oder --ena",
                    "--entfernen oder --ent","--integrieren oder ing",
                    "--grsdti oder --grs","--editieren oder --edt","--symzln oder --szn",
                    "--andegrose oder --agr", "--version oder --vrs","--sicherung oder --scr","--xexport dder --xp","--ximport oder --xm",
                    "--herstellen oder --hen","--stats oder --sts","--suchen oder --sun",
                    "--hostinfo oder --hos","--abshalten oder --abs","--neustarten oder --nsn","--fspr oder --fsp",
                    "--sauber oder --sab","--pingen oder --png","--intprok oder --ipk","--unterbrechen oder --ubk",
                    "--filterieren oder --fir","--md5gen oder --mgn", "--sha256gen oder --sgn",
                    "--frieren oder --frn","--einzigartig oder --ezn","--stat oder --sat","--teilen oder --ten",
                    "--rsync oder --rnc","verglen oder --ven","--sysinfo oder --sin","--jungste oder --jng",
                    "--aktiv oder --akt","--benutzername oder --btn","--schneiden oder --scn",
                    "--andkent oder ank","--abmelden oder --abn","--fbef oder --fbf","--gzip oder --gzp",
                    "--fdir oder --fdi","--tilden oder --tln, --spiegel oder --spl, --aktualisieren oder --akl"
            ));
            for(String alle : prompt) {
                System.out.println(alle);
            }
        }
        for (String arg : args) {
            final Path vomDatei = Paths.get("Directory.txt");
            if(Objects.equals(istLogoutValue, "true")) {
                System.err.println(ROT + "Sie konnen diese befehle aktuellen nicht" + RESET);
                System.exit(0);
            }
            switch (arg) {
                case "--helfen", "--hel" -> alleBefehlen();
                case "--hinzufugen", "--hin" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben eine name fur datei: ");
                    String dateiName = operation.nextLine();
                    System.out.println("Schreiben ein text fur datei: ");
                    String text = operation.nextLine();
                    Files.writeString(Path.of(dateiName),text + System.lineSeparator(),
                            StandardOpenOption.CREATE,StandardOpenOption.APPEND);
                }
                case "--lesen", "--les" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben eine dateiName fur prufung es existieren: ");
                    String dateiName = operation.nextLine();
                    if (Files.exists(Path.of(dateiName))) {
                        try (BufferedReader bekommen = new BufferedReader(new FileReader(dateiName))) {
                            System.out.println(bekommen.readLine());
                        } catch (IOException e) {
                            throw new RuntimeException(ROT + "Das ist fehler fur schreiben eines text zu datei");
                        }
                    } else {
                        System.err.println(ROT + "Diese datei existiert nicht" + RESET);
                    }
                }
                case "--loschen", "--los" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben eine dateiName fur prufung es existieren: ");
                    String dateiName = operation.nextLine();
                    datei = new File(dateiName);
                    if (Files.exists(datei.toPath())) {
                        System.out.println(GRUN + "Diese datei: " + datei.toPath() + " war loscht erfolgreich" + RESET);
                        Files.delete(datei.toPath());
                    } else {
                        System.err.println(ROT + "Diese datei existiert nicht" + RESET);
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
                        System.err.println(ROT+ "Diese datei existiert nicht" + RESET);
                    }
                }
                case "--bewegen", "--bew" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben eine name fur datei: ");
                    String dateiName = operation.nextLine();
                    System.out.println("Schreiben eine neue platte: ");
                    char neuePlatte = operation.nextLine().charAt(0);
                    datei = new File(dateiName);
                    if (Files.exists(datei.toPath())) {
                        if(neuePlatte == 'C') {
                            Files.move(datei.toPath(), Path.of(neuePlatte + ":\\", datei.getName()), StandardCopyOption.REPLACE_EXISTING);
                        } else if(neuePlatte == '/') {
                            Files.move(datei.toPath(), Path.of(neuePlatte + "home/", datei.getName()), StandardCopyOption.REPLACE_EXISTING);
                        } else {
                            System.err.println(ROT + "Diese typen fur platte existiert nicht" + RESET);
                            System.exit(0);
                        }
                        System.out.println(GRUN + "Diese datei war bewegt zu andere platte erfolgreich" + RESET);
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
                            System.err.println(ROT + "Das ist ein fehler mit eingang/ausgang operationen" + RESET);
                        }
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
                        System.out.println(GRUN + "Diese zeitwilige datei war erfolgreich einstellt: " +
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
                            System.out.println(GRUN + "Jar datei war erfolgreich hinzufugt: " + new File(name).getAbsolutePath() + RESET);
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
                            System.out.println(GRUN + "Zip datei war erfolgreich hinzufugt: " + new File(name).getAbsolutePath() + RESET);
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
                    ArrayList<String> saveTheTokens = new ArrayList<>();
                    int zahlenWorten;
                    StringTokenizer token;
                    System.out.println("Schreiben eine datei oder direktorei: ");
                    String dateiOderDirektorei = operation.nextLine();
                    String finalName, daten = "";
                    Path start = Path.of(dateiOderDirektorei);
                    if(new File(dateiOderDirektorei).isDirectory()) {
                        System.out.println("Schreiben eine datei oder eine veranderungen fur es: ");
                        finalName = operation.nextLine();
                        List<Path> allFiles;
                        try(Stream<Path> streamWeg = Files.walk(start)) {
                            allFiles = streamWeg.filter(comparePath -> String.valueOf(comparePath).endsWith(finalName)).toList();
                        } catch (IOException aus) {
                            throw new RuntimeException(aus.getLocalizedMessage());
                        }
                        for(Path hinAlleWegen : allFiles) {
                            saveTheTokens.add(hinAlleWegen.toString());
                        }
                        for(String findenGrepElem : saveTheTokens) {
                            if(Objects.equals(findenGrepElem,finalName)) {
                                saveTheTokens.set(saveTheTokens.indexOf(findenGrepElem),ROT + findenGrepElem + RESET);
                            }
                        }
                        saveTheTokens.forEach(System.out::println);
                        saveTheTokens.clear();
                    } else if(new File(dateiOderDirektorei).isFile()) {
                        if (Files.exists(start)) {
                            finalName = dateiOderDirektorei;
                        } else {
                            System.out.println("This file doesn't exist. Create the new");
                            finalName = operation.nextLine();
                            System.out.println("Write the text in the file: ");
                            daten = operation.nextLine();
                            try (BufferedWriter writeTo = new BufferedWriter(new FileWriter(finalName))) {
                                writeTo.write(daten);
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
                        zahlenWorten = (int) saveTheTokens.stream().filter(object -> Objects.equals(object, text)).count();
                        for (int findText = 0; findText < saveTheTokens.size(); ++findText) {
                            if (Objects.equals(saveTheTokens.get(findText), text)) {
                                saveTheTokens.set(findText, ROT + text + RESET);
                            }
                        }
                        saveTheTokens.add("| " + zahlenWorten);
                        saveTheTokens.forEach(System.out::println);
                        saveTheTokens.clear();
                    } else {
                        System.err.println(ROT + "Error naming file or directory" + RESET);
                    }
                }
                case "--geschichte","--ges" -> {
                    hinzufugenGeschichte((index++) + " | " + arg);
                    System.out.println("Schreiben Sie deine benutzername: ");
                    String benutzername = operation.nextLine();
                    if(Objects.equals(Files.readString(Path.of("Username.txt")),benutzername)) {
                        for(int lesenAlleBefehlen = 1; lesenAlleBefehlen < loadenGeschichte().size(); ++lesenAlleBefehlen) {
                            System.out.println(loadenGeschichte().get(lesenAlleBefehlen));
                        }
                    } else {
                        System.err.println(ROT + "Diese benutzername existiert nicht" + RESET);
                    }
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
                            System.err.println(ROT + "Erweiterungen mussen mit '.' aktiv sein" + RESET);
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
                            System.err.println(ROT + "Es gibt keinen string fur erstetzenung" + RESET);
                        } else {
                            daten = line;
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex.getLocalizedMessage());
                    }
                    char erste = ersteZeichne.charAt(0), zweite = zweiteZeichne.charAt(0);
                    neueDaten = daten.replace(erste,zweite);
                    System.out.println(GRUN + neueDaten + RESET);
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
                        System.err.println(ROT + "Directoreis mussen mit C:\\ (fur Windows) oder / (fur Linux} beginnen sein: " + RESET);
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
                        System.out.println(GRUN + "Direktorei war hinzufugt: " + weg.toAbsolutePath() + RESET);
                    }
                }
                case "--lshdir", "--lsh" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben Sie eine direktorei welche wollen Sie loschen: ");
                    String findenDirektorei = operation.nextLine();
                    if(!findenDirektorei.startsWith("C:\\") && !findenDirektorei.startsWith("/")) {
                        System.err.println(ROT + "Direktoreis mussen mit C:\\ (fur Windows) oder / (fur Linux} starten sein: " + RESET);
                    } else {
                        Path weg = Path.of(findenDirektorei);
                        Files.deleteIfExists(weg);
                        if(!Files.isDirectory(weg)) {
                            List<String> loschenDirektorei = new ArrayList<>(Files.readAllLines(vomDatei));
                            loschenDirektorei.remove(findenDirektorei);
                            Files.delete(vomDatei);
                            for(String schreibenWiderAlleDirektoreis : loschenDirektorei) {
                                hinzufugenWegDirectoreis(schreibenWiderAlleDirektoreis);
                            }
                            System.out.println(GRUN + "Direktorei '" + findenDirektorei + "' war erfolgreich loscht" + RESET);
                        } else {
                            System.out.println("Direktorei'" + findenDirektorei + "' existiert noch");
                        }
                    }
                }
                case "--exstdirs", "--exs" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    loadenWegDirectoreis().forEach(System.out::println);
                }
                case "--tldr","--tld" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    String []allCommandsInstruction =
                            {"",
                                    "hinzufugen","lesen","loschen","kopieren",
                                    "bewegen","umbenennen","taskmanageren","zeitweiligen",
                                    "GBS","jar","pfeifen / tar.gz","schreiben",
                                    "grep","geschichte","finden","lstkat",
                                    "ersetzen","ertdir","lshdir","exstdirs","andriten",
                                    "adverungen","symlink","symzln","andegrose",
                                    "sicherung","ximport","xexport","herstellen",
                                    "stats","suchen","hostinfo","abshalten",
                                    "neustarten","fspr","sauber","pingen",
                                    "intprok","unterbrechen","filterieren", "md5gen",
                                    "sha256gen","frieren","einzartig","stat",
                                    "teilen","rsync","verglen","sysinfo",
                                    "jungste","aktiv","benutzername",
                                    "schneiden","andkent","abmelden","fbef",
                                    "gzip","fdir","tilden","spiegel",
                                    "aktualisieren","wasIst","bekweg","erste",
                                    "letzte"
                            };
                    for(int i = 1; i < allCommandsInstruction.length; ++i) {
                        System.out.println(i + ") " + allCommandsInstruction[i]);
                    }
                    System.out.println("Write the command (1,2,3,4....) for which you want to see the instruction how use: ");
                    int commandChoose = operation.nextInt();
                    switch(commandChoose) {
                        case 1 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility) " +
                                "java src\\\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--hinzufugen / --hin [ENTER] -> " +
                                "Dateinamen eingeben: -> (Name Ihrer Datei. Beispiel: test.txt oder ein anderer) -> [ENTER] -> " +
                                "Dateitext eingeben: -> (Text Ihrer Datei. Beispiel: Hello_World) -> [ENTER] " +
                                "{WENN ERFOLG} -> Text Hello_World wurde in test.txt geschrieben.");
                        case 2 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--lesen / --les [ENTER] -> " +
                                "Schreiben Sie den Dateinamen, um seine Existenz zu überprüfen: " +
                                "-> (Ihre mit --add erstellte Datei) [ENTER] -> " +
                                "{WENN ERFOLG} -> <EXAMPLE> Hello_World (Text aus test.txt wurde gelesen)");
                        case 3 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--loschen / --los [ENTER] -> " +
                                "Geben Sie den Namen der Datei ein, um die Existenz zu überprüfen: " +
                                "-> (Ihre Datei, die Sie mit --add erstellt haben) [ENTER] -> " +
                                "{WENN ERFOLG} -> <MESSAGE> 'Diese Datei: (Ihre Datei) wurde erfolgreich gelöscht'");
                        case 4 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--kopieren / --kop [ENTER] -> " +
                                "Geben Sie den Namen der Datei ein, um die Existenz zu überprüfen: " +
                                "-> (Ihre mit --add erstellte Datei) [ENTER] -> " +
                                "Geben Sie den Namen der neuen Datei ein, in die die Informationen aus der vorherigen Datei eingefügt werden: " +
                                "-> (Der Name der neuen Datei, in der Ihre Informationen gespeichert werden (Beispiel: copy.txt) [ENTER] -> " +
                                "{WENN ERFOLG} -> <MESSAGE> Das Kopieren von einer Datei in eine andere war erfolgreich: (Pfad zur neuen zu kopierenden Datei)");
                        case 5 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--bewegen / --bew [ENTER] -> " +
                                "Geben Sie den Namen der Datei ein, um die Existenz zu überprüfen: " +
                                "-> (Ihre mit --add erstellte Datei) [ENTER] -> " +
                                "Geben Sie den neuen Datenträger ein: -> (Systemdatenträger, auf dem Sie Ihre Datei oder Freigabe speichern möchten) -> [ENTER] -> " +
                                "{WENN ERFOLG} -> <MESSAGE> Diese Datei wurde erfolgreich auf einen anderen Datenträger verschoben");
                        case 6 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--umbenennen / --umb [ENTER] -> " +
                                "Schreiben Sie die Datei, die Sie umbenennen möchten: " +
                                "-> (Ihre mit --add erstellte Datei) [ENTER] -> " +
                                "Schreiben Sie den neuen Namen für die Datei: -> (Neuer Name für Ihre Datei) -> [ENTER] -> " +
                                "{WENN ERFOLG} -> (Ihre Datei hat den neuen Namen erhalten)");
                        case 7 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--zeitweiligen / --zei [ENTER] -> " +
                                "Name der Übergangsdatei eingeben: -> (Name der temporären Datei) -> [ENTER] -> " +
                                "Dateierweiterung eingeben: -> (Dateierweiterung. Beispiel: .txt, .bin usw.) -> [ENTER} " +
                                "-> <MElDUNG> 'Die Übergangsdatei wurde erfolgreich erstellt: (Pfad zu Ihrer temporären Datei)'");
                        case 8 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--GUI / --gi [ENTER] -> (GUI-Version für ConsoleUtility. Funktioniert unter Windows und Linux)");
                        case 9 -> System.out.println("""
                                Ihr Pfad (C:\\CU-ConsoleUtility \
                                java src\\ConsoleUtilityItself.java (Windows) \
                                oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) \
                                --jar / --jr [ENTER] -> \
                                Name der JAR-Datei eingeben: -> (Name der JAR-Datei. Beispiel: java.jar) -> [ENTER] -> " +
                                {IF JAR FILE EXISTS} -> Geben Sie die Datei ein, die Sie in die JAR-Datei aufnehmen möchten: " +
                                (Ihre Datei. Beispiel: test.txt) {ELSE} Diese JAR-Datei existiert nicht. Erstellen Sie eine neue: " +
                                -> (die neue JAR-Datei) -> [ENTER] -> \
                                <MELDUNG> JAR-Datei wurde erfolgreich erstellt: (Pfad zu Ihrer JAR-Datei)""");
                        case 10 -> System.out.println("""
                                Ihr Pfad (C:\\CU-ConsoleUtility \
                                java src\\ConsoleUtilityItself.java (Windows) \
                                oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                --zip,--zp (für Windows) / --tar.gz,--tr [ENTER] -> \
                                Geben Sie den Namen der Zip-Datei (oder Tar.gz für Linux) ein: -> [ENTER] -> " +
                                Geben Sie die Datei ein, die Sie in die Zip-Datei (oder Tar.gz) einbinden möchten: -> (Ihre Datei. Beispiel: test.txt) " +
                                -> [ENTER] -> {WENN IHRE DATEI VORHANDEN} -> <MELDUNG> Zip-Datei wurde erfolgreich erstellt: (Pfad zu Ihrer Zip-Datei) -> " +
                                {SONST} Diese Zip-Datei (Tar.gz) existiert nicht. Erstellen Sie eine neue: -> (Ihre Datei mit zip,tar.gz)""");
                        case 11 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--write,--wt -> [ENTER] -> " +
                                "Schreiben Sie den Text oder die Daten: -> (Ihr Text) -> [ENTER] -> " +
                                "Schreiben Sie den Dateinamen, in den die Daten geschrieben werden sollen: -> (Name Ihrer Datei) -> [ENTER] -> \" +\n" +
                                "{WENN EXISTIERT} -> (Ihr Text wurde in die Datei geschrieben) {ELSE} -> Diese Datei existiert nicht. Erstellen Sie die neue:");
                        case 12 -> System.out.println("""
                                Ihr Pfad (C:\\CU-ConsoleUtility \
                                java src\\ConsoleUtilityItself.java (Windows) " \
                                oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) \
                                --grep,--gp -> [ENTER] -> \
                                Schreiben Sie die Datei, aus der Sie den Text beziehen möchten: -> (Ihre Datei. Beispiel: test.txt) -> [ENTER] -> " +
                                {WENN EXISTIERT} -> Schreiben Sie das Wort oder den Text, der aus der Datei stammen soll: (Wort aus Ihrem Text) {ELSE} -> " +
                                Diese Datei existiert nicht. Erstellen Sie eine neue -> (Neue Datei erstellen) -> [ENTER] -> Schreiben Sie den Text in die Datei: -> " +
                                (Text in die Datei)""");
                        case 13 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--history / --hi -> [ENTER] -> (zeigt die Liste der Befehle an, die Sie im ConsoleUtility verwendet haben)");
                        case 14 -> System.out.println("""
                                Ihr Pfad (C:\\CU-ConsoleUtility \
                                java src\\\\ConsoleUtilityItself.java (Windows) \
                                oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) \
                                --find / --fd -> [Eingabe] -> \
                                Geben Sie das Verzeichnis ein, in dem Sie die Dateien suchen möchten: -> (Ihr Verzeichnis) -> [Eingabe] -> " +
                                Geben Sie die Dateierweiterung für die Suche ein: -> (Ihre Dateierweiterung: Beispiel: .txt, .bin) -> [Eingabe] -> " +
                                (zeigt alle Dateien/Kataloge mit einer bestimmten Erweiterung an)""");
                        case 15 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--lstcat / --ls -> [ENTER] -> " +
                                "Geben Sie das Verzeichnis ein, in dem die Kataloge angezeigt werden sollen: -> (Ihr Verzeichnis) -> [ENTER] -> \" +\n" +
                                "(zeigt alle Dateien/Kataloge im angegebenen Verzeichnis an)");
                        case 16 -> System.out.println("""
                                Ihr Pfad (C:\\CU-ConsoleUtility \
                                java ConsoleUtilityItself.java (Windows) " +
                                oder /home/CU-ConsoleUtility java src\\\\ConsoleUtilityItself.java (Linux)) " +
                                --replace / --re -> [ENTER] -> " +
                                Schreiben Sie die Datei, in der Sie die Zeichen ändern möchten: -> (Ihre Datei. Beispiel: test.txt) -> [ENTER] -> " +
                                {IF EXISTS} -> Schreiben Sie das erste Zeichen, das Sie ändern möchten: (erstes Zeichen. Beispiel: c) -> [ENTER] -> " +
                                Schreiben Sie das zweite Zeichen, das Sie ändern möchten: -> (zweites Zeichen. Beispiel: i) -> [ENTER] -> (ersetzt das erste Zeichen durch das zweite)""");
                        case 17 -> System.out.println("""
                                Ihr Pfad (C:\\CU-ConsoleUtility "\s
                                java srConsoleUtilityItself.java (Windows) "\s
                                oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) "\s
                                --crtdir / --cr -> [EINGABE] -> "\s
                                Geben Sie den Namen des Verzeichnisses ein: -> (Ihr Verzeichnis) -> [EINGABE] -> "\s
                                Geben Sie das Hauptverzeichnis ein: (Ihr Hauptverzeichnis) (Beispiel: C:\\Users\\... oder /home/...) -> [EINGABE] -> "\s
                                <MELDUNG> 'Das Verzeichnis wurde erstellt: (Ihr erstelltes Verzeichnis)'""");
                        case 18 -> System.out.println("""
                                Ihr Pfad (C:\\CU-ConsoleUtility "\s
                                java src\\ConsoleUtilityItself.java (Windows) "\s
                                oder /home/CU-ConsoleUtility java src\\\\ConsoleUtilityItself.java (Linux)) "                                \s
                                --candir / --ca -> [ENTER] -> "\s
                                Geben Sie das zu löschende Verzeichnis ein: (Ihr zu löschendes Verzeichnis) -> [ENTER] -> "\s
                                Das Verzeichnis (Ihr Verzeichnis) wurde erfolgreich gelöscht""");
                        case 19 -> System.out.println("""
                                Ihr Pfad (C:\\CU-ConsoleUtility "\s
                                java src\\ConsoleUtilityItself.java (Windows) "\s
                                oder /home/CU-ConsoleUtility java src\\\\ConsoleUtilityItself.java (Linux)) "\s
                                --exstdirs / --ex -> [ENTER] -> "
                                (zeigt alle mit crtdir erstellten Verzeichnisse an)""");
                        case 20 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility" +
                                "java src\\ConsoleUtilityItself.java (Windows)" +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--andriten / --and -> [ENTER] -> " +
                                "Schreiben eine datei welche wollen Sie fur richten in datei andern: (deine datei) -> " +
                                "[ENTER] -> Schreiben richten in octal system (Beispiel: 700): (richten mit octal system) -> [ENTER]" +
                                "{IF ERFOLG} -> <MELDUNG> Richten fur datei: '(deine spreicherische datei)' waren anderen erfolgreich");
                        case 21 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility" +
                                " java src\\ConsoleUtilityItself.java (Windows)" +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--adverungen / --and -> [ENTER] -> Schreiben deine datei ohne verlanderung: (deine datei ohne verlanderung) [ENTER] -> " +
                                "Schreiben eine verlanderung fur deine datei: (verlanderung dur deine datei. Beispiel: .txt) -> [ENTER]" +
                                "Schreiben eine neue verlanderung due deine datei: (neue verlanderung fur deine datei. Beispiel: .bin) -> [ENTER] -> " +
                                "{WENN ERFOLG} -> <MELDUNG> Die dateis verlanderung war andert erfolgreich");
                        case 22 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility" +
                                "java src\\ConsoleUtilityItself.java (Windows)" +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--symlink / --sym -> [ENTER] -> Schreiben Sie symbole link: (deine symbole link) -> [ENTER] -> " +
                                "{WENN ERFOLG} -> <MELDUNG> Symbole link war erstellt erfolgreich: (ein weg zu deinem symbole link)");
                        case 23 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility) " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--leer / --lee -> [ENTER] -> " +
                                "Geben Sie die Datei ein, aus der Sie den Text löschen möchten: (Ihre Datei. Beispiel: test.txt) -> [ENTER] -> " +
                                "{WENN ERFOLG} -> <NACHRICHT> Der Text aus der Datei wurde erfolgreich gelöscht.");
                        case 24 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--sortieren / --sor -> [ENTER] -> " +
                                "Schreiben Sie Ihre Datei: -> (Ihre Datei. Beispiel: test.txt) " +
                                "Schreiben Sie Ihr Verzeichnis: -> (Ihr Verzeichnis) -> [ENTER]");
                        case 25 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--umkehren / --umk -> [ENTER] -> " +
                                "Schreiben Sie die Datei, aus der Sie Daten lesen möchten: (Ihre Datei. Beispiel: test.txt) -> [ENTER]");
                        case 26 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--entAlle / --ena -> [ENTER] -> " +
                                "Schreiben Sie Ihr Verzeichnis: -> (Ihr Verzeichnis) -> [ENTER] -> " +
                                "{WENN ERFOLG} -> <NACHRICHT> Alle Kataloge und Dateien in (Ihrem Verzeichnis) wurden erfolgreich gelöscht");
                        case 27 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--entfernen / --ent -> [ENTER] -> " +
                                "Dateinamen eingeben: -> (Ihre Datei. Beispiel: test.txt) -> [ENTER] -> " +
                                "Verzeichnis eingeben: -> (Ihr Verzeichnis) -> [ENTER] -> " +
                                "{WENN ERFOLG} -> <NACHRICHT> Die Datei (Ihre Datei) wurde erfolgreich aus dem Verzeichnis (Ihrem Verzeichnis) gelöscht");
                        case 28 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--integrieren / --ing -> [ENTER] -> " +
                                "Schreiben Sie Ihre Datei: -> (Ihre Datei. Beispiel: test.txt) -> [ENTER] -> " +
                                "Schreiben Sie Ihr Verzeichnis: -> (Ihr Verzeichnis) -> [ENTER] -> " +
                                "{WENN ERFOLG} -> <NACHRICHT> Datei (Ihre Datei) wurde erfolgreich zum Verzeichnis (Ihr Verzeichnis) hinzugefügt");
                        case 29 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--grsdti / --grs -> [ENTER] -> " +
                                "Geben Sie das Verzeichnis (für die Analyse aller Dateien) oder die Datei ein: (Ihr Verzeichnis oder Ihre Datei) -> [ENTER] -> " +
                                "{WENN SIE VERZEICHNIS SCHREIBEN} -> (Alle Dateien werden mit der Größe in Bytes analysiert)" +
                                "<> {SONST WENN SIE PFAD ZUR DATEI SCHREIBEN} -> Geben Sie den Pfad zu Ihrer Datei ein: (Ihr Pfad zur Datei) -> [ENTER] ->\" +\n" +
                                "(Die bestimmte Datei wurde mit der Größe in Bytes analysiert)");
                        case 30 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--editieren / --edt -> [ENTER] -> " +
                                "(will zeigt datei in GUI version. Sie konnen schreiben alles was wollen Sie mit moglichungen spreichern und offnen datei");
                        case 31 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--symzln / --szn -> [ENTER] -> " +
                                "{WENN ERFOLG} -> (will zahlen alle symbole in deinem daten vom deinem datei)");
                        case 32 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--andegrose / --agr -> [ENTER] -> " +
                                "Schreiben Sie eine name fur deinem datei: (name fur deinem datei) -> [ENTER] -> " +
                                "Schreiben Sie eine neue grose fur deinem datei: (neue grose fur deinem datei) -> [ENTER] -> " +
                                "{WENN ERFOLG} -> <NACHRICHT> Dateis grose war erfolgreich erstellt"
                                );
                        case 33 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--version / --vrs -> [ENTER] -> " +
                                "(Die aktuelle version fur deinem Utility)");
                        case 34 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--sicherung / --scr -> [ENTER] -> " +
                                "Schreiben Sie deine datei welche hat eine daten: " +
                                "{WENN ERFOLG} <NACHRICHT> Fur spreicherung daten vom datei war ReserveCopieren.bin file erstellt"
                                );
                        case 35 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--xexport / --exp -> [ENTER] -> " +
                                "Schreiben Sie eine daten: (deine daten) -> [ENTER] -> " +
                                "{WENN ERFOLG} <NACHRICHT> XML Datei mit daten war erfolgreich erstellt"
                                );
                        case 36 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--ximport / --xim -> [ENTER] -> " +
                                "<NACHRICHT> (Nachricht uber deinem daten in datei)"
                                );
                        case 37 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--herstellen / --hen -> [ENTER] -> " +
                                "<NACHRICHT> (Daten vom reserv datei waren kopieren erfolgreich)"
                                );
                        case 38 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--stats / --sts -> [ENTER] -> " +
                                "<NACHRICHT> (Deine zeit fur benutzerung dieser projekt)"
                                );
                        case 39 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--suchen / --sun -> [ENTER] -> " +
                                "Schreiben welche befehle wollen Sie finden: -> (schreiben Sie eine befehle) " +
                                "-> [ENTER] -> {WENN ERFOLG} -> (Wollen zeigen alle befehle, welche wollen Sie finden)");
                        case 40 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--hostinfo / --hos -> [ENTER] -> " +
                                "(dienstprogramm will zeigen dir information uber deinem host daten)");
                        case 41 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--abshalten / --abs -> [ENTER] -> " +
                                "(dein komputer will abshalten seine arbeit und prozesse)");
                        case 42 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--neustarten / --nsn -> [ENTER] -> " +
                                "(dein komputer will neu starten seine arbeit)");
                        case 43 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--fspr / --fsp -> [ENTER] -> " +
                                "(dein komputer will zeigen dir eine information uber frei spreicher in bytes)");
                        case 44 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility " +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--sauber / --sab -> [ENTER] -> " +
                                "(Datei welche spreichert deine befehlen, will sauber ihnen)");
                        case 45 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows)" +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux))" +
                                "--pingen / --png -> [ENTER] ->" +
                                "Geben Sie die IP-Adresse oder den DNS-Namen ein, die/den Sie anpingen möchten: (Ihre IP-Adresse oder Ihr DNS) ->" +
                                "Geben Sie die Anzahl der Ping-Anfragen ein: (Anzahl der Ping-Anfragen) ->" +
                                "{IF SUCCESS} -> (Sie erhalten Informationen zum Pingen Ihrer IP-Adresse oder Ihres DNS)");
                        case 46 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows)" +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux))" +
                                "--intprok / --ipk -> [ENTER] ->" + "(Sie erhalten die Informationen zu Ihrer IP)");
                        case 47 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows)" +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux))" +
                                "--unterbrechen / --ubr -> [ENTER] ->" + "Schreiben Sie die ID Ihres Prozesses: (ID Ihres Prozesses) ->" +
                                "(ID des bestimmten Prozesses, der unterbrochen wird und seine Arbeit beendet)");
                        case 48 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows)" +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux))" +
                                "--filterieren / --fir -> [ENTER] ->" + "Schreiben Sie das Verzeichnis: (Ihr Verzeichnis) ->" +
                                "Schreiben Sie die Größe zum Filtern der Dateien im Verzeichnis: (Größe der Dateien im Verzeichnis) ->" +
                                "Schreiben Sie den Vergleichsoperator zum Suchen von Dateien mit einem bestimmten Muster: (Operator > < = !) ->" +
                                "{IF SUCCESS} -> (Sie können die Dateien mit Ihrem bestimmten Muster sehen)");
                        case 49 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows)" +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux))" +
                                "--md5gen / --mgn -> [ENTER] ->" + "Schreiben deine datei fur analysierung daten mit md5 algorithmus:  ->" +
                                "(will zeigen daten vom datei in byte-hex mit md5 algorithmus");
                        case 50 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows)" +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux))" +
                                "--sha256gen / --sgn -> [ENTER] ->" + "Schreiben deine datei fur analysierung daten mit sha256 algorithmus:  ->" +
                                "(will zeigen daten vom datei in byte-hex mit sha256 algorithmus");
                        case 51 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows)" +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux))" +
                                "--frieren / --frn -> [ENTER] ->" + "Schreiben dein id fur prozess: (id fur deinen prozess) -> [ENTER] ->" +
                                "(will zeigen daten vom datei in byte-hex mit sha256 algorithmus"
                                );
                        case 52 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows)" +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux))" +
                                "--einzigartig / --ezn -> [ENTER] -> " +
                                "Schreiben deine datei: (dein datei) -> [ENTER] -> " +
                                "(wollen zeigen alle einzartige und sortige stringen vom datei)");
                        case 53 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows)" +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux))" +
                                "--stat / --sat -> [ENTER] -> " +
                                "Schreiben name for deinem datei: " + "(deine datei) -> [ENTER] -> " + "(Will zeigen eine information uber deinem datei)");
                        case 54 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows)" +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux))" +
                                "--teilen / --ten -> [ENTER] -> " +
                                "Schreiben eine name fur deinem datei: " + "(deine datei) -> [ENTER] -> " + "(Will teilen deine datei zu zwei unterminated");
                        case 55 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows)" +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux))" +
                                "--rsync / --rnc -> [ENTER] -> " +
                                "Schreiben deine quelle datei: " + "(deine quelle datei) -> [ENTER] -> " + "Schreiben deine ziele datei: " +
                                "(deine zweite datei) -> [ENTER] -> " + "{WENN ERFOLG} -> <NACHRICHT> Byte daten mit grose: (deine datei) waren synchroniziert zu neue datei erfolgreich"
                                );
                        case 56 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows)" +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux))" +
                                "--verglen / --ven -> [ENTER] -> " +
                                "Schreiben die erste datei: " + "(deine erste datei) -> [ENTER] -> " + "Schreiben die zweite datei: " +
                                "(deine zweite datei) -> [ENTER] -> " + "(Wollen zeigen alle stringen vom zwei sortische datei)"
                                );
                        case 57 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows)" +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux))" +
                                "--sysinfo / --sin -> [ENTER] -> " +
                                "(Will zeigen eine information uber deinem system)");
                        case 58 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder/home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--jungste / --jng -> [ENTER] -> " + "zeigt die Informationen zur letzten Eingabe durch Anmeldung oder Registrierung im Dienstprogramm an");
                        case 59 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder/home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--aktiv / --akt -> [ENTER] -> " + "zeigt die Informationen über den Benutzer im System und seine Aktivität ab dem Zeitpunkt der Anmeldung oder Registrierung im Dienstprogramm an");
                        case 60 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows) " +
                                "oder/home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)) " +
                                "--benutzername / --btn -> [ENTER] -> " + "zeigt den eindeutigen zufälligen Spitznamen für den Benutzer im System an");
                        case 61 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows)" +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)" +
                                "--schneiden / --scn -> [ENTER] -> " + "(Als Nächstes erhalten Sie die Varianten zum Arbeiten mit dem Aufteilen von Zeichenketten. Aufteilen von Zeichenketten mit Indizes oder mit Trennzeichen)");
                        case 62 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows)" +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)" +
                                "--andkent / --ank -> [EINGABE] -> " + "Kennwort ändern: (Ihr aktuelles Kennwort) -> {WENN ERFOLG} -> Neues Kennwort eingeben (neues Kennwort) -> {WENN ERFOLG}" +
                                "<NACHRICHT> Kennwort wurde erfolgreich geändert");
                        case 63 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows)" +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)" +
                                "--abmelden / --abn -> [ENTER] -> " + "<NACHRICHT> Benutzername und Password wurden erfolgreich gelöscht");
                        case 64 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows)" +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)" +
                                "--fbef / --fbf -> [ENTER] -> " +
                                " Geben Sie den Namen des Befehls aus dem Verlauf ein: (Name des Befehls aus dem Verlauf) -> (zeigt den Verlauf der Befehle anhand regulärer Ausdrücke im Dienstprogramm an)");
                        case 65 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows)" +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)" +
                                "--gzip / --gzp -> [EINGABE] -> " +
                                "Geben Sie den Namen der zu lesenden Datei ein: (Name der zu lesenden Datei) -> " +
                                "Wählen Sie die Option: 1. Datei komprimieren 2. Datei dekomprimieren (Sie können eine von zwei Varianten für die Bearbeitung der Daten aus der Datei auswählen) -> " +
                                "{WENN ERFOLG} -> Originalgröße: (Größe der Daten) Komprimierte Datei: (Größe der komprimierten Daten in der Datei)");
                        case 66 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows)" +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)" +
                                "--fdir / --fdi -> [ENTER] -> " +
                                "Geben Sie den Namen Ihres Verzeichnisses ein: (" +
                                "Name für Ihr Verzeichnis) -> (zeigt die Liste der Verzeichnisse per regulärem Ausdruck im Dienstprogramm an)");
                        case 67 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows)" +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)" +
                                "--tilden / --tln -> [ENTER] -> " + "<NACHRICHT> Liste der Verzeichnisse wurde erfolgreich gelöscht");
                        case 68 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows)" +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)" +
                                "--spiegel / --spl -> [ENTER] -> " + "Schreiben eine name vom datei: (deine datei) -> [ENTER] -> {WENN ERFOLG}"
                                + "Daten vom datei waren geandert erfolgreich");
                        case 69 -> System.out.println("Ihr Pfad (C:\\CU-ConsoleUtility)" +
                                "java src\\ConsoleUtilityItself.java (Windows)" +
                                "oder /home/CU-ConsoleUtility java src\\ConsoleUtilityItself.java (Linux)" +
                                "--aktualisieren / --akl -> [ENTER] -> " + "<NACHRICHT> Dateis fur konfiguration were geaktualisiert erfolgreich");
                        case 70 -> System.out.println("Ihr Pfad (C:\\\\CU-ConsoleUtility)" +
                                "\"java src\\\\ConsoleUtilityItself.java (Windows)" +
                                "\"oder /home/CU-ConsoleUtility java src\\\\ConsoleUtilityItself.java (Linux))" +
                                "\"--wasIst / --was -> [ENTER] -> \" + \"Schreiben Sie eine befehle von DienstProgramm: (befehle von DienstProgramm) -> [ENTER] -> (eine definische befehle von DienstProgramm)");
                        case 71 -> System.out.println("Ihr Pfad (C:\\\\CU-ConsoleUtility)" +
                                "\"java src\\\\ConsoleUtilityItself.java (Windows)" +
                                "\"oder /home/CU-ConsoleUtility java src\\\\ConsoleUtilityItself.java (Linux))" +
                                "\"--bekweg / --bkw -> [ENTER] -> \" + \"Schreiben einen Namen für die Datei: (Name für die Datei) -> [ENTER]" +
                                "\"(eine kanonische und absolute wegs für datei)\n");
                        case 72 -> System.out.println("Ihr Pfad (C:\\\\CU-ConsoleUtility)" +
                                "\"java src\\\\ConsoleUtilityItself.java (Windows)" +
                                "\"oder /home/CU-ConsoleUtility java src\\\\ConsoleUtilityItself.java (Linux))" +
                                "\"--erste / --est -> [ENTER] -> \" + \"Schreiben einen Namen für die Datei: (name für die Datei) -> [ENTER] -> " +
                                "„Schreiben Sie die erste Anzahl der Zeichenfolgen in der Datei: (erste Anzahl der Zeichenfolgen) -> (zeigt die definitive Anzahl der Zeichenfolgen aus der Datei an)");
                        case 73 -> System.out.println("Ihr Pfad (C:\\\\\\\\CU-ConsoleUtility)" +
                                "\"\\\"java src\\\\\\\\ConsoleUtilityItself.java (Windows)" +
                                "\"\\\"oder /home/CU-ConsoleUtility java src\\\\\\\\ConsoleUtilityItself.java (Linux))" +
                                "\"\\\"--letzte / --lzt -> [ENTER] -> \\\" + \\\"Schreiben Sie den Namen der Datei: (Name für Ihre Datei) -> [ENTER] -> " +
                                "„Schreiben Sie die letzte Anzahl der Zeilen in der Datei: (letzte Anzahl der Zeilen) -> (zeigt die definitive Anzahl der Zeilen aus der Datei an)");
                        default -> System.err.println("Diese befehle existiert nicht oder es ist noch nicht standartisch in system");
                    }
                }
                case "--andriten", "--and" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben eine datei welche wollen Sie fur richten in datei andern: ");
                    String dateiName = operation.nextLine();
                    Path path = Path.of(dateiName);
                    if(Files.exists(path)) {
                        String linie;
                        try(BufferedReader lesenDatenVonDatei = new BufferedReader(new FileReader(dateiName))) {
                            linie = lesenDatenVonDatei.readLine();
                            if(linie.isEmpty()) {
                                linie = "";
                            }
                        } catch (IOException ex) {
                            throw new RuntimeException(ex.getLocalizedMessage());
                        }
                        Files.delete(path);
                        System.out.println("Schreiben richten in octal system (Beispiel: 700): ");
                        String octalSystem = operation.nextLine();
                        if(octalSystem.length() > 3) {
                            System.err.println(ROT + "Richten mussen nicht mehr als 3 nummeren sein" + RESET);
                        } else {
                            for(Character charNummeren : octalSystem.toCharArray()) {
                                nummerRichten.add(charNummeren);
                            }
                            List<Integer> nummeren = new ArrayList<>();
                            for(int i = 0; i < octalSystem.length(); ++i) {
                                int toNumber = octalSystem.charAt(i) - 48;
                                nummeren.add(toNumber);
                            }
                            for (Integer nummer : nummeren) {
                                ergebnisRichten.add(richten.get(nummer));
                            }
                            String alleRichten = ergebnisRichten.getFirst() + ergebnisRichten.get(1) + ergebnisRichten.getLast();
                            Set<PosixFilePermission> perms = PosixFilePermissions.fromString(alleRichten);
                            FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
                            Files.createFile(path,attr);
                            try(BufferedWriter schreibenZuruckZuDatei = new BufferedWriter(new FileWriter(dateiName))) {
                                schreibenZuruckZuDatei.write(linie);
                            } catch (IOException aus) {
                                throw new RuntimeException(aus.getLocalizedMessage());
                            }
                            System.out.println(GRUN + "Richten fur datei: '" + dateiName + "' waren anderen erfolgreich" + RESET);
                        }
                    }
                }
                case "--adverungen", "--adv" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    String dateiName,neueDateiName = "",verlanderung,neueVerlanderung,wennExistiert,daten = "";
                    System.out.println("Schreiben deine datei ohne verlanderung: ");
                    dateiName = operation.nextLine();
                    System.out.println("Schreiben eine verlanderung fur deine datei: ");
                    verlanderung = operation.nextLine();
                    wennExistiert = dateiName + verlanderung;
                    Path path = Path.of(wennExistiert);
                    if(Files.exists(path)) {
                        try(BufferedReader lesenVonDatei = new BufferedReader(new FileReader(wennExistiert))) {
                            String linie = lesenVonDatei.readLine();
                            if(linie == null || linie.isEmpty()) {
                                daten = " ";
                            } else {
                                daten = linie;
                            }
                        } catch (IOException ex) {
                            throw new RuntimeException(ex.getLocalizedMessage());
                        }
                    } else {
                        System.err.println(ROT + "Diese datei existiert nicht" + RESET);
                        System.exit(0);
                    }
                    Files.deleteIfExists(path);
                    System.out.println("Schreiben eine neue verlanderung due deine datei: ");
                    neueVerlanderung = operation.nextLine();
                    if(!neueVerlanderung.startsWith(".")) {
                        System.err.println(ROT + "Verlanderungen mussen mit '.' starten" + RESET);
                    } else {
                        neueDateiName = dateiName + neueVerlanderung;
                    }
                    try(BufferedWriter schreibenZuruckZuDatei = new BufferedWriter(new FileWriter(neueDateiName))) {
                        schreibenZuruckZuDatei.write(daten);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex.getLocalizedMessage());
                    }
                    System.out.println(GRUN + "Die dateis verlanderung war andert erfolgreich" + RESET);
                }
                case "--symlink","--sym" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    String symlink, direktorei;
                    System.out.println("Schreiben eine direktorei fur welche Sie wollen symbole link erstellen: ");
                    direktorei = operation.nextLine();
                    List<String> findenDirektorei = new ArrayList<>(Files.readAllLines(vomDatei));
                    String findischDirektorei = findenDirektorei.get(findenDirektorei.indexOf(direktorei) + 2);
                    System.out.println(findischDirektorei);
                    if(findischDirektorei.isEmpty()) {
                        System.err.println(ROT + "Diese direktorei existiert nicht" + RESET);
                    } else {
                        Path ziel = Path.of(findischDirektorei);
                        System.out.println("Schreiben Sie symbole link: ");
                        symlink = operation.nextLine();
                        Files.createSymbolicLink(Path.of(symlink),ziel);
                        System.out.println(GRUN + "Symbole link war erstellt erfolgreich: " + new File(symlink).getAbsolutePath() + RESET);
                    }
                }
                case "--leer","--lee" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben eine datei woher wollen Sie einen text loschen: ");
                    String dateiName = operation.nextLine();
                    String name = "", neueDatei;
                    if(Files.exists(Path.of(dateiName))) {
                        name = dateiName;
                    } else {
                        System.out.println(ROT + "Diese datei existiert nicht " + RESET);
                    }
                    try(BufferedReader prufenLeerDaten = new BufferedReader(new FileReader(name))) {
                        String prufen = prufenLeerDaten.readLine();
                        if(prufen == null || prufen.isEmpty()) {
                            System.out.println(GELB + "Diese datei ist schon leer" + RESET);
                        }
                    }
                    neueDatei = name;
                    Files.deleteIfExists(Path.of(name));
                    Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxrwxrwx");
                    FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
                    Files.createFile(Path.of(neueDatei),attr);
                    System.out.println(GRUN + "Text vom datei was loscht erfolgreich" + RESET);
                }
                case "--sortieren","--sor" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben Sie deine Datei: ");
                    String datei_ = operation.nextLine();
                    System.out.println("Schreiben Sie deine direktorei: ");
                    String direktorei = operation.nextLine();
                    String vollWeg = String.format("%s/%s",direktorei,datei_);
                    List<String> alleKatalogien = Files.readAllLines(Path.of(vollWeg));
                    Collections.sort(alleKatalogien);
                    alleKatalogien.forEach(System.out::println);
                    alleKatalogien.clear();
                }
                case "--umkehren","--umk" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben Sie eine datei woher wollen Sie eine daten lesen: ");
                    String dateiName = operation.nextLine();
                    List<String> strings = new ArrayList<>();
                    String linie;
                    try(BufferedReader lesenVom = new BufferedReader(new FileReader(dateiName))) {
                        linie = lesenVom.readLine();
                        if(linie == null || linie.isEmpty()) {
                            System.err.println(ROT + "Daten vom datei sind null" + RESET);
                        }
                    }
                    StringTokenizer token = new StringTokenizer(linie);
                    while(token.hasMoreTokens()) {
                        strings.add(token.nextToken());
                    }
                    Collections.reverse(strings);
                    strings.forEach(System.out::println);
                    strings.clear();
                }
                case "--entAlle","--ena" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben Sie deine direktorei: ");
                    String direktorei = operation.nextLine();
                    if(!direktorei.startsWith("C:\\") && !direktorei.startsWith("/")) {
                        System.err.println(ROT + "Directoreis mussen mit C:\\ (fur Windows) oder / (Linux) starten sein" + RESET);
                    } else {
                        Path start = Path.of(direktorei); 
                        try(Stream<Path> paths = Files.walk(start)) {
                            paths.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(file_ -> {
                                try {
                                    Files.delete(file_.toPath());
                                } catch (IOException e) {
                                    System.err.println(ROT + "Fehler fur dateis loschen: " + file_ + RESET);
                                }
                            });
                        }
                        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxrwxrwx");
                        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
                        Files.createDirectory(start,attr);
                        System.out.println(GRUN + "Alle katalogien und dateis in " + direktorei + " waren loschen erfolgreich" + RESET);
                    }
                }
                case "--entfernen","--ent" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben Sie name fur datei: ");
                    String dateiName = operation.nextLine();
                    System.out.println("Schreiben Sie deine direktorei: ");
                    String direktorei = operation.nextLine();
                    if(!direktorei.startsWith("C:\\") && !direktorei.startsWith("/")) {
                        System.err.println(ROT + "Directories must be started with C:\\ (for Windows) or / (Linux)" + RESET);
                    } else {
                        String fullPath = String.format("%s/%s",direktorei,dateiName);
                        Files.deleteIfExists(Path.of(fullPath));
                        System.out.println(GRUN + "Datei '" + dateiName + "' war loscht vom direktorei '" + direktorei + "' erfolgreich" + RESET);
                    }
                }
                case "--integrieren","--ing" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben Sie deine datei: ");
                    String neueDatei = operation.nextLine();
                    System.out.println("Schreiben Sie deine direktorei: ");
                    String direktorei = operation.nextLine();
                    String fullPath = String.format("%s/%s",direktorei,neueDatei );
                    Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxrwxrwx");
                    FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
                    Files.createFile(Path.of(fullPath),attr);
                    System.out.println(GRUN + "Datei '" + neueDatei  + "' war hinzufugt zu direktorei '" + direktorei + "' erfolgreich" + RESET);
                }
                case "--grsdti","--grs" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben Sie direktorei (fur analysieren alle datei) oder datei: ");
                    String alleOderEin = operation.nextLine();
                    if(new File(alleOderEin).isDirectory()) {
                        if(!alleOderEin.startsWith("C:\\") && !alleOderEin.startsWith("/")) {
                            System.err.println(ROT + "Direktoreis mussen mit C:\\ (fur Windows) oder / (Linux) starten sein" + RESET);
                        } else {
                            try(Stream<Path> wegs = Files.walk(Path.of(alleOderEin))) {
                                wegs.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach
                                        (datei_ -> System.out.println(datei_ + " = " + GELB + datei_.length() + RESET + " bytes"));
                            }
                        }
                    } else {
                        System.out.println("Schreiben Sie ein weg zu deinem datei: ");
                        alleOderEin = operation.nextLine();
                        if(Files.exists(Path.of(alleOderEin))) {
                            System.out.println(alleOderEin + " = " + GELB + new File(alleOderEin).length() + RESET + " bytes");
                        } else {
                            System.err.println(GELB + "Dieser datei existiert nicht" + RESET);
                        }
                    }
                }
                case "--editieren","--edt" -> {
                   hinzufugenGeschichte((index) + " | " + arg);
                   SwingUtilities.invokeLater(() -> new KonsoleDienstProgrammsGBS.TextEditor().setVisible(true));
                }
                case "--symzln","--szn" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben Sie ein name fur deinem datei: ");
                    String dateiName = operation.nextLine();
                    if(Files.exists(Path.of(dateiName))) {
                        try(BufferedReader symboleZahlen = new BufferedReader(new FileReader(dateiName))) {
                            String linie = symboleZahlen.readLine();
                            if(linie == null || linie.isEmpty()) {
                                System.out.println(GRUN + "Symbole: " + 0 + RESET);
                            } else {
                                System.out.println(GRUN + "Symbole: " + linie.length() + RESET);
                            }
                        }
                    } else {
                        System.err.println(ROT + "Dieser datei existiert nicht" + RESET);
                    }
                }
                case "--andegrose","--agr" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben Sie eine name fur deinem datei: ");
                    String dateiname = operation.nextLine();
                    Path path = Path.of(dateiname);
                    if(Files.exists(path)) {
                        System.out.println("Schreiben Sie eine neue grose fur deinem datei: ");
                        long neueGrose = operation.nextLong();
                        String []dateiDaten = new String[2];
                        try(BufferedReader lesenVom = new BufferedReader(new FileReader(dateiname))) {
                            String linie = lesenVom.readLine();
                            if(linie == null || linie.isEmpty()) {
                                linie = "";
                                dateiDaten[0] = linie;
                            } else {
                                dateiDaten[0] = linie;
                            }
                        }
                        dateiDaten[1] = dateiname;
                        Files.deleteIfExists(path);
                        try(RandomAccessFile erstellenWiederUndGroseAndern = new RandomAccessFile(dateiDaten[1],"rw")) {
                            erstellenWiederUndGroseAndern.setLength(neueGrose);
                        }
                        try(BufferedWriter schreibenWiederZuruck = new BufferedWriter(new FileWriter(dateiDaten[1]))) {
                            schreibenWiederZuruck.write(dateiDaten[0]);
                        } catch (IOException exc) {
                            throw new RuntimeException(exc.getLocalizedMessage());
                        }
                        System.out.println(GRUN + "Dateis grose war erfolgreich erstellt" + RESET);
                    } else {
                        System.err.println(ROT + "Diese datei existiert nicht" + RESET);
                    }
                }
                case "--version","--vrs" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("3.3.0");
                }
                case "--sicherung","--scr" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben Sie deine datei welche hat eine daten: ");
                    String dateiname = operation.nextLine();
                    Path path = Path.of(dateiname);
                    if(Files.exists(path)) {
                        try(BufferedReader prufenDatenInDatei = new BufferedReader(new FileReader(dateiname))) {
                            String data = prufenDatenInDatei.readLine();
                            if(data == null || data.isEmpty()) {
                                System.err.println(ROT + "Daten in datei ist leer" + RESET);
                            }
                        }
                        Path reserveCopieren = Path.of("ReserveCopieren.bin");
                        try {
                            Files.copy(path,reserveCopieren,StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException exc) {
                            throw new RuntimeException(exc.getLocalizedMessage());
                        }
                        System.out.println(GRUN + "Fur spreicherung daten vom datei war ReserveCopieren.bin file erstellt" + RESET);
                    } else {
                        System.err.println(ROT + "Diese datei existiert nicht" + RESET);
                    }
                }
                case "--xexport","--exp" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben Sie eine daten: ");
                    String daten = operation.nextLine();
                    XMLOutputFactory fabrik = XMLOutputFactory.newInstance();
                    XMLStreamWriter schreibenZuXML = fabrik.createXMLStreamWriter(new FileWriter("XMLFormat.xml"));
                    schreibenZuXML.writeStartDocument("UTF-8","1.0");
                    schreibenZuXML.writeStartElement("Nachricht");
                    schreibenZuXML.writeCData(daten);
                    schreibenZuXML.writeEndElement();
                    schreibenZuXML.writeEndDocument();
                    schreibenZuXML.close();
                    System.out.println(GRUN + "XML Datei mit daten war erfolgreich erstellt" + RESET);
                }
                case "--ximport","--xm" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    XMLInputFactory fabrik = XMLInputFactory.newInstance();
                    XMLStreamReader lesenVomXML = fabrik.createXMLStreamReader(new FileReader("XMLFormat.xml"));
                    if(lesenVomXML.hasNext()) {
                        lesenVomXML.next();
                    }
                    String []datenVomXML = new String[4];
                    datenVomXML[0] = "Name: " + lesenVomXML.getName();
                    datenVomXML[1] = "Codierung: " + lesenVomXML.getEncoding();
                    datenVomXML[2] = "Version: " + lesenVomXML.getVersion();
                    datenVomXML[3] = "Text " + lesenVomXML.getElementText();
                    for(String info : datenVomXML) {
                        System.out.println(info);
                    }
                    lesenVomXML.close();
                }
                case "--herstellen","--hen" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    try {
                        Files.copy(Path.of("KopierenReserv.bin"),Path.of("VomReserv.txt"),StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOError exc) {
                        throw new RuntimeException(exc.getLocalizedMessage());
                    }
                    try(BufferedReader datenVomReserv = new BufferedReader(new FileReader("VomReserv.txt"))) {
                        System.out.println( datenVomReserv.readLine());
                    } catch (IOException exc) {
                        throw new RuntimeException(exc.getLocalizedMessage());
                    }
                    System.out.println(GRUN + "Daten vom reserv datei waren kopieren erfolgreich" + RESET);
                }
                case "--stats","--sts" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    String zeitBekommen;
                    try(BufferedReader lesenZeit = new BufferedReader(new FileReader("UtilityStatistic.txt"))) {
                        zeitBekommen = lesenZeit.readLine();
                        if(zeitBekommen == null || zeitBekommen.isEmpty()) {
                            zeitBekommen = "";
                        }
                    } catch (IOException exc) {
                        throw new RuntimeException(exc.getLocalizedMessage());
                    }
                    List<Integer> StundeMinuteSekunde = new ArrayList<>();
                    ArrayList<String> zahlenBenutzBefehlen = new ArrayList<>(Files.readAllLines(HistorischDatei));
                    StringTokenizer dividierenZeit = new StringTokenizer(zeitBekommen);
                    while(dividierenZeit.hasMoreTokens()) {
                        StundeMinuteSekunde.add(Integer.parseInt(dividierenZeit.nextToken()));
                    }
                    System.out.println("Benutzen befehlen: " + zahlenBenutzBefehlen.size() + " | BenZeit: " + (LocalTime.now().getHour() - StundeMinuteSekunde.getFirst())
                            + ":" + (LocalTime.now().getMinute() - StundeMinuteSekunde.get(1))
                            + ":" + (Math.abs(LocalTime.now().getSecond() - StundeMinuteSekunde.getLast())));
                }
                case "--suchen","--sun" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    List<String> suchen = new ArrayList<>(List.of(
                            "--hilfen oder --hil","--hinzufugen oder --hin",
                            "--lesen oder --les", "--loschen oder --los","--kopieren oder --kop",
                            "--bewegen oder --bew","--umbenennen oder --umb",
                            "--zeitweiligen oder --zei", "--GBS oder --gbs","--jarchiv oder --jar",
                            "--pfeifen oder -pfe (Windows) / --tar.gz oder -tr (Linux)",
                            "--schreiben oder --sch","--grep oder --gre",
                            "--geschichte oder --ges","--finden oder --fin",
                            "--lstkat oder --lst","--ersetzen oder --ers","--ertdir oder --ert",
                            "--lshdir oder --lsh","--exstdirs oder --exs",
                            "--tldr oder --tld","--andriten oder --and",
                            "--adverungen oder --adv","--symlink oder --sym",
                            "--leer oder --lee","--sortieren oder --sor",
                            "--umkehren oder --umk","--entAlle oder --ena",
                            "--entfernen oder --ent","--integrieren oder ing",
                            "--grsdti oder --grs","--editieren oder --edt","--symzln oder --szn",
                            "--andegrose oder --agr", "--version oder --vrs","--sicherung oder --scr","--xexport dder --xp","--ximport oder --xm",
                            "--herstellen oder --hen","--stats oder --sts","--suchen oder --sun",
                            "--hostinfo oder --hos","--abshalten oder --abs","--neustarten oder --nsn","--fspr oder --fsp",
                            "--sauber oder --sab","--pingen oder --png","--intprok oder --ipk","--unterbrechen oder --ubk",
                            "--filterieren oder --fir","--md5gen oder --mgn", "--sha256gen oder --sgn",
                            "--frieren oder --frn","--einzigartig oder --ezn","--stat oder --sat","--teilen oder --ten",
                            "--rsync oder --rnc","verglen oder --ven","--sysinfo oder --sin","--jungste oder --jng",
                            "--aktiv oder --akt","--benutzername oder --btn","--schneiden oder --scn",
                            "--andkent oder ank","--abmelden oder --abn","--fbef oder --fbf","--gzip oder --gzp",
                            "--fdir oder --fdi","--tilden oder --tln, --spiegel oder --spl, --aktualisieren oder --akl"
                    ));
                    System.out.println("Schreiben welche befehle wollen Sie finden: ");
                    String befehle = operation.nextLine();
                    suchen.stream().filter(findenBefehle -> findenBefehle.startsWith(befehle)).forEach(System.out::println);
                }
                case "--hostinfo","--hos" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    String[] hostInfo = new String[]
                            {
                                    "Host name: " + Inet4Address.getLocalHost().getHostName(),
                                    "Host addresse: " + InetAddress.getLocalHost().getHostAddress(),
                                    "Kanonisch host name: " + InetAddress.getLocalHost().getCanonicalHostName()
                            };
                    for(String info : hostInfo) {
                        System.out.println(info);
                    }
                }
                case "--abshalten","--abs" -> {
                    String bekommenOsName = System.getProperty("os.name");
                    if(Objects.equals(bekommenOsName,"Linux") || Objects.equals(bekommenOsName,"Windows")) {
                        String[]shutdown = new String[]{"shutdown -h now"};
                        try {
                            Process abshaltenProzess = Runtime.getRuntime().exec(shutdown);
                            System.out.println(abshaltenProzess);
                            System.exit(0);
                        } catch (RuntimeOperationsException exc) {
                            throw new RuntimeException(exc.getLocalizedMessage());
                        }
                    } else {
                        System.out.println(ROT + "Utility unterstutzt diese OS nicht" + RESET);
                    }
                }
                case "--neustarten","--nsn" -> {
                    String []OSCBefehle = new String[1];
                    String bekommenOsName = System.getProperty("os.name");
                    if(Objects.equals(bekommenOsName,"Linux")) {
                        OSCBefehle[0] = "reboot";
                    } else if(Objects.equals(bekommenOsName,"Windows")){
                        OSCBefehle[0] = "restart-computer";
                    }
                    try {
                        Process neustartProzess = Runtime.getRuntime().exec(OSCBefehle);
                        System.out.println(neustartProzess);
                        System.exit(0);
                    } catch (RuntimeOperationsException exc) {
                        throw new RuntimeException(exc.getLocalizedMessage());
                    }
                }
                case "--fspr","--fsp" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println(Runtime.getRuntime().freeMemory());
                }
                case "--sauber","--sab" -> {
                    Files.deleteIfExists(HistorischDatei);
                    System.out.println(GRUN + "Geshichte fur befehlen war loscht erfolgreich" + RESET);
                }
                case "--pingen","--png" -> {
                    String IPoderDNS; int pingen_mals;
                    System.out.println("Schreiben Sie eine IP oder DNS welche wollen Sie pingen: ");
                    IPoderDNS = operation.nextLine();
                    System.out.println("Schreiben wie viele mals wollen Sie pingen: ");
                    pingen_mals = operation.nextInt();
                    String[] befehle = new String[0];
                    String OSName = System.getProperty("os.name");
                    if (Objects.equals(OSName,"Windows")) {
                        befehle = new String[]{"ping", "-n", String.valueOf(pingen_mals),IPoderDNS};
                    } else if(Objects.equals(OSName,"Linux")) {
                        befehle = new String[]{"ping", "-c", String.valueOf(pingen_mals), IPoderDNS};
                    } else {
                        System.err.println("DienstProgramm unterstutzt diese OS nicht");
                    }
                    try {
                        Process prozess = Runtime.getRuntime().exec(befehle);
                        BufferedReader lesen = new BufferedReader(new InputStreamReader(prozess.getInputStream()));
                        String linie;
                        while((linie = lesen.readLine()) != null) {
                            System.out.println(linie);
                        }
                        int exitCode = prozess.waitFor();
                        System.out.println(exitCode);
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "--intprok","--ipk" -> {
                    String OSName = System.getProperty("os.name");
                    String[]befehle = new String[0];
                    if(Objects.equals(OSName,"Linux")) {
                        befehle = new String[]{"ip","a"};
                    } else if(Objects.equals(OSName,"Windows")) {
                        befehle = new String[]{"ipconfig"};
                    } else {
                        System.err.println("DienstProgramm unterstutzt diese OS nicht");
                    }
                    try {
                        Process zeigenIP = Runtime.getRuntime().exec(befehle);
                        BufferedReader lesenInText = new BufferedReader(new InputStreamReader(zeigenIP.getInputStream()));
                        String linie;
                        while((linie = lesenInText.readLine()) != null) {
                            System.out.println(linie);
                        }
                        System.out.println(zeigenIP);
                    } catch (IOException exc) {
                        throw new RuntimeException(exc.getLocalizedMessage());
                    }
                }
                case "--unterbrechen","--ubr" -> {
                    long prozess_id;
                    System.out.println("Write the id of your process: ");
                    prozess_id = operation.nextLong();
                    String[]befehle = new String[0];
                    String OSName = System.getProperty("os.name");
                    if(Objects.equals(OSName,"Linux")) {
                        befehle = new String[]{"kill",String.valueOf(prozess_id)};
                    } else if(Objects.equals(OSName,"Windows")) {
                        befehle = new String[]{"taskkill","/PID",String.valueOf(prozess_id),"/F"};
                    } else {
                        System.err.println("DienstProgramm unterstutzt diese OS nicht");
                    }
                    try {
                        Process unterbrechenProzess = Runtime.getRuntime().exec(befehle);
                        System.out.println(unterbrechenProzess);
                    } catch (IOException exc) {
                        throw new RuntimeException(exc.getLocalizedMessage());
                    }
                }
                case "--filterieren","--fir" -> {
                    System.out.println("Schreiben eine direktorei: ");
                    String direktorei = operation.nextLine();
                    System.out.println("Schreiben die grose fur dateis vom direktorei:");
                    int grose = operation.nextInt();
                    operation.nextLine();
                    System.out.println("Schreiben einen operator fur vergleichenung und suchenung dateis in direktorei: ");
                    String compare = operation.nextLine();
                    if (!direktorei.startsWith("C:\\") && !direktorei.startsWith("/")) {
                        System.err.println(ROT + "Directoreis mussen starten mit C:\\ (for Windows) oder / (Linux) sein" + RESET);
                    } else {
                        try (Stream<Path> wegs = Files.walk(Path.of(direktorei))) {
                            Stream<Path> nurDateis = wegs.filter(Files::isRegularFile);
                            switch (compare.charAt(0)) {
                                case '<' -> nurDateis.filter(groseDatei -> {
                                    try {
                                        return Files.size(groseDatei) < grose;
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach
                                        (datei_ -> System.out.println(datei_ + " = " + GELB + datei_.length() + RESET + " bytes"));
                                case '>' -> nurDateis.filter(groseDatei -> {
                                    try {
                                        return Files.size(groseDatei) > grose;
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach
                                        (datei_ -> System.out.println(datei_ + " = " + GELB + datei_.length() + RESET + " bytes"));
                                case '=' -> nurDateis.filter(groseDatei -> {
                                    try {
                                        return Files.size(groseDatei) == grose;
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach
                                        (datei_-> System.out.println(datei_ + " = " + GELB + datei_.length() + RESET + " bytes"));
                                case '!' -> nurDateis.filter(groseDatei -> {
                                    try {
                                        return Files.size(groseDatei) != grose;
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach
                                        (datei_ -> System.out.println(datei_  + " = " + GELB + datei_ .length() + RESET + " bytes"));
                            }
                        }
                    }
                }
                case "--md5gen","--mgn" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben deine datei fur analysierung daten mit md5 algorithmus: ");
                    String bekDateiFurMd5 = operation.nextLine();
                    Path weg = Path.of(bekDateiFurMd5);
                    if(Files.exists(weg)) {
                        String getEingangDaten = Files.readString(weg, StandardCharsets.UTF_8);
                        if(getEingangDaten == null || getEingangDaten.isEmpty()) {
                            getEingangDaten = "0";
                        }
                        MessageDigest md5;
                        try {
                            md5 = MessageDigest.getInstance("MD5");
                        } catch (NoSuchAlgorithmException exc) {
                            throw new RuntimeException(exc.getLocalizedMessage());
                        }
                        byte[] byteHex = md5.digest(getEingangDaten.getBytes(StandardCharsets.UTF_8));
                        for(byte bekByteInHex : byteHex) {
                            System.out.printf("%s",Integer.toHexString(0xFF & bekByteInHex));
                        }
                        System.out.println("\n");
                    }
                }
                case "--sha256gen","--sgn" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben deine datei fur analysierung daten mit sha256 algorithmus: ");
                    String bekDateiFurSha256 = operation.nextLine();
                    Path path = Path.of(bekDateiFurSha256);
                    if (Files.exists(path)) {
                        String getEingangDaten = Files.readString(path, StandardCharsets.UTF_8);
                        if (getEingangDaten == null || getEingangDaten.isEmpty()) {
                            getEingangDaten = "0";
                        }
                        MessageDigest sha256;
                        try {
                            sha256 = MessageDigest.getInstance("SHA-256");
                        } catch (NoSuchAlgorithmException exc) {
                            throw new RuntimeException(exc.getLocalizedMessage());
                        }
                        byte[] byteHex = sha256.digest(getEingangDaten.getBytes(StandardCharsets.UTF_8));
                        for (byte bekByteInHex : byteHex) {
                            System.out.printf("%s", Integer.toHexString(0xFF & bekByteInHex));
                        }
                        System.out.println("\n");
                    }
                }
                case "--frieren","--frn" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    long prozess_id;
                    System.out.println("Schreiben dein id fur prozess: ");
                    prozess_id = operation.nextLong();
                    String []befehle= new String[0];
                    String OSName = System.getProperty("os.name");
                    if(Objects.equals(OSName,"Linux")) {
                        befehle = new String[]{"kill","-STOP",String.valueOf(prozess_id)};
                    } else if(Objects.equals(OSName,"Windows")) {
                        befehle = new String[]{"taskkill","/PID",String.valueOf(prozess_id),"/F"};
                    } else {
                        System.err.println("Utility unterstutzt diese OS nicht");
                    }
                    try {
                        Process freezeProcess = Runtime.getRuntime().exec(befehle);
                        System.out.println(freezeProcess);
                    } catch (IOException exc) {
                        throw new RuntimeException(exc.getLocalizedMessage());
                    }
                }
                case "--einzigartig","--ezg" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    System.out.println("Schreiben deine datei: ");
                    String bekDatei = operation.nextLine();
                    Path weg = Path.of(bekDatei);
                    if (Files.exists(weg)) {
                        List<String> bekAlleStringen = new ArrayList<>(Files.readAllLines(weg));
                        Set<String> einzigartigStringen = new LinkedHashSet<>(bekAlleStringen);
                        bekAlleStringen.clear();
                        einzigartigStringen.forEach(System.out::println);
                        einzigartigStringen.clear();
                    } else {
                            System.err.println(ROT + "Diese datei existiert nicht" + RESET);
                    }
                }
                case "--stat","--sat" -> {
                    System.out.println("Schreiben name for deinem datei: ");
                    String dateiName = operation.nextLine();
                    Path path = Path.of(dateiName); 
                    if(Files.exists(path)) {
                        System.out.println("Groses name: " + new File(dateiName).getName());
                        System.out.println("Groses size: " + new File((dateiName)).length());
                        BasicFileAttributes attributes = Files.readAttributes(path,BasicFileAttributes.class);
                        FileTime erstellungZeit = attributes.creationTime();
                        FileTime letzteEinstellungZeit = attributes.lastModifiedTime();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd");
                        LocalDateTime erstellungDatumZeit = LocalDateTime.ofInstant(erstellungZeit.toInstant(), ZoneId.systemDefault());
                        LocalDateTime erstellungEinstellungZeit = LocalDateTime.ofInstant(letzteEinstellungZeit.toInstant(), ZoneId.systemDefault());
                        System.out.println("Erstellung dateis zeit: " + erstellungDatumZeit.format(formatter));
                        System.out.println("Letzte einstellungs zeit: " + erstellungEinstellungZeit.format(formatter));
                    } else {
                        System.err.println(ROT + "Diese datei existiert nicht" + RESET);
                    }
                }
                case "--teilen","--ten" -> {
                    System.out.println("Schreiben eine name fur deinem datei: ");
                    String dateiName = operation.nextLine();
                    Path path = Path.of(dateiName);
                    if(Files.exists(path)) {
                        String datenVomDatei;
                        try(BufferedReader readData = new BufferedReader(new FileReader(dateiName))) {
                            datenVomDatei = readData.readLine();
                            if(datenVomDatei == null || datenVomDatei.isEmpty()) {
                                datenVomDatei = " ";
                            }
                        }
                        byte[]byteDaten = Files.readAllBytes(path);
                        byte[]ersteHalbbyten = new byte[byteDaten.length / 2];
                        byte[]zweiteHalbbyten = new byte[byteDaten.length];
                        if (byteDaten.length >= 0) System.arraycopy(byteDaten, 0, zweiteHalbbyten, 0, byteDaten.length);
                        if (byteDaten.length / 2 >= 0)
                            System.arraycopy(byteDaten, 0, ersteHalbbyten, 0, byteDaten.length / 2);
                        Files.deleteIfExists(path);
                        String new_ = "Neue";
                        String datei_ = dateiName.substring(0,dateiName .indexOf('.'));
                        String veranderungen = dateiName .substring(dateiName.indexOf('.'),dateiName.length());
                        File neueDatei = new File(new_ + datei_ + veranderungen);
                        File neueDatei2 = new File(new_ + datei_ + "2" + veranderungen);
                        try {
                            Files.write(neueDatei.toPath(),ersteHalbbyten,StandardOpenOption.CREATE,StandardOpenOption.APPEND);
                        } catch (IOException exc) {
                            throw new RuntimeException(exc.getLocalizedMessage());
                        }
                        try {
                            Files.write(neueDatei2.toPath(),zweiteHalbbyten,StandardOpenOption.CREATE,StandardOpenOption.APPEND);
                        } catch (IOException exc) {
                            throw new RuntimeException(exc.getLocalizedMessage());
                        }
                        System.out.println(GRUN + "Teilenung datei " + dateiName  + " ist erfolgreich" + RESET);
                    } else {
                        System.err.println(ROT + "Diese datei fur teilenung existiert nicht" + RESET);
                    }
                }
                case "--rsync","--rnc" -> {
                    System.out.println("Schreiben deine quelle datei: ");
                    String QuelleDatei = operation.nextLine();
                    System.out.println("Schreiben deine ziele datei: ");
                    String ZieleDatei = operation.nextLine();
                    Path path = Path.of(QuelleDatei);
                    if(Files.exists(path)) {
                        Path path1 = Path.of(ZieleDatei);
                        if(Files.exists(path1)) {
                            byte[] byteDaten = Files.readAllBytes(path);
                            try {
                                Files.write(path1, byteDaten,StandardOpenOption.APPEND);
                            } catch(IOException exc) {
                                throw new RuntimeException(exc.getLocalizedMessage());
                            }
                            System.out.println(GRUN +"Byte daten mit grose: " + byteDaten.length + " waren synchroniziert zu neue datei erfolgreich" + RESET);
                            System.out.println(GRUN + "Die operation war endet zu " + LocalDateTime.now() + RESET);
                        } else {
                            System.err.println(ROT + "Fehler. Das ist unmoglich fur schreibenung daten vom quelle datei zu unbekannt ziele. Erstellen Sie bitte eine neue ziele datei" + RESET);
                        }
                    } else {
                        System.err.println(ROT + "Fehler. Das ist unmoglich fur schreibenung daten vom unbekannt ziele datei zu ziele datei. Erstellen Sie bitte eine neue quelle datei und schreiben daten zu diesel datei" + RESET);
                    }
                }
                case "--verglen","--ven" -> {
                    String ersteDatei, zweiteDatei, ersteDaten = "", zweiteDaten = "";
                    System.out.println("Schreiben die erste datei: ");
                    ersteDatei = operation.nextLine();
                    System.out.println("Schreiben die zweite datei: ");
                    zweiteDatei = operation.nextLine();
                    Path path1 = Path.of(zweiteDatei);
                    Path path2 = Path.of(ersteDatei);
                    if(!Files.exists(path2) || !Files.exists(path1)) {
                        System.err.println(ROT + "Ein von zwelen dateis ist keine erkannt" + RESET);
                    } else {
                        Path path = path2;
                        List<String> prufenSorten1 = new ArrayList<>(Files.readAllLines(path));
                        List<String> prufenSorten2 = new ArrayList<>(Files.readAllLines(path1));
                        if(prufenSorten1.stream().sorted().isParallel() || prufenSorten2.stream().sorted().isParallel()) {
                            ersteDaten = String.valueOf(Files.readAllLines(path));
                            zweiteDaten = String.valueOf(Files.readAllLines(path1));
                        }
                        if(Objects.equals(ersteDaten, zweiteDaten)) {
                            System.out.println(ersteDaten);
                        } else {
                            System.out.println(ersteDaten + " | " + zweiteDaten);
                        }
                    }
                }
                case "--sysinfo","--sin" -> {
                    System.out.println("System Information: ");
                    System.out.println(GRUN + "CPU >> " + RESET);
                    for(String partInfo : bekommCpuInfo()) {
                        System.out.println(partInfo);
                    }
                    System.out.println(GRUN + "Platte >> " + RESET);
                    File[] roots = File.listRoots();
                    for(File diskInfo : roots) {
                        System.out.println(GELB + "Platte > " + RESET + diskInfo.getAbsolutePath());
                        System.out.println(GELB + "Total spreicher > " + RESET + diskInfo.getTotalSpace());
                        System.out.println(GELB + "Nutzbar spreicher > " + RESET + diskInfo.getUsableSpace());
                        System.out.println(GELB + "Frei spreicher > " + RESET + diskInfo.getFreeSpace());
                    }
                    System.out.println(GELB + "Heap >> " + RESET);
                    for(String heapInfo : bekommHeapInfo()) {
                        System.out.println(heapInfo);
                    }
                }
                case "--jungste","--jng" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    Path alleJungsteAktiv = Path.of("Recent.txt");
                    new ArrayList<>(Files.readAllLines(alleJungsteAktiv)).forEach(System.out::println);
                }
                case "--aktiv","--akt" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    String benutzername, aktivVom;
                    if(Files.exists(Path.of("Username.txt"))) {
                        try(BufferedReader readUserName = new BufferedReader(new FileReader("Username.txt"))) {
                            benutzername = readUserName.readLine();
                            if(benutzername == null || benutzername.isEmpty()) {
                                benutzername= "";
                            }
                        } catch (IOException exc) {
                            throw new RuntimeException(exc.getLocalizedMessage());
                        }
                        try(BufferedReader readTime = new BufferedReader(new FileReader("UtilityStatistic.txt"))) {
                            aktivVom = readTime.readLine();
                            if(aktivVom == null || aktivVom.isEmpty()) {
                                aktivVom = "";
                            }
                        } catch (IOException exc) {
                            throw new RuntimeException(exc.getLocalizedMessage());
                        }
                        StringTokenizer dividierenZeitDaten = new StringTokenizer(aktivVom);
                        List<String> zeitIntervalen = new ArrayList<>();
                        while(dividierenZeitDaten.hasMoreTokens()) {
                            zeitIntervalen.add(dividierenZeitDaten.nextToken());
                        }
                        String formatZeit = zeitIntervalen.getFirst() + ":" + zeitIntervalen.get(1) + ":" + zeitIntervalen.getLast();
                        zeitIntervalen.clear();
                        System.out.println("BENUTZERNAME: " + benutzername + " | AKTIV VOM: " + formatZeit + " | ZEIT JETZT: " + LocalDateTime.now());
                    } else {
                        System.err.println(ROT + "Sie haben abmelden vom Utilitys system." + RESET);
                    }
                }
                case "--benutzername","--btn" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    String benutzername;
                    Console lesenKennwort = System.console();
                    char[] kennwort = lesenKennwort.readPassword( "Schreiben Sie einen kennwort: ");
                    try(BufferedReader prufenKennwort = new BufferedReader(new FileReader("PasswordManager.txt"))) {
                        if(Arrays.equals(kennwort, prufenKennwort.readLine().toCharArray())) {
                            try(BufferedReader lesenBenutzerName = new BufferedReader(new FileReader("Username.txt"))) {
                                benutzername = lesenBenutzerName.readLine();
                                if(benutzername == null || benutzername.isEmpty()) {
                                    benutzername = "";
                                }
                            }
                            System.out.println(GRUN + benutzername + RESET);
                        } else {
                            System.err.println(ROT + "Dieser kennwort ist falsch oder Sie haben abmelden vom Utilitys system" + RESET);
                            System.exit(0);
                        }
                    } catch (IOException exc) {
                        throw new RuntimeException(exc.getLocalizedMessage());
                    }
                }
                case "--schneiden","--scn" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    int startIndex, endIndex, wahlen;
                    System.out.println("Schreiben Sie eine name fur datei: ");
                    String dateiName = operation.nextLine();
                    String daten;
                    if(Files.exists(Path.of(dateiName))) {
                        try(BufferedReader lesenDaten = new BufferedReader(new FileReader(dateiName))) {
                            daten = lesenDaten.readLine();
                            if(daten == null || daten.isEmpty()) {
                                daten = "";
                            }
                        }
                        System.out.println("Wahlen Sie eine variante fur schneidenung stringen: ");
                        System.out.println("1. Scnneiden stringen mit indexen");
                        System.out.println("2. Schneiden stringen mit trennzeichnen: ");
                        wahlen = operation.nextInt();
                        if(wahlen == 1) {
                            System.out.println("Schreiben einen erste index: ");
                            startIndex = operation.nextInt();
                            System.out.println("Schreiben einen zweite index: ");
                            endIndex = operation.nextInt();
                            if(startIndex < 0) {startIndex = 0;}
                            if(endIndex >= daten.length()) {endIndex = daten.length();}
                            System.out.println(daten.substring(startIndex,endIndex));
                        } else if(wahlen == 2) {
                            StringTokenizer dividierenStringFurTokenen = new StringTokenizer(daten);
                            while(dividierenStringFurTokenen.hasMoreTokens()) {
                                System.out.println(dividierenStringFurTokenen.nextToken());
                            }
                        }
                    } else {
                        System.err.println(ROT + "Diese datei existiert nicht" + RESET);
                    }
                }
                case "--andkent","--ank" -> {
                    hinzufugenGeschichte((index) + " | " + arg);
                    File spreichernKennwort = new File("PasswordManager.txt");
                    Console deinKennwort = System.console(), andernKennwort = System.console();
                    char[] kennwort = deinKennwort.readPassword("Schreiben deinen kennwort: ");
                    try(BufferedReader lesen = new BufferedReader(new FileReader(spreichernKennwort))) {
                        if(Arrays.equals(kennwort, lesen.readLine().toCharArray())) {
                            char[] neueKennwort = andernKennwort.readPassword("Andern Sie einen kennwort: ");
                            if(neueKennwort.length < 8) {
                                System.err.println(ROT + "Grose fur kennwort muss nicht kleiner als 8 symbolen sein" + RESET);
                            } else {
                                try(BufferedWriter schreibenEineNeueKennwort = new BufferedWriter(new FileWriter(spreichernKennwort))) {
                                    schreibenEineNeueKennwort.write(neueKennwort);
                                } catch (IOException exc) {
                                    throw new RuntimeException(exc.getLocalizedMessage());
                                }
                                System.out.println(GRUN + "Kennwort war andert erfolgreich" + RESET);
                            }
                        } else {
                            System.err.println(ROT + "Fehler. Dieser kennwort existiert nicht oder war abmeldet vom DienstProgramm" + RESET);
                        }
                    } catch (IOException exc) {
                        throw new RuntimeException(exc.getLocalizedMessage());
                    }
                }
                case "--abmelden","--abn" -> {
                    Files.deleteIfExists(Path.of("Username.txt"));
                    Files.deleteIfExists(Path.of("PasswordManager.txt"));
                    try(BufferedWriter istLogout = new BufferedWriter(new FileWriter("IsLogout.txt"))) {
                        istLogout.write("true");
                    } catch (IOException exc) {
                        throw new RuntimeException(exc.getLocalizedMessage());
                    }
                    System.out.println(GRUN + "Loschenung kennwort war erfolgreich" + RESET);
                }
                case "--fbef", "--fbf" -> {
                    System.out.println("Schreiben eine name fur befehle vom geschichte: ");
                    String befehleName = operation.nextLine();
                    loadenGeschichte().stream().filter(findenBefehle -> findenBefehle.endsWith(befehleName)).forEach(System.out::println);
                }
                case "--gzip", "--gzp" -> {
                    List<byte[]> bytes = new ArrayList<>();
                    List<String> dateis = new ArrayList<>();
                    System.out.println("Schreiben eine name fur datei fur lesenung daten vom:");
                    String dateiName = operation.nextLine();
                    if (Files.exists(Path.of(dateiName))) {
                        dateis.add(dateiName);
                        String daten;
                        try (BufferedReader readDataString = new BufferedReader(new FileReader(dateiName))) {
                            daten = readDataString.readLine();
                            if (daten == null || daten.isEmpty()) {
                                daten = "";
                            }
                        }
                        System.out.println("Wahlen eine optionen: ");
                        System.out.println("1. Kompresse datei");
                        System.out.println("2. Dekompresse datei");
                        int option = operation.nextInt();
                        if (option < 1 || option > 2) {
                            System.err.println(ROT + "Wahlerung datei fehler" + RESET);
                        } else if (option == 1) {
                            ByteArrayOutputStream AusGabeByteString = new ByteArrayOutputStream();
                            try (GZIPOutputStream schreibenStringenBytes = new GZIPOutputStream(AusGabeByteString)) {
                                schreibenStringenBytes.write(dateiName.getBytes());
                            } catch (IOException exc) {
                                throw new RuntimeException(exc.getLocalizedMessage());
                            }
                            System.out.println(GRUN + "Original grose: " + daten.length() + RESET);
                            System.out.println(GRUN + "Kompresse grose: " + AusGabeByteString.size() + RESET);
                            byte[] bekommeBytesString = Base64.getEncoder().encodeToString(AusGabeByteString.toByteArray()).getBytes();
                            bytes.add(bekommeBytesString);
                        } else {
                            ByteArrayInputStream EinGabeByteString = new ByteArrayInputStream(bytes.get(dateis.indexOf(dateiName)));
                            String dekompresseString =  bekommenString(EinGabeByteString);
                            System.out.println(GRUN + "Dekompresse daten: " + dekompresseString + RESET);
                            System.out.println(GRUN + "Dekompresse grose: " + dekompresseString.length() + RESET);
                        }
                    }
                }
                case "--fdir", "--fdi" -> {
                    System.out.println("Schreiben eine name fur deinem direktorei: ");
                    String findenDirektorei = operation.nextLine();
                    loadenWegDirectoreis().stream().filter(findenDir -> findenDir.endsWith(findenDirektorei)).forEach(System.out::println);
                }
                case "--tilgen", "--tlg" -> {
                    Files.deleteIfExists(DirektoreiDatei);
                    try (BufferedWriter herstellen = new BufferedWriter(new FileWriter(DirektoreiDatei.toFile()))) {
                        herstellen.write("");
                    } catch (IOException aus) {
                        throw new RuntimeException(aus.getLocalizedMessage());
                    }
                    System.out.println(GRUN + "Eine liste fur direktoreis " + RESET);
                }
                case "--spiegel","--spl" -> {
                    System.out.println("Schreiben eine name vom datei: ");
                    String dateiName = operation.nextLine();
                    Path weg = Path.of(dateiName);
                    if(Files.exists(weg)) {
                        List<String> bekStringen = Files.readAllLines(weg);
                        Files.delete(weg);
                        StringBuilder ersString = new StringBuilder();
                        while(!bekStringen.isEmpty()) {
                            ersString.append(bekStringen.getFirst());
                            ersString.append(" ");
                            bekStringen.remove(bekStringen.getFirst());
                        }
                        String forByteString = ersString.toString();
                        String reversedString = new StringBuilder(forByteString).reverse().toString();
                        byte[] bekBytes = reversedString.getBytes();
                        Files.write(weg,bekBytes,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
                        System.out.println(GRUN + "Daten vom datei waren geandert erfolgreich" + RESET);
                        bekStringen.clear();
                    } else {
                        System.err.println(ROT + "Diese datei existiert nicht" + RESET);
                    }
                }
                case "--aktualisieren","--akl" -> {
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
                    System.out.println(GRUN + "Dateis fur konfiguration were geaktualisiert erfolgreich" + RESET);
                }
                case "--wasIst","--was" -> {
                    hinzufugenGeschichte((index++) + " | " + arg);
                    System.out.println("Schreiben Sie eine befehle von DienstProgramm: ");
                    String befehle = operation.nextLine();
                    if(befehle.length() <= 2) {
                        System.err.println(ROT + "Diese befehle existiert nicht" + RESET);
                    } else {
                        DienstProgrammBefehlen.stream().filter(getCommand -> getCommand.startsWith(befehle)).forEach(System.out::println);
                    }
                }
                case "--bekweg","--bkw" -> {
                    System.out.println("Schreiben eine name fur datei: ");
                    String dateiName = operation.nextLine();
                    if(Files.exists(Path.of(dateiName))) {
                        String[]typWeg = new String[3];
                        typWeg[0] = "";
                        typWeg[1] = "Absolute weg: " + new File(dateiName).getAbsolutePath();
                        typWeg[2] = "Kanonisch weg: " + new File(dateiName).getCanonicalPath();
                        for(int bekweg = 1; bekweg < typWeg.length; ++bekweg) {
                            System.out.println(typWeg[bekweg]);
                        }
                    } else {
                        System.err.println(ROT + "Diese datei existiert nicht" + RESET);
                    }
                }
                case "--erste","--est" -> {
                    System.out.println("Schreiben Sie eine name fur datei: ");
                    String dateiName = operation.nextLine();
                    System.out.println("Schreiben Sie erste zahlen fur stringen von datei: ");
                    int ersteZahlen = operation.nextInt();
                    List<String> datenVonDatei = Files.readAllLines(Path.of(dateiName));
                    if(ersteZahlen <= 0 || ersteZahlen > datenVonDatei.size()) {
                        System.err.println(ROT + "Ungültige Zählzeichenfolgen" + RESET);
                    } else {
                        for(int getAllData = 0; getAllData < ersteZahlen; ++getAllData) {
                            if(datenVonDatei.get(getAllData).isEmpty()) {
                                break;
                            }
                            System.out.println(datenVonDatei.get(getAllData));
                        }
                    }
                }
                case "--letzte","--lzt" -> {
                    System.out.println("Schreiben Sie eine name fur datei: ");
                    String dateiName = operation.nextLine();
                    List<String> datenVonDatei = Files.readAllLines(Path.of(dateiName));
                    System.out.println("Schreiben Sie letzte zahlen fur stringen von datei: ");
                    int letzteZahlen = operation.nextInt(),aktuelleZahlung = datenVonDatei.size() - letzteZahlen;
                    if(letzteZahlen <= 0) {
                        System.err.println(ROT + "Ungültige Zählzeichenfolgen" + RESET);
                    } else {
                        for(int bekAlleDaten = aktuelleZahlung; bekAlleDaten < datenVonDatei.size(); ++bekAlleDaten) {
                            if(datenVonDatei.get(bekAlleDaten).isEmpty()) {
                                break;
                            }
                            System.out.println(datenVonDatei.get(bekAlleDaten));
                        }
                    }
                }
                case null, default -> System.err.println(ROT + "Diese operation existiert nicht" + RESET);
            }
        }
    }
    private static String bekommenString(ByteArrayInputStream InPutByteString) throws IOException {
        StringBuilder dekompresseString = new StringBuilder();
        try(GZIPInputStream lesenStringBytes = new GZIPInputStream(InPutByteString)) {
            byte[] bytePuffer = new byte[1024];
            int len;
            while((len = lesenStringBytes.read(bytePuffer)) > 0) {
                dekompresseString.append(new String(bytePuffer,0,len));
            }
        }
        return dekompresseString.toString();
    }
    private static String[] bekommCpuInfo() {
        OperatingSystemMXBean sysinfo = ManagementFactory.getOperatingSystemMXBean();
        return new String[]{
                GELB + "Name > " + RESET + sysinfo.getName(),
                GELB + "Architektur > "  + RESET + sysinfo.getArch(),
                GELB  + "Systems version > " + RESET + sysinfo.getVersion(),
                GELB  + "CPUs faden > " + RESET + sysinfo.getAvailableProcessors(),
                GELB + "Lastdurchschnitt > " + RESET + sysinfo.getSystemLoadAverage()
        };
    }
    private static String[] bekommHeapInfo() {
        Runtime getHeapInfo = Runtime.getRuntime();
        return new String[]{
                GELB  + "Total Heap > " + RESET + getHeapInfo.totalMemory(),
                GELB  + "Max Heap > " + RESET + getHeapInfo.maxMemory(),
                GELB  + "Frei Heap > " + RESET + getHeapInfo.freeMemory(),
                GELB  + "Usable Heap > " + RESET + (getHeapInfo.maxMemory() - getHeapInfo.freeMemory())
        };
    }
    public static List<String> alleBefehlen() {
        return new LinkedList<>(
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
                        "--exstdirs       / --exs = analysieren alle direktoreis welche hat ein benutzer erstellt",
                        "--tldr           / --tld = anweisungen fur alles befehlen in diese KonsoleDienstProgramm",
                        "--andriten       / --and = andern eine richten fur datei. Beispiel: 700",
                        "--adverungen     / --adv = andern eine verlanderungen fur datei",
                        "--symlink        / --sym = erstellen eine symbole linke fur datei",
                        "--leer           / --lee = deine datei hat keine daten",
                        "--sortieren      / --sor = sortieren deine daten vom datei",
                        "--umkehren       / --umk = umkehren deine daten vom begin zu end",
                        "--entAlle        / --ena = entfernen alle katalogien vom deiner direktorei",
                        "--entfernen      / --ent = entfernen eine katalogie vom deiner direktorei",
                        "--integrieren    / --ing = integrieren deine katalogie zur direktorei",
                        "--grsdti         / --grs = analysieren alle datei oder ein datei mit grose in bytes",
                        "--editieren      / --edt = editieren datei mit GUI version wann will benutzer schreiben all texten in datei",
                        "--symzln         / --szn = symbole zahlen in deinem datei",
                        "--andegrose      / --agr = andern eine grose fur datei",
                        "--version        / --vrs = zeigen eine version fur deinem Konsole Dienst Programm",
                        "--sicherung      / --scr = spreichern sicherung mit daten in ReserveCopieren.bin",
                        "--xexport        / --exp = exportieren eine daten zu XML datei",
                        "--ximport        / --exm = importieren daten vom XML datei",
                        "--herstellen     / --hen = zuruckgeben daten vom vom sicherung datei",
                        "--stats          / --sts = zeit fur benutzerung dieser projekt",
                        "--suchen         / --sun = suchen eine befehle fur shablone",
                        "--hostinfo       / --hos = dienstprogramm will zeigen dir information uber deinem host daten",
                        "--abshalten      / --abs = dein komputer will abshalten seine arbeit und prozesse",
                        "--neustarten     / --nsn = dein komputer will neustarten seine arbeit",
                        "--fspr           / --fsp = dein komputer will zeigen dir eine information uber frei spreicher in bytes",
                        "--sauber         / --sab = datei welche spreichert deine befehlen, will sauber ihnen",
                        "--pingen         / --png = pingen eine IP oder DNS in benutzers komputer",
                        "--intprok        / --ipk = zeigen eine benutzers IP",
                        "--unterbrechen   / --ubr = unterbrechen eine ID fur prozess in benutzers komputer",
                        "--filterieren    / --fir = filterieren eine dateis in direktorei mit grose",
                        "--md5gen         / --mgn = bekommen eine daten vom datei in byte-hex mit sha256 algorithmus",
                        "--sha256gen      / --sgn = bekommen eine daten vom datei in byte-hex mit md5 algorithmus",
                        "--frieren        / --frn = frieren mit id einen prozess",
                        "--einzigartig    / --ezn = bekommen eine einzigartige und sortiere daten",
                        "--stat           / --sat = dateis beschreibenung",
                        "--teilen         / --ten = teilen eine datei zu zwei datei",
                        "--rsync          / --rnc = synchronizieren daten vom einem datei zu anderem datei",
                        "--verglen        / --ven = vergleichen zwei sortische datei mit daten",
                        "--sysinfo        / --sin = zeigen eine information uber system benutzers",
                        "--jungste        / --jng = zeigt die Informationen zur letzten Eingabe durch Anmeldung oder Registrierung im Dienstprogramm an",
                        "--aktiv          / --akt = zeigt die Informationen über den Benutzer im System und seine Aktivität ab dem Zeitpunkt der Anmeldung oder Registrierung im Dienstprogramm an",
                        "--benutzername   / --btn = zeigt den eindeutigen zufälligen Spitznamen für den Benutzer im System an",
                        "--andkent        / --ank = andern kennwort in deiner system in DienstProgramm",
                        "--abmelden       / --abn = abmelden vom Utilitys system",
                        "--fbef           / --fbf = finden befehle vom befehles geschichte",
                        "--gzip           / --gzp = kompressen und dekompressen daten vom datei",
                        "--fdir           / --fdi = finden direktorei vom liste fur direktoreis",
                        "--tilden         / --tln = tilden direktoreis vom list fur direktoreis",
                        "--spiegel        / --spl = umkehren stringen mit bytes",
                        "--aktualisieren  / --akl = aktualisieren alle dateis fur konfiguration fur datei",
                        "--wasIst         / --was = bekommen einen definitiv string von datei",
                        "--bekweg         / --bkw = typen fur dateis weg - absolute und kanonisch",
                        "--erste          / --est = bekommen definitiv erste stringen von datei",
                        "--letzte         / --lzt = bekommen definitiv letzte stringen von datei"
                ));
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
        private static class TextEditor extends JFrame {
            private final JTextArea textplatz;
            private final JFileChooser dateiWahler;
            public TextEditor() {
                setTitle("Datei editor");
                setSize(800, 600);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                textplatz = new JTextArea();
                JScrollPane scrollPane = new JScrollPane(textplatz);
                add(scrollPane, BorderLayout.CENTER);
                dateiWahler = new JFileChooser();
                initMenu();
            }
            private void initMenu() {
                JMenuBar menuBar = new JMenuBar();
                JMenu dateiMenu = new JMenu("Datei");
                dateiMenu.setSize(300,300);
                JMenuItem offnen = new JMenuItem("Offnen");
                offnen.addActionListener(e -> offnenDatei());
                JMenuItem spreichern = new JMenuItem("Spreichern");
                spreichern.addActionListener(e -> spreichernDatei());
                dateiMenu.add(offnen);
                dateiMenu.add(spreichern);
                menuBar.add(dateiMenu);
                setJMenuBar(menuBar);
            }
            private void offnenDatei() {
                int zuruckVal = dateiWahler.showOpenDialog(this);
                if (zuruckVal == JFileChooser.APPROVE_OPTION) {
                    File file = dateiWahler.getSelectedFile();
                    try (BufferedReader leser = new BufferedReader(new FileReader(file))) {
                        String linie;
                        StringBuilder content = new StringBuilder();
                        while ((linie = leser.readLine()) != null) {
                            content.append(linie).append("\n");
                        }
                        textplatz.setText(content.toString());
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(this, ROT + "Lesen datei fehler" + RESET);
                    }
                }
            }
            private void spreichernDatei() {
                int zuruckVal = dateiWahler.showSaveDialog(this);
                if (zuruckVal  == JFileChooser.APPROVE_OPTION) {
                    File datei = dateiWahler.getSelectedFile();
                    try (BufferedWriter schreiber = new BufferedWriter(new FileWriter(datei))) {
                        schreiber.write(textplatz.getText());
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(this, ROT + "Spreichern datei fehler" + RESET);
                    }
                }
            }
        }
    }
}

