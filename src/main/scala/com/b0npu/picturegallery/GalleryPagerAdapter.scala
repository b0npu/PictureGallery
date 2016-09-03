package com.b0npu.picturegallery

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.{Gravity, View, ViewGroup}
import android.widget.TextView

class GalleryPagerAdapter(context: Context) extends PagerAdapter {

  val galleryContext: Context = context

  var galleryArray: Array[Int] = Array.empty

  def addItem(id: Int): Unit = {
    galleryArray :+= id
  }

  override def instantiateItem(container: ViewGroup, position: Int): AnyRef = {

    val id = galleryArray(position)

    val textView: TextView = new TextView(galleryContext)
    textView.setText("Page:" + position)
    textView.setTextSize(30)
    textView.setTextColor(id)
    textView.setGravity(Gravity.CENTER)

    container.addView(textView)
    textView
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