package com.udacity

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
            file_text_view_description.text = getStringExtra(FILE_NAME)

            download_status_text_view_description.text = getStringExtra(DOWNLOAD_STATUS)
        }
    }

}
