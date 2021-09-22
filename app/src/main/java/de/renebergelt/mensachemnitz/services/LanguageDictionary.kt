package de.renebergelt.mensachemnitz.services

class LanguageDictionary {

    val keys = HashMap<String, String>()

    constructor() {
        keys.put("holiday:NewYear" , "Neujahr")
        keys.put("holiday:Epiphany" , "Heilige Drei Könige")
        keys.put("holiday:LaborDay" , "Tag der Arbeit")
        keys.put("holiday:FeastOfTheAssumption" , "Mariä Himmelfahrt")
        keys.put("holiday:GermanUnityDay" , "Tag der deutschen Einheit")
        keys.put("holiday:ReformationDay" , "Reformationstag")
        keys.put("holiday:AllSaintsDay" , "Allerheiligen")
        keys.put("holiday:ChristmasDay" , "1. Weihnachtstag")
        keys.put("holiday:SecondChristmasDay" , "2. Weihnachtstag")
        keys.put("holiday:GoodFriday" , "Karfreitag")
        keys.put("holiday:EasterSunday" , "Ostersonntag")
        keys.put("holiday:EasterMonday" , "Ostermontag")
        keys.put("holiday:AscensionDay" , "Christi Himmelfahrt")
        keys.put("holiday:WhitSunday" , "Pfingstsonntag")
        keys.put("holiday:WhitMonday" , "Pfingstmontag")
        keys.put("holiday:FeastOfCorpusChristi" , "Fronleichnam")
        keys.put("holiday:PenanceDay" , "Buß- und Bettag")
    }

    operator fun get(key : String) : String {
        val v = keys.get(key)
        return v ?: ""
    }
}