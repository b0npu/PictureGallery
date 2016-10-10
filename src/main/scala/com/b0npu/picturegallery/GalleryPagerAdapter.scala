package com.b0npu.picturegallery

import android.content.Context
import android.graphics.Bitmap
import android.support.v4.view.PagerAdapter
import android.view.{View, ViewGroup}
import android.widget.ImageView

/**
  * 画像をViewPagerに表示するためのAdapterクラス(PagerAdapterのサブクラス)
  *
  * ViewPagerを表示するActivityの情報(Context)を引数で受取りViewPagerのPageを生成する
  * ViewPagerのPageにはImageViewを配置しaddPictureメソッドで受取った画像を表示する
  */
class GalleryPagerAdapter(context: Context) extends PagerAdapter {

  /**
    * フィールドの定義
    *
    * コンストラクタの引数(Context)を格納する定数を定義
    * addPictureメソッドで受取った画像(ビットマップ画像)を格納する配列も定義
    * (自クラスで使うだけのフィールドはprivateにして明示的に非公開にしてます)
    */
  private val galleryContext: Context = context
  private var galleryArray: Array[Bitmap] = Array.empty

  /**
    * addPictureメソッドの定義
    *
    * 引数に画像(ビットマップ画像)を受取り配列に格納する
    * TODO: 画像を直接配列に格納しているので画像の枚数が多くなると重くなるかもしれません
    */
  def addPicture(bitmap: Bitmap): Unit = {
    galleryArray :+= bitmap
  }

  /**
    * instantiateItemメソッドをオーバーライド
    *
    * このメソッドはViewPagerにPageを追加するメソッドで
    * 引数のcontainer(ViewGroup)のpositionの場所にViewを表示する
    * containerに追加したImageViewにaddPictureメソッドで
    * galleryArrayに格納した画像(ビットマップ画像)を配置して
    * ViewPagerに画像を表示する
    */
  override def instantiateItem(container: ViewGroup, position: Int): AnyRef = {

    val bitmapPicture: Bitmap = galleryArray(position)
    val imageView: ImageView = new ImageView(galleryContext)

    container.addView(imageView)
    imageView.setImageBitmap(bitmapPicture)
    imageView
  }

  /**
    * destroyItemメソッドをオーバーライド
    *
    * このメソッドはViewPagerからPageを削除するメソッドで
    * 引数のcontainer(ViewGroup)のpositionの場所にあるobj(View等のObject)を削除する
    */
  override def destroyItem(container: ViewGroup, position: Int, obj: Object): Unit = {
    container.removeView(obj.asInstanceOf[View])
  }

  /**
    * getCountメソッドをオーバーライド
    *
    * このメソッドはViewPagerに追加するViewの数を取得する
    * galleryArrayに格納した画像(SDカードに保存されている全画像)の数を取得する
    */
  override def getCount: Int = {
    galleryArray.length
  }

  /**
    * isViewFromObjectメソッドをオーバーライド
    *
    * このメソッドはViewPagerのPageにViewがあるか確認するメソッドで
    * instantiateItemメソッドで追加されたItem(Object)がViewであればTrueになる
    */
  override def isViewFromObject(view: View, obj: Object): Boolean = {
    view == obj
  }

}