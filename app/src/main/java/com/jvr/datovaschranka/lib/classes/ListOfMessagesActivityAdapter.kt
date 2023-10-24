package com.jvr.datovaschranka.lib.classes

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html
import android.view.*
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.jvr.datovaschranka.R
import com.jvr.datovaschranka.api.model.getListOfReceivedMessages.GetListOfReceivedMessagesResponseDmRecords

internal class ListOfMessagesActivityAdapter(private var itemsList : List<GetListOfReceivedMessagesResponseDmRecords>,
                                             private var onItemClickListener: ((
                                 view: View?, layoutPosition: Int, motionEvent: MotionEvent?
                                , eventAction : MyGestureListenerExtended.EventAction) -> Unit)?) :

    RecyclerView.Adapter<ListOfMessagesActivityAdapter.TreeViewHolder>() {
    private var context : Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreeViewHolder {
        context = parent.context
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_list_of_messages_item, parent, false)
        return TreeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TreeViewHolder, position: Int) {
        val item = itemsList[position]
        val sourceString  = "Popis : ${item.dmAnnotation})"
        val htmlText  = Html.fromHtml(sourceString)

        holder.lblMessageDescription.setText(htmlText)
        holder.lblMessageDate.text = item.dmMessageStatus?.toString()
        /*
        if (item.active) {
            holder.lblActive.text = context!!.resources.getString(R.string.account_active)
        } else {
            holder.lblActive.text = context!!.resources.getString(R.string.account_in_active)
        }

        if (!item.original.active) {
            holder.itemLayout.setBackgroundColor(Color.parseColor("#af978e"))
        } else if (!item.original.testItem){
            holder.itemLayout.setBackgroundColor(Color.parseColor("#62c483"))
        } else {
            holder.itemLayout.setBackgroundColor(Color.parseColor("#858ced"))
        }*/
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

        var lblMessageDescription: TextView
        var lblMessageDate1: TextView
        var lblMessageDate : TextView

        init {
            lblMessageDescription = view.findViewById(R.id.activity_list_of_messages__lblMessageDescription)
            lblMessageDescription.setOnTouchListener(touchListener)

            lblMessageDate = view.findViewById(R.id.activity_list_of_messages__lblMessageDate)
            lblMessageDate.setOnTouchListener(touchListener)

            lblMessageDate1 = view.findViewById(R.id.activity_list_of_messages__lblMessageDate1)
            lblMessageDate1.setOnTouchListener(touchListener)

        }
    }
}