package ua.ukma.edu.danki.screens.new_cards_viewer.viewmodel

import kotlinx.datetime.Clock
import ua.ukma.edu.danki.core.viewmodel.ViewModel
import ua.ukma.edu.danki.models.UserCardCollectionDTO
import ua.ukma.edu.danki.screens.new_cards_viewer.model.NewCardViewerModel


class NewCardViewerViewModel() : ViewModel<NewCardViewerState, NewCardViewerAction, NewCardViewerEvent>(initialState = NewCardViewerState.Loading) {

    private val mockCollections = listOf(
        UserCardCollectionDTO(
            "1", "Fruits", Clock.System.now(),
            own = false,
            favorite = false
        ),
        UserCardCollectionDTO(
            "2", "Family", Clock.System.now(),
            own = false,
            favorite = false
        ),
        UserCardCollectionDTO(
            "3", "Work", Clock.System.now(),
            own = false,
            favorite = false
        )
    )

    private val mockData = listOf(
        NewCardViewerModel(
            "Banan",
            "Definition for Banan: Banan is a tropical fruit known for its curved shape and yellow skin. It is a good source of potassium and is often used in various culinary dishes.",
            mockCollections[0]
        ),
        NewCardViewerModel(
            "Apple",
            "Definition for Apple: Apple is a widely consumed fruit with a variety of flavors and colors. It is often used in making pies, applesauce, and various desserts.",
            mockCollections[0]
        ),
        NewCardViewerModel(
            "Orange",
            "Definition for Orange: Orange is a citrus fruit that is rich in vitamin C. It is commonly consumed as fresh fruit or as a refreshing juice.",
            mockCollections[1]
        ),
        NewCardViewerModel(
            "Grapes",
            "Definition for Grapes: Grapes are small, sweet, and juicy fruits that are used to make wine, raisins, and consumed as a healthy snack.",
            mockCollections[2]
        ),
        NewCardViewerModel(
            "Strawberry",
            "Definition for Strawberry: Strawberry is a red, heart-shaped fruit known for its sweet taste. It is used in a variety of desserts, jams, and as a topping for many dishes.",
            mockCollections[0]
        ),
        NewCardViewerModel(
            "Pineapple",
            "Definition for Pineapple: Pineapple is a tropical fruit with a spiky skin and sweet, juicy flesh. It is often used in fruit salads, smoothies, and as a pizza topping.",
            mockCollections[1]
        ),
        NewCardViewerModel(
            "Watermelon",
            "Definition for Watermelon: Watermelon is a refreshing and hydrating fruit with a green rind and pink or red flesh. It's a popular fruit during the summer months.",
            mockCollections[2]
        ),
        NewCardViewerModel(
            "Mango",
            "Definition for Mango: Mango is a tropical fruit with a sweet and juicy flesh. It is often eaten fresh, added to salads, or used in mango lassi and chutney.",
            mockCollections[0]
        ),
        NewCardViewerModel(
            "Cherry",
            "Definition for Cherry: Cherry is a small, round fruit with a tart or sweet flavor. It is used in various desserts, pies, and is a popular ice cream topping.",
            mockCollections[1]
        ),
        NewCardViewerModel(
            "Kiwi",
            "Definition for Kiwi: Kiwi is a small, green fruit with fuzzy skin and vibrant green flesh. It is rich in vitamin C and can be eaten with or without the skin.",
            mockCollections[2]
        )
    )

    override fun obtainEvent(viewEvent: NewCardViewerEvent) {
        when (viewEvent) {
            NewCardViewerEvent.GoBack -> processGoBack()
            is NewCardViewerEvent.OnCardClick -> processOnCardClick(viewEvent.newCardViewerModel)
        }
    }

    init {
        withViewModelScope {
            setViewState(NewCardViewerState.Loading)
            setViewState(NewCardViewerState.NewCardCards(newCardViewerModelMap = getNewCardMap()))
        }
    }

    private suspend fun getNewCardMap () : Map<UserCardCollectionDTO?,MutableList<NewCardViewerModel>> {
        // TODO("get all new cards")
        return mockData.run {
            val map = mutableMapOf<UserCardCollectionDTO?,MutableList<NewCardViewerModel>>()
            this.forEach {
                if(map.containsKey(it.collection)){
                    map[it.collection]?.add(it)
                }else {
                    map[it.collection] = mutableListOf(it)
                }
            }
            map
        }
    }

    private fun processOnCardClick(newCardViewerModel: NewCardViewerModel) {
        withViewModelScope {
            setViewAction(NewCardViewerAction.OpenCardToEdit(newCardViewerModel = newCardViewerModel))
        }
    }

    private fun processGoBack() {
        withViewModelScope {
            setViewAction(NewCardViewerAction.NavigateBack)
        }
    }

}