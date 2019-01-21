package com.apro.recky.battleSpree.views.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.apro.recky.battleSpree.R
import com.apro.recky.battleSpree.models.EachNotification
import com.apro.recky.battleSpree.viewModels.NotificationViewModel
import com.apro.recky.battleSpree.views.adapter.RvPagedAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber
import kotlinx.android.synthetic.main.fragment_notifications.*

class Notifications : Fragment(), LifecycleOwner {

    var notificationViewModel : NotificationViewModel ?= null
    var compositeDisposable : CompositeDisposable ?= null
    var rvNotifcationsAdapter : RvPagedAdapter ?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            it.title = "Notifications"
            rvNotifcationsAdapter = RvPagedAdapter()
            rvNotifications.adapter = rvNotifcationsAdapter
            rvNotifications.layoutManager = LinearLayoutManager(it)
        }

        compositeDisposable = CompositeDisposable()

        notificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel::class.java)

        compositeDisposable?.add(notificationViewModel?.pagedListFlowable
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeWith(object : ResourceSubscriber<PagedList<EachNotification>>(){
                override fun onComplete() {
                }

                override fun onNext(t: PagedList<EachNotification>?) {
                    rvNotifcationsAdapter?.submitList(t)
                }

                override fun onError(e: Throwable?) {
                }

            }))

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }
}
