package ua.ukma.edu.danki.screens.collections.viewmodel

sealed class CollectionEvent {
    data class SortList(val sortingMethod: SortingMethod) : CollectionEvent()
}

enum class SortingMethod {
    ByName,
    ByDate;

    override fun toString(): String {
        if (this == ByName)
            return "By name"
        return "By date"
    }
}