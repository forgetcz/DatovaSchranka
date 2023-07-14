package com.jvr.datovaschranka.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.result.ActivityResult
import com.jvr.common.lib.logger.BasicLogger
import com.jvr.common.lib.logger.ComplexLogger
import com.jvr.common.lib.logger.HistoryLogger
import com.jvr.datovaschranka.R
import com.jvr.datovaschranka.databinding.ActivityMainBinding
import com.jvr.datovaschranka.dbhelper.DbHelper
import com.jvr.datovaschranka.dbhelper.tableModel.UserTable
import java.util.*

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DbHelper
    private lateinit var usersList : ArrayList<UserTable.Item>

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
        //TODO("Not yet implemented")
        val data = resultData?.data
        if (data != null) {
            logger.i(getTag(), "Back$data")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)//https://www.youtube.com/watch?v=JxsJxuNIcMk&list=PL79lyfcua_t7yBuRfGu5wXD7ojFKNn1vG
        setContentView(binding.root)

        dbHelper = DbHelper(this, null)

        binding.apply {
            val users = dbHelper.getUserTable.selectAll()

            if (users != null) {
                usersList = users
                fillTable(
                    tableId = R.id.table_AccountList, columnNames = listOf("id", "nickName")
                    , tableData = usersList,   listener =  { view1 -> tableCellProcessClick(view1.id) }
                    , "id")
            }

            btnActivityMainAddNewAccount.setOnClickListener {
                val nextIntent = Intent(applicationContext, AddNewAccountActivity::class.java)
                startNextIntent(nextIntent)
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
                    val userTableItem = usersList.first{ f -> f.id == key.toInt()}

                    val nextIntent = Intent(applicationContext, AddNewAccountActivity::class.java)
                    nextIntent.putExtra(UserTable.Item::class.java.toString(), userTableItem)
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
