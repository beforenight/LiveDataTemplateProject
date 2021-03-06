package com.pdsu.live

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.pdsu.live.databinding.ActivitySecondBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    private val TAG = "Flow"

    //1.创建一个MainScope
    val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_second)

        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        //2.启动协程
//        scope.launch(Dispatchers.Main) {
//
////            val one = async {
////                getResult(20)
////            }
//            val one = async(start = CoroutineStart.LAZY) {
//                getResult(20)
//            }
//            val two = async {
//                getResult(40)
//            }
//
//            //val resultNum = one.await() + two.await()
//
//            Log.e("Test", one.await().toString())
//            Log.e("Test", two.await().toString())
////            binding.tvNum.text = resultNum.toString()
//            binding.tvNum.text = (one.await() + two.await()).toString()

//
//        //2.启动协程
//        scope.launch(Dispatchers.Unconfined) {
//            val one = getResult(20)
//            val two = getResult(40)
//
//            runOnUiThread {
//                binding.tvNum.text = "(one + two).toString()"
//                binding.tvNum.text = (one + two).toString()
//            }
//        }


        //2.启动协程
        scope.launch(Dispatchers.Unconfined) {
            val one = async { getResult(20) }
            val two = async { getResult(40) }

            Log.e("resultContent：", "start")

            val resultContent = (one.await() + two.await()).toString()

            Log.e("resultContent：", resultContent)
            runOnUiThread {
                binding.tvNum.text = resultContent
            }
        }

        lifecycleScope.launch {
            //创建一个协程 Flow<T>

            createFlow().flowOn(Dispatchers.IO)
                .collect { num ->

                    Log.e("flow", "flow: $num")
                }


            flow<Int> {
                for (i in 1..3) {
                    Log.e("flow", "flow: $i emit")
                    emit(i)
                }
            }.filter {
                Log.e("flow", "flow: $it filter")
                it % 2 != 0
            }.map {
                Log.e("flow", "flow: $it map")
                "${it * it} money"
            }.collect {
                Log.e("Flow", "i get $it")
            }
        }



        lifecycleScope.launch {

            val flow = flow<Int> {
                for (i in 1..3) {
                    delay(500)
                    Log.e(TAG, "emit: $i")
                    emit(i)
                }
            }


            withTimeoutOrNull(1600)
            {

                flow.collect {
                    delay(500)
                    Log.e(TAG, "consume $it")
                }
            }

            Log.e(TAG, "flow cancel")
        }


//        Channel

        lifecycleScope.launch {
            //1.生成一个Channel
            val channel = Channel<Int>()

            //2.channel 发送数据

            launch {

                for (i in 1..5) {
                    delay(200)
                    channel.send(i * i)
                }

                channel.close()

            }


            //3. channel 接收数据
            launch {
                for (y in channel) {
                    Log.e(TAG, "Channel: get $y")
                }
            }
        }
    }

    private fun createFlow(): Flow<Int> {
        return (1..10).asFlow()
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