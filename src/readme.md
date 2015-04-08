#SadSquareCoffin - Projektspec
_Niklas Fransson, Johan Sylvan, Aron Tornberg_

##Idé
Ett spel där spelaren åker längst en bana och ska undvika hinder/samla saker på vägen.
https://www.shadertoy.com/view/MsX3Rf
http://youtu.be/MFvgmExk89k

ERIK: Roligt spännande ide! 

##Styrning
Spelkaraktären styrs genom handflatans position, vinkel och hastighet.

ERIK: Här kan jag tänka mig att man kan använda en hög hastighetsrörelse för att t.ex. accelerara framåt eller bakåt tillfälligt. 


Spelkaraktärens hastighet styrs genom att röra handen upp och ner eller eventuellt via simmande rörelse med handen. Styrning sker genom handflatans rullning (rotation kring handleden) och position i sidled. Spelaren styr med en hand och använder sig av relativt små rörelser för att spelet ska vara bekvämt att spela i längden.

##Återkoppling
Förutom rörelse i förhållande till spelvärlden så är tanken att spelkaraktären även rör sig beroende på vad spelaren gör. 
T.ex. om spelkaraktären är en fågel flaxar efter handens rörelse upp och ner.

ERIK: FÖr återkopplingnen är det viktigt att ni visar styrkan i förndring i spelhastighet och en fågel-analogi kan ni funka där man kan vifta mer eller mindre med vingarna för att visual kraften. Även när fågeln tappar kraft behveör ni förstås visa. Vinkelstyrningen är ju lättare att se. KAnske kan man även guida användaren att ha handen på bästa stället mot sensorn med grafik. 

##Teknik
För att styra kommer Leapmotion att användas. Spelet kommer att utvecklas i JMonkeyEngine där språket är Java. Gitlab används för versionshantering.