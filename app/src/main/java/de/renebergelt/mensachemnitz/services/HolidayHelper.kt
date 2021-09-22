package de.renebergelt.mensachemnitz.services

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter


/**
 * Kotlin port of C# HolidayHelper
 * http://www.mycsharp.de/wbb2/thread.php?threadid=36132
 * http://www.mycsharp.de/wbb2/thread.php?postid=161415#post161415
 */
class HolidayHelper {

    val interna = Feiertage()

    /*
     * Einfache Abfrage, ob das übergebene Datum überhaupt ein Feiertag ist
     */
    fun isFeiertag(datum : LocalDate, bundesweit: Boolean) : Boolean {
        return interna.isFeiertag(datum, bundesweit)
    }

    /*
     * Einfache Abfrage, ob das übergebene Datum in dem jeweiligen Land ein Feiertag ist
     */
    fun isFeiertag(datum : LocalDate, land : Land) : Boolean {
        return interna.isFeiertag(datum, land)
    }

    /*
     * Ergänzende Prüfung, ob das übergebene Datum in (irgend-)einem Land
     * teilweise ein Feiertag ist
     */
    fun isTeilweiseFeiertag(datum : LocalDate) : Boolean {
        return interna.isTeilweiseFeiertag(datum)
    }

    /**
     * Die Bezeichnung eines möglichen bundesweiten Feiertags
     * anhand des Datums ermitteln
     */
    fun getName(datum : LocalDate) : String {
        return interna.getName(datum)
    }

    fun getName(datum : LocalDate, land : Land) : String {
        return interna.getName(datum, land)
    }
}

class Feiertage {
    val festeFeiertage = HashMap<String, Feiertag>()
    val beweglicheFeiertage = HashMap<Int, HashMap<LocalDate, Feiertag>>()

    constructor () {
        initFesteFeiertage()
        initBeweglicheFeiertage(LocalDate.now().year)
    }

    fun initFesteFeiertage() {
        festeFeiertage.put("01.01.", Feiertag("NewYear", "01.01."))
        festeFeiertage.put(
            "06.01.",
            Feiertag(
                "Epiphany",
                "06.01.",
                arrayOf(Land.Baden_Wuerttemberg, Land.Bayern, Land.Sachsen_Anhalt)
            )
        )
        festeFeiertage.put("01.05.", Feiertag("LaborDay", "01.05."))
        festeFeiertage.put(
            "15.08.",
            Feiertag("FeastOfTheAssumption", "15.08.", arrayOf(Land.Saarland, Land.teilweise))
        )
        festeFeiertage.put("03.10.", Feiertag("GermanUnityDay", "03.10."))
        festeFeiertage.put(
            "31.10.", Feiertag(
                "ReformationDay", "31.10.", arrayOf(
                    Land.Brandenburg, Land.Mecklenburg_Vorpommern,
                    Land.Sachsen, Land.Sachsen_Anhalt, Land.Thueringen
                )
            )
        )
        festeFeiertage.put(
            "01.11.", Feiertag(
                "AllSaintsDay", "01.11.", arrayOf(
                    Land.Baden_Wuerttemberg, Land.Bayern, Land.Nordrhein_Westfalen,
                    Land.Rheinland_Pfalz, Land.Saarland
                )
            )
        )
        festeFeiertage.put("25.12.", Feiertag("ChristmasDay", "25.12."))
        festeFeiertage.put("26.12.", Feiertag("SecondChristmasDay", "26.12."))
    }

    fun initBeweglicheFeiertage(jahr : Int) {
        val aktuelleFeiertage = HashMap<LocalDate, Feiertag>()

        //  hole zuerst Ostersonntag
        val ostern = getOstersonntag(jahr)

        //  daraus alle "abhängigen" Feiertage ableiten und registrieren
        //  daraus alle "abhängigen" Feiertage ableiten und registrieren
        aktuelleFeiertage.put(ostern.plusDays(-2), Feiertag("GoodFriday", ostern, -2))
        aktuelleFeiertage.put(ostern, Feiertag("EasterSunday", ostern, 0))
        aktuelleFeiertage.put(ostern.plusDays(1), Feiertag("EasterMonday", ostern, 1))
        aktuelleFeiertage.put(ostern.plusDays(39), Feiertag("AscensionDay", ostern, 39))
        aktuelleFeiertage.put(ostern.plusDays(49), Feiertag("WhitSunday", ostern, 49))
        aktuelleFeiertage.put(ostern.plusDays(50), Feiertag("WhitMonday", ostern, 50))
        aktuelleFeiertage.put(
            ostern.plusDays(60), Feiertag(
                "FeastOfCorpusChristi", ostern, 60, arrayOf(
                    Land.Baden_Wuerttemberg, Land.Bayern, Land.Hessen,
                    Land.Nordrhein_Westfalen, Land.Rheinland_Pfalz, Land.Saarland,
                    Land.teilweise
                )
            )
        )

        //  Sonderfall ist der Buß- und Bettag
        val busstag = getBussUndBettag(jahr)
        aktuelleFeiertage.put(busstag, Feiertag("PenanceDay", busstag, 0, arrayOf(Land.Sachsen)))

        //	alle beweglichen Feiertage für Prüfungen registrieren
        beweglicheFeiertage.put(jahr, aktuelleFeiertage);
    }

    /**
     * Errechnet das Datum des Ostersonntags aus dem übergebenen Jahr
     */
    fun getOstersonntag(jahr : Int) : LocalDate {
        val c: Int
        var i: Int
        var j: Int
        val k: Int
        val l: Int
        val n: Int
        val OsterTag: Int
        val OsterMonat: Int

        c = jahr / 100
        n = jahr - 19 * (jahr / 19)
        k = (c - 17) / 25
        i = c - c / 4 - (c - k) / 3 + 19 * n + 15
        i = i - 30 * (i / 30)
        i =
            i - i / 28 * ((1 - i / 28) * (29 / (i + 1)) * ((21 - n) / 11))
        j = jahr + jahr / 4 + i + 2 - c + c / 4
        j = j - 7 * (j / 7)
        l = i - j

        OsterMonat = 3 + (l + 40) / 44
        OsterTag = l + 28 - 31 * (OsterMonat / 4)

        return LocalDate.of(jahr, OsterMonat, OsterTag)
    }

    fun getBussUndBettag(jahr : Int) : LocalDate {
        //	wenn Silvester ein Mittwoch ist,
        //	dann liegt der Buß- und Bettag genau 6 Wochen vorher;
        //	andernfalls bewegt er sich darum herum
        //	wenn Silvester ein Mittwoch ist,
        //	dann liegt der Buß- und Bettag genau 6 Wochen vorher;
        //	andernfalls bewegt er sich darum herum
        var result = LocalDate.of(jahr, 12, 31).minusDays(42)
        if (result.dayOfWeek !== DayOfWeek.WEDNESDAY) { //	die genaue Abweichung ergibt sich aus folgender Überlegung:
            //  Silvester         Mo  Di  Mi  Do  Fr  Sa  So
            //	Heiligabend       Mo  Di  Mi  Do  Fr  Sa  So
            //	4. Advent         -1  -2  -3  -4  -5  -6   0  Tage Differenz
            //	Totensonntag      gleicher Wochentag wie der 4. Advent
            //	Buß- und Bettag   -4                          Tage Differenz einheitlich
            //                    gleicher Wochentag wie Silvester,
            //  also Differenz bis zum Mittwoch der betreffenden Woche:
            //                    +2  +1   0  -1  -2  -3  -4
            //  dabei sind die enum-Werte von DayOfWeek zu beachten:
            //                     1   2   3   4   5   6   0
            if (result.dayOfWeek === DayOfWeek.SUNDAY) {
                result = result.minusDays(4)
            } else {
                result = result.plusDays((DayOfWeek.WEDNESDAY.value - result.dayOfWeek.value).toLong())
            }
        }
        return result
    }

    fun getFeiertag(datum : LocalDate) : Feiertag? {
        val ttmm = datum.format(DateTimeFormatter.ofPattern("dd.MM."))
        //  erster Versuch: fester Feiertag?
        val festerFeiertag : Feiertag? = festeFeiertage.get(ttmm)
        if (festerFeiertag == null) {
            //  zweiter Versuch: bewegliche Feiertage des betreffenden Jahres feststellen
            val jahr : Int = datum.year
            var beweglich = beweglicheFeiertage.get(jahr)
            if (beweglich == null) {
                // noch nicht verhanden, dann hinzufügen und verwenden
                initBeweglicheFeiertage(jahr)
                beweglich = beweglicheFeiertage.get(jahr)
            }

            return beweglich?.get(datum)
        }
        return festerFeiertag
    }

    /*
     * Einfache Abfrage, ob das übergebene Datum in dem jeweiligen Land ein Feiertag ist
     */
    fun isFeiertag(datum : LocalDate, bundesweit : Boolean) : Boolean {
        val f: Feiertag? = getFeiertag(datum)
        if (f != null) return f.bundesweit
        return false
    }

    fun isFeiertag(datum : LocalDate, land : Land) : Boolean {
        val f: Feiertag? = getFeiertag(datum)
        if (f != null) {
            return f.bundesweit || (f.laender?.contains(land) ?: false)
        }
        return false
    }

    /**
     * Ergänzende Prüfung, ob das übergebene Datum in (irgend-)einem Land
     * teilweise ein Feiertag ist
     */
    fun isTeilweiseFeiertag(datum : LocalDate) : Boolean {
        val f: Feiertag? = getFeiertag(datum)
        if (f != null) return f.teilweise
        return false
    }

    /*
     * Die Bezeichnung eines möglichen bundesweiten Feiertags
     * anhand des Datums ermitteln
     */
    fun getName(datum : LocalDate) : String {
        val f = getFeiertag(datum)
        if (f != null && f.bundesweit)
            return f.name
        return ""
    }

    /**
     * Die Bezeichnung eines möglichen Feiertags anhand des Datums
     * und des jeweiligen Bundeslandes ermitteln
     */
    fun getName(datum : LocalDate, land : Land) : String {
        val f = getFeiertag(datum)
        if (f != null) {
            if (f.bundesweit || f.laender?.contains(land) ?: false) {
                return f.name
            }
        }

        return ""
    }
}

class Feiertag {
    var name : String = ""
    var ttmm : String = ""
    var datum : LocalDate? = null
    var bundesweit = false
    var beweglich = false
    var tageHinzu = 0
    var laender : Array<Land>? = null

    //	Hinweise
    //	--------
    //	Bei den Konstruktoren wird die Liste der Bundesländer geprüft:
    //	**	Bei den Konstruktoren "in allen Ländern" wird bundesweit = true gesetzt;
    //			länder bleibt unverändert als null festgesetzt.
    //	**	Bei den Konstruktoren mit einer Länderliste gilt:
    //	  **	Wenn ein "Land.teilweise" enthalten ist, gilt bundesweit = false.
    //		**	Wenn dies nicht enthalten ist, dann gilt:
    //			**	Wenn 16 Bundesländer aufgeführt werden, wird bundesweit = true gesetzt;
    //					länder bleibt unverändert als null festgesetzt.
    //			**	In allen anderen Fällen gilt bundesweit = false.
    //			Wenn diese Prüfungen "bundesweit == false" ergeben, wird die Liste der
    //			Länder übernommen.
    //
    //	Damit gilt für jede Instanz:
    //	**	bundesweit == true   =>  länder == null
    //	**	bundesweit == false  =>  länder != null

    val teilweise : Boolean
        get() {
            if (bundesweit)
                return false
            else
                return laender?.contains(Land.teilweise) ?: false
        }

    constructor(itemName : String, itemTTMM : String) {
        name = itemName
        ttmm = itemTTMM
        datum = LocalDate.of(LocalDate.now().year, ttmm.subSequence(3, 5).toString().toInt(), ttmm.subSequence(0, 2).toString().toInt())
        beweglich = false
        bundesweit = true
        tageHinzu = 0
    }

    constructor(itemName : String, itemTTMM : String, itemLaender : Array<Land>) {
        name = itemName
        ttmm = itemTTMM
        datum = LocalDate.of(LocalDate.now().year, ttmm.subSequence(3, 5).toString().toInt(), ttmm.subSequence(0, 2).toString().toInt())
        beweglich = false
        bundesweit = isBundesweit(itemLaender)
        tageHinzu = 0
        if (!bundesweit) {
            laender = itemLaender
        }
    }

    constructor(itemName : String, ostern : LocalDate, itemTageDifferenz : Int) {
        name = itemName;
        datum = ostern.plusDays(itemTageDifferenz.toLong());
        ttmm = datum?.format(DateTimeFormatter.ofPattern("dd.MM.")) ?: ""
        beweglich = true
        bundesweit = true;
        tageHinzu = itemTageDifferenz;
    }

    constructor(itemName : String, ostern : LocalDate, itemTageDifferenz : Int, itemLaender: Array<Land>) {
        name = itemName;
        datum = ostern.plusDays(itemTageDifferenz.toLong());
        ttmm = datum?.format(DateTimeFormatter.ofPattern("dd.MM.")) ?: ""
        beweglich = true;
        bundesweit = isBundesweit(itemLaender);
        tageHinzu = itemTageDifferenz;
        if (!bundesweit)
            laender = itemLaender;
    }

    fun isBundesweit(list : Array<Land>?) : Boolean {
        if (list == null || list.size == 0) {
            return true
        }
        else {
            return !list.contains(Land.teilweise) && list.size == 16
        }
    }
}

enum class Land {
    teilweise,
    Schleswig_Holstein,
    Hamburg,
    Niedersachsen,
    Bremen,
    Nordrhein_Westfalen,
    Hessen,
    Rheinland_Pfalz,
    Baden_Wuerttemberg,
    Bayern,
    Saarland,
    Berlin,
    Brandenburg,
    Mecklenburg_Vorpommern,
    Sachsen,
    Sachsen_Anhalt,
    Thueringen
}