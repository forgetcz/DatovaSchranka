package com.jvr.datovaschranka.activities

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jvr.datovaschranka.R
import com.jvr.datovaschranka.api.model.getListOfReceivedMessages.GetListOfReceivedMessages
import com.jvr.datovaschranka.databinding.ActivityListOfMessagesBinding
import com.jvr.datovaschranka.dbhelper.DbHelper
import com.jvr.datovaschranka.dbhelper.tableModel.v1.UsersTable
import com.jvr.datovaschranka.lib.classes.ListOfMessagesActivityAdapter
import com.jvr.datovaschranka.lib.classes.MyGestureListenerExtended
import java.util.*

class ListOfMessages : BaseActivity() {
    private lateinit var dbHelper: DbHelper
    private lateinit var binding: ActivityListOfMessagesBinding
    private var userTableItem: UsersTable.Item? = null
    private var dbId :String? = null
    private lateinit var customAdapter: ListOfMessagesActivityAdapter

    override fun processTimerEvent(inputDate: Date): Boolean {
        return true
    }

    override fun returnFromPreviousActivity(resultData: ActivityResult?) {
        TODO("Not yet implemented")
    }

    private fun onItemClick(
        layoutPosition: Int, eventAction: MyGestureListenerExtended.EventAction
    ) {

    }

    private fun mainBind() {
        val lastMessages = GetListOfReceivedMessages.lastMessages[userTableItem!!._id!!]
        val records = lastMessages!!.second.body.getListOfReceivedMessagesResponse.dmRecords

        customAdapter =
            ListOfMessagesActivityAdapter(records)
                { _: View?, layoutPosition: Int, _: MotionEvent?
                                              , eventAction: MyGestureListenerExtended.EventAction
                ->
                onItemClick(layoutPosition, eventAction)
            }

        val layoutManager = LinearLayoutManager(applicationContext)
        val recyclerView: RecyclerView = findViewById(R.id.activity_list_of_messages__recycler)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = customAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListOfMessagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras
        if (extras != null) {
            val userTableKey = UsersTable.Item::class.java.toString()
            if (extras.containsKey(userTableKey)) {
                userTableItem = extras.getParcelable(userTableKey)
                dbId = userTableItem?.dbId
            }
        } else {
            myFinishActivity(RESULT_OK)
        }

        dbHelper = DbHelper(this, null)

        binding.apply {
            mainBind()
        }
    }
}