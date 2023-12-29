package apps.user.repair.uitl

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.FileUtils
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


/**
 * @author:
 * @created on: 2022/9/1 14:53
 * @description:
 */
object FileUtil {
    /**
     * 根据Uri获取文件绝对路径，解决Android4.4以上版本Uri转换 兼容Android 10
     *
     * @param context
     * @param imageUri
     */
    fun getFileAbsolutePath(context: Context?, imageUri: Uri?): String? {
        if (context == null || imageUri == null) {
            return null
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return getRealFilePath(context, imageUri)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                val docId = DocumentsContract.getDocumentId(imageUri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(imageUri)) {
                val id = DocumentsContract.getDocumentId(imageUri)
                val contentUri: Uri =
                    ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(imageUri)) {
                val docId = DocumentsContract.getDocumentId(imageUri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = MediaStore.Images.Media._ID + "=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } // MediaStore (and general)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return uriToFileApiQ(context, imageUri)
        } else if ("content".equals(imageUri.getScheme(), ignoreCase = true)) {
            // Return the remote address
            return if (isGooglePhotosUri(imageUri)) {
                imageUri.getLastPathSegment()
            } else getDataColumn(context, imageUri, null, null)
        } else if ("file".equals(imageUri.getScheme(), ignoreCase = true)) {
            return imageUri.getPath()
        }
        return null
    }

    //此方法 只能用于4.4以下的版本
    private fun getRealFilePath(context: Context, uri: Uri?): String? {
        if (null == uri) {
            return null
        }
        val scheme: String? = uri.getScheme()
        var data: String? = null
        if (scheme == null) {
            data = uri.getPath()
        } else if (ContentResolver.SCHEME_FILE == scheme) {
            data = uri.getPath()
        } else if (ContentResolver.SCHEME_CONTENT == scheme) {
            val projection = arrayOf(MediaStore.Images.ImageColumns.DATA)
            val cursor: Cursor? =
                context.getContentResolver().query(uri, projection, null, null, null)

//            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    val index: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    if (index > -1) {
                        data = cursor.getString(index)
                    }
                }
                cursor.close()
            }
        }
        return data
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.getAuthority()
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.getAuthority()
    }

    private fun getDataColumn(context: Context?, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = MediaStore.Images.Media.DATA
        val projection = arrayOf(column)
        try {
            if (context != null) {
                cursor =
                    uri?.let { context.getContentResolver().query(it, projection, selection, selectionArgs, null) }
            }
            if (cursor != null && cursor.moveToFirst()) {
                val index: Int = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.getAuthority()
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.getAuthority()
    }


    /**
     * Android 10 以上适配 另一种写法
     * @param context
     * @param uri
     * @return
     */
    private fun getFileFromContentUri(context: Context, uri: Uri?): String? {
        if (uri == null) {
            return null
        }
        val filePath: String
        val filePathColumn =
            arrayOf(MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME)
        val contentResolver: ContentResolver = context.getContentResolver()
        val cursor: Cursor? = contentResolver.query(
            uri, filePathColumn, null, null, null
        )
        if (cursor != null) {
            cursor.moveToFirst()
            try {
                filePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]))
                return filePath
            } catch (e: Exception) {
            } finally {
                cursor.close()
            }
        }
        return ""
    }

    /**
     * Android 10 以上适配
     * @param context
     * @param uri
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private fun uriToFileApiQ(context: Context, uri: Uri): String {
        var file: File? = null
        //android10以上转换
        if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            file = File(uri.getPath())
        } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //把文件复制到沙盒目录
            val contentResolver: ContentResolver = context.getContentResolver()
            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    val displayName: String =
                        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    try {
                        val `is`: InputStream? = contentResolver.openInputStream(uri)
                        val cache =
                            File(context.getExternalCacheDir()?.absolutePath, Math.round((Math.random() + 1) * 1000).toString() + displayName)
                        val fos = FileOutputStream(cache)
                        if (`is` != null) {
                            FileUtils.copy(`is`, fos)
                        }
                        file = cache
                        fos.close()
                        if (`is` != null) {
                            `is`.close()
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        if (file != null) {
            return file.absolutePath
        }
        return ""
    }

    //文件名称
    fun delCacheFile(context: Context, file: String) {
        val mutable: MutableList<String> = mutableListOf()
        val path = context.getExternalFilesDirs(Environment.MEDIA_MOUNTED)[0]!!.absolutePath
        mutable.add(File("${path}/${file}").absolutePath)
        delFolderFileSpecify(path, mutable)
    }

    private fun delFolderFileSpecify(path: String, mubs: MutableList<String>) {
        val f = File(path)
        val fs = f.listFiles() ?: return
        for (s in mubs) {
            for (index in fs.indices) {
                val name = fs[index].absolutePath
                if (name == s) {
                    deleteFolderFile(name, true)
                }
            }
        }
    }

    private fun deleteFolderFile(filePath: String, deleteThisPath: Boolean) {
        filePath.let {
            val file = File(filePath)
            if (file.isDirectory) {//
                val fs = file.listFiles()
                for (index in fs.indices) {
                    deleteFolderFile(fs[index].absolutePath, true)
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory) {//文件则删除
//                    SPreUtil.put(AppUtils.getContext(), "upgrade_version", "")
                    file.delete()
                } else {
                    val fs = file.listFiles()
                    if (fs != null && fs.isEmpty()) {//目录无文件
                        file.delete()
                    }
                }
            }
        }
    }

    fun deleteWebVieCache(context: Context): Boolean {
        return File(context.filesDir.absolutePath + "/webCache").deleteRecursively()
    }

    //内部存储
    fun getCacheDir(context: Context): File {
        return context.cacheDir.absoluteFile
    }

    fun getFilesDir(context: Context): File {
        return context.filesDir.absoluteFile
    }


    //外部存储
    fun getObbDir(context: Context): File {
        return context.obbDir
    }

    fun getExternalCacheDir(context: Context): File? {
        return context.externalCacheDir
    }

    fun getExternalFilesDir(context: Context): File? {
        return context.getExternalFilesDir(Environment.MEDIA_MOUNTED)
    }


}