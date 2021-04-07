package com.udacity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.Constants.DOWNLOAD_STATUS
import com.udacity.Constants.FILE_NAME
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        intent.apply {
            val fileDescription = getStringExtra(FILE_NAME)
            file_text_view_description.text = fileDescription
            file_text_view_description.contentDescription = fileDescription

            val downloadStatus = getStringExtra(DOWNLOAD_STATUS)
            download_status_text_view_description.text = downloadStatus
            download_status_text_view_description.contentDescription = downloadStatus
        }

        confirm_button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }

            startActivity(intent)
        }
    }

}
