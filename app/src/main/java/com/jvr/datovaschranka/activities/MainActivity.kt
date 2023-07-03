package com.jvr.datovaschranka.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import com.jvr.common.lib.logger.BasicLogger
import com.jvr.common.lib.logger.ComplexLogger
import com.jvr.common.lib.logger.HistoryLogger
import com.jvr.datovaschranka.databinding.ActivityMainBinding
import com.jvr.datovaschranka.dbhelper.DbHelper
import java.util.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)//https://www.youtube.com/watch?v=JxsJxuNIcMk&list=PL79lyfcua_t7yBuRfGu5wXD7ojFKNn1vG
        setContentView(binding.root)

        binding.apply {

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

        val l =
        val locale = getLocale()
        println(locale)
        setLocale(this, "cz")

        val userModel = usersDBHelper.getUserModel
        userModel. = "Jiri"

        userModel.selectAll()
            .forEach{f ->
                println(f)
            }
        //userModel.insertUser()
        val name = "jiri 2"
        val entry = UserModel(name)
        var result = usersDBHelper.insertUser(entry)

        println(result.toString())

        var all = usersDBHelper.readAllUsers()
        all.forEach {
            println(it)
        }*/
