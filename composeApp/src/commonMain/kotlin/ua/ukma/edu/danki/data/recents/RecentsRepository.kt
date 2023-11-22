package ua.ukma.edu.danki.data.recents

import ua.ukma.edu.danki.models.GetRecentTerms
import ua.ukma.edu.danki.models.RecentTerms

interface RecentsRepository {
    suspend fun getRecentTerms(request: GetRecentTerms): RecentTerms?
}