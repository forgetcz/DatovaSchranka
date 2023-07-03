package com.jvr.datovaschranka

import androidx.appcompat.app.AppCompatActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.jvr.datovaschranka.activities.BaseActivity
import com.jvr.datovaschranka.activities.MainActivity
import com.jvr.datovaschranka.constatns.Utils
import com.jvr.datovaschranka.dbhelper.DbHelper
import com.jvr.datovaschranka.dbhelper.tableModel.TimeTable
import com.jvr.datovaschranka.dbhelper.tableModel.UserTable
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
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
        val newNickName = Utils().currentDateTimeString()
        val item  = UserTable.Item(nickName = newNickName, testItem = true)
        val testResult = userTable.insert(item)
        assertEquals(true, testResult)
        assertNotNull(item.id)
    }

    @Test
    fun testUserTableUpdate()
    {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val dbHelper = DbHelper(appContext, null)
        val userTable = dbHelper.getUserTable
        val userTableItems = userTable.select("WHERE testItem = 1",1)//"WHERE nickname like 'nickName1'
        assertNotNull(userTableItems)
        if (userTableItems != null && userTableItems.isNotEmpty()) {
            val created = Utils().currentDateTimeString()
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
        assertNotNull(item.id)
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
            val created = Utils().currentDateTimeString()
            timeTableItems[0].mark = "New Mark : $created"
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
}