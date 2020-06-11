package com.example.storytellerSampleAndroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.storyteller.Storyteller
import com.storyteller.domain.*
import com.storyteller.services.Error
import com.storyteller.ui.row.StorytellerRowView
import com.storyteller.ui.row.StorytellerRowViewDelegate
import java.util.*

class MainActivity : AppCompatActivity(), StorytellerRowViewDelegate {

    private lateinit var refreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initialize sdk - this is not required if api key is already included in the manifest
//        Storyteller.initialize("[APIKEY]")

        //enable sdk logging for debug
//        Storyteller.enableLogging = true

        //setup user
        val userId = UUID.randomUUID().toString()
        Storyteller.setUserDetails(UserInput(userId), {
            Log.i("Storyteller Sample", "setUserDetails success $userId")
            Storyteller.reloadData({
                refreshLayout.isRefreshing = false
            })
        }, {
            Log.i("Storyteller Sample", "setUserDetails failed, error $it")
        })

        //setup refresh layout
        refreshLayout = findViewById<SwipeRefreshLayout>(R.id.refreshLayout).apply {
            setOnRefreshListener {
                Storyteller.reloadData({
                    refreshLayout.isRefreshing = false
                })
            }
        }

        //setup callbacks
        val storytellerRowView = findViewById<StorytellerRowView>(R.id.channelRowView)
        storytellerRowView.delegate = this
    }

    override fun onStoryDataLoadComplete(success: Boolean, error: Error?, dataCount: Int) {
        Log.i("Storyteller Sample", "onChannelsDataLoadComplete callback: success $success, error $error, dataCount $dataCount")
    }

    override fun onStoryDataLoadStarted() {
        Log.i("Storyteller Sample", "onStoryDataLoadStarted callback")
    }

    override fun onStoryDismissed() {
        Log.i("Storyteller Sample", "onStoryDismissed callback")
    }

    override fun onUserActivityOccurred(type: UserActivity.EventType, data: UserActivityData) {
        Log.i("Storyteller Sample", "onUserActivityOccurred: type $type data $data")
    }

    override fun getAdsForRow(stories: List<ClientStory>, onComplete: (AdResponse) -> Unit) {
        Log.i("Storyteller Sample", "getAdsForRow: stories $stories")
    }
}
