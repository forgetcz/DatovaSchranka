package com.jvr.datovaschranka.lib.classes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jvr.datovaschranka.R
import com.jvr.datovaschranka.dbhelper.tableModel.v1.UsersTable

internal class CustomAdapter(private var itemsList: List<UsersTable.Item>,
                             private var onItemClickListener: ((view: View?, position: Int) -> Unit)?) :

    RecyclerView.Adapter<CustomAdapter.TreeViewHolder>() {
    private var context : Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreeViewHolder {
        context = parent.context
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_main_account_item, parent, false)
        return TreeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TreeViewHolder, position: Int) {
        val item = itemsList[position]
        holder.lblNickName.text = item.nickName
        if (item.active) {
            holder.lblActive.text = context!!.resources.getString(R.string.account_active)
        } else {
            holder.lblActive.text = context!!.resources.getString(R.string.account_in_active)
        }
        holder.lblAccountType.text = item.testItem.toString()
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    internal inner class TreeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var lblNickName: TextView
        var lblActive : TextView
        var lblAccountType : TextView

        init {
            lblNickName = view.findViewById(R.id.activity_main_item_lblNickName)
            lblNickName.setOnClickListener{
                onItemClickListener?.invoke(it, layoutPosition)
            }

            lblActive = view.findViewById(R.id.activity_main_item_lblActive)
            lblActive.setOnClickListener{
                onItemClickListener?.invoke(it, layoutPosition)
            }

            lblAccountType = view.findViewById(R.id.activity_main_item_lblAccountType)
            lblAccountType.setOnClickListener{
                onItemClickListener?.invoke(it, layoutPosition)
            }
        }
    }
}