package com.example.tpuhelper

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tpuhelper.entity.TwoWords
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.StompMessage
import ua.naiksoftware.stomp.provider.OkHttpConnectionProvider
import java.lang.reflect.Type

class MainActivity : AppCompatActivity() {
    private var stompClient:StompClientEx? = null
    private var foundedItems:ArrayList<TwoWords> = ArrayList()
    private lateinit var founded_items_listView: RecyclerView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        founded_items_listView = findViewById(R.id.founded_items_list)
//        founded_items_listView.layoutManager = LinearLayoutManager(this)
        founded_items_listView.adapter = StateAdapter(this,foundedItems)
        stompClient = StompClientEx()
        stompClient!!.addToComposite(getSubscriber())
        stompClient!!.initChat();

        addItem("nameeeeeee","description")
        addItem("asdasd","description")
        addItem("asd","description")
        addItem("sd","description")
        addItem("stass item","molodets")
        addItem(TwoWords("zhenya","super proger",2))
        addItem("nameeeeeee","description")
        val searchEditText: EditText = findViewById(R.id.search_edit_text)
        val buttonAdd: Button = findViewById(R.id.button_add)
        val buttonRemove: Button = findViewById(R.id.button_remove)
        searchEditText.addTextChangedListener(getTextWatcher())
        buttonAdd.setOnClickListener { addItem(TwoWords("By button", "Descriptoionono", 1)) }
        buttonRemove.setOnClickListener { removeItems() }

    }

    private fun addItem(name:String, description:String) {
        addItem(TwoWords(name, description))
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addItem(words:TwoWords) {
        foundedItems.add(words)
        founded_items_listView.adapter?.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun removeItems()
    {
        foundedItems.clear();
        founded_items_listView.adapter?.notifyDataSetChanged();
    }


    private fun getTextWatcher() : TextWatcher {

        return object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.isNotEmpty())
                    stompClient?.sendMessage(s.toString());
            }
        }
    }
    private fun getSubscriber():Disposable
    {
        val topicSubscribe = stompClient?.mStompClient!!.topic("/users/queue/messages")
            .subscribeOn(Schedulers.io(), false)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { topicMessage: StompMessage ->
                val itemsListType: Type = object : TypeToken<List<Temp?>?>() {}.type
                val foundedItems: List<Temp> = Gson().fromJson(topicMessage.payload, itemsListType)
                removeItems();
                for (foundedItem: Temp in foundedItems) {
                    addItem(foundedItem.name, foundedItem.score)
                }
            }
        return topicSubscribe;
    }



}
