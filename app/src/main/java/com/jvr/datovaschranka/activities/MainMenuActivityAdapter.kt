package com.jvr.datovaschranka.activities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jvr.datovaschranka.R
import com.jvr.datovaschranka.dbhelper.tableModel.v1.UsersTable

class MainMenuActivityAdapter(
    parentContext: Context?,
    layoutId: Int,
    menuList: ArrayList<UsersTable.Item>?,
    onItemClickListener: ((view: View?, position: Int) -> Unit)?
) :
    RecyclerView.Adapter<MainMenuActivityAdapter.TreeViewHolder>() {
    private val listItemLayout: Int
    private val menuList: ArrayList<UsersTable.Item>?
    private val mInflater: LayoutInflater
    private val mClickListener: ((view: View?, position: Int) -> Unit)?

    // specify the row layout file and click for each row
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TreeViewHolder {
        val view = mInflater.inflate(
            listItemLayout,
            parent,
            false
        ) //LayoutInflater.from(parent.getContext())
        return TreeViewHolder(view)
    }

    // load data in each row element
    override fun onBindViewHolder(holder: TreeViewHolder, position: Int) {
        val menu: UsersTable.Item = menuList!![position]
        //var text = (position + 1).toString() + ". " + menu.ModuleName
        /*if (menu.getTerminalAction() === enums.TerminalAction.Terminal_Forward) {
            text += " -> ..."
        } else if (menu.getTerminalAction() === enums.TerminalAction.Terminal_Back) {
            text += " <-"
        }*/
        holder.lblNickName.text = menu.nickName
        if (menu.testItem) {
            holder.checkActive.text = "Active"
        } else {
            holder.checkActive.text = "In Active"
        }
    }

    // get the size of the list
    override fun getItemCount(): Int {
        return this.menuList?.size ?: 0
    }

    inner class TreeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var lblNickName: TextView
        val checkActive: TextView

        override fun onClick(view: View) {
            mClickListener?.invoke(view, layoutPosition)
            //Log.d("onclick", "onClick $position")
        }

        init {
            itemView.setOnClickListener(this)
            lblNickName = itemView.findViewById(R.id.activity_main_item_lblNickName)
            lblNickName.setOnClickListener { onClick(it) }

            checkActive = itemView.findViewById(R.id.activity_main_item_lblActive)
            checkActive.setOnClickListener { onClick(it) }
        }
    }

    init {
        mInflater = LayoutInflater.from(parentContext)
        listItemLayout = layoutId
        this.menuList = menuList
        mClickListener = onItemClickListener
    }
}
