package com.richi_mc.whatsup.network

import android.util.Log
import com.richi_mc.whatsup.R
import com.richi_mc.whatsup.ui.model.Chat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.MulticastSocket
import java.net.SocketException
import java.net.UnknownHostException

// Configuración del Multicast
private const val MULTICAST_ADDRESS = "231.1.1.1" // Ejemplo de dirección de grupo
private const val PORT = 8080
private const val PACKET_LENGTH = 65530

class NetworkDataSource {
    private val socket: MulticastSocket
    private var address: InetAddress? = null
    private var HOST: String? = null
    private var user: String? = null

    fun setHost(host: String?) {
        try {
            this.HOST = host
            address = InetAddress.getByName(host)
        } catch (e: UnknownHostException) {
            Log.d("NetworkDataSource", "Error al intentar conectarse al HOST")
            throw RuntimeException(e)
        }
    }

    fun sendData(query: String): String? {

        val data = query.toByteArray()
        val packet = DatagramPacket(data, data.size, address, PORT)

        try {
            // Manda el paquete.
            socket.send(packet)

            val eco = ByteArray(PACKET_LENGTH)
            val packetR = DatagramPacket(eco, eco.size, address, PORT)

            // Recibe el ECO de la petición.
            socket.receive(packetR)

            return String(packetR.data, 0, packetR.length)
        } catch (e: IOException) {
            Log.e("NetworkDataSource", "Error al enviar/recibir el paquete")
            throw RuntimeException(e)
        }
    }

    fun sendMessage(content: String, chatId : Int) {
        val query   = "SENDMESSAGE,$user,$chatId,$content"
        val buffer  = query.toByteArray()
        val packet  = DatagramPacket(buffer, buffer.size, address, PORT)

        socket.send(packet)

        Log.d("NetworkDataSource", "Mensaje enviado")
    }

    fun getFlowMessage(): Flow<String> = flow {
        while(true) {
            val buffer = ByteArray(PACKET_LENGTH)
            val packet = DatagramPacket(buffer, buffer.size)
            socket.receive(packet)
            val message = String(packet.data, 0, packet.length)

            emit(message)
        }
    }

    init {
        try {
            socket = MulticastSocket(PORT)
        } catch (e: SocketException) {
            Log.d("NetworkDataSource", "Error creating socket")
            throw RuntimeException(e)
        }
    }

    fun sendUsername(name: String): String {
        val query = "ENTER," + name
        this.user = name

        // Send username.
        val message = sendData(query)

        Log.d("NetworkDataSource", "sendUsername: Message received: " + message)

        return message ?: "error"
    }
    fun createGroup(name: String): String {
        val query = "CREATE," + name + "," + user
        val message = sendData(query)
        Log.d("NetworkDataSource", "createGroup: Message received: $message")
        return message ?: "error"
    }

    val chats: MutableList<Chat> get() {
            val query = "GET," + user
            val data = sendData(query)
            try {
                val chats = ArrayList<Chat>()
                val array = JSONArray(data)

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
    fun getChatInfo(chatId: Int) {

        val query = "GETCHAT,$chatId,$user"
        val buffer  = query.toByteArray()
        val packet  = DatagramPacket(buffer, buffer.size, address, PORT)

        socket.send(packet)
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
    fun leaveChat(chatId: Int) {
        val query = "LEAVECHAT,$user,$chatId"
        val message = sendData(query)

        Log.d("NetworkDataSource", "leaveChat: $message")
    }

    companion object {
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
