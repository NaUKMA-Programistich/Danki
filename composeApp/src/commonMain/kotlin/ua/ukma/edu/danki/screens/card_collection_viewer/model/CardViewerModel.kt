package ua.ukma.edu.danki.screens.card_collection_viewer.model

import ua.ukma.edu.danki.models.UserCardCollectionDTO

data class CardViewerModel (
    var term : String,
    var definition : String,
    var collection : UserCardCollectionDTO? = null
)