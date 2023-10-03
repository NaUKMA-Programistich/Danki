package ua.ukma.edu.danki.models

import io.ktor.resources.*

@Resource("/hello")
class Articles(val sort: String? = "new")
