package com.jvr.datovaschranka.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.jvr.common.contracts.BaseActivityClass
import com.jvr.datovaschranka.R
import java.lang.reflect.Field
import java.util.*

abstract class BaseActivity: BaseActivityClass() {

    override fun getTag(): String { return javaClass.name }

    private lateinit var timer: Timer

    //usually return true, false in case of api error to increase time for next try
    protected abstract fun processTimerEvent(inputDate: Date): Boolean
    protected abstract fun returnFromPreviousActivity(resultData: ActivityResult?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(getLayoutResourceId())
        //ButterKnife.bind(this)
        startTimer()
        //logger.d(this, "Start activity..." + this.getClass());
    }

    override fun onDestroy() {
        stopTimer()
        super.onDestroy()
        //unregisterReceiver(mbrReceiver); // Toto se deje pri logout
    }

    //region Option menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        return if (itemId == R.id.option_menu_item_logger) {
            //logger.d(this, "Selected option item: menu_item_root_logger")
            //StartNextIntent(LoggerActivity::class.java)
            true
        } else if (itemId == R.id.option_menu_item_sys_settings) {
            //logger.d(this, "Selected option item: menu_item_root_item2")
            //StartNextIntent(SysInfoActivity::class.java)
            //new LibsBuilder().start(this);
            true
        } else {
            //StartTimer()
            //logger.d(this, "Selected option item: Default...")
            super.onOptionsItemSelected(item)
        }
    }
    //endregion

    //region  Timer
    private var period: Long = 1000
    private var timerErrors = 0

    /** Zobrazuje cas na display a informace o karte a ... */
    protected open fun startTimer() {
        timer = Timer("Timer", true)
        timer.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    val date : Date = Calendar.getInstance().time
                    if (!processTimerEvent(date)) {
                        stopTimer()
                        timerErrors++
                        if (timerErrors < 5) {
                            period = (1000 * 60).toLong() // 1 minute
                        } else {
                            period = (1000 * 60 * 10).toLong() // 10 minutes
                        }
                        startTimer()
                    }
                }
            }
        }, 0, period)
    }

    protected open fun stopTimer() {
        timer.cancel()
        timer.purge()
    }
    //endregion

    protected open fun myFinishActivity(resultCode: Int) {
        stopTimer()
        val resultData = Intent()
        /*if (values != null) {
            for (x in values.indices) {
                //val oneItem: FinishActivityData = values[x]
                //resultData.putExtra(oneItem.Name, oneItem.Value) // insert your extras here
            }
        }*/
        setResult(resultCode, resultData)
        finish()
    }

    protected open fun startNextIntent(intent: Intent?) {
        stopTimer()
        someActivityResultLauncher.launch(intent)
    }

    protected open fun startNextIntent(activity: Class<*>) {
        val intent = Intent(this, activity)
        startNextIntent(intent)
    }

    /**
     * Navrat z navazujiciho intentu
     */
    private val someActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult? ->
        try {
            //val data = result?.data;
            startTimer()
            returnFromPreviousActivity(result)
        } catch (ex: Exception) {
            //logger.e(this, ex)
        }
    }

    // region table
    private fun initTopRowsInTable(tableLayout: TableLayout, columnsNames: List<String>,
                                   textColor : Int) {
        val tableRow = TableRow(this)

        columnsNames.forEach{
            val tableColumn = TextView(this)
            tableColumn.text = it
            tableColumn.height = 10
            tableColumn.setTextColor(textColor)

            tableRow.addView(tableColumn)
        }

        tableLayout.addView(tableRow)
    }

    // region fill table
    // https://stackoverflow.com/questions/16966629/what-is-the-difference-between-getfields-and-getdeclaredfields-in-java-reflectio
    private fun getField(clazz: Class<*>?, fieldName: String): Field? {
        var innerClazz = clazz
        var field: Field? = null
        while (innerClazz != null && field == null) {
            try {
                field = innerClazz.getDeclaredField(fieldName)
            } catch (e: Exception) {
            }
            innerClazz = innerClazz.superclass
        }
        return field
    }

    /**
     *
     */
    fun <T> fillTable1(tableId: Int, columnNames: List<String>, tableData: List<T>
                      , listener: View.OnClickListener?, KeyField: String?
    ) {
        //val propertyNames = UserTable.Item::class.primaryConstructor!!.parameters.map { it.name }
        //println(propertyNames)
        val textColor = Color.WHITE

        val tableLayout = findViewById<TableLayout>(tableId)
        tableLayout.removeAllViews()
        initTopRowsInTable(tableLayout, columnNames, textColor)

        for (dataField in tableData) {
            if (dataField != null) {
                val tableRow = TableRow(this)
                tableRow.layoutParams = TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
                )

                columnNames.forEach {
                    val field = getField(dataField!!::class.java, it) as Field
                    field.isAccessible = true
                    val fieldValue = field.get(dataField)

                    val text = TextView(this)
                    if (fieldValue != null) {
                        text.text = fieldValue.toString()
                    }
                    text.setTextColor(textColor)
                    text.gravity = Gravity.CENTER
                    if (listener != null) {
                        text.setOnClickListener(listener)
                    }
                    text.id = View.generateViewId()
                    text.layoutParams = TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                    )
                    tableRow.addView(text)
                }
                tableLayout.addView(tableRow)
            }
        }
    }

    //endregion
    fun <T>fillTable(tableId: Int, columnNames: List<String>, data: List<T>
                      , listener: View.OnClickListener?, KeyField: String?
    ) {
        val textColor = Color.WHITE
        val tableLayout = findViewById<TableLayout>(tableId)
        tableLayout.removeAllViews()
        initTopRowsInTable(tableLayout, columnNames, textColor)
        try {
            for (dataField in data) {
                val tableRow = TableRow(this)
                tableRow.layoutParams = TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
                )

                val clazz1: Class<*> = dataField!!::class.java
                val clazz2 = dataField.javaClass

                var keyValue: Any? = null
                if (KeyField != null) {
                    val keyField = clazz1.getField(KeyField)
                    keyValue = keyField[dataField]
                }
                for (columnName in columnNames.indices) {
                    val fieldName = columnNames[columnName]
                    val field1 =
                        clazz2.getField(fieldName) //Note, this can throw an exception if the field doesn't exist.
                    val fieldValue = field1[dataField]

                    val tv = TextView(this)
                    if (fieldValue != null) {
                        tv.text = fieldValue.toString()
                    }
                    tv.setTextColor(Color.WHITE)
                    tv.gravity = Gravity.CENTER
                    tv.setOnClickListener(listener)
                    tv.id = View.generateViewId()
                    if (keyValue != null) {
                        tv.tag = keyValue
                    }
                    tv.layoutParams = TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                    )
                    tableRow.addView(tv)
                }
                tableLayout.addView(tableRow)
            }
        } catch (ex: java.lang.Exception) {
            logger.e(this, ex)
        }
    }

    //endregion

    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    /*private val isoDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.getDefault())
    protected fun currentDateTime(): Date {
        return Date()
    }
    protected fun currentDateTimeString(): String {
        return isoDateFormat.format(Date())
    }*/

}