package com.example.fmveiculos.presenter.contract

import com.example.fmveiculos.data.model.UserInfoModel

interface SettingsContract {
    interface View {
        fun displayUserInfo(userInfo: UserInfoModel)
        fun showError(message: String)
        fun navigateToHome()
    }

    interface Presenter {
        fun loadUserInfo(userId: String?)
    }
}