package ua.ukma.edu.danki.flow

import kotlinx.coroutines.flow.StateFlow

class KMMStateFlow<T>(
    private val origin: StateFlow<T>
) : KMMFlow<T>(), StateFlow<T> by origin
