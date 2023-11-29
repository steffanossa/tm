# Transfermodul Programmieraufgabe
#gradle, #javafx, #mvp, #jdbc, #sqlite

## HowTo
### Windows
- have Java JDK version 8 or higher 
- get and unzip <a href="https://gradle.org/releases/">gradle</a>
- add gradle/bin to PATH
- clone this repo: `git clone https://github.com/steffanossa/tm.git`
- run ``gradlew run`` in cloned dir
### Linux & Mac
- have Java JDK version 8 or higher 
- get and unzip <a href="https://gradle.org/releases/">gradle</a>
- Unzip the distribution zip file in the directory of your choosing, e.g.:

```
  mkdir /opt/gradle
  unzip -d /opt/gradle gradle-8.4-bin.zip
  ls /opt/gradle/gradle-8.4
  LICENSE  NOTICE  bin  getting-started.html  init.d  lib  media
```
- add to path:  `export PATH=$PATH:/opt/gradle/gradle-8.4/bin`
- clone this repo: `git clone https://github.com/steffanossa/tm.git`
- execute <order66> `gradle run` in cloned dir

## Anforderungen
<details>
  <summary>(<i>click to show/hide image</i>)</summary>
  <!-- has to be followed by an empty line! -->
<img src="https://github.com/steffanossa/tm/assets/94658723/0551d5fe-8406-42fb-92d0-6b25ba029b2b" width="400">
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
- [ ] custom exceptions MEH
- [x] edit window umbennenen
- [x] bad input sprache
- [x] uniquenessmessage sprache
- [x] DAO Interface
- [x] Edit-Button
- [x] bilder updaten
- [x] menubar mit open database, preferences, help und about
- [x] about view
- [x] help view
- [x] howTo schreiben
- [ ] __testen unter linux / mac__
- [ ] uml gedöns
  - [x] mvp
  - [ ] dao
  - [ ] add process
  - [ ] class diagram
- [ ] __datenbank interaktionen minimieren!__
  - [ ] remove
  - [ ] edit
  - [ ] add
- [x] __generic alert for any errors!__
- [x] checkbox column
  - [x] adds to selected
  - [x] not in preview
  - [ ] not in reordering -> setReorderable=false still allows "passive reordering" :(
  - [x] not hideable
  - [x] not in clipboard & saveTo
  - [ ] update what happens after exception alert: always exit app no good

im moment ist der uniqueness check: add to sql methode schlägt fehl oder nicht. sollte anhand der schon gezogenen daten stattfindne, so ist unnützer verbindungsaufbau..

## MVP

Business Logic im Model (M), GUI-Elemente im View (V), der Presenter (P) agiert als "Vermittler". Jegliche Interaktionen mit der Nutzeroberfläche wird vom Presenter verarbeitet. Die zugehörigen Methoden des Models werden von ihm aufgerufen und die Rückgaben im Anschluss von ihm verarbeitet und die Nutzeroberfläche aktualisiert. Durch die strikte Trennung von Nutzeroberfläche und Geschäftslogik wird eine Modularität erreicht, durch die Änderungen an einzelnen Komponenten möglich sind, ohne dadurch Änderungen an anderen Stellen nötig werden zu lassen (lose Kopplung).
<details>
  <summary>(<i>click to show/hide image</i>)</summary>
  <!-- has to be followed by an empty line! -->
  
![NOsxJiGm44LxVyNWQXVa5RGq92K5We97RUmB6ScFO8z9WVXteYGGYNREkMUwAhP8Yq6ToO08YbPGs2fX2Q2-k845hXNGeN8S0xyn_56vO6kbXuAA92ZqfIxebPD_OVyEUSoz4yxfSBocpyn-XctqLl1qkns-rPQuonvXcTUFS3-YsD_XpIuzCwRPCJGj-PNjp_NbwNHEch8nXlILNgF_X](https://github.com/steffanossa/tm/assets/94658723/ad076175-a976-4346-b2af-3b87889a949d)
</details>

## DAO, DTO

Um die persistente Datenschicht von der Anwendungsschicht zu trennen, wurde mit dem Konzept des Data Access Object (DAO) gearbeitet. So ist es für die Anwendungsschicht irrelevant, welche Art von Datenbank zum Einsatz kommt, jegliche Berührungspunkte zu ihr gehen über die DAO-Schnittstelle. Sollte zukünftig von SQLite auf MongoDB o.ä. gewechselt werden, bleibt die Anwendungsschicht davon unberührt, lediglich die DAO-Ebene müsste angepasst werden.
Die in der Anwendung gezeigten Daten sind keine Livedaten, sondern Transferobjekte (Data Tranfer Objects). Die Daten wurden einmal aus der Datenbank herausgelesen und in diese DTOs verwandelt. Sämtliche Operationen können mit ihnen durchgeführt werden, ohne dass die Datenbank permanent verbunden oder befragt werden muss.

## Gradle

<a href="https://gradle.org/releases/">Gradle</a> ist ein open source Build-Management-Tool. Es vereinfacht die Entwicklung, das Testen und die Bereitstelung von Anwendungen verschiedener Programmiersprachen. Alternativ hätte Maven genutzt werden können.

## Grafische Nutzeroberfläche (JavaFX)

Dem Start des Programms folgt die Anzeige eines Dialogfensters, über das die zu öffnende Datenbank selektiert werden kann.
<details>
  <summary>(<i>click to show/hide image</i>)</summary>
  <!-- has to be followed by an empty line! -->
  
![image](https://github.com/steffanossa/tm/assets/94658723/fceee68c-0d84-4b1e-b577-f64e217f150c)
</details>

Die Auswahl einer inkompatiblen Datei löst ein weiteres Dialogfenster aus, das über die fehlerhafte Auwahl informiert. Anschließend wird das vorige Fenster erneut angezeigt.
<details>
  <summary>(<i>click to show/hide image</i>)</summary>
  <!-- has to be followed by an empty line! -->
  
![image](https://github.com/steffanossa/tm/assets/94658723/1b3e4941-a51d-49cf-b506-fcb5c0a5ec71)
</details>

Ist eine gültige Auswahl getroffen worden, wird das Hauptfenster geöffnet.
<details>
  <summary>(<i>click to show/hide image</i>)</summary>
  <!-- has to be followed by an empty line! -->
  
![image](https://github.com/steffanossa/tm/assets/94658723/59ff4840-2e36-49b0-876b-aec49706482c)
</details>

Die Daten der Studententabelle werden in einem TableView-Objekt angezeigt. Die Spaltenreihenfolge lässt sich per Drag-And-Drop der jeweiligen Spaltenüberschriftfelder anpassen. Über das mit einem Rechtsklick in den TableView erreichbare Kontextmenü lassen sich die Spalten einzeln de- oder aktivieren, d.h. un- bzw. sichtbar machen. Einzelne Zeilen lassen sich mit gedrücktgehaltener STRG-Taste und Linksklicks aus- und abwählen.
Für das Hinzufügen, Editieren und Entfernen von Einträgen gibt es jeweils einen Button oberhalb der Tabelle.
Ein Klick auf den Add-Button öffnet ein Dialogfenster, in dem die Daten des hinzuzufügenden Studenten eingegeben werden können.
<details>
  <summary>(<i>click to show/hide image</i>)</summary>
  <!-- has to be followed by an empty line! -->
  
![image](https://github.com/steffanossa/tm/assets/94658723/59ca2799-12db-4c5e-be45-c5285aa010fd)
</details>

Das Kreuz und der Cancel-Button beenden den Vorgang.
Das Klicken des OK-Buttons fügt die Daten nach Überprüfung auf formelle Richtigkeit der Tabelle und der Datenbank hinzu, es sei denn in der Datenbank befindet sich bereits eine Beobachtung mit der eingegebenen Matrikulationsnummer und/oder FH-Kennung. Der Nutzer wird über jegliche fehlerhafte Eingabe nach Betätigung des OK-Buttons mittels Dialog-Fenster informiert und kann die Eingaben daraufhin erneut prüfen.
<details>
  <summary>(<i>click to show/hide image</i>)</summary>
  <!-- has to be followed by an empty line! -->
  
![image](https://github.com/steffanossa/tm/assets/94658723/34e3430a-67df-46a5-b773-324787b842bf)
![image](https://github.com/steffanossa/tm/assets/94658723/dab26eab-91bc-455c-bb99-eadb13de6b52)
</details>

Der Edit-Button ist nur klickbar, wenn genau eine Zeile ausgewählt ist. Er löst ebenfalls ein Dialogfenster aus, nur dass die Textfelder jetzt die Daten des Studenten enthalten, der zuvor ausgewählt wurde. Der weitere Ablauf ist analog zum Hinzufügen.
<details>
  <summary>(<i>click to show/hide image</i>)</summary>
  <!-- has to be followed by an empty line! -->
  
![image](https://github.com/steffanossa/tm/assets/94658723/3bca4572-5d1c-4f42-a08f-a33ba251b40e)
</details>

Der Remove-Button nur, wenn mindestens eine Zeile ausgewählt ist. Über ihn lassen sich die ausgewählten Zeilen aus Tabelle und Datenbank entfernen, bevor dies geschieht wird jedoch mit einem Dialogfenster eine Bestätigung abgefragt.
<details>
  <summary>(<i>click to show/hide image</i>)</summary>
  <!-- has to be followed by an empty line! -->
  
![image](https://github.com/steffanossa/tm/assets/94658723/0782f9d3-b00f-4114-b919-b0292f326e96)
</details>

Unterhalb der Tabelle befindet sich der Previewstring. Durch ihn wird beispielhaft dargestellt, wie die Daten später ausgegeben würden. Die Reihenfolge der Spalten sowie die Information, welche Spalten aktiviert sind, finden sich hier wieder. Dazu wird das Trennzeichen, das über ein Auswahlfeld selektiert werden kann mit dargestellt. Über dieses Auswahlfeld lässt sich eines von vier festgelegten Trennzeichen auswählen.
<details>
  <summary>(<i>click to show/hide image</i>)</summary>
  <!-- has to be followed by an empty line! -->
  
![image](https://github.com/steffanossa/tm/assets/94658723/7cbaa57f-548f-494b-947a-80788ed56bc7)
</details>

Die Daten der ausgewählten Spalten können mit dem Clipboard-Button in die Zwischenablage kopiert werden. Die Formatierung folgt der Spaltenreihenfolge und dem gewählten Trennzeichen. Der Button ist nur aktiv, sollte mindestens eine Zeile ausgewählt sein.
Die Daten der ausgewählten Spalten können ebenso als Reintext in einer Datei gespeichert werden. Hierzu dient der Save-Button. Ein Klick öffnet ein Dialogfenster, über das Ort und Name der zu speichernden Datei festgelegt werden kann.
<details>
  <summary>(<i>click to show/hide image</i>)</summary>
  <!-- has to be followed by an empty line! -->
  
![image](https://github.com/steffanossa/tm/assets/94658723/e2206fa7-c5ec-4a8f-bb7c-e6092d1d1fbe)
</details>

Hilfe

<details>
  <summary>(<i>click to show/hide image</i>)</summary>
  <!-- has to be followed by an empty line! -->

![image](https://github.com/steffanossa/tm/assets/94658723/4fec96c9-1f3c-4995-ac16-5a58caf6b1bb)
</details>

Über

<details>
  <summary>(<i>click to show/hide image</i>)</summary>
  <!-- has to be followed by an empty line! -->

![image](https://github.com/steffanossa/tm/assets/94658723/67cf0e4d-5ee6-4dd6-8ce0-a7c8fb9db951)
</details>

Der Scenebuilder wurde nicht verwendet, da er mit (F)XML arbeitet. XML ist Mist.

## 

## SQLite

- gemeinfreie (public domain) Programmbibliothek
- keine Client-Server-Architektur
- keine Verwaltung von Nutzer- und Zugriffsrechten
- weit verbreitet[^2][^3]
- leichtgewichtig[^4]


[^2]: Reference: https://www.statista.com/statistics/809750/worldwide-popularity-ranking-database-management-systems/

[^3]: Reference: https://sqlite.org/mostdeployed.html

[^4]: Reference: https://sqlite.org/about.html
