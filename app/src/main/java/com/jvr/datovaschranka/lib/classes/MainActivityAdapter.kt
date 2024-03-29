package com.jvr.datovaschranka.lib.classes

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Html
import android.view.*
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.jvr.datovaschranka.R
import com.jvr.datovaschranka.dbhelper.tableModel.v1.UsersTable


internal class MainActivityAdapter(private var itemsList: MutableList<UsersTable.UserItemWithMessageData>,
                                   private var onItemClickListener: ((
                                 view: View?, layoutPosition: Int, motionEvent: MotionEvent?
                                , eventAction : MyGestureListenerExtended.EventAction) -> Unit)?) :

    RecyclerView.Adapter<MainActivityAdapter.TreeViewHolder>() {
    private var context : Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreeViewHolder {
        context = parent.context
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_main_account_item, parent, false)
        return TreeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TreeViewHolder, position: Int) {
        val item = itemsList[position]
        var sourceString  = "${item.original.nickName} (Unread: <b>${item.receivedItemsUnread}</b>" +
                "/ read: ${item.receivedItemsRead} / sent ${item.sentItems})"
        val htmlText  = Html.fromHtml(sourceString)
        holder.lblNickName.setText(htmlText)

        /*if (item.active) {
            holder.lblActive.text = context!!.resources.getString(R.string.account_active)
        } else {
            holder.lblActive.text = context!!.resources.getString(R.string.account_in_active)
        }*/

        if (!item.original.active) {
            holder.itemLayout.setBackgroundColor(Color.parseColor("#af978e"))
        } else if (!item.original.testItem){
            holder.itemLayout.setBackgroundColor(Color.parseColor("#62c483"))
        } else {
            holder.itemLayout.setBackgroundColor(Color.parseColor("#858ced"))
        }
        //holder.lblAccountType.text = item.testItem.toString()
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    // https://stackoverflow.com/questions/45054908/how-to-add-a-gesture-detector-to-a-view-in-android
    @SuppressLint("ClickableViewAccessibility")
    internal inner class TreeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var touchListener: View.OnTouchListener = object : View.OnTouchListener {
            //var mDetector: GestureDetector = GestureDetector(context, MyGestureListener())
            val gestureDetector = GestureDetector(
                context, MyGestureListenerExtended { eventAction : MyGestureListenerExtended.EventAction
                                                     , motionEvent: MotionEvent?
                    ->
                    run {
                        /*Log.d(
                            "",
                            view.id.toString() + ":" + eventAction.toString() + motionEvent?.toString()
                        )*/
                        onItemClickListener?.invoke(view, layoutPosition, motionEvent, eventAction)
                    }
                }
            )

            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return gestureDetector.onTouchEvent(event)
            }
        }

        var lblNickName: TextView
        var itemLayout : ConstraintLayout

        init {
            lblNickName = view.findViewById(R.id.activity_main_item__lblNickName)
            lblNickName.setOnTouchListener(touchListener)

            itemLayout = view.findViewById(R.id.activity_main_item__layout)
            itemLayout.setOnTouchListener(touchListener)

            /*lblAccountType = view.findViewById(R.id.activity_main_item_lblAccountType)
            lblAccountType.setOnClickListener{
                onItemClickListener?.invoke(it, layoutPosition)
            }*/
        }
    }
}