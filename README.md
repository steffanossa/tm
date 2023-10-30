# Transfermodul Programmieraufgabe
#gradle, #javafx, #mvp, #jdbc, #sqlite
## Anforderungen

<img src="https://github.com/steffanossa/tm/assets/94658723/dc33e8b4-7dbe-4861-9bb8-c1e292acef70.png" width="300" height="400">


- Anbindung zu Datenbank mit JDBC
- Graphische Benutzeroberfläche mit JavaFX
- Darstellung von Studentendaten
- Auswählen von Studenten zum Kopieren der Daten in die Zwischenablage mit wählbarem Separator
- Auswählen von relevanten Spalten

## Fehler

- Alle Spalten abwählen führt zu StringIndexOutOfBoundsException: Range [0, -4]
  - catch, disable export buttons, preview auf leer
