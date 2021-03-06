package com.b0npu.picturegallery

import android.Manifest
import android.content.pm.PackageManager
import android.content.{ContentResolver, ContentUris, DialogInterface, Intent}
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.{BaseColumns, MediaStore, Settings}
import android.support.v4.app.ActivityCompat
import android.support.v4.content.PermissionChecker
import android.support.v4.view.ViewPager
import android.support.v7.app.{AlertDialog, AppCompatActivity}

class PictureViewerActivity extends AppCompatActivity with TypedFindView {

  /**
    * フィールドの定義
    *
    * requestPermissionsメソッドで権限を要求した際に
    * コールバックメソッドのonRequestPermissionsResultメソッドに渡す定数を定義
    * (自クラスで使うだけのフィールドはprivateにして明示的に非公開にしてます)
    */
  private val REQUEST_READ_STORAGE_PERMISSION_CODE: Int = 0x01

  /**
    * アプリの画面を生成
    *
    * アプリを起動するとonCreateが呼ばれてActivityが初期化される
    * 画像の表示に必要なパーミッション(SDカードのデータの読み込み)を確認して
    * パーミッションが許可されていない場合はrequestReadStoragePermissionメソッドで
    * パーミッションの許可を要求する
    * パーミッションが許可されていればviewGalleryPagerメソッドで画像を表示する
    */
  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)

    if (PermissionChecker.checkSelfPermission(PictureViewerActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
      != PackageManager.PERMISSION_GRANTED) {
      requestReadStoragePermission
    } else {
      viewGalleryPager
    }
  }

  /**
    * viewGalleryPagerメソッドの定義
    *
    * SDカードの画像を読み込んでViewPagerに配置したImageViewに表示する
    * ImageViewへの画像の配置はGalleryPagerAdapter(PagerAdapterを継承したサブクラス)を
    * 使うので画像を格納したGalleryPagerAdapterをViewPagerにセットして画像を表示する
    * TODO: SDカードに画像が無い場合のエラー処理をしてないので注意
    * TODO: 画像を直接配列に格納しているので画像の枚数が多くなると重くなるかもしれません
    */
  private def viewGalleryPager: Unit = {

    /* ViewPagerはインスタンスの生成の代わりにレイアウトXMLに記述する方法でも良い */
    val galleryPager = new ViewPager(PictureViewerActivity.this)
    val galleryPagerAdapter = new GalleryPagerAdapter(PictureViewerActivity.this)

    /* SDカードの画像データのURIに問い合わせをして検索結果をCursorに格納する */
    val imageMediaStoreUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val mediaContentResolver: ContentResolver = getContentResolver
    val pictureCursor: Cursor = mediaContentResolver.query(imageMediaStoreUri, null, null, null, null)

    /* Cursorに格納した画像データの検索結果の先頭から順に画像を取得しGalleryPagerAdapterに格納する */
    pictureCursor.moveToFirst
    do {
      /* 画像データのURIとIDから画像(ビットマップ画像)を取得する */
      val pictureId = pictureCursor.getLong(pictureCursor.getColumnIndex(BaseColumns._ID)).asInstanceOf[Int]
      val bmpImageUri: Uri = ContentUris.withAppendedId(imageMediaStoreUri, pictureId)
      val bmpImage: Bitmap = MediaStore.Images.Media.getBitmap(mediaContentResolver, bmpImageUri)

      galleryPagerAdapter.addPicture(bmpImage)

    } while (pictureCursor.moveToNext)

    /* 画像を配置したViewPagerをレイアウトに設置して画面に画像を表示する */
    galleryPager.setAdapter(galleryPagerAdapter)
    setContentView(galleryPager)
  }

  /**
    * openSettingsメソッドの定義
    *
    * インテントを使ってアプリの設定画面を開く
    */
  private def openSettings: Unit = {

    val appSettingsIntent: Intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val appPackageUri: Uri = Uri.fromParts("package", getPackageName, null)

    /* インテントにアプリのURIを指定してアプリ情報の画面を開く */
    appSettingsIntent.setData(appPackageUri)
    startActivity(appSettingsIntent)
  }

  /**
    * requestReadStoragePermissionメソッドの定義
    *
    * READ_EXTERNAL_STORAGEのパーミッションの許可(権限取得)を要求する
    * shouldShowRequestPermissionRationaleメソッドを使って
    * 以前にパーミッションの許可を拒否されたことがあるか確認し
    * 拒否されたことがある場合はパーミッションの許可が必要な理由を
    * ダイアログに表示してからパーミッションの許可を要求する
    */
  private def requestReadStoragePermission: Unit = {

    /* パーミッションの許可を拒否されたことがあるか確認する */
    if (ActivityCompat.shouldShowRequestPermissionRationale(PictureViewerActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

      /* パーミッションの許可を拒否されたことがあれば許可が必要な理由を説明してから許可を要求する */
      new AlertDialog.Builder(PictureViewerActivity.this)
        .setTitle("パーミッションの追加説明")
        .setMessage("このアプリで画像を表示するにはパーミッションが必要です")
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener {

          override def onClick(dialogInterface: DialogInterface, i: Int): Unit = {
            /* パーミッションの許可を要求 */
            ActivityCompat.requestPermissions(
              PictureViewerActivity.this,
              Array[String](Manifest.permission.READ_EXTERNAL_STORAGE),
              REQUEST_READ_STORAGE_PERMISSION_CODE
            )
          }
        })
        .create
        .show

    } else {
      /* 初回要求時か「今後は確認しない」を選択されている場合のパーミッションの許可の要求 */
      ActivityCompat.requestPermissions(
        PictureViewerActivity.this,
        Array[String](Manifest.permission.READ_EXTERNAL_STORAGE),
        REQUEST_READ_STORAGE_PERMISSION_CODE
      )
    }
  }

  /**
    * onRequestPermissionsResultメソッドをオーバーライド
    *
    * このメソッドはrequestPermissionsメソッドのコールバックメソッドで
    * requestPermissionsメソッドでパーミッションの許可を要求した結果を取得する
    * 引数のrequestCodeで要求されたパーミッションを区別し
    * grantResultの要素でパーミッションの許可・不許可を確認する
    */
  override def onRequestPermissionsResult(requestCode: Int, permissions: Array[_root_.java.lang.String], grantResults: Array[Int]): Unit = {

    /* 要求されたパーミッションによって対応が変わるので何のパーミッションか確認する */
    requestCode match {

      case REQUEST_READ_STORAGE_PERMISSION_CODE ⇒
        /* パーミッションの要求が拒否されていた場合はダイアログに表示する */
        if (grantResults.length != 1 || grantResults(0) != PackageManager.PERMISSION_GRANTED) {

          /* 「今後は確認しない」が選択されていなければ再度パーミッションの許可を要求する */
          if (ActivityCompat.shouldShowRequestPermissionRationale(PictureViewerActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(PictureViewerActivity.this)
              .setTitle("パーミッション取得エラー")
              .setMessage("画像の表示に必要なパーミッションが取得できませんでした")
              .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener {

                override def onClick(dialogInterface: DialogInterface, i: Int): Unit = {
                  requestReadStoragePermission
                }
              })
              .create
              .show

          } else {
            /* 「今後は確認しない」を選択されている場合はアプリの設定画面を開く */
            new AlertDialog.Builder(PictureViewerActivity.this)
              .setTitle("パーミッション取得エラー")
              .setMessage("今後は許可しないが選択されました！！アプリ設定＞権限を確認してください（権限をON/OFFすることで状態はリセットされます）")
              .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener {

                override def onClick(dialogInterface: DialogInterface, i: Int): Unit = {
                  /* アプリの設定画面を開いて手動で許可してもらう */
                  openSettings
                }
              })
              .create
              .show
          }

        } else {
          /* パーミッションが許可された場合は画像を表示する */
          viewGalleryPager
        }
    }
  }
}