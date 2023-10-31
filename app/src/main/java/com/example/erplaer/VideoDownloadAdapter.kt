package com.example.erplaer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.item_download_list.view.*
import top.xuqingquan.m3u8downloader.FileDownloader
import top.xuqingquan.m3u8downloader.entity.*
import top.xuqingquan.m3u8downloader.utils.md5
import java.io.File
import java.text.DecimalFormat


/**
 * Created by 许清泉 on 2019-10-15 13:15
 */
class VideoDownloadAdapter(private val list: MutableList<VideoDownloadEntity>) :
        RecyclerView.Adapter<VideoDownloadAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_download_list, parent, false
                )
        )



}

    override fun getItemCount() = list.size


    fun removeData(position: Int) {
        list.removeAt(position)
        //删除动画
        notifyItemRemoved(position)
        notifyDataSetChanged()



    }

    /**
     * 避免出现整个item闪烁
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNullOrEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            holder.updateProgress(list[position])
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(list[position])
        System.out.println("查看list all"+list)

        holder.dele.setOnClickListener(View.OnClickListener { v ->

            System.out.println("查看list 0003"+list)

            val file = File(Environment.getExternalStorageDirectory(), "sanmiaoyy/" + md5(list[position].originalUrl))
           file.deleteRecursively()
            removeData(position)
        })

    }


    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<TextView>(R.id.title)
        private val currentSize = view.findViewById<TextView>(R.id.current_size)
        private val speed = view.findViewById<TextView>(R.id.speed)
        val dele = view.findViewById<TextView>(R.id.delete)
        private val download = view.findViewById<TextView>(R.id.download)

        /**
         * 设置数据
         */
        @SuppressLint("SetTextI18n")
        fun setData(data: VideoDownloadEntity?) {
            if (data == null) {
                return
            }
            val context = view.context

            //注释掉url显示
           // url.text = data.originalUrl
            val name = if (data.name.isNotEmpty()) {
                if (data.subName.isNotEmpty()) {
                    "${data.name}(${data.subName})"
                } else {
                    data.name
                }
            } else {
                if (data.subName.isNotEmpty()) {
                    "${context.getString(R.string.unknow_movie)}(${data.subName})"
                } else {
                    context.getString(R.string.unknow_movie)
                }
            }
            title.text = name
            updateProgress(data)


        }


        /**
         * 删除文件
         */
        @SuppressLint("SetTextI18n")
        fun deletefile(data: VideoDownloadEntity?){
            if (data == null) {
                System.out.println("测试数据data为空啊")
                return
            }
            System.out.println("测试数据data不为空 进行处理")
            val file = File(Environment.getExternalStorageDirectory(), "sanmiaoyy/" + md5(data.originalUrl))
            file.deleteRecursively()
        }


        /**
         * 进度更新
         */
        @SuppressLint("SetTextI18n")
        fun updateProgress(data: VideoDownloadEntity) {
            if (data.originalUrl.endsWith(".m3u8") || data.status == COMPLETE) {
                currentSize.text =
                        getSizeUnit(data.currentSize.toDouble())
            } else {
                currentSize.text =
                        "${getSizeUnit(data.currentSize.toDouble())}/${getSizeUnit(
                                data.fileSize.toDouble()
                        )}"
            }
            speed.text =
                    "${DecimalFormat("#.##%").format(data.currentProgress)}|${data.currentSpeed}"
            val context = view.context
            //下载删除
//
//          dele.setOnClickListener() {
//              System.out.println("删除：删除：====" )
//                val file = File(Environment.getExternalStorageDirectory(), "sanmiaoyy/" + md5(data.originalUrl))
//                file.deleteRecursively()
//
//            }




//
//                val localpath: String = FileDownloader.getBaseDownloadPath().toString() + "/" + md5(data.originalUrl)
//                System.out.println("本地路径：：====" + localpath)
//                //   deleteDirectoryFiles(localpath)
//                val file = File(Environment.getExternalStorageDirectory(), "sanmiaoyy/" + md5(data.originalUrl))
//               file.deleteRecursively()
//                System.out.println("删除：：====" + localpath)
//
//
//            }


            //状态逻辑处理
            when (data.status) {
                NO_START -> {
                    download.setTextColor(ContextCompat.getColor(context, R.color.blue))
                    download.background =
                            ContextCompat.getDrawable(context, R.drawable.shape_download_prepare)
                    download.setText(R.string.btn_download)
                    download.isVisible = true
                    speed.isVisible = false
                    currentSize.isVisible = false
                    currentSize.setText(R.string.wait_download)
                    download.setOnClickListener {
                        if (data.startDownload != null) {
                            data.startDownload!!.invoke()
                        } else {
                            FileDownloader.downloadVideo(data)
                        }
                    }
                }
                DOWNLOADING -> {
                    currentSize.isVisible = true
                    speed.isVisible = true
                    speed.setTextColor(ContextCompat.getColor(speed.context, R.color.blue))
                    download.isVisible = true
                    download.setText(R.string.pause)
                    download.setOnClickListener {
                        data.downloadContext?.stop()
                        data.downloadTask?.cancel()
                    }
                    download.setTextColor(ContextCompat.getColor(context, R.color.white))
                    download.background =
                            ContextCompat.getDrawable(context, R.drawable.shape_blue_btn)
                }
                PAUSE -> {
                    currentSize.isVisible = true
                    download.setTextColor(ContextCompat.getColor(context, R.color.white))
                    download.background =
                            ContextCompat.getDrawable(context, R.drawable.shape_blue_btn)
                    download.isVisible = true
                    download.setText(R.string.go_on)
                    download.setOnClickListener {
                        if (data.startDownload != null) {
                            data.startDownload!!.invoke()
                        } else {
                            FileDownloader.downloadVideo(data)
                        }
                    }
                    speed.isVisible = true
                    speed.setText(R.string.already_paused)
                    speed.setTextColor(ContextCompat.getColor(speed.context, R.color.red))
                }
                COMPLETE -> {
                    download.setTextColor(ContextCompat.getColor(context, R.color.white))
                    download.background =
                            ContextCompat.getDrawable(context, R.drawable.shape_blue_btn)
                    download.isVisible = true
                    download.setText("播放")
                    currentSize.isVisible = true
                    speed.isVisible = false


                    download.setOnClickListener {

                        val localpath: String = FileDownloader.getBaseDownloadPath().toString() + "/" + md5(data.originalUrl)
                        val m3u8l = localpath + "/localPlaylist.m3u8"

                        var intent3: Intent = Intent(view.getContext(), LocalPlayer().javaClass)
                        intent3.putExtra("dbs", m3u8l)
                        view.getContext().startActivity(intent3)

                    }

                }
                PREPARE -> {
                    currentSize.isVisible = true
                    download.setText(R.string.prepareing)
                    currentSize.setText(R.string.wait_download)
                    download.isVisible = true
                    download.setOnClickListener {
                        if (data.startDownload != null) {
                            data.startDownload!!.invoke()
                        } else {
                            FileDownloader.downloadVideo(data)
                        }
                    }
                    download.setTextColor(ContextCompat.getColor(context, R.color.blue))
                    download.background =
                            ContextCompat.getDrawable(context, R.drawable.shape_download_prepare)
                    speed.isVisible = false
                }
                ERROR -> {
                    currentSize.isVisible = false
                    speed.isVisible = false
                    download.isVisible = true
                    download.setText(R.string.retry)
                    download.setOnClickListener {
                        if (data.startDownload != null) {
                            data.startDownload!!.invoke()
                        } else {
                            FileDownloader.downloadVideo(data)
                        }
                    }
                    download.setTextColor(ContextCompat.getColor(context, R.color.white))
                    download.background =
                            ContextCompat.getDrawable(context, R.drawable.shape_blue_btn)
                }
            }
        }


    }













}