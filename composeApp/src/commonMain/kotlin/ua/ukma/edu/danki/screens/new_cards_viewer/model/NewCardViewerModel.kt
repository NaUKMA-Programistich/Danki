package ua.ukma.edu.danki.screens.new_cards_viewer.model

import ua.ukma.edu.danki.models.UserCardCollectionDTO

data class NewCardViewerModel (
    var term : String,
    var definition : String,
    var collection : UserCardCollectionDTO? = null
)