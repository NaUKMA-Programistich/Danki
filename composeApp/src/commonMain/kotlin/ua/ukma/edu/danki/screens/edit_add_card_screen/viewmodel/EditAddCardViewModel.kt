package ua.ukma.edu.danki.screens.edit_add_card_screen.viewmodel

import ua.ukma.edu.danki.core.viewmodel.ViewModel
import ua.ukma.edu.danki.data.Injection
import ua.ukma.edu.danki.data.card.CardRepository
import ua.ukma.edu.danki.data.card_collections.CardCollectionsRepository
import ua.ukma.edu.danki.models.*


class EditAddCardViewModel(
    private val card : CardDTO,
    private val cardRepository: CardRepository = Injection.cardRepository,
    private val cardCollectionsRepository: CardCollectionsRepository = Injection.cardCollectionsRepository
) : ViewModel<EditAddCardState, EditAddCardAction, EditAddCardEvent>(initialState = EditAddCardState.Loading) {

    override fun obtainEvent(viewEvent: EditAddCardEvent) {
        when (viewEvent) {
            EditAddCardEvent.Cancel -> processCancel()
            is EditAddCardEvent.SaveCard -> processSavingCard(
                card = viewEvent.card,
                collectionId = viewEvent.collectionId,
                isNew = viewEvent.isNew
            )

            is EditAddCardEvent.DeleteCard -> processDeletingCard(card = viewEvent.card)
        }
    }

    private fun processDeletingCard(card: CardDTO) {
        withViewModelScope {
            card.id?.let {
                cardRepository.deleteCards(DeleteCardsRequest(listOf(it)))
            }
        }
    }

    private fun processSavingCard(card: CardDTO, collectionId: String?, isNew: Boolean) {
        withViewModelScope {
            if (isNew) {
                collectionId?.let {
                    cardRepository.createCardInCollection(CreateCardInCollectionRequest(card, collectionId))
                } ?: run {
                    cardRepository.createNewCard(card)
                }
                processToSearch()
            } else {
                cardRepository.updateCard(card)
                collectionId?.let { collection ->
                    card.id?.let { id ->
                        if (!getCollectionCards(collection).contains(card)) {
                            cardRepository.moveCardToCollection(
                                MoveCardToCollectionRequest(
                                    card = id,
                                    collection = collection
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private suspend fun getCollectionCards (collectionId : String): List<CardDTO> {
        return cardRepository.getCardsOfCollection(GetCardsOfCollection(collectionId))?.cards ?: emptyList()
    }

    private suspend fun getCollections (): List<UserCardCollectionDTO> {
        return cardCollectionsRepository.getUserCollections(GetUserCollections())?.cardCollections  ?: emptyList()
    }

    init {
        withViewModelScope {
            setViewState(EditAddCardState.Loading)
            setViewState(
                EditAddCardState.CardToEdit(
                    card = card, collectionList = getCollections ()
                )
            )
        }
    }

    private fun processToSearch() {
        withViewModelScope {
            setViewAction(EditAddCardAction.NavigateToSearch)
        }
    }

    private fun processCancel() {
        withViewModelScope {
            setViewAction(EditAddCardAction.NavigateBack)
        }
    }

}