package com.timatifey.views

import com.timatifey.controllers.MainController
import com.timatifey.controllers.MouseController
import com.timatifey.models.client.Client
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.input.MouseEvent
import tornadofx.*
import java.util.concurrent.Callable

class ScreenControlView : View("View") {
    private val mainController: MainController by inject()
    private val mouseController: MouseController by inject()
    private val image = Client.screenReceiver.image

    override val root = form {
        title = "${mainController.ip}:${mainController.port}"
        setPrefSize(640.0, 500.0)
        minWidth = 256.0
        minHeight = 144.0

        imageview(image) {
            paddingAll = 0.0
            isPreserveRatio = true
            fitHeightProperty().bind(Bindings.createDoubleBinding(
                    Callable {
                        parent.layoutBounds.height
                    }, parent.layoutBoundsProperty()
            ))
            fitWidthProperty().bind(Bindings.createDoubleBinding(
                    Callable {
                        parent.layoutBounds.width
                    }, parent.layoutBoundsProperty()
            ))
            addEventHandler(MouseEvent.ANY) {
                mouseController.sendMouseEvent(it!!)
            }
        }

        currentWindow?.setOnCloseRequest {
            it.consume()
            confirm("Вы уверены, что хотите разорвать соединение?") {
                mainController.disconnect()
                find(ScreenControlView::class).replaceWith(MainView::class)
            }
        }
    }
}
