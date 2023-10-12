package ua.ukma.edu.danki.models

import io.ktor.resources.*

@Resource("/collections")
class GetCollectionsCards(val sort: String? = "new")



enum class CollectionSortParam {
    ByName, ByDate
}

@Resource("/collections/")
class GetCollection(val sort: CollectionSortParam)

@Resource("/articles")
class Articles()