package org.triplepy.sh8email.sh8.activities.login.presenter

import org.triplepy.sh8email.sh8.api.Sh8Client
import org.triplepy.sh8email.sh8.utils.LogAppEventUtil
import retrofit2.adapter.rxjava.HttpException
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * The sh8email-android Project.
 * ==============================
 * org.triplepy.sh8email.sh8.activities.login
 * ==============================
 * Created by igangsan on 2016. 9. 3..
 */
class LoginPresenterImpl : LoginPresenter {
    val client: Sh8Client
    val view: LoginPresenter.View

    @Inject
    constructor(view: LoginPresenter.View, client: Sh8Client) {
        this.view = view
        this.client = client
    }

    override fun loginWithId(id: String) {
        view.showProgressBar()

        client.getMailBox(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.hideProgressBar()
                    view.showToast("ResponseBody : ${it?.string()}")
                    LogAppEventUtil.eventLogin("email", true)
                }, {
                    if (it is HttpException) {
                        view.hideProgressBar()
                        view.showToast("ErrorCode : ${it.code()}")
                        LogAppEventUtil.eventLogin("email", false, it.code())
                    }
                })
    }

}