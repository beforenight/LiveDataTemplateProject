package com.pdsu.live

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.pdsu.live.databinding.ActivitySecondBinding
import kotlinx.coroutines.*

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    //1.创建一个MainScope
    val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_second)

        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //2.启动协程
        scope.launch(Dispatchers.Main) {

//            val one = async {
//                getResult(20)
//            }
            val one = async(start = CoroutineStart.LAZY) {
                getResult(20)
            }
            val two = async {
                getResult(40)
            }

            //val resultNum = one.await() + two.await()

            Log.e("Test", one.await().toString())
            Log.e("Test", two.await().toString())
//            binding.tvNum.text = resultNum.toString()
            binding.tvNum.text = (one.await() + two.await()).toString()
        }


    }


    // 3. 销毁的时候释放
    override fun onDestroy() {
        super.onDestroy()

        scope.cancel()
    }

    private suspend fun getResult(num: Int): Int {
        delay(5000)
        return num * num
    }
}