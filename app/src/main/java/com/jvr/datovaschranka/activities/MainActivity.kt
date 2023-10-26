package com.jvr.datovaschranka.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jvr.common.lib.logger.BasicLogger
import com.jvr.common.lib.logger.ComplexLogger
import com.jvr.common.lib.logger.HistoryLogger
import com.jvr.datovaschranka.R
import com.jvr.datovaschranka.api.model.ApiEnums
import com.jvr.datovaschranka.api.model.getListOfReceivedMessages.GetListOfReceivedMessages
import com.jvr.datovaschranka.api.model.getListOfSentMessages.GetListOfSentMessages
import com.jvr.datovaschranka.databinding.ActivityMainBinding
import com.jvr.datovaschranka.dbhelper.DbHelper
import com.jvr.datovaschranka.dbhelper.tableModel.v1.UsersTable
import com.jvr.datovaschranka.lib.classes.MainActivityAdapter
import com.jvr.datovaschranka.lib.classes.MyGestureListenerExtended
import com.jvr.datovaschranka.services.NotificationService
import java.util.*

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DbHelper
    private lateinit var usersList : ArrayList<UsersTable.Item>
    private var serviceStatus : Boolean = false
    private lateinit var customAdapter: MainActivityAdapter

    override val Log: ComplexLogger = ComplexLogger(
        this.javaClass.name,mutableListOf(
            BasicLogger(), HistoryLogger()
        )
    )

    override fun processTimerEvent(inputDate: Date): Boolean {
        mainBind()
        return true
        /*try {

        } catch (ex: Exception) {

        }*/

    }

    override fun returnFromPreviousActivity(resultData: ActivityResult?) {
        val resultCode = resultData?.resultCode
        if (resultCode == RESULT_CANCELED) {
            return
        } else {
            mainBind()
        }
    }

    private fun onItemClick(
        layoutPosition: Int, eventAction: MyGestureListenerExtended.EventAction
    ) {
        if (eventAction == MyGestureListenerExtended.EventAction.onDoubleTap) {
            val currentSelectedUser = usersList[layoutPosition]
            val nextIntent = Intent(applicationContext, AddNewAccountActivity::class.java)
            nextIntent.putExtra(UsersTable.Item::class.java.toString(), currentSelectedUser)
            startNextIntent(nextIntent)
        } else if (eventAction == MyGestureListenerExtended.EventAction.onSingleTapConfirmed) {
            val currentSelectedUser = usersList[layoutPosition]
            val nextIntent = Intent(applicationContext, ListOfMessagesActivity::class.java)
            nextIntent.putExtra(UsersTable.Item::class.java.toString(), currentSelectedUser)
            startNextIntent(nextIntent)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // https://www.youtube.com/watch?v=JxsJxuNIcMk&list=PL79lyfcua_t7yBuRfGu5wXD7ojFKNn1vG
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        //val curFormatter = SimpleDateFormat("dd/MM/yyyy")
        //val dateObj = curFormatter.parse("14/09/2023")
        /*GetListOfReceivedMessages().getListOfReceivedMessages(
            1, "h63c6h", "5CPOMFtsrX8yfejMnKlO9A", true
            , DsApi.addDay(Date(), -1000)!!, Date())
        */

        /*GetListOfSentMessages().getListOfSentMessages(
            1, "h63c6h", "5CPOMFtsrX8yfejMnKlO9A", true
            , DsApi.addDay(Date(), -1000)!!, Date())*/
        dbHelper = DbHelper(this, null)

        binding.apply {
            mainBind()

            activityMainBtnAddNewAccount.setOnClickListener {
                val nextIntent = Intent(applicationContext, AddNewAccountActivity::class.java)
                startNextIntent(nextIntent)
            }

            activityMainBtnStartStopService.text = "Start service"
            activityMainBtnStartStopService.setOnClickListener{
                startStopService(it)
            }

            startStopService(activityMainBtnStartStopService)
        }
    }

    private fun mainBind() {
        val users = dbHelper.getUserTable.selectAll()

        if (users != null && users.size > 0) {
            usersList = users
            val lastReceivedMessages = GetListOfReceivedMessages.lastMessages
            val lastSentMessages = GetListOfSentMessages.lastMessages
            val valuedList : MutableList<UsersTable.UserItemWithMessageData> = mutableListOf()

            usersList.forEach{
                val userId = it._id
                val newData : UsersTable.UserItemWithMessageData = UsersTable.UserItemWithMessageData(it, 0, 0,0)
                newData.original = it

                val lastReceivedMessageForUser = lastReceivedMessages[userId]
                if (lastReceivedMessageForUser != null) {
                    val messages = lastReceivedMessageForUser.second.body.getListOfReceivedMessagesResponse.dmRecords

                    val othersMessage = messages.filter { otherIt ->
                        otherIt.translatedDmMessageStatus != ApiEnums.MessageStatus.Prectena
                    }
                    newData.receivedItemsUnread = othersMessage.size

                    val readMessages = messages.filter { readedIt ->
                        readedIt.translatedDmMessageStatus == ApiEnums.MessageStatus.Prectena
                    }
                    newData.receivedItemsRead = readMessages.size
                }

                val lastSentMessagesForUser = lastSentMessages[userId]
                if (lastSentMessagesForUser != null) {
                    newData.sentItems = lastSentMessagesForUser.second.body.getListOfSentMessagesResponse.dmRecords.size
                }

                valuedList.add(newData)
            }

            customAdapter =
                MainActivityAdapter(valuedList) { _: View?, layoutPosition: Int, _: MotionEvent?
                                                  , eventAction: MyGestureListenerExtended.EventAction
                    ->
                    onItemClick(layoutPosition, eventAction)
                }

            val layoutManager = LinearLayoutManager(applicationContext)
            val recyclerView: RecyclerView = findViewById(R.id.activity_main__userRecycler)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = customAdapter
        }
    }

    private fun startStopService(it: View?) {
        if (!serviceStatus) {
            serviceStatus = true
            (it as Button).text = "Stop service"
            val serviceToStart = Intent(applicationContext, NotificationService::class.java)
            serviceToStart.putExtra("Par1", "Val1")
            startService(serviceToStart)
        } else {
            serviceStatus = false
            (it as Button).text = "Start service"
            stopService(Intent(this@MainActivity, NotificationService::class.java))
        }
    }

    /*private fun tableCellProcessClick(viewId: Int) {
        try {
            val tv: TextView = findViewById(viewId)
            val trow = tv.parent as TableRow
            val keyRow = trow.getChildAt(0) as TextView
            val tag = keyRow.tag
            if (tag != null) {
                val key = keyRow.tag.toString()
                if (key.isNotEmpty()) {
                    val userTableItem = usersList.first{ f -> f._id == key.toInt()}

                    val nextIntent = Intent(applicationContext, AddNewAccountActivity::class.java)
                    nextIntent.putExtra(UsersTable.Item::class.java.toString(), userTableItem)
                    startNextIntent(nextIntent)
                }
            }
        } catch (ex: java.lang.Exception) {
            logger.e(this, ex)
        }
    }*/
}
    /*
        val viewModel: DbViewModel = ViewModelProvider(storeOwner)[DbViewModel::class.java]
        viewModel.getUserTableHash.observe(storeOwner) {
            //userTable.modelVersion(viewModel)
            println("$it----------------------------")
        }

        val locale = getLocale()
        println(locale)
        setLocale(this, "cz")
     */
