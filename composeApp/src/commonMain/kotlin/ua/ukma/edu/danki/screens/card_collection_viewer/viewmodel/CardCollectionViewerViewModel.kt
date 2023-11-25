package ua.ukma.edu.danki.screens.card_collection_viewer.viewmodel

import ua.ukma.edu.danki.core.viewmodel.ViewModel
import ua.ukma.edu.danki.data.Injection
import ua.ukma.edu.danki.data.card.CardRepository
import ua.ukma.edu.danki.data.card_collections.CardCollectionsRepository
import ua.ukma.edu.danki.models.CardDTO
import ua.ukma.edu.danki.models.DeleteCollectionsRequest
import ua.ukma.edu.danki.models.GetCardsOfCollection
import ua.ukma.edu.danki.models.UserCardCollectionDTO
import ua.ukma.edu.danki.screens.game.viewmodel.GameState


class CardCollectionViewerViewModel(
    private val cardCollectionsRepository: CardCollectionsRepository = Injection.cardCollectionsRepository,
    private val cardRepository: CardRepository = Injection.cardRepository,
    private val collection: UserCardCollectionDTO
) : ViewModel<CardCollectionViewerState, CardCollectionViewerAction, CardCollectionViewerEvent>(initialState = CardCollectionViewerState.Loading) {

//    private val mockData = listOf(
//        CardDTO(
//            id = null,
//            "Banan",
//            "Definition for Banan: Banan is a tropical fruit known for its curved shape and yellow skin. It is a good source of potassium and is often used in various culinary dishes.",
//            null
//        ),
//        CardDTO(
//            id = null,
//            "Apple",
//            "Definition for Apple: Apple is a widely consumed fruit with a variety of flavors and colors. It is often used in making pies, applesauce, and various desserts.",
//            null
//        ),
//        CardDTO(
//            id = null,
//            "Orange",
//            "Definition for Orange: Orange is a citrus fruit that is rich in vitamin C. It is commonly consumed as fresh fruit or as a refreshing juice.",
//            null
//        ),
//        CardDTO(
//            id = null,
//            "Grapes",
//            "Definition for Grapes: Grapes are small, sweet, and juicy fruits that are used to make wine, raisins, and consumed as a healthy snack.",
//            null
//        ),
//        CardDTO(
//            id = null,
//            "Strawberry",
//            "Definition for Strawberry: Strawberry is a red, heart-shaped fruit known for its sweet taste. It is used in a variety of desserts, jams, and as a topping for many dishes.",
//            null
//        ),
//        CardDTO(
//            id = null,
//            "Pineapple",
//            "Definition for Pineapple: Pineapple is a tropical fruit with a spiky skin and sweet, juicy flesh. It is often used in fruit salads, smoothies, and as a pizza topping.",
//            null
//        ),
//        CardDTO(
//            id = null,
//            "Watermelon",
//            "Definition for Watermelon: Watermelon is a refreshing and hydrating fruit with a green rind and pink or red flesh. It's a popular fruit during the summer months.",
//            null
//        ),
//        CardDTO(
//            id = null,
//            "Mango",
//            "Definition for Mango: Mango is a tropical fruit with a sweet and juicy flesh. It is often eaten fresh, added to salads, or used in mango lassi and chutney.",
//            null
//        ),
//        CardDTO(
//            id = null,
//            "Cherry",
//            "Definition for Cherry: Cherry is a small, round fruit with a tart or sweet flavor. It is used in various desserts, pies, and is a popular ice cream topping.",
//            null
//        ),
//        CardDTO(
//            id = null,
//            "Kiwi",
//            "Definition for Kiwi: Kiwi is a small, green fruit with fuzzy skin and vibrant green flesh. It is rich in vitamin C and can be eaten with or without the skin.",
//            null
//        )
//    )

    override fun obtainEvent(viewEvent: CardCollectionViewerEvent) {
        when (viewEvent) {
            CardCollectionViewerEvent.GoBack -> processGoBack()
            is CardCollectionViewerEvent.OnCardClick -> processOnCardClick(viewEvent.card)
            is CardCollectionViewerEvent.DeleteCollection -> processDeletingCollection(collection = viewEvent.collection)
            CardCollectionViewerEvent.PlayGame -> processPlayGame()
        }
    }

    init {
        withViewModelScope {
            setViewState(CardCollectionViewerState.Loading)
            setViewState(
                CardCollectionViewerState.CollectionCards(
                    collection = collection,
                    cardList = getCardViewerModelList()
                )
            )
        }
    }

    private suspend fun getCardViewerModelList(): List<CardDTO> {
        val listOfCards = cardRepository.getCardsOfCollection(
            GetCardsOfCollection(
                collection = collection.id
            )
        )
        return listOfCards?.cards ?: emptyList()
    }

    private fun processDeletingCollection(collection: UserCardCollectionDTO) {
        withViewModelScope {
            cardCollectionsRepository.deleteCollection(
                DeleteCollectionsRequest(collections = listOf(collection.id))
            )
            setViewAction(CardCollectionViewerAction.NavigateBack)
        }
    }

    private fun processOnCardClick(card: CardDTO) {
        withViewModelScope {
            setViewAction(CardCollectionViewerAction.OpenCardToEdit(card = card))
        }
    }

    private fun processGoBack() {
        withViewModelScope {
            setViewAction(CardCollectionViewerAction.NavigateBack)
        }
    }

    private fun processPlayGame() {
        withViewModelScope {
            val state = viewStates().value
            if (state !is CardCollectionViewerState.CollectionCards) return@withViewModelScope

            setViewAction(CardCollectionViewerAction.PlayGame(state.collection.id))
        }
    }
}
