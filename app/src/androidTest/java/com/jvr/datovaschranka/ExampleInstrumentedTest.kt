package com.jvr.datovaschranka

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.jvr.datovaschranka.lib.classes.TimeUtils
import com.jvr.datovaschranka.dbhelper.DbHelper
import com.jvr.datovaschranka.dbhelper.tableModel.v1.TimeTable
import com.jvr.datovaschranka.dbhelper.tableModel.v1.UsersTable
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    /*@Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.jvr.datovaschranka", appContext.packageName)
    }*/
    @Test
    fun test(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        //val app = MainActivity()
        val dbHelper = DbHelper(appContext, null)
        val userTable = dbHelper.getUserTable
        val maxId = userTable.getMaxUserId()
        println(maxId)
    }

    @Test
    fun test_UserTableInsert()
    {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val dbHelper = DbHelper(appContext, null)
        val userTable = dbHelper.getUserTable
        val newNickName = TimeUtils.currentDateTimeString(Date())
        val item  = UsersTable.Item(nickName = newNickName, testItem = true)
        val testResult = userTable.insert(item)
        assertEquals(true, testResult)
        assertNotNull(item._id)
    }

    @Test
    fun testUserTableUpdate()
    {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val dbHelper = DbHelper(appContext, null)
        val userTable = dbHelper.getUserTable
        val userTableItems = userTable.select("WHERE "+ UsersTable.COLUMN_TEST_ITEM + " = 1",1)//"WHERE nickname like 'nickName1'
        assertNotNull(userTableItems)
        if (userTableItems != null && userTableItems.isNotEmpty()) {
            val created = TimeUtils.currentDateTimeString(Date())
            userTableItems[0].mark = "New Mark : $created"
            val updateResult = userTable.update(userTableItems[0])
            assertEquals(true, updateResult)
        }
    }

    @Test
    fun test_UserTableSelect() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val dbHelper = DbHelper(appContext, null)
        val userTable = dbHelper.getUserTable
        val userTableItems = userTable.select(null,1)//"nickname like 'nickName3'"
        assertEquals(true, userTableItems != null && userTableItems.size > 0)
    }

    @Test
    fun test_UserTableSelectAll()
    {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val dbHelper = DbHelper(appContext, null)
        val userTable = dbHelper.getUserTable
        val userTableItems = userTable.selectAll()
        assertEquals(true, userTableItems != null && userTableItems.size > 0)
        userTableItems?.forEach{f ->
            println(f.toString())
        }
    }

    //////////////////////////////////////////////////////////////

    @Test
    fun test_TimeTableInsert(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val dbHelper = DbHelper(appContext, null)
        val timeTable = dbHelper.getTimeTable
        val item = TimeTable.Item(interval = 1, intervalUnit = TimeUnit.MINUTES, fkUserId = 1, testItem = true )
        val insertResult = timeTable.insert(item)
        assertEquals(true,insertResult,)
        assertNotNull(item._id)
        val timeTableItems = timeTable.selectAll()
        timeTableItems?.forEach{f ->
            println(f.toString())
        }
    }

    @Test
    fun test_TimeTableUpdate()
    {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val dbHelper = DbHelper(appContext, null)
        val timeTable = dbHelper.getTimeTable
        val timeTableItems = timeTable.select(null, 1)
        assertNotNull(timeTableItems)
        if (timeTableItems != null && timeTableItems.isNotEmpty()) {
            val created = TimeUtils.currentDateTimeString(Date())
            timeTableItems[0].mark = "New mark : $created"
            val updateResult = timeTable.update(timeTableItems[0])
            assertEquals(true, updateResult)
        }
    }

    @Test
    fun test_TimeTableSelectAll()
    {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val dbHelper = DbHelper(appContext, null)
        val timeTable = dbHelper.getTimeTable
        val timeTableItems = timeTable.selectAll()
        timeTableItems?.forEach{f ->
            println(f.toString())
        }
        assertEquals(true, timeTableItems != null && timeTableItems.size > 0)
    }

    /////////////////////////// Test time utils ///////////////////////////////////
    @Test
    fun test_TimeUtils()
    {
        val currentDateTime = TimeUtils.currentDateTime()
        val currentDateTimeString = TimeUtils.currentDateTimeString(currentDateTime)
        val getDateFromString = TimeUtils.getDateFromString(currentDateTimeString)
        assertEquals(true, currentDateTime == getDateFromString)
    }
    //////////////////////////////////////////////////////////////
    @Test
    fun test_AppSettings_findDataBox2()
    {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val dbHelper = DbHelper(appContext, null)
        val appSettingsTable = dbHelper.getAppSettingsTable
        val appSettingsTableItems = appSettingsTable.findDataBox2()
        assertNotNull(appSettingsTableItems)
    }
}