package com.timatifey.models.senders

import com.google.gson.Gson
import com.timatifey.models.client.id
import com.timatifey.models.data.DataPackage
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket
import java.net.SocketException
import java.util.concurrent.LinkedBlockingQueue

class MessageSender(private val socket: Socket): Runnable {
    @Volatile private var needStop = false
    private val queueMessages = LinkedBlockingQueue<String>()

    fun sendMessage(msg: String) { queueMessages.put(msg)
    }

    override fun run() {
        try {
            val output = PrintWriter(OutputStreamWriter(socket.getOutputStream()), true)
            val firstMsg = Gson().toJson(DataPackage(DataPackage.DataType.MESSAGE,
                message = "$id:MESSAGE_SOCKET"))
            output.println(firstMsg)
            println("Message Sender Start")
            needStop = false
            while (!needStop) {
                val msg = queueMessages.take()
                val data = DataPackage(DataPackage.DataType.MESSAGE, message = msg)
                val json = Gson().toJson(data)
                output.println(json)
            }
            output.close()
        } catch (e: IOException) {
            println("Message sender Socket Error: $e")
        } finally {
            needStop = true
            println("Message sender Stop")
            try {
                socket.close()
            } catch (e: SocketException) {}
        }
    }

    fun stop() { needStop = true }
}