package de.renebergelt.mensachemnitz.types

enum class MensaType {

    MensaStraNa,
    CafeteriaStraNa,
    MensaReichenhain,
    CafeteriaReichenhain;

    fun getHumanFriendlyName() : String {
        when(this) {
            MensaStraNa -> return "Mensa - Straße der Nationen"
            CafeteriaStraNa -> return "Caféteria - Straße der Nationen"
            MensaReichenhain -> return "Mensa - Reichenhainer Straße"
            CafeteriaReichenhain -> return "Caféteria - Reichenhainer Straße"
            else -> return "MensaChemnitz"
        }
    }

    fun getXmlId() : String {
        when(this) {
            MensaStraNa -> return "773823070"
            CafeteriaStraNa -> return "6"
            MensaReichenhain -> return "1479835489"
            CafeteriaReichenhain -> return "7"
            else -> return ""
        }
    }
}