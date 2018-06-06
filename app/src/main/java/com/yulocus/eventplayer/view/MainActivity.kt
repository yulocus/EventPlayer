package com.yulocus.eventplayer.view

import android.content.Context
import android.media.MediaPlayer
import android.view.View
import com.bumptech.glide.Glide
import com.yulocus.eventplayer.R
import com.yulocus.eventplayer.base.MvpActivity
import com.yulocus.eventplayer.bean.Alert
import com.yulocus.eventplayer.contract.MainContract
import com.yulocus.eventplayer.presenter.MainPresenter
import com.yulocus.eventplayer.util.StatusBarUtils
import com.yulocus.eventplayer.widget.RulerRecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : MvpActivity<MainPresenter>(), MainContract.View {

    private var mediaPlayer: MediaPlayer? = null

    override fun bindLayoutId(): Int = R.layout.activity_main

    override fun createPresenter(): MainPresenter = MainPresenter(this)

    override fun setupStatusBar() = StatusBarUtils.hideStatusBar(this)

    override fun getContext(): Context = this

    override fun initView() {
        // toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        // player
        //video_player.setMediaController(MediaController(this@MainActivity))
        recycler_view.initRuler(this)
        recycler_view.setCallback(object: RulerRecyclerView.RulerResultCallback{
            override fun setResult(alert: Alert) {
                runOnUiThread({
                    Glide.with(this@MainActivity)
                            .load(alert.image)
                            .dontAnimate()
                            .centerCrop()
                            .into(image_preview)
                    image_preview.visibility = View.VISIBLE
                })
                //video_player.setVideoURI(Uri.parse(alert.video))
                //video_player.start()
            }
        })

        // load events
        presenter.loadEvents()
    }

    override fun initListener() {
    }

    override fun showLoading(isLoading: Boolean) {
        Timber.d("showLoading(), isLoading=$isLoading")
    }

    override fun showError(message: String) {
        Timber.e("showError(), message=$message")
    }

    override fun showEvents(list: MutableList<Alert>) {
        recycler_view.updateEvents(list)
    }

}
