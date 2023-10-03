package controllers

import io.ktor.resources.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import io.ktor.server.routing.get
import ua.ukma.edu.danki.models.Articles


fun Routing.cardCollectionsControllers() {
    route("/oh-no") {
        get<Articles> {

        }
    }
}