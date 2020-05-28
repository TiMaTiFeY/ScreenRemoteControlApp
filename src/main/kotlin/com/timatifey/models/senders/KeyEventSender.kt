package com.timatifey.models.senders

import com.google.gson.Gson
import com.timatifey.models.client.id
import com.timatifey.models.data.DataPackage
import com.timatifey.models.data.Key
import java.io.IOException
import java.io.PrintWriter
import java.net.Socket
import java.util.concurrent.LinkedBlockingQueue

class KeyEventSender(private val socket: Socket): Runnable {
    @Volatile private var needStop = false
    private val queueKey = LinkedBlockingQueue<Key>()

    fun putKeyEvent(key: Key) {
        queueKey.put(key)
    }

    override fun run() {
        try {
            val output = PrintWriter(socket.getOutputStream(), true)

            val firstMsg = Gson().toJson(DataPackage(DataPackage.DataType.MESSAGE,
                message = "$id:KEY_SOCKET"))
            output.println(firstMsg)

            while (!needStop) {
                val key = queueKey.take()
                val data = DataPackage(DataPackage.DataType.KEY, key = key)
                val json = Gson().toJson(data)
                output.println(json)
            }
            output.close()
            socket.close()
        } catch (e: IOException) {
            println("Key Event Sender Client Socket Error: $e")
        }
    }

    fun stop() { needStop = true }
}
