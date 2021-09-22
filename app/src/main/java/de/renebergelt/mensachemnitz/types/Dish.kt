package de.renebergelt.mensachemnitz.types

class Dish(
    val id : String,
    val name : String,
    val description : String,
    val imageUri : String?,
    val ingredients : DishIngredients,
    val prices : List<Price>
) {
    // --
}