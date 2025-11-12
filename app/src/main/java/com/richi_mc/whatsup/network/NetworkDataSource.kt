package com.richi_mc.whatsup.network

import android.util.Log
import com.richi_mc.whatsup.R
import com.richi_mc.whatsup.ui.model.Chat
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketException
import java.net.UnknownHostException

class NetworkDataSource {

    private val socket: DatagramSocket
    private var address: InetAddress? = null
    private var HOST: String? = null
    private var user: String? = null

    fun setHost(host: String?) {
        try {
            this.HOST = host
            address = InetAddress.getByName(host)
        } catch (e: UnknownHostException) {
            Log.d("NetworkDataSource", "Unknown host")
            throw RuntimeException(e)
        }
    }

    fun sendData(query: String): ByteArray? {
        val data = query.toByteArray()
        val packet = DatagramPacket(data, data.size, address, PORT)
        try {
            socket.send(packet)
            Log.d("NetworkDataSource", "data sended")
            val eco = ByteArray(PACKET_LENGTH)
            val packetR = DatagramPacket(eco, eco.size, address, PORT)
            socket.receive(packetR)
            Log.d("NetworkDataSource", "data received")

            return packetR.getData()
        } catch (e: IOException) {
            Log.e("NetworkDataSource", "data send failed")
            throw RuntimeException(e)
        }
    }

    init {
        try {
            socket = DatagramSocket()
        } catch (e: SocketException) {
            Log.d("NetworkDataSource", "Error creating socket")
            throw RuntimeException(e)
        }
    }

    fun sendUsername(name: String): String {
        val query = "ENTER," + name
        this.user = name

        // Send username.
        val eco_receive = sendData(query)
        val message = kotlin.text.String(eco_receive!!)

        Log.d("NetworkDataSource", "Message received: " + message)

        return message
    }

    fun createGroup(name: String): String {
        val query = "CREATE," + name + "," + user
        val data = query.toByteArray()
        val packet = DatagramPacket(data, data.size, address, PORT)

        try {
            socket.send(packet)
            Log.d("NetworkDataSource", "Create group sendend")
            socket.receive(packet)
            val message = String(packet.getData(), 0, packet.getLength())
            Log.d("NetworkDataSource", "Message received: " + message)

            return message
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    val chats: MutableList<Chat>
        get() {
            val query = "GET," + user
            val data = sendData(query)
            try {
                val chats = ArrayList<Chat>()
                val array = JSONArray(String(data!!))

                for (i in 0..< array.length()) {
                    val `object` = array.getJSONObject(i)
                    val name = `object`.getString("name")
                    chats.add(Chat(i, name, R.drawable.outline_groups_24))
                }
                return chats
            } catch (e: JSONException) {
                throw RuntimeException(e)
            }
        }

    fun sendMessage(content: String?, chatId : Int): String {
        val query = "SENDMESSAGE,$user,$chatId,$content"
        val eco = sendData(query)
        val message = String(eco!!)

        Log.d("NetworkDataSource", "Message received: $message")

        return limpiarCadenaJson(message) ?: "{}"
    }
    fun getChatInfo(chatId: Int) : String {
        val query = "GETCHAT,$chatId"
        val eco = sendData(query)

        val message = String(eco!!)
        Log.d("NetworkDataSource", "Message received: $message")
        return limpiarCadenaJson(message) ?: "{}"
    }

    fun closeConnection() {
        socket.close()
    }
    fun limpiarCadenaJson(stringSucio: String): String? {
        val lastBraceIndex = stringSucio.lastIndexOf('}')

        if (lastBraceIndex != -1) {
            return stringSucio.substring(0, lastBraceIndex + 1)
        }
        return null
    }

    companion object {
        private const val PORT = 8080
        private const val PACKET_LENGTH = 65530


        var instance: NetworkDataSource? = null
            get() {
                if (field == null) {
                    field = NetworkDataSource()
                }
                return field
            }
            private set
    }
}
