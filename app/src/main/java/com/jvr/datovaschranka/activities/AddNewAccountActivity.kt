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

            btnOk.setOnClickListener {
                btnOkClick()
            }

            btnTest.setOnClickListener {
                btnTestClick()
            }

            btnCancel.setOnClickListener {
                myFinishActivity(RESULT_CANCELED)
            }

            btnDelete.setOnClickListener{
                btnDeleteClick()
            }
        }
    }

    private fun ActivityAddNewAccountBinding.checkInput(dbIdChek : Boolean) : String {
        //region Check values
        val okCheck : StringBuilder = StringBuilder()

        val nickName = txtNickName.text.toString()
        if (nickName == "") {
            val pleaseEnterNickname = resources.getString(R.string.please_enter_nickname)
            txtNickName.hint = pleaseEnterNickname
            txtNickName.error = pleaseEnterNickname
            okCheck.appendLine(pleaseEnterNickname)
        }

        val userName = txtUserName.text.toString()
        if (userName == "") {
            txtUserName.hint = "please enter user name"
            txtUserName.error = "please enter user name"
            okCheck.appendLine("please enter user name")
        }

        val password = txtPassword.text.toString()
        if (password == "") {
            txtPassword.hint = "please enter password"
            txtPassword.error = "please enter password"
            okCheck.appendLine("please enter password")
        }

        val retypePass = txtRetypePassword.text.toString()
        if (retypePass != password) {
            txtRetypePassword.hint = "password and retype password do not match"
            txtRetypePassword.error = "password and retype password do not match"
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
            thisUserTableItem.nickName = txtNickName.text.toString()
            thisUserTableItem.testItem = chckTestAccount.isChecked
            thisUserTableItem.dbId = dbId!!
            
            if (userTableItem == null) {
                dbHelper.getUserTable.insert(thisUserTableItem)
            } else {
                dbHelper.getUserTable.update(thisUserTableItem)
            }

            val thisNamePassModelTable = NamePasswordTable.Item()
            thisNamePassModelTable._id = namePassTableItem?._id
            thisNamePassModelTable.fkUserId = thisUserTableItem._id
            thisNamePassModelTable.userName = txtUserName.text.toString()
            thisNamePassModelTable.userPassword = txtPassword.text.toString()
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
            val userName = txtUserName.text.toString()
            val password = txtPassword.text.toString()
            val checkTest = chckTestAccount.isChecked

            RunCommandAsyncKotlin<AppCompatActivity, Any, Int>(myActivityContext
                , "Check user connection"
                , {
                    try {
                        val ownerResponse = GetOwnerInfoFromLogin2().getOwnerInfoFromLogin2(userName, password, checkTest)
                        dbId = ownerResponse?.getOwnerInfoBody?.getOwnerInfoFromLogin2Response?.getOwnerInfoDbOwnerInfo?.dbID
                        it?.runOnUiThread{
                            if (ownerResponse != null
                                    && ownerResponse.getOwnerInfoBody.getOwnerInfoFromLogin2Response.getOwnerInfoDbOwnerInfo.dbID != "") {
                                txtNickName.setTextColor(Color.GREEN)
                                txtUserName.setTextColor(Color.GREEN)
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
                                txtNickName.setTextColor(Color.RED)
                                txtUserName.setTextColor(Color.RED)
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
                        Log.e(logger.getTag(), ex.message!!)
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
        txtNickName.setText(userTableItem?.nickName)
        txtUserName.setText(namePassTableItem?.userName)
        txtPassword.setText(namePassTableItem?.userPassword)
        txtRetypePassword.setText(namePassTableItem?.userPassword)
        chckTestAccount.isChecked = userTableItem?.testItem == true
    }
}
