package com.b0npu.picturegallery

import android.content.{ContentResolver, Context}
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.view.PagerAdapter
import android.view.{View, ViewGroup}
import android.widget.ImageView

/**
  * 画像を表示する
  *
  * @param context gallery context
  */
class GalleryPagerAdapter(context: Context) extends PagerAdapter {

  /* コンテキスト */
  val galleryContext: Context = context

  /* SDカードの画像データのURIに問い合わせをして検索結果をCursorに格納する */
  val imageMediaStoreUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
  val mediaContentResolver: ContentResolver = galleryContext.getContentResolver

  var galleryArray: Array[Bitmap] = Array.empty

  def addPicture(bitmap: Bitmap): Unit = {
    galleryArray :+= bitmap
  }

  override def instantiateItem(container: ViewGroup, position: Int): AnyRef = {

    val bitmapPicture = galleryArray(position)

    val imageView: ImageView = new ImageView(galleryContext)

    imageView.setImageBitmap(bitmapPicture)
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