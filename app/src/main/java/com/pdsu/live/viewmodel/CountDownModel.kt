package com.pdsu.live.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CountDownModel : ViewModel() {

    val countDownLivaData = MutableLiveData<String>()
    private var remainSecond = 2000//剩余秒数

    init {
        var countDown = object : CountDownTimer(2000 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainSecond--
                countDownLivaData.postValue("剩余：${remainSecond} 秒")
            }

            override fun onFinish() {
                countDownLivaData.postValue("倒计时结束")
            }
        }
        countDown.start()
    }
}