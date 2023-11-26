# Transfermodul Programmieraufgabe
#gradle, #javafx, #mvp, #jdbc, #sqlite

## HowTo
### Windows
- have java
- get <a href="https://gradle.org/releases/">gradle</a>
- add gradle bin to PATH
- `git clone https://github.com/steffanossa/tm.git`
- run ``gradlew run`` in cloned dir
### Linux
- have java
- get gradle (`sudo snap install gradle`)
- `git clone https://github.com/steffanossa/tm.git`
- run `gradle run` in cloned dir
- (i think)

## Anforderungen
<details>
  <summary>(<i>click to show/hide sceenshot</i>)</summary>
  <!-- has to be followed by an empty line! -->
<img src="https://github.com/steffanossa/tm/assets/94658723/b2cf610b-20ab-44c0-bac0-66648f866720" width="400">
</details>

- Anbindung zu Datenbank mit JDBC
- Graphische Benutzeroberfläche mit JavaFX
- Darstellung von Studentendaten
- Auswählen von Studenten zum Kopieren der Daten in die Zwischenablage mit wählbarem Trennzeichen
- Auswählen von relevanten Spalten
- Freie Anordnung der Spalten
- Hinzufügen & Löschen einzelner bzw. mehrerer Studenten

## TODO
- [x] mock db in test dir
- [x] unit tests
  - [x] studentDAO
  - [x] sqlitebuddy
  - [x] mainmodel
  - [x] inputdialogmodel
- [ ] custom exceptions
- [x] edit window umbennenen
- [x] bad input sprache
- [x] uniquenessmessage sprache
- [x] DAO Interface
- [x] Edit-Button
- [x] bilder updaten
- [x] menubar mit open database, preferences, help und about
- [x] about view
- [x] help view
- [ ] howTo schreiben
- [ ] uml gedöns

## MVP

Business Logic im Model (M), GUI-Elemente im View (V), der Presenter (P) agiert als "Vermittler". Jegliche Interaktionen mit der Nutzeroberfläche wird vom Presenter verarbeitet. Die nötigen Methoden des Models werden von ihm aufgerufen und die Rückgaben im Anschluss von ihm an den View übergeben. Durch die strikte Trennung von Nutzeroberfläche und Geschäftslogik wird eine Modularität erreicht, durch die Änderungen an einzelnen Komponenten möglich sind, ohne dadurch Änderungen an anderen Stellen nötig werden zu lassen (lose Kopplung).
<details>
  <summary>(<i>click to show/hide example process</i>)</summary>
  <!-- has to be followed by an empty line! -->
  
![add_process drawio](https://github.com/steffanossa/tm/assets/94658723/604792b8-1277-470d-88ff-db6db5cae0fc)
</details>

## Gradle

Gradle ist <xmlstinkt>Maven</xmlstinkt> in besser.

## DAO

kommt...

## Grafische Nutzeroberfläche (JavaFX)

Dem Start des Programms folgt die Anzeige eines Dialogfensters, über das die zu öffnende Datenbank selektiert werden kann.
<details>
  <summary>(<i>click to show/hide sceenshot</i>)</summary>
  <!-- has to be followed by an empty line! -->
  
![image](https://github.com/steffanossa/tm/assets/94658723/fceee68c-0d84-4b1e-b577-f64e217f150c)
</details>

Die Auswahl einer inkompatiblen Datei löst ein weiteres Dialogfenster aus, das über die fehlerhafte Auwahl informiert. Anschließend wird das vorige Fenster erneut angezeigt.
<details>
  <summary>(<i>click to show/hide sceenshot</i>)</summary>
  <!-- has to be followed by an empty line! -->
  
![image](https://github.com/steffanossa/tm/assets/94658723/1b3e4941-a51d-49cf-b506-fcb5c0a5ec71)
</details>

Ist eine gültige Auswahl getroffen worden, wird das Hauptfenster geöffnet.
<details>
  <summary>(<i>click to show/hide sceenshot</i>)</summary>
  <!-- has to be followed by an empty line! -->
  
![image](https://github.com/steffanossa/tm/assets/94658723/59ff4840-2e36-49b0-876b-aec49706482c)
</details>

Die Daten der Studententabelle werden in einem TableView-Objekt angezeigt. Die Spaltenreihenfolge lässt sich per Drag-And-Drop der jeweiligen Spaltenüberschriftfelder anpassen. Über das mit einem Rechtsklick in den TableView erreichbare Kontextmenü lassen sich die Spalten einzeln de- oder aktivieren, d.h. un- bzw. sichtbar machen. Einzelne Zeilen lassen sich mit gedrücktgehaltener STRG-Taste und Linksklicks aus- und abwählen.
Für das Hinzufügen, Editieren und Entfernen von Einträgen gibt es jeweils einen Button oberhalb der Tabelle.
Ein Klick auf den Add-Button öffnet ein Dialogfenster, in dem die Daten des hinzuzufügenden Studenten eingegeben werden können.
<details>
  <summary>(<i>click to show/hide sceenshot</i>)</summary>
  <!-- has to be followed by an empty line! -->
  
![image](https://github.com/steffanossa/tm/assets/94658723/59ca2799-12db-4c5e-be45-c5285aa010fd)
</details>

Das Kreuz und der Cancel-Button beenden den Vorgang.
Das Klicken des OK-Buttons fügt die Daten nach Überprüfung auf formelle Richtigkeit der Tabelle und der Datenbank hinzu, es sei denn in der Datenbank befindet sich bereits eine Beobachtung mit der eingegebenen Matrikulationsnummer und/oder FH-Kennung. Der Nutzer wird über jegliche fehlerhafte Eingabe nach Betätigung des OK-Buttons mittels Dialog-Fenster informiert und kann die Eingaben daraufhin erneut prüfen.
<details>
  <summary>(<i>click to show/hide sceenshot</i>)</summary>
  <!-- has to be followed by an empty line! -->
  
![image](https://github.com/steffanossa/tm/assets/94658723/34e3430a-67df-46a5-b773-324787b842bf)
![image](https://github.com/steffanossa/tm/assets/94658723/dab26eab-91bc-455c-bb99-eadb13de6b52)
</details>

Der Edit-Button ist nur klickbar, wenn genau eine Zeile ausgewählt ist. Er löst ebenfalls ein Dialogfenster aus, nur dass die Textfelder jetzt die Daten des Studenten enthalten, der zuvor ausgewählt wurde. Der weitere Ablauf ist analog zum Hinzufügen.
<details>
  <summary>(<i>click to show/hide sceenshot</i>)</summary>
  <!-- has to be followed by an empty line! -->
  
![image](https://github.com/steffanossa/tm/assets/94658723/3bca4572-5d1c-4f42-a08f-a33ba251b40e)
</details>

Der Remove-Button nur, wenn mindestens eine Zeile ausgewählt ist. Über ihn lassen sich die ausgewählten Zeilen aus Tabelle und Datenbank entfernen, bevor dies geschieht wird jedoch mit einem Dialogfenster eine Bestätigung abgefragt.
<details>
  <summary>(<i>click to show/hide sceenshot</i>)</summary>
  <!-- has to be followed by an empty line! -->
  
![image](https://github.com/steffanossa/tm/assets/94658723/0782f9d3-b00f-4114-b919-b0292f326e96)
</details>

Unterhalb der Tabelle befindet sich der Previewstring. Durch ihn wird beispielhaft dargestellt, wie die Daten später ausgegeben würden. Die Reihenfolge der Spalten sowie die Information, welche Spalten aktiviert sind, finden sich hier wieder. Dazu wird das Trennzeichen, das über ein Auswahlfeld selektiert werden kann mit dargestellt. Über dieses Auswahlfeld lässt sich eines von vier festgelegten Trennzeichen auswählen.
<details>
  <summary>(<i>click to show/hide sceenshot</i>)</summary>
  <!-- has to be followed by an empty line! -->
  
![image](https://github.com/steffanossa/tm/assets/94658723/7cbaa57f-548f-494b-947a-80788ed56bc7)
</details>

Die Daten der ausgewählten Spalten können mit dem Clipboard-Button in die Zwischenablage kopiert werden. Die Formatierung folgt der Spaltenreihenfolge und dem gewählten Trennzeichen. Der Button ist nur aktiv, sollte mindestens eine Zeile ausgewählt sein.
Die Daten der ausgewählten Spalten können ebenso als Reintext in einer Datei gespeichert werden. Hierzu dient der Save-Button. Ein Klick öffnet ein Dialogfenster, über das Ort und Name der zu speichernden Datei festgelegt werden kann.
<details>
  <summary>(<i>click to show/hide sceenshot</i>)</summary>
  <!-- has to be followed by an empty line! -->
  
![image](https://github.com/steffanossa/tm/assets/94658723/e2206fa7-c5ec-4a8f-bb7c-e6092d1d1fbe)
</details>

Hilfe

<details>
  <summary>(<i>click to show/hide sceenshot</i>)</summary>
  <!-- has to be followed by an empty line! -->

![image](https://github.com/steffanossa/tm/assets/94658723/4fec96c9-1f3c-4995-ac16-5a58caf6b1bb)
</details>

Über

<details>
  <summary>(<i>click to show/hide sceenshot</i>)</summary>
  <!-- has to be followed by an empty line! -->

![image](https://github.com/steffanossa/tm/assets/94658723/67cf0e4d-5ee6-4dd6-8ce0-a7c8fb9db951)
</details>

Der Scenebuilder wurde nicht verwendet, da er mit XML arbeitet. XML ist Mist.

## SQLite

- gemeinfreie (public domain) Programmbibliothek
- keine Client-Server-Architektur
- keine Verwaltung von Nutzer- und Zugriffsrechten
- weit verbreitet[^2][^3]
- leichtgewichtig[^4]


[^2]: Reference: https://www.statista.com/statistics/809750/worldwide-popularity-ranking-database-management-systems/

[^3]: Reference: https://sqlite.org/mostdeployed.html

[^4]: Reference: https://sqlite.org/about.html
