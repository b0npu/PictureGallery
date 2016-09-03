package com.b0npu.picturegallery

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity

class PictureViewerActivity extends AppCompatActivity with TypedFindView {

  var galleryPager: ViewPager = _

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)

    galleryPager = new ViewPager(this)

    val adapter: GalleryPagerAdapter = new GalleryPagerAdapter(this)

    adapter.addItem(R.drawable.icecreamsandwich)
    adapter.addItem(R.drawable.jellybean)
    adapter.addItem(R.drawable.kitkat)
    adapter.addItem(R.drawable.lollipop)
    adapter.addItem(R.drawable.marshmallow)

    galleryPager.setAdapter(adapter)
    setContentView(galleryPager)
  }

}