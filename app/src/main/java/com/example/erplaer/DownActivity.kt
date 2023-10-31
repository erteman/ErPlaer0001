package com.example.erplaer

import android.Manifest
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_down.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import top.xuqingquan.m3u8downloader.FileDownloader
import top.xuqingquan.m3u8downloader.entity.*
import java.io.File
import kotlin.concurrent.thread

@RuntimePermissions
class DownActivity : AppCompatActivity() {

    private lateinit var adapter: VideoDownloadAdapter
    private val videoList = arrayListOf<VideoDownloadEntity>()
    private val tempList = arrayListOf<String>()
    private val gson = GsonBuilder().create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_down)
        initListView()
        initListWithPermissionCheck()

        //接收进度通知
        FileDownloader.downloadCallback.observe(this, Observer {
            onProgress(it)
        })


        //新建下载
        val bundle = this.intent.extras
        val ourl = bundle?.get("urls").toString()
        val name:String = bundle?.get("names").toString()
        val subname:String = bundle?.get("espode").toString()
          //  newDownload()

        if ( ourl.length>5){
           System.out.println("测试========== ourl长度"+ ourl.length)
        newDownload(ourl,name,subname)
        }

    }

    private fun initListView() {
        adapter = VideoDownloadAdapter(videoList)
        list.adapter = adapter
    }

    @NeedsPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    fun initList() {
        thread {//在线程中处理，防止ANR
            try {
                FileDownloader.getBaseDownloadPath().listFiles().forEach {
                    val file = File(it, "video.config")
                    if (file.exists()) {
                        val text = file.readText()
                        if (text.isNotEmpty()) {
                            val data = gson.fromJson<VideoDownloadEntity>(
                                    text,
                                    VideoDownloadEntity::class.java
                            )
                            if (data != null) {
                                if (data.status == DELETE) {
                                    it.deleteRecursively()

                                } else if (!tempList.contains(data.originalUrl)) {
                                    videoList.add(data)
                                    tempList.add(data.originalUrl)
                                }
                            }
                        }
                    }
                }
            }catch (e: Exception){
                e.printStackTrace();
                println("程序出现了未知异常，可能是您的人品太差了。${e.message}")
            }
            runOnUiThread {
                //主线程通知刷新布局
                adapter.notifyDataSetChanged()
            }
            videoList.sort()
            //依次添加下载队列
            videoList.filter { it.status == DOWNLOADING }.forEach {
                FileDownloader.downloadVideo(it)
            }
            videoList.filter { it.status == PREPARE }.forEach {
                FileDownloader.downloadVideo(it)
            }
            videoList.filter { it.status == NO_START }.forEach {
                FileDownloader.downloadVideo(it)
            }
        }
    }

    @OnPermissionDenied(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    fun onDenied() {
        toast(R.string.need_permission_tips)
    }

    private fun toast(@StringRes msg: Int) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun onProgress(entity: VideoDownloadEntity) {
        for ((index, item) in videoList.withIndex()) {
            if (item.originalUrl == entity.originalUrl) {
                videoList[index].status = entity.status
                videoList[index].currentSize = entity.currentSize
                videoList[index].currentSpeed = entity.currentSpeed
                videoList[index].currentProgress = entity.currentProgress
                videoList[index].fileSize = entity.fileSize
                videoList[index].tsSize = entity.tsSize
                videoList[index].downloadContext = entity.downloadContext
                videoList[index].downloadTask = entity.downloadTask
                videoList[index].startDownload = entity.startDownload
                adapter.notifyItemChanged(index, 0)
                break
            }
        }
    }




    private fun newDownload(hurl:String,name:String,subname:String) {

                    if (tempList.contains(hurl)) {
                        toast(R.string.already_download)

                    }
//                    val name = if (hurl.contains("?")) {
//                        hurl.substring(hurl.lastIndexOf("/") + 1, hurl.indexOf("?"))
//                    } else {
//                        hurl.substring(hurl.lastIndexOf("/") + 1)
//                    }
                    val entity = VideoDownloadEntity(hurl, name,subname)
                    entity.toFile()
                    videoList.add(0, entity)
                    adapter.notifyItemInserted(0)
                    FileDownloader.downloadVideo(entity)
                }



    }

