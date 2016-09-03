package com.b0npu.picturegallery

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class PictureViewerActivity extends AppCompatActivity with TypedFindView {

    override def onCreate( savedInstanceState: Bundle ): Unit = {
        super.onCreate( savedInstanceState )
        setContentView( R.layout.activity_main )
    }
}
