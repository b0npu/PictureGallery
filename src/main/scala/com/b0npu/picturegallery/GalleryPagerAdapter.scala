package com.b0npu.picturegallery

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.{View, ViewGroup}
import android.widget.ImageView

class GalleryPagerAdapter(context: Context) extends PagerAdapter {

  val galleryContext: Context = context

  var galleryArray: Array[Int] = Array.empty

  def addItem(id: Int): Unit = {
    galleryArray :+= id
  }

  override def instantiateItem(container: ViewGroup, position: Int): AnyRef = {

    val id = galleryArray(position)

    val imageView: ImageView = new ImageView(galleryContext)
    imageView.setImageResource(id)

    container.addView(imageView)
    imageView
  }

  override def destroyItem(container: ViewGroup, position: Int, obj: Object): Unit = {
    container.removeView(obj.asInstanceOf[View])
  }

  override def getCount: Int = {
    galleryArray.length
  }

  override def isViewFromObject(view: View, obj: Object): Boolean = {
    view == obj
  }

}