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
    fun <T : Any> fillTable(tableId: Int, columnNames: List<String>, tableData: List<T>
                      , listener: View.OnClickListener?, KeyField: String?
    ) {
        //val propertyNames = UserTable.Item::class.primaryConstructor!!.parameters.map { it.name }
        //println(propertyNames)
        val textColor = Color.WHITE

        val tableLayout = findViewById<TableLayout>(tableId)
        tableLayout.removeAllViews()

        val tableRowHead = TableRow(this)
        columnNames.withIndex().forEach{
            val tableColumn = TextView(this)
            tableColumn.text = it.value
            tableColumn.setTextColor(textColor)
            //tableColumn.height = 10
            when (it.index) {
                0 -> {
                    tableColumn.gravity = Gravity.LEFT
                }
                columnNames.size - 1 -> {
                    tableColumn.gravity = Gravity.RIGHT
                }
                else -> {
                    tableColumn.gravity = Gravity.CENTER
                }
            }
            tableColumn.id = View.generateViewId()
            tableColumn.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )

            tableRowHead.addView(tableColumn)
        }
        tableLayout.addView(tableRowHead)

        for (dataField in tableData) {
            val tableRow = TableRow(this)
            tableRow.layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )

            var keyValue: Any? = null
            if (KeyField != null) {
                val keyField = getField(dataField::class.java, KeyField) as Field
                keyField.isAccessible = true
                keyValue = keyField[dataField]
            }

            columnNames.forEach {
                val field = getField(dataField::class.java, it) as Field
                field.isAccessible = true
                val fieldValue = field.get(dataField)

                val tableColumn = TextView(this)
                if (fieldValue != null) {
                    tableColumn.text = fieldValue.toString()
                }
                tableColumn.setTextColor(textColor)
                tableColumn.gravity = Gravity.CENTER
                if (listener != null) {
                    tableColumn.setOnClickListener(listener)
                }
                tableColumn.id = View.generateViewId()
                tableColumn.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )

                if (keyValue != null) {
                    tableColumn.tag = keyValue
                }

                tableRow.addView(tableColumn)

            }
            tableLayout.addView(tableRow)
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