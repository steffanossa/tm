# Transfermodul Programmieraufgabe
#gradle, #javafx, #mvp, #jdbc, #sqlite
## Anforderungen

<img src="https://github.com/steffanossa/tm/assets/94658723/dc33e8b4-7dbe-4861-9bb8-c1e292acef70.png" width="300" height="400">


- Anbindung zu Datenbank mit JDBC
- Graphische Benutzeroberfläche mit JavaFX
- Darstellung von Studentendaten
- Auswählen von Studenten zum Kopieren der Daten in die Zwischenablage mit wählbarem Separator
- Auswählen von relevanten Spalten
- Peppermint Patty

## TODO
- [ ] UNIT TESTS, JUNGE
- [ ] pentesting oder so. kp, hab schreibmaschine
- [ ] DAO Interface
- [ ] Edit-Button

## Vorgehen
## MVP-Architektur

Business Logic im Model (M), GUI-Elemente im View (V), der Presenter (P) agiert als "Vermittler". Jegliche Nutzerinteraktion mit der Nutzeroberfläche wird vom Presenter verarbeitet, die nötigen Methoden des Models werden von ihm aufgerufen und die Rückgaben werden im Anschluss von im an den View geliefert. Durch diese strikte Trennung von Nutzeroberfläche und Geschäftslogik wird eine Modularität erreicht, durch die Änderungen einzelner Komponenten möglich sind, ohne dass dadurch Änderungen an anderen Stellen nötig werden (lose Kopplung).

## Gradle

Gradle ist Maven in besser. Alleine schon, weil kein XML verwendet wird.

## DAO

kommt...
