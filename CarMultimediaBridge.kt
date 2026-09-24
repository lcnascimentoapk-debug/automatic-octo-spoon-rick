package com.rick.assistant.car

import androidx.car.app.CarAppService
import androidx.car.app.Screen
import androidx.car.app.Session
import androidx.car.app.model.*

/**
 * Android Auto / Car App Library Integration
 * Hands-free HUD voice assistant for vehicle head units
 */
class RickCarAppService : CarAppService() {
    override fun onCreateSession(): Session {
        return object : Session() {
            override fun onCreateScreen(intent: android.content.Intent): Screen {
                return RickCarMainScreen(carContext)
            }
        }
    }
}

class RickCarMainScreen(carContext: androidx.car.app.CarContext) : Screen(carContext) {
    override fun onGetTemplate(): Template {
        return PaneTemplate.Builder(
            Pane.Builder().apply {
                addRow(
                    Row.Builder()
                        .setTitle("Rick — Assistente Veicular")
                        .addText("Toque no microfone ou diga 'E aí Rick'")
                        .build()
                )
            }.build()
        )
        .setTitle("Rick Auto HUD")
        .setHeaderAction(Action.APP_ICON)
        .build()
    }
}