package ru.sumin.servicestest

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PersistableBundle
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyJobService: JobService() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onStartJob(p0: JobParameters?): Boolean {
        log("onStartJob")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            coroutineScope.launch {
                var workItem = p0?.dequeueWork()
                while (workItem != null) {

                    val page = workItem.intent.getIntExtra(PAGE, 0)
                    log("$page")
                    for (i in 0 until 5) {
                        delay(1000)
                        log("Timer $i $page")
                    }
                    //завершаем текущую очередь
                    p0?.completeWork(workItem)
                    workItem = p0?.dequeueWork()

                }
                jobFinished(p0, false)
            }
        }
//        возвращаем true если сервис еще выполняется(например асинхронная работа)
        return true
    }
//вызывается только если система сама убила сервис
    override fun onStopJob(p0: JobParameters?): Boolean {
        log("onStartJob")
        return  true

    }

    override fun onCreate() {
        super.onCreate()
        log("onCreate")    }



    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
        coroutineScope.cancel()

    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyJobService: $message")
    }

    companion object {

        const val JOB_ID = 11
        private const val PAGE = "page"

        fun newIntent(page: Int): Intent {
            return Intent().apply {
                putExtra(PAGE, page)
        }
}

    }


}