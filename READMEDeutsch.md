# CU-ConsoleUtility 
Die einfache Version des Konsolen-Dienstprogramms mit einfachen Befehlen. Es wurde in Java geschrieben und unterstützt die beiden Sprachen Deutsch und Englisch.
# Java
Dieses Projekt wurde in einer Java-ähnlichen Sprache erstellt. Die Java-Version ist 23. ConsoleUtility unterstützt alle Java-Versionen ab Java 10/11.
# Befehlszeilen
ConsoleUtility funktioniert in Windows PowerShell und der Bash-Zeile unter Linux.
# Compiler für Java
Zum Starten dieses Projekts benötigen Sie einen Java-Compiler oder JavaC. Unter Windows wird dieser Compiler automatisch hinzugefügt, wenn Sie die Intellig Idea IDE verwenden. Wenn Sie unter Linux das Standard-JDK-Paket nicht haben, müssen Sie es in Ihre Bash-Zeile und Ihre verwendete Distribution importieren. Sie können das Standard-JDK-Paket einfach mit `sudo apt install default-jdk` installieren (Beispiel für die Ubuntu-Distribution unter Linux).
# Wie starte ich das Projekt?
Laden Sie die Datei CU-ConsoleUtility aus diesem Repository herunter und platzieren Sie sie in Windows PowerShell nach dem Pfad C:\ oder in Linux nach dem Pfad /home/ in der Bash-Zeile.

*(Hinweis: Verwenden Sie zum Ändern des Pfads (unter Windows) den Befehl set-location wie in Windows PowerShell. Unter Linux können Sie mit cd in das Verzeichnis dieses Repositorys wechseln und mit cd - zum vorherigen Katalog bzw. zum Home-Verzeichnis /home/ zurückkehren.)*

Und der Pfad ändert sich zu `C:\CU-ConsoleUtility` (unter Windows). Im Linux-System in der Bash-Zeile: `/home/CU-ConsoleUtility`. Nach `C:\CU-ConsoleUtility` müssen Sie schreiben: **java src/com/console/application/Main.java**. In der Bash-Zeile unter Linux nach `/home/CU-ConsoleUtility`: **java src\com\console\application\Main.java**. Dieser Befehl startet das ConsoleUtility und Sie müssen sich beim ConsoleUtility registrieren (WARNUNG: Die Passwortverwaltung in diesem Projekt ist einfach und verwendet die TXT-Datei des Programms. Dies ist das Schulungshandbuch). Nach der Registrierung oder Anmeldung (falls Sie das Passwort bereits hatten) können Sie eine Arbeitssprache auswählen: Deutsch oder Englisch. Nach der Auswahl des Programms erhalten Sie den Pfad zur richtigen Version des ConsoleUtility: src/ConsoleUtilityItself.java (Englisch) oder src/KonsoleDienstProgramm.java (Deutsch). Außerdem werden Ratschläge zur Einarbeitung in die Befehle des konkreten Dienstprogramms gegeben: --help, --h (englisches ConsoleUtility) oder --helfen, --hel (deutsches ConsoleUtility).

# Montagesystem
ConsoleUtility wie ein Projekt, das mit dem Mavens-System gesammelt wurde
# Ein Beispiel für die Arbeit mit diesem Projekt: 
<img width="1920" height="320" alt="471703479-02edef4c-22da-4bcd-9b94-d708704f8c7f" src="https://github.com/user-attachments/assets/1d6c1631-8310-4195-9d84-171d61e625bc" />

# Integration mit anderer IDE
Sie können dieses Projekt für Ihre IDE integrieren. Diese Repositorei enthält die pom.xml-Datei, die ein Erstellungssystem für ein Projekt hat. 
# Andere Wege wie dieses Projekt erstellen
Sie können dieses Projekt mit „<> Code“ Taste im GutHub erstellen. Anderer Weg: mit diesen Befehlen: `git clone https://github.com/Frolotey1/ConsoleUtility-.git` Diese Befehle werden das Projekt dort erstellen, wo sie die Befehle gestartet haben.
