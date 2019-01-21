package com.apro.recky.battleSpree.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.apro.recky.battleSpree.R
import com.apro.recky.battleSpree.models.EachNotification
import com.apro.recky.battleSpree.views.viewHolders.NotificationViewHolder

class RvPagedAdapter : PagedListAdapter<EachNotification, NotificationViewHolder>(DIFF_CALLBACK) {

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