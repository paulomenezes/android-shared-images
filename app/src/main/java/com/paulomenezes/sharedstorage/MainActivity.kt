package com.paulomenezes.sharedstorage

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.paulomenezes.sharedstorage.utils.ContentProviderUtils
import com.paulomenezes.sharedstorage.adapters.FileImageAdapter
import com.paulomenezes.sharedstorage.databinding.ActivityMainBinding
import com.paulomenezes.sharedstorage.models.FileImage

class MainActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_PERMISSION_CODE = 1
        const val REQUEST_DETAIL = 2
        const val REQUEST_DELETE = 3
        const val INTENT_EXTRA_NAME = "file"
        const val INTENT_EXTRA_DELETE = "delete"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: FileImageAdapter
    private val contentProvider = ContentProviderUtils(this)
    private var uriToDelete: Uri? = null

    private var images = mutableListOf<FileImage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        requestPermission()

        images = contentProvider.loadImages()

        adapter = FileImageAdapter(images) {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(INTENT_EXTRA_NAME, images[it])

            startActivityForResult(intent, REQUEST_DETAIL)
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
    }

    private fun showMessage(@StringRes resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                showMessage(R.string.permission_denied)
            } else if(images.size == 0) {
                images.addAll(contentProvider.loadImages())

                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val uri = data?.getParcelableExtra<Uri>(INTENT_EXTRA_DELETE)

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_DETAIL && uri != null) {
                uriToDelete = uri

                contentProvider.deleteImage(uri)
            } else if (requestCode == REQUEST_DELETE && uriToDelete != null) {
                contentProvider.deleteImage(uriToDelete!!)

                uriToDelete = null

                images.clear()
                images.addAll(contentProvider.loadImages())

                adapter.notifyDataSetChanged()
            }
        }
    }
}