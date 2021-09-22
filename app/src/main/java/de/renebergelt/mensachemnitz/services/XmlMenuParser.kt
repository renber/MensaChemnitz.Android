package de.renebergelt.mensachemnitz.services

import de.renebergelt.mensachemnitz.types.Dish
import de.renebergelt.mensachemnitz.types.DishIngredients
import de.renebergelt.mensachemnitz.types.Price
import org.w3c.dom.Element
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory

class XmlMenuParser {

    fun parseMenu(stream : InputStream) : List<Dish> {

        val dishes = ArrayList<Dish>()

        try {
            val builderFactory = DocumentBuilderFactory.newInstance()
            val docBuilder = builderFactory.newDocumentBuilder()
            val doc = docBuilder.parse(stream)

            val dishNodes = doc.getElementsByTagName("essen");

            for(i in 0..dishNodes.length - 1) {
                try {
                    val node = dishNodes.item(i)
                    if (node is Element) {
                        val id = node.getAttribute("id")?.trim() ?: "-1"
                        val name = node.getAttribute("kategorie")?.trim() ?: ""
                        val description = node.getElementsByTagName("deutsch").item(0).textContent?.trim() ?: ""
                        var imageUri : String? = null

                        if (node.getAttribute("img") == "true") {
                            imageUri = node.getAttribute("img_small")?.trim()
                        }

                        // vegan menus are only indicated in the meal description, there is no tag
                        // -> set vegetarian to true, if the meal descriptions contains "vegan"
                        val vegan = description.contains("vegan", true)

                        val ingredients = DishIngredients(
                            vegan || node.getAttribute("vegetarisch") == "true",
                            node.getAttribute("schwein") == "true",
                            node.getAttribute("rind") == "true",
                            node.getAttribute("alkohol") == "true"
                        )

                        val prices = ArrayList<Price>()

                        val priceNodes = node.getElementsByTagName("pr")
                        for(pidx in 0..priceNodes.length - 1) {
                            val pnode = priceNodes.item(pidx)
                            if (pnode is Element) {
                                prices.add(Price(pnode.getAttribute("gruppe")?.trim() ?: "", pnode.textContent?.trim() ?: ""))
                            }
                        }

                        dishes.add(Dish(id, name, description, imageUri, ingredients, prices))
                    }
                } catch (e: Exception) {
                    // error while reading
                }
            }

        } catch (e: Exception) {
            // --
        }

        return dishes
    }

}