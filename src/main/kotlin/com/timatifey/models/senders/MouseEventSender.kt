package com.timatifey.models.senders

import com.google.gson.Gson
import com.timatifey.Mouse
import javafx.scene.input.MouseEvent
import java.io.IOException
import java.io.PrintWriter
import java.lang.Thread.sleep
import java.net.Socket

class MouseEventSender(private val client: Socket): Runnable {
    var needSend = false
    lateinit var eventMouse: MouseEvent

//    private fun takeCords() {
//        val mousePoint = MouseInfo.getPointerInfo()
//        val x = mousePoint.location.x
//        val y = mousePoint.location.y
//    }

    fun setEvent(event: MouseEvent) {
        eventMouse = event
        needSend = true
        println(event)
    }

    override fun run() {
        try {
            val output = PrintWriter(client.getOutputStream(), true)
            while (true) {
                if (needSend) {
                    val mouse = Mouse(
                            eventMouse.eventType,
                            eventMouse.x,
                            eventMouse.y,
                            eventMouse.screenX,
                            eventMouse.screenY,
                            eventMouse.button,
                            eventMouse.clickCount,
                            eventMouse.isShiftDown,
                            eventMouse.isControlDown,
                            eventMouse.isAltDown,
                            eventMouse.isMetaDown,
                            eventMouse.isPrimaryButtonDown,
                            eventMouse.isMiddleButtonDown,
                            eventMouse.isPopupTrigger,
                            eventMouse.isStillSincePress,
                            eventMouse.isSecondaryButtonDown
                    )
                    val json = Gson().toJson(mouse)
                    println("mouse $json")
                    output.println(mouse)
                    needSend = false
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            println("Cursor Sender Client Socket Error: $e")
        }
    }
}
