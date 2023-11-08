package ua.ukma.edu.danki.screens.ui_models

import ua.ukma.edu.danki.models.UserCardCollectionDTO

data class UiCardModel(
    var term : String,
    var definition : String,
    var collection : UserCardCollectionDTO? = null
)