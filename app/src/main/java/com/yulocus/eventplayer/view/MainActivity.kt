package com.yulocus.eventplayer.view

import android.content.Context
import android.view.View
import com.jarvanmo.exoplayerview.media.SimpleMediaSource
import com.yulocus.eventplayer.R
import com.yulocus.eventplayer.base.MvpActivity
import com.yulocus.eventplayer.bean.Alert
import com.yulocus.eventplayer.contract.MainContract
import com.yulocus.eventplayer.presenter.MainPresenter
import com.yulocus.eventplayer.util.StatusBarUtils
import com.yulocus.eventplayer.widget.RulerRecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.*

class MainActivity : MvpActivity<MainPresenter>(), MainContract.View {

    companion object {
        private const val REFRESH_TIME = 1 * 60 * 60 * 1000L // 5 minutes
    }

    override fun bindLayoutId(): Int = R.layout.activity_main

    override fun createPresenter(): MainPresenter = MainPresenter(this)

    override fun setupStatusBar() = StatusBarUtils.hideStatusBar(this)

    override fun getContext(): Context = this

    override fun initView() {
        // toolbar
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayShowTitleEnabled(false)
//        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        recycler_view.setCallback(object: RulerRecyclerView.EventCallback {
            override fun stopVideo() {
                video_player.stop()
            }

            override fun setEvent(alert: Alert) {
                runOnUiThread({
                    video_player.visibility = View.VISIBLE
                    val mediaSource = SimpleMediaSource(alert.video)
                    video_player.play(mediaSource, true)
                })
//                Glide.with(this@MainActivity)
//                        .load(alert.image)
//                        .dontAnimate()
//                        .centerCrop()
//                        .listener(object: RequestListener<String, GlideDrawable> {
//                            override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
//                                e?.let { Timber.d("${it.message}") }
//                                return false
//                            }
//
//                            override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
//                                image_preview.visibility = View.VISIBLE
//                                button_play_video.visibility = View.VISIBLE
//                                return false
//                            }
//                        })
//                        .into(image_preview)
            }
        })

        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                recycler_view.initRuler(this@MainActivity)
                // load events
                presenter.loadEvents()
            }
        }, 0, REFRESH_TIME)
    }

    override fun initListener() {
//        RxView.clicks(button_play_video)
//                .throttleFirst(1, TimeUnit.SECONDS)
//                .subscribe{
//                    layout_preview.visibility = View.GONE
//                    video_player.visibility = View.VISIBLE
//                    video_player.play(mediaSource, true)
//                }
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

    override fun onStart() {
        super.onStart()
        video_player.resume()
    }

    override fun onResume() {
        super.onResume()
        video_player.resume()
    }

    public override fun onPause() {
        super.onPause()
        video_player.pause()
    }

    public override fun onStop() {
        super.onStop()
        video_player.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        video_player.releasePlayer()
    }

}
