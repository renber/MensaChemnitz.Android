package de.renebergelt.mensachemnitz

import de.renebergelt.mensachemnitz.services.XmlMenuParser
import org.junit.Test

import org.junit.Assert.*
import java.io.ByteArrayInputStream
import java.nio.charset.Charset

class MenuParserTest {

    @Test
    fun test_parseSampleXml() {
        val testXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<speiseplan xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"speiseplan.xsd\">\n" +
                "\t<datum tag=\"18\" monat=\"11\" jahr=\"2019\"/>\n" +
                "\t<essen id=\"8547\" kategorie=\"Abend - Pasta, Pasta\" bewertung=\"0\" img=\"false\" schwein=\"false\" rind=\"false\" vegetarisch=\"false\" alkohol=\"false\">\n" +
                "\t\t<deutsch>Vegan: Gefüllte Teigtaschen mit Babyspinat (3,24,81) und Mozzarella-Basilikum-Minz-Soße (3,19,24,49,81) </deutsch>\n" +
                "\t\t<pr gruppe=\"S\">3.50</pr>\n" +
                "\t\t<pr gruppe=\"M\">5.50</pr>\n" +
                "\t\t<pr gruppe=\"G\">6.60</pr>\n" +
                "\t</essen>\n" +
                "\t<essen id=\"8548\" kategorie=\"Abend - Pasta, Pasta\" bewertung=\"0\" img=\"false\" schwein=\"true\" rind=\"false\" vegetarisch=\"false\" alkohol=\"true\">\n" +
                "\t\t<deutsch>Maccheroni mit geräuchertem Mett, Tomaten und frischen Kräutern (19,24,44,51,81)</deutsch>\n" +
                "\t\t<pr gruppe=\"S\">2.40</pr>\n" +
                "\t\t<pr gruppe=\"M\">4.30</pr>\n" +
                "\t\t<pr gruppe=\"G\">5.40</pr>\n" +
                "\t</essen>\n" +
                "\t<essen id=\"7704\" kategorie=\"Campusteller\" bewertung=\"0\" img=\"true\" img_small=\"https://www.swcz.de/bilderspeiseplan/bilder_190/7704.png\" img_big=\"https://www.swcz.de/bilderspeiseplan/bilder_350/7704.png\" schwein=\"true\" rind=\"false\" vegetarisch=\"false\" alkohol=\"false\">\n" +
                "\t\t<deutsch>Hackfleisch-Käse-Topf mit Kartoffeln, Champignons und Lauch (1,2,19,51,81), Brötchen (81,83)</deutsch>\n" +
                "\t\t<pr gruppe=\"S\">1.90</pr>\n" +
                "\t\t<pr gruppe=\"M\">3.80</pr>\n" +
                "\t\t<pr gruppe=\"G\">4.65</pr>\n" +
                "\t</essen>\n" +
                "\t<essen id=\"8551\" kategorie=\"Campusteller\" bewertung=\"0\" img=\"true\" img_small=\"https://www.swcz.de/bilderspeiseplan/bilder_190/8551.png\" img_big=\"https://www.swcz.de/bilderspeiseplan/bilder_350/8551.png\" schwein=\"false\" rind=\"false\" vegetarisch=\"false\" alkohol=\"false\">\n" +
                "\t\t<deutsch>Germknödel mit Pflaumenmusfüllung und Mohn-Zucker (3,15,19,81), dazu Vanillesoße (19)</deutsch>\n" +
                "\t\t<pr gruppe=\"S\">1.40</pr>\n" +
                "\t\t<pr gruppe=\"M\">2.95</pr>\n" +
                "\t\t<pr gruppe=\"G\">3.90</pr>\n" +
                "\t</essen>\n" +
                "\t<essen id=\"8552\" kategorie=\"Grill\" bewertung=\"0\" img=\"true\" img_small=\"https://www.swcz.de/bilderspeiseplan/bilder_190/8552.png\" img_big=\"https://www.swcz.de/bilderspeiseplan/bilder_350/8552.png\" schwein=\"false\" rind=\"false\" vegetarisch=\"false\" alkohol=\"false\">\n" +
                "\t\t<deutsch>Feine lauwarme geräucherte Lachsmedaillons (16) mit Honig-Senf-Soße (22,48) und Meerrettich-Dip (3,19,24), dazu Röstitaler und Garnitur</deutsch>\n" +
                "\t\t<pr gruppe=\"S\">3.90</pr>\n" +
                "\t\t<pr gruppe=\"M\">5.90</pr>\n" +
                "\t\t<pr gruppe=\"G\">6.90</pr>\n" +
                "\t</essen>\n" +
                "\t<essen id=\"8554\" kategorie=\"Heiße Theke - Pastabar\" bewertung=\"0\" img=\"true\" img_small=\"https://www.swcz.de/bilderspeiseplan/bilder_190/8554.png\" img_big=\"https://www.swcz.de/bilderspeiseplan/bilder_350/8554.png\" schwein=\"false\" rind=\"false\" vegetarisch=\"false\" alkohol=\"false\">\n" +
                "\t\t<deutsch>Pasta mit Tofubolognese (18,21,49,81), Mascarpone-Balsamico-Soße (19,24,81), geriebener Parmesan oder Gouda (19), Fingermöhren</deutsch>\n" +
                "\t\t<pr gruppe=\"S\">2.00</pr>\n" +
                "\t\t<pr gruppe=\"M\">3.70</pr>\n" +
                "\t\t<pr gruppe=\"G\">4.75</pr>\n" +
                "\t</essen>\n" +
                "\t<essen id=\"8546\" kategorie=\"Schneller Teller\" bewertung=\"0\" img=\"true\" img_small=\"https://www.swcz.de/bilderspeiseplan/bilder_190/8546.png\" img_big=\"https://www.swcz.de/bilderspeiseplan/bilder_350/8546.png\" schwein=\"false\" rind=\"false\" vegetarisch=\"false\" alkohol=\"false\">\n" +
                "\t\t<deutsch>Hähnchenbrustschnitzel mit Zitronenecke (54,81), dazu Creme fraiche-Soße mit Kräutern verfeinert (19,81), Kaisergemüse und gebackene Macairekartoffeln (81)</deutsch>\n" +
                "\t\t<pr gruppe=\"S\">2.40</pr>\n" +
                "\t\t<pr gruppe=\"M\">4.30</pr>\n" +
                "\t\t<pr gruppe=\"G\">5.40</pr>\n" +
                "\t</essen>\n" +
                "</speiseplan>"

        val parser = XmlMenuParser()
        val stream = ByteArrayInputStream(testXml.toByteArray(Charset.forName("UTF-8")))

        val dishes = parser.parseMenu(stream)

        assertEquals(7, dishes.size)
        assertEquals("Abend - Pasta, Pasta", dishes[0].name)
        assertEquals("Vegan: Gefüllte Teigtaschen mit Babyspinat (3,24,81) und Mozzarella-Basilikum-Minz-Soße (3,19,24,49,81)", dishes[0].description)
        assertEquals(3, dishes[0].prices.size)

        assertTrue(dishes[1].ingredients.pork)
        assertTrue(dishes[1].ingredients.alcohol)
    }
}
