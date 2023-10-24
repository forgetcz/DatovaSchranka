package com.jvr.datovaschranka.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import com.jvr.common.lib.async.RunCommandAsyncKotlin
import com.jvr.datovaschranka.R
import com.jvr.datovaschranka.api.model.getOwnerInfo.GetOwnerInfoFromLogin2
import com.jvr.datovaschranka.databinding.ActivityAddNewAccountBinding
import com.jvr.datovaschranka.dbhelper.DbHelper
import com.jvr.datovaschranka.dbhelper.tableModel.v1.NamePasswordTable
import com.jvr.datovaschranka.dbhelper.tableModel.v1.UsersTable
import java.util.*

class AddNewAccountActivity : BaseActivity() {
    private lateinit var binding: ActivityAddNewAccountBinding
    private lateinit var dbHelper: DbHelper
    private lateinit var myActivityContext: AppCompatActivity
    private var userTableItem: UsersTable.Item? = null
    private var namePassTableItem: NamePasswordTable.Item? = null
    private var dbId :String? = null

    override fun processTimerEvent(inputDate: Date): Boolean {
        //TODO("Not yet implemented")
        return true
    }

    override fun returnFromPreviousActivity(resultData: ActivityResult?) {
        //TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (parent != null) {
            Log.d("", parent.toString())
        }
        dbHelper= DbHelper(this, null)

        val extras = intent.extras
        if (extras != null) {
            val userTableKey = UsersTable.Item::class.java.toString()
            if (extras.containsKey(userTableKey)) {
                userTableItem = extras.getParcelable(userTableKey)
                dbId = userTableItem?.dbId
                namePassTableItem = dbHelper.getNamePasswordTable
                    .select(NamePasswordTable.COLUMN_FK_USER_ID+ "=" + userTableItem?._id)
                    ?.first()
            }
        }

        myActivityContext = this
        binding.apply {
            fillFormData(userTableItem, namePassTableItem)

            activityAddNewAccountBtnOk.setOnClickListener {
                btnOkClick()
            }

            activityAddNewAccountBtnTest.setOnClickListener {
                btnTestClick()
            }

            activityAddNewAccountBtnCancel.setOnClickListener {
                myFinishActivity(RESULT_CANCELED)
            }

            activityAddNewAccountBtnDelete.setOnClickListener{
                btnDeleteClick()
            }
        }
    }

    private fun ActivityAddNewAccountBinding.checkInput(dbIdChek : Boolean) : String {
        //region Check values
        val okCheck : StringBuilder = StringBuilder()

        val nickName = activityAddNewAccountTxtNickName.text.toString()
        if (nickName == "") {
            val pleaseEnterNickname = resources.getString(R.string.please_enter_nickname)
            activityAddNewAccountTxtNickName.hint = pleaseEnterNickname
            activityAddNewAccountTxtNickName.error = pleaseEnterNickname
            okCheck.appendLine(pleaseEnterNickname)
        }

        val userName = activityAddNewAccountTxtUserName.text.toString()
        if (userName == "") {
            activityAddNewAccountTxtUserName.hint = "please enter user name"
            activityAddNewAccountTxtUserName.error = "please enter user name"
            okCheck.appendLine("please enter user name")
        }

        val password = activityAddNewAccountTxtPassword.text.toString()
        if (password == "") {
            activityAddNewAccountTxtPassword.hint = "please enter password"
            activityAddNewAccountTxtPassword.error = "please enter password"
            okCheck.appendLine("please enter password")
        }

        val retypePass = activityAddNewAccountTxtRetypePassword.text.toString()
        if (retypePass != password) {
            activityAddNewAccountTxtRetypePassword.hint = "password and retype password do not match"
            activityAddNewAccountTxtRetypePassword.error = "password and retype password do not match"
            okCheck.appendLine("password and retype password do not match")
        }
        //endregion

        if (dbIdChek) {
            if (dbId == null) {
                okCheck.appendLine("Not tested account !")
            }
        }
        return okCheck.toString()
    }

    private fun ActivityAddNewAccountBinding.btnOkClick() {
        val okCheck = checkInput(true)
        if (okCheck.isEmpty()) {
            val thisUserTableItem = UsersTable.Item()
            thisUserTableItem._id = userTableItem?._id
            thisUserTableItem.nickName = activityAddNewAccountTxtNickName.text.toString()
            thisUserTableItem.testItem = activityAddNewAccountCheckTestAccount.isChecked
            thisUserTableItem.dbId = dbId!!
            thisUserTableItem.active = activityAddNewAccountCheckActive.isChecked

            if (userTableItem == null) {
                dbHelper.getUserTable.insert(thisUserTableItem)
            } else {
                dbHelper.getUserTable.update(thisUserTableItem)
            }

            val thisNamePassModelTable = NamePasswordTable.Item()
            thisNamePassModelTable._id = namePassTableItem?._id
            thisNamePassModelTable.fkUserId = thisUserTableItem._id
            thisNamePassModelTable.userName = activityAddNewAccountTxtUserName.text.toString()
            thisNamePassModelTable.userPassword = activityAddNewAccountTxtPassword.text.toString()
            if (namePassTableItem == null) {
                dbHelper.getNamePasswordTable.insert(thisNamePassModelTable)
            } else {
                dbHelper.getNamePasswordTable.update(thisNamePassModelTable)
            }

            myFinishActivity(RESULT_OK)
        }
    }

    private fun ActivityAddNewAccountBinding.btnTestClick() {
        val okCheck = checkInput(false)
        if (okCheck.isEmpty()) {
            val userName = activityAddNewAccountTxtUserName.text.toString()
            val password = activityAddNewAccountTxtPassword.text.toString()
            val checkTest = activityAddNewAccountCheckTestAccount.isChecked

            RunCommandAsyncKotlin<AppCompatActivity, Any, Int>(myActivityContext
                , "Check user connection"
                , {
                    try {
                        val ownerResponse = GetOwnerInfoFromLogin2().getOwnerInfoFromLogin2(userName, password, checkTest)
                        dbId = ownerResponse?.getOwnerInfoBody?.getOwnerInfoFromLogin2Response?.getOwnerInfoDbOwnerInfo?.dbID
                        it?.runOnUiThread{
                            if (ownerResponse != null
                                    && ownerResponse.getOwnerInfoBody.getOwnerInfoFromLogin2Response.getOwnerInfoDbOwnerInfo.dbID != "") {
                                activityAddNewAccountTxtNickName.setTextColor(Color.GREEN)
                                activityAddNewAccountTxtUserName.setTextColor(Color.GREEN)
                                AlertDialog.Builder(it)
                                    .setTitle(":)") //<font color='#e9e91e'>"
                                    .setMessage(Html.fromHtml("<font color='green'>Connect ok</font>"))
                                    .setCancelable(false)
                                    .setPositiveButton("OK") {
                                            dialog: DialogInterface,
                                            _: Int -> dialog.dismiss()
                                    }
                                    .create()
                                    .show()
                            } else {
                                activityAddNewAccountTxtNickName.setTextColor(Color.RED)
                                activityAddNewAccountTxtUserName.setTextColor(Color.RED)
                                AlertDialog.Builder(it)
                                    .setTitle(":(") //<font color='#e9e91e'>"
                                    .setMessage(Html.fromHtml("<font color='red'>Connect failed</font>"))
                                    .setCancelable(false)
                                    .setPositiveButton("OK") {
                                            dialog: DialogInterface,
                                            _: Int -> dialog.dismiss()
                                    }
                                    .create()
                                    .show()
                            }
                        }
                    } catch (ex: Exception) {
                        Log.e(ex)
                    }
                }, null ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, myActivityContext)
        } else {
            AlertDialog.Builder(myActivityContext)
                .setTitle(":(") //<font color='#e9e91e'>"
                .setMessage(Html.fromHtml("<font color='red'>Not tested account !</font>"))
                .setCancelable(false)
                .setPositiveButton("OK") {
                        dialog: DialogInterface,
                        _: Int -> dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    private fun ActivityAddNewAccountBinding.btnDeleteClick() {
        yesNoDialog(true) {
            if (it) {
                if (namePassTableItem != null) {
                    dbHelper.getNamePasswordTable.delete(namePassTableItem!!)
                }
                if (userTableItem != null) {
                    dbHelper.getUserTable.delete(userTableItem!!)
                }
                myFinishActivity(RESULT_OK)
            }
        }
    }

    private fun ActivityAddNewAccountBinding.fillFormData(
        userTableItem: UsersTable.Item?,
        namePassTableItem: NamePasswordTable.Item?
    ) {
        activityAddNewAccountTxtNickName.setText(userTableItem?.nickName)
        activityAddNewAccountTxtUserName.setText(namePassTableItem?.userName)
        activityAddNewAccountTxtPassword.setText(namePassTableItem?.userPassword)
        activityAddNewAccountTxtRetypePassword.setText(namePassTableItem?.userPassword)
        activityAddNewAccountCheckTestAccount.isChecked = userTableItem?.testItem == true
        activityAddNewAccountCheckActive.isChecked = userTableItem?.active == true
    }
}
