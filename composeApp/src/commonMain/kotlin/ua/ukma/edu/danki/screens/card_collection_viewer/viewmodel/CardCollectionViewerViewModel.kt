package ua.ukma.edu.danki.screens.card_collection_viewer.viewmodel

import kotlinx.datetime.Clock
import ua.ukma.edu.danki.core.viewmodel.ViewModel
import ua.ukma.edu.danki.models.UserCardCollectionDTO
import ua.ukma.edu.danki.screens.card_collection_viewer.model.CardCollectionViewerModel
import ua.ukma.edu.danki.screens.card_collection_viewer.model.CardViewerModel
import ua.ukma.edu.danki.screens.edit_card_screen.model.EditCard
import ua.ukma.edu.danki.screens.edit_card_screen.viewmodel.EditCardAction
import ua.ukma.edu.danki.screens.edit_card_screen.viewmodel.EditCardEvent
import ua.ukma.edu.danki.screens.edit_card_screen.viewmodel.EditCardState
import kotlin.time.Duration.Companion.hours


class CardCollectionViewerViewModel(cardCollectionViewerModel : CardCollectionViewerModel) : ViewModel<CardCollectionViewerState, CardCollectionViewerAction, CardCollectionViewerEvent>(initialState = CardCollectionViewerState.Loading) {

    private val mockData = listOf(
        CardViewerModel(
            "Banan",
            "Definition for Banan: Banan is a tropical fruit known for its curved shape and yellow skin. It is a good source of potassium and is often used in various culinary dishes.",
            null
        ),
        CardViewerModel(
            "Apple",
            "Definition for Apple: Apple is a widely consumed fruit with a variety of flavors and colors. It is often used in making pies, applesauce, and various desserts.",
            null
        ),
        CardViewerModel(
            "Orange",
            "Definition for Orange: Orange is a citrus fruit that is rich in vitamin C. It is commonly consumed as fresh fruit or as a refreshing juice.",
            null
        ),
        CardViewerModel(
            "Grapes",
            "Definition for Grapes: Grapes are small, sweet, and juicy fruits that are used to make wine, raisins, and consumed as a healthy snack.",
            null
        ),
        CardViewerModel(
            "Strawberry",
            "Definition for Strawberry: Strawberry is a red, heart-shaped fruit known for its sweet taste. It is used in a variety of desserts, jams, and as a topping for many dishes.",
            null
        ),
        CardViewerModel(
            "Pineapple",
            "Definition for Pineapple: Pineapple is a tropical fruit with a spiky skin and sweet, juicy flesh. It is often used in fruit salads, smoothies, and as a pizza topping.",
            null
        ),
        CardViewerModel(
            "Watermelon",
            "Definition for Watermelon: Watermelon is a refreshing and hydrating fruit with a green rind and pink or red flesh. It's a popular fruit during the summer months.",
            null
        ),
        CardViewerModel(
            "Mango",
            "Definition for Mango: Mango is a tropical fruit with a sweet and juicy flesh. It is often eaten fresh, added to salads, or used in mango lassi and chutney.",
            null
        ),
        CardViewerModel(
            "Cherry",
            "Definition for Cherry: Cherry is a small, round fruit with a tart or sweet flavor. It is used in various desserts, pies, and is a popular ice cream topping.",
            null
        ),
        CardViewerModel(
            "Kiwi",
            "Definition for Kiwi: Kiwi is a small, green fruit with fuzzy skin and vibrant green flesh. It is rich in vitamin C and can be eaten with or without the skin.",
            null
        )
    )

    override fun obtainEvent(viewEvent: CardCollectionViewerEvent) {
        when (viewEvent) {
            CardCollectionViewerEvent.GoBack -> processGoBack()
            is CardCollectionViewerEvent.OnCardClick -> processOnCardClick(viewEvent.cardViewerModel)
            is CardCollectionViewerEvent.DeleteCollection -> processDeletingCollection (cardCollectionViewerModel = viewEvent.cardCollectionViewerModel)
        }
    }

    init {
        withViewModelScope {
           setViewState(CardCollectionViewerState.Loading)
           setViewState(CardCollectionViewerState.CollectionCards(cardCollectionViewerModel = cardCollectionViewerModel, cardViewerModelList = getCardViewerModelList ()))
        }
    }

    private suspend fun getCardViewerModelList () : List<CardViewerModel> {
        // TODO("get cards of certain collection")
        return mockData
    }

    private fun processDeletingCollection(cardCollectionViewerModel : CardCollectionViewerModel) {
        withViewModelScope {
            // TODO("delete collection")
        }
    }

    private fun processOnCardClick(cardViewerModel: CardViewerModel) {
        withViewModelScope {
            setViewAction(CardCollectionViewerAction.OpenCardToEdit(cardViewerModel = cardViewerModel))
        }
    }

    private fun processGoBack() {
        withViewModelScope {
            setViewAction(CardCollectionViewerAction.NavigateBack)
        }
    }

}