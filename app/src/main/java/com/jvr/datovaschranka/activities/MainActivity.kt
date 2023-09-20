package com.jvr.datovaschranka.activities

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.View
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jvr.common.lib.logger.BasicLogger
import com.jvr.common.lib.logger.ComplexLogger
import com.jvr.common.lib.logger.HistoryLogger
import com.jvr.datovaschranka.R
import com.jvr.datovaschranka.lib.classes.CustomAdapter
import com.jvr.datovaschranka.databinding.ActivityMainBinding
import com.jvr.datovaschranka.dbhelper.DbHelper
import com.jvr.datovaschranka.dbhelper.tableModel.v1.UsersTable
import com.jvr.datovaschranka.lib.services.NotificationService
import java.util.*

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DbHelper
    private lateinit var usersList : ArrayList<UsersTable.Item>
    private var serviceStatus : Boolean = false
    private lateinit var customAdapter: CustomAdapter

    override val logger: ComplexLogger = ComplexLogger(
        listOf(
            BasicLogger(), HistoryLogger()
        )
    )

    override fun processTimerEvent(inputDate: Date): Boolean {
        try {

        } catch (ex: Exception) {

        }
        return true
    }

    override fun returnFromPreviousActivity(resultData: ActivityResult?) {
        val resultCode = resultData?.resultCode
        if (resultCode == RESULT_CANCELED) {
            return
        } else {
            val data = resultData?.data
            if (data != null) {
                logger.d(getTag(), "Back $data")
                val users = dbHelper.getUserTable.selectAll()
                if (users != null) {
                    usersList = users
                    val recyclerView: RecyclerView = findViewById(R.id.activity_main_user_list_recycler)
                    customAdapter = CustomAdapter(usersList) { view: View?, position: Int
                        -> onItemClick(view, position) }
                    recyclerView.adapter = customAdapter
                //customAdapter.notifyDataSetChanged()
                    // prepareItems(10)
                    /*val menuAdapter = MainMenuActivityAdapter(
                        this, R.layout.activity_main_account_item, usersList
                    ) { view: View?, position: Int -> onItemClick(view!!, position) }

                    val recyclerView: RecyclerView = findViewById(R.id.main_menu_item_list)
                    recyclerView.layoutManager = LinearLayoutManager(this)
                    recyclerView.itemAnimator = DefaultItemAnimator()
                    recyclerView.adapter = menuAdapter
*/
                    /*fillTable(
                        tableId = R.id.table_AccountList, columnNames = listOf(
                            UsersTable.COLUMN_ID
                            , UsersTable.COLUMN_NICK_NAME), tableData = usersList
                        ,   listener = { view1 -> tableCellProcessClick(view1.id) }
                        , UsersTable.COLUMN_ID)*/
                }
            }
        }
    }

    /*private fun onItemClick(iview:View, position: Int ) {
        val l = iview.findViewById<TextView>(R.id.activity_main_item_lblNickName)
        Log.d("", l.text.toString())
        Log.d(getTag(),position.toString())
    }*/

    private fun onItemClick(iview: View?, position: Int ) {
        val currentSelectedUser = usersList[position]
        val nextIntent = Intent(applicationContext, AddNewAccountActivity::class.java)
        nextIntent.putExtra(UsersTable.Item::class.java.toString(), currentSelectedUser)
        startNextIntent(nextIntent)
    }

    /*private fun prepareItems(count : Int) {
        for (i in 1..count) {
            itemsList.add("Item $i")
        }
        customAdapter.notifyDataSetChanged()
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // https://www.youtube.com/watch?v=JxsJxuNIcMk&list=PL79lyfcua_t7yBuRfGu5wXD7ojFKNn1vG
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        //logger.d(getTag(), TimeUtils.currentDateTimeString())
        /*val testXml = "<?xml version='1.0' encoding='utf-8'?>\n" +
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n" +
                "    <SOAP-ENV:Body>\n" +
                "        <p:GetOwnerInfoFromLogin2Response xmlns:p=\"http://isds.czechpoint.cz/v20\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "            <p:dbOwnerInfo>\n" +
                "                <p:dbID>jptjjj9</p:dbID>\n" +
                "                <p:aifoIsds>false</p:aifoIsds>\n" +
                "                <p:dbType>FO</p:dbType>\n" +
                "                <p:ic xsi:nil=\"true\"></p:ic>\n" +
                "                <p:pnGivenNames>Jiri</p:pnGivenNames>\n" +
                "                <p:pnLastName>Vrabec</p:pnLastName>\n" +
                "            </p:dbOwnerInfo>\n" +
                "            <p:dbStatus>\n" +
                "                <p:dbStatusCode>0000</p:dbStatusCode>\n" +
                "                <p:dbStatusMessage>Provedeno úspěšně.</p:dbStatusMessage>\n" +
                "            </p:dbStatus>" +
                "        </p:GetOwnerInfoFromLogin2Response>\n" +
                "    </SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>";

        DsApi().fromString(testXml)*/
        dbHelper = DbHelper(this, null)

        binding.apply {
            val users = dbHelper.getUserTable.selectAll()



            if (users != null && users.size > 0) {
                usersList = users

                customAdapter = CustomAdapter(usersList) { view: View?, position: Int
                    -> onItemClick(view, position) }

                val layoutManager = LinearLayoutManager(applicationContext)
                val recyclerView: RecyclerView = findViewById(R.id.activity_main_user_list_recycler)
                //recyclerView.itemAnimator = DefaultItemAnimator()
                recyclerView.layoutManager = layoutManager
                recyclerView.adapter = customAdapter
                //customAdapter.notifyDataSetChanged()

                //prepareItems(5)
                /*val menuAdapter = MainMenuActivityAdapter(
                    s, R.layout.activity_main_account_item, usersList
                ) { view: View?, position: Int -> onItemClick(view!!, position) }

                val recyclerView: RecyclerView = findViewById(R.id.main_menu_item_list)
                recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                recyclerView.itemAnimator = DefaultItemAnimator()
                recyclerView.adapter = menuAdapter
                */
                /*fillTable(

                    tableId = R.id.table_AccountList,
                    columnNames = listOf(
                        UsersTable.COLUMN_ID, UsersTable.COLUMN_NICK_NAME
                    ),
                    tableData = usersList,
                    listener = { view1 -> tableCellProcessClick(view1.id) },
                    UsersTable.COLUMN_ID)*/
            }

            // val fileContent = this::class.java.getResource("/html/file.html").readText()
            btnActivityMainAddNewAccount.setOnClickListener {
                val nextIntent = Intent(applicationContext, AddNewAccountActivity::class.java)
                startNextIntent(nextIntent)
            }

            btnStartStopService.setOnClickListener{
                if (!serviceStatus) {
                    (it as Button).text = "Start service"
                    val i = Intent(this@MainActivity, NotificationService::class.java)
                    //i.putExtra("Par1","Val1")
                    startService(i)
                }
                else {
                    (it as Button).text = "Stop service"
                    stopService(Intent(this@MainActivity, NotificationService::class.java))
                }
            }
        }
    }

    private fun tableCellProcessClick(viewId: Int) {
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
    }
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
