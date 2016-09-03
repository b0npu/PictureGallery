package com.b0npu.picturegallery

import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity

class PictureViewerActivity extends AppCompatActivity with TypedFindView {

  var galleryPager: ViewPager = _

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)

    galleryPager = new ViewPager(this)

    val adapter: GalleryPagerAdapter = new GalleryPagerAdapter(this)

    adapter.addItem(Color.BLACK)
    adapter.addItem(Color.RED)
    adapter.addItem(Color.GREEN)
    adapter.addItem(Color.BLUE)
    adapter.addItem(Color.CYAN)
    adapter.addItem(Color.MAGENTA)
    adapter.addItem(Color.YELLOW)

    galleryPager.setAdapter(adapter)
    setContentView(galleryPager)
  }

}