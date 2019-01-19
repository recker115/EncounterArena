package com.example.shantunu.encounterarena.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.shantunu.encounterarena.R
import com.example.shantunu.encounterarena.models.EachNotification
import com.example.shantunu.encounterarena.views.viewHolders.NotificationViewHolder

class RvPagedAdapter(context: Context) : PagedListAdapter<EachNotification, NotificationViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.notification_item, parent, false)
        return NotificationViewHolder(layoutView)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        var item = getItem(position)
        holder.tvRoomId.text = item?.roomId
        holder.tvTitle.text = item?.title
        holder.tvRoomPass.text = item?.password
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EachNotification>() {
            override fun areItemsTheSame(oldNotificationEntity: EachNotification, newNotificationEntity: EachNotification): Boolean {
                return oldNotificationEntity.id == newNotificationEntity.id
            }

            override fun areContentsTheSame(
                oldNotificationEntity: EachNotification,
                newNotificationEntity: EachNotification
            ): Boolean {
                return oldNotificationEntity == newNotificationEntity
            }
        }
    }

}