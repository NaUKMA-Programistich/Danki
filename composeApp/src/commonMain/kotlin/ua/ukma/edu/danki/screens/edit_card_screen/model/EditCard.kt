package ua.ukma.edu.danki.screens.edit_card_screen.model

import ua.ukma.edu.danki.models.UserCardCollectionDTO

data class EditCard (
    var term : String,
    var definition : String,
    var collection : UserCardCollectionDTO? = null
)