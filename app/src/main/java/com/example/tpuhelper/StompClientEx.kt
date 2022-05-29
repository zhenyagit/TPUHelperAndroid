package com.example.tpuhelper


import android.os.Message
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import ua.naiksoftware.stomp.dto.StompMessage
import ua.naiksoftware.stomp.provider.OkHttpConnectionProvider.TAG
import java.lang.reflect.Type


class StompClientEx {
    //:ViewModel()
    companion object{
//        const val SOCKET_URL = "ws://192.168.137.132:8080/message/websocket"
//        const val SOCKET_URL = "ws://192.168.137.198:8080/message/websocket"
//        const val CHAT_TOPIC = "/chat/messages"
//        const val CHAT_LINK_SOCKET = "/app/message"
//const val SOCKET_URL = "ws://192.168.137.198:8080/hello/websocket"
        const val SOCKET_URL = "ws://5.187.3.41:8080/hello/websocket"
        const val CHAT_TOPIC = "/users/queue/messages"
        const val CHAT_LINK_SOCKET = "/app/hello"
    }

    var mStompClient: StompClient? = null
    private var compositeDisposable: CompositeDisposable? = null
    private val _chatState = MutableLiveData<Message?>()

    init {
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SOCKET_URL/*, headerMap*/)
            .withServerHeartbeat(30000)
        resetSubscriptions()
    }

    public fun initChat() {
        if (mStompClient != null) {
            //todo create this in main activity
            val topicSubscribe = mStompClient!!.topic(CHAT_TOPIC)
                .subscribeOn(Schedulers.io(), false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ topicMessage: StompMessage ->
                    Log.d("message payload", topicMessage.payload)
                },
                    {
                        Log.e(TAG, "Error!", it)
                    }
                )

            val lifecycleSubscribe = mStompClient!!.lifecycle()
                .subscribeOn(Schedulers.io(), false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { lifecycleEvent: LifecycleEvent ->
                    when (lifecycleEvent.type!!) {
                        LifecycleEvent.Type.OPENED -> Log.d(TAG, "Stomp connection opened")
                        LifecycleEvent.Type.ERROR -> Log.e(TAG, "Error", lifecycleEvent.exception)
                        LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT,
                        LifecycleEvent.Type.CLOSED -> {
                            Log.d(TAG, "Stomp connection closed")
                        }
                    }
                }

            compositeDisposable!!.add(lifecycleSubscribe)
            compositeDisposable!!.add(topicSubscribe)
            val headers: ArrayList<StompHeader> = ArrayList()
            headers.add(StompHeader("username", "zhenya"))
            if (!mStompClient!!.isConnected) {
                mStompClient!!.connect(headers)
            }


        } else {
            Log.e(TAG, "mStompClient is null!")
        }
    }
    fun addToComposite(item:Disposable)
    {
        compositeDisposable!!.add(item)
    }

    fun sendMessage(text: String) {
//        val message = Message(text = text, author = "Me")
//        val chatSocketMessage = entityToDto(message)
        sendCompletable(mStompClient!!.send(CHAT_LINK_SOCKET, "{\"message\":\"$text\"}"))
    }


    private fun sendCompletable(request: Completable) {
        compositeDisposable?.add(
            request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Log.d(TAG, "Stomp sended")
                    },
                    {
                        Log.e(TAG, "Stomp error", it)
                    }
                )
        )
    }

    private fun resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable!!.dispose()
        }

        compositeDisposable = CompositeDisposable()
    }

//    override fun onCleared() {
//        super.onCleared()
//
//        mStompClient?.disconnect()
//        compositeDisposable?.dispose()
//    }
}
internal class Temp(var name: String, var description: String, var score: String)

