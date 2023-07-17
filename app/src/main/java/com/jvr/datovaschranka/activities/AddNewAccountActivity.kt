package com.jvr.datovaschranka.activities

import android.os.Bundle
import androidx.activity.result.ActivityResult
import com.jvr.datovaschranka.R
import com.jvr.datovaschranka.databinding.ActivityAddNewAccountBinding
import com.jvr.datovaschranka.dbhelper.DbHelper
import com.jvr.datovaschranka.dbhelper.tableModel.NamePasswordTable
import com.jvr.datovaschranka.dbhelper.tableModel.UserTable
import java.util.*

class AddNewAccountActivity : BaseActivity() {
    private lateinit var binding: ActivityAddNewAccountBinding
    private lateinit var dbHelper: DbHelper

    private var userTableItem: UserTable.Item? = null
    private var namePassTableItem: NamePasswordTable.Item? = null

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

        dbHelper= DbHelper(this, null)

        val extras = intent.extras
        if (extras != null) {
            val userTableKey = UserTable.Item::class.java.toString()
            if (extras.containsKey(userTableKey)) {
                userTableItem = extras.getParcelable(userTableKey)
                namePassTableItem = dbHelper.getNamePasswordTable
                    .select(NamePasswordTable.COLUMN_FK_USER_ID+ "=" + userTableItem?._id)
                    ?.first()
            }
        }

        binding.apply {
            fillFormData(userTableItem, namePassTableItem)

            btnOk.setOnClickListener {
                btnOkClick()
            }

            btnCancel.setOnClickListener {
                myFinishActivity(RESULT_CANCELED)
            }
        }
    }

    private fun ActivityAddNewAccountBinding.btnOkClick() {
        //region Check values
        var okCheck = true
        val nickName = txtNickName.text.toString()
        if (nickName == "") {
            okCheck = false
            val pleaseEnterNickname = resources.getString(R.string.please_enter_nickname)
            txtNickName.hint = pleaseEnterNickname
            txtNickName.error = pleaseEnterNickname
        }

        val userName = txtUserName.text.toString()
        if (userName == "") {
            okCheck = false
            txtUserName.hint = "please enter user name"
            txtUserName.error = "please enter user name"
        }

        val password = txtPassword.text.toString()
        if (password == "") {
            okCheck = false
            txtPassword.hint = "please enter password"
            txtPassword.error = "please enter password"
        }

        val retypePass = txtRetypePassword.text.toString()
        if (retypePass != password) {
            okCheck = false
            txtRetypePassword.hint = "password and retype password do not match"
            txtRetypePassword.error = "password and retype password do not match"
        }
        //endregion

        if (okCheck) {
            val thisUserTableItem = UserTable.Item()
            thisUserTableItem._id = userTableItem?._id
            thisUserTableItem.nickName = txtNickName.text.toString()
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
            thisNamePassModelTable.testItem = chckTestAccount.isActivated
            if (namePassTableItem == null) {
                dbHelper.getNamePasswordTable.insert(thisNamePassModelTable)
            } else {
                dbHelper.getNamePasswordTable.update(thisNamePassModelTable)
            }

            myFinishActivity(RESULT_OK)
        }
    }

    private fun ActivityAddNewAccountBinding.fillFormData(
        userTableItem: UserTable.Item?,
        namePassTableItem: NamePasswordTable.Item?
    ) {
        txtNickName.setText(userTableItem?.nickName)
        txtUserName.setText(namePassTableItem?.userName)
        txtPassword.setText(namePassTableItem?.userPassword)
        txtRetypePassword.setText(namePassTableItem?.userPassword)
        if (namePassTableItem?.testItem == true) {
            chckTestAccount.isActivated = true
        }
    }
}
