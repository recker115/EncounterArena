package com.apro.recky.battleSpree.viewModels

import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.apro.recky.battleSpree.AppClass
import com.apro.recky.battleSpree.models.EachNotification
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

class NotificationViewModel : ViewModel() {

    var pagedListFlowable: Flowable<PagedList<EachNotification>> ?= null
    init {
        val appDatabase = AppClass.getAppInstance()?.appDatabase
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPageSize(30)
            .setInitialLoadSizeHint(50)
            .setPrefetchDistance(10)
            .build()

        appDatabase?.let {
            pagedListFlowable = RxPagedListBuilder(
                it.getNotificationDao().getAllNotifications(),config).buildFlowable(BackpressureStrategy.LATEST)
        }

    }
}