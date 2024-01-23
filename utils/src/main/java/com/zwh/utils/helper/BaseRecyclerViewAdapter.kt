package com.zwh.utils.helper

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * D 数据类型
 */
abstract class BaseRecyclerViewAdapter<D, V : ViewDataBinding> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var context: Context

    protected var data = arrayListOf<D>()
    open fun setNewData(list: ArrayList<D>) {
        data = list
        notifyDataSetChanged()
    }

    open fun addData(list: ArrayList<D>) {
        data.addAll(list)
        notifyDataSetChanged()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    abstract fun getItemId(): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate =
            DataBindingUtil.inflate<V>(LayoutInflater.from(context),/* layoutId = */
                getItemId(),/* parent = */
                parent,/* attachToParent = */
                false)
        return object : RecyclerView.ViewHolder(inflate.root) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<V>(holder.itemView)
        if (binding != null) {
            handleItemData(binding, position)
            binding.executePendingBindings()
        }
    }

    abstract fun handleItemData(binding: V, position: Int)
    override fun getItemCount() = data.size

}