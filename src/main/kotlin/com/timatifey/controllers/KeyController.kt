package com.timatifey.controllers

import com.timatifey.models.data.Key
import javafx.scene.input.KeyEvent
import tornadofx.*

class KeyController: Controller() {
    private val clientController: ClientController by inject()

    fun sendKeyEvent(eventKey: KeyEvent) {
        if (clientController.keyCheck.value) {
            clientController.client.keyEventSender.putEvent(Key(
                    Key.KeyEventType.valueOf(eventKey.eventType.name),
                    eventKey.character,
                    eventKey.text,
                    eventKey.code,
                    eventKey.isShiftDown,
                    eventKey.isControlDown,
                    eventKey.isAltDown,
                    eventKey.isMetaDown
            ))
        }
    }
}