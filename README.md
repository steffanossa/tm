# Transfermodul Programmieraufgabe
#gradle, #javafx, #mvp, #jdbc, #sqlite
## Anforderungen

<img src="https://github.com/steffanossa/tm/assets/94658723/dc33e8b4-7dbe-4861-9bb8-c1e292acef70.png" width="400">

- Anbindung zu Datenbank mit JDBC
- Graphische Benutzeroberfläche mit JavaFX
- Darstellung von Studentendaten
- Auswählen von Studenten zum Kopieren der Daten in die Zwischenablage mit wählbarem Trennzeichen
- Auswählen von relevanten Spalten
- Freie Anordnung der Spalten
- Hinzufügen & Löschen einzelner bzw. mehrerer Studenten
- Peppermint Patty

## TODO
- [ ] unit tests
- [x] edit window umbennenen
- [x] bad input sprache
- [x] uniquenessmessage sprache
- [x] DAO Interface
- [x] Edit-Button
- [x] bilder updaten
- [ ] menubar mit preferences und hilfe
- [ ] sprach jsons

## Vorgehen
## MVP-Architektur

Business Logic im Model (M), GUI-Elemente im View (V), der Presenter (P) agiert als "Vermittler". Jegliche Interaktionen mit der Nutzeroberfläche wird vom Presenter verarbeitet. Die nötigen Methoden des Models werden von ihm aufgerufen und die Rückgaben im Anschluss von ihm an den View übergeben. Durch die strikte Trennung von Nutzeroberfläche und Geschäftslogik wird eine Modularität erreicht, durch die Änderungen an einzelnen Komponenten möglich sind, ohne dadurch Änderungen an anderen Stellen nötig werden zu lassen (lose Kopplung).

...

## Gradle

Gradle ist Maven in besser. Alleine schon, weil kein XML

## DAO

kommt...

## Grafische Nutzeroberfläche (JavaFX)

Dem Start des Programms folgt die Anzeige eines Dialogfensters, über das die zu öffnende Datenbank selektiert werden kann.

![image](https://github.com/steffanossa/tm/assets/94658723/1397d2b6-66a2-45d9-9547-671b2bbadfdf)

Die Auswahl einer inkompatiblen Datei löst ein weiteres Dialogfenster aus, das über die fehlerhafte Auwahl informiert.

![image](https://github.com/steffanossa/tm/assets/94658723/e4baf282-52a6-407f-a229-8749bc76be60)

Ist eine gültige Auswahl getroffen worden, wird das Hauptfenster geöffnet.

![image](https://github.com/steffanossa/tm/assets/94658723/0cb91a31-463c-4594-af4c-6b3cecc1520d)

Die Daten der Studententabelle werden in einem TableView-Objekt angezeigt. Die Spaltenreihenfolge lässt sich per Drag-And-Drop der jeweiligen Spaltenüberschriftfelder anpassen. Über das mit einem Rechtsklick in den TableView erreichbare Kontextmenü lassen sich die Spalten einzeln de- oder aktivieren, d.h. un- bzw. sichtbar machen. Einzelne Zeilen lassen sich mit gedrücktgehaltener STRG-Taste und Linksklicks aus- und abwählen.
Für das Hinzufügen, Editieren und Entfernen von Einträgen gibt es jeweils einen Button oberhalb der Tabelle.
Ein Klick auf den Add-Button öffnet ein Dialogfenster, in dem die Daten des hinzuzufügenden Studenten eingegeben werden können.

![image](https://github.com/steffanossa/tm/assets/94658723/8df5b475-a06f-4b0c-ab59-83db2b8eb42d)

Das Kreuz und der Cancel-Button beenden den Vorgang.
Das Klicken des OK-Buttons fügt die Daten nach Überprüfung auf formelle Richtigkeit der Tabelle und der Datenbank hinzu, es sei denn in der Datenbank befindet sich bereits eine Beobachtung mit der eingegebenen Matrikulationsnummer und/oder FH-Kennung. Der Nutzer wird über jegliche fehlerhafte Eingabe nach Betätigung des OK-Buttons mittels Dialog-Fenster informiert und kann die Eingaben daraufhin erneut prüfen.

![image](https://github.com/steffanossa/tm/assets/94658723/7e02dbc7-5336-4c77-99eb-882858028241)
![image](https://github.com/steffanossa/tm/assets/94658723/a498cb31-5fd5-484e-96e5-e920c8c77f08)

Der Edit-Button ist nur klickbar, wenn genau eine Zeile ausgewählt ist. Er löst ebenfalls ein Dialogfenster aus, nur dass die Textfelder jetzt die Daten des Studenten enthalten, der zuvor ausgewählt wurde. Der weitere Ablauf ist analog zum Hinzufügen.

![image](https://github.com/steffanossa/tm/assets/94658723/adadae4e-0ee9-4229-90ec-0685fb7baf37)

Der Remove-Button nur, wenn mindestens eine Zeile ausgewählt ist. Über ihn lassen sich die ausgewählten Zeilen aus Tabelle und Datenbank entfernen, bevor dies geschieht wird jedoch mit einem Dialogfenster eine Bestätigung abgefragt.

![image](https://github.com/steffanossa/tm/assets/94658723/f8ad5d0e-63b2-4a51-91cd-93b8a51aa850)

Unterhalb der Tabelle befindet sich der Previewstring. Durch ihn wird beispielhaft dargestellt, wie die Daten später ausgegeben würden. Die Reihenfolge der Spalten sowie die Information, welche Spalten aktiviert sind, finden sich hier wieder. Dazu wird das Trennzeichen, das über ein Auswahlfeld selektiert werden kann mit dargestellt. Über dieses Auswahlfeld lässt sich eines von vier festgelegten Trennzeichen auswählen.

![image](https://github.com/steffanossa/tm/assets/94658723/3e552c08-be5c-4432-8976-e9565673644f)

Die Daten der ausgewählten Spalten können mit dem Clipboard-Button in die Zwischenablage kopiert werden. Die Formatierung folgt der Spaltenreihenfolge und dem gewählten Trennzeichen. Der Button ist nur aktiv, sollte mindestens eine Zeile ausgewählt sein.
Die Daten der ausgewählten Spalten können ebenso als Reintext in einer Datei gespeichert werden. Hierzu dient der Save-Button. Ein Klick öffnet ein Dialogfenster, über das Ort und Name der zu speichernden Datei festgelegt werden kann.

![image](https://github.com/steffanossa/tm/assets/94658723/2d4c7809-f66e-4238-9d93-620b2cad78db)


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
