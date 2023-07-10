package com.jvr.datovaschranka.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import com.jvr.common.lib.logger.BasicLogger
import com.jvr.common.lib.logger.ComplexLogger
import com.jvr.common.lib.logger.HistoryLogger
import com.jvr.datovaschranka.R
import com.jvr.datovaschranka.databinding.ActivityMainBinding
import com.jvr.datovaschranka.dbhelper.DbHelper
import com.jvr.datovaschranka.dbhelper.tableModel.UserTable
import java.lang.reflect.Field
import java.util.*
import kotlin.reflect.full.primaryConstructor

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DbHelper

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

    private fun getField(clazz: Class<*>?, fieldName: String): Field? {
        var innerClazz = clazz
        var field: Field? = null
        while (clazz != null && field == null) {
            try {
                field = clazz.getDeclaredField(fieldName)
            } catch (e: Exception) {
            }
            innerClazz = clazz.superclass
        }
        return field
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)//https://www.youtube.com/watch?v=JxsJxuNIcMk&list=PL79lyfcua_t7yBuRfGu5wXD7ojFKNn1vG
        setContentView(binding.root)

        dbHelper= DbHelper(this, null)

        binding.apply {
            val users = dbHelper.getUserTable.selectAll()
            /*if (users != null) {
                for (dataField in users) {

                    val id = getField(dataField::class.java, "id")
                    if (id != null){
                        id.isAccessible = true
                        println(id.get(dataField))
                    }
                }
            }*/

            if (users != null) {
                fillTable1(R.id.table_AccountList, listOf("id", "nickName")
                    , users, null, null)
            }
            btnActivityMainAddNewAccount.setOnClickListener {
                val nextIntent = Intent(applicationContext, AddNewAccountActivity::class.java)
                //val iItem = UserModel.Item(1,"","","")
                //nextIntent.putExtra(UserModel.Item::class.java.toString())
                startNextIntent(nextIntent)
            }
        }
    }
}
    /*dbHelper = DbHelper(this, null)
        val storeOwner = this@MainActivity
        val viewModel: DbViewModel = ViewModelProvider(storeOwner)[DbViewModel::class.java]
        viewModel.getUserTableHash.observe(storeOwner) {
            //userTable.modelVersion(viewModel)
            println("$it----------------------------")
        }

        val locale = getLocale()
        println(locale)
        setLocale(this, "cz")

        }*/
