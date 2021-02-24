package com.paulomenezes.sharedstorage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.paulomenezes.sharedstorage.databinding.ActivityDetailBinding
import com.paulomenezes.sharedstorage.models.FileImage

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val file = intent.getParcelableExtra<FileImage>(MainActivity.INTENT_EXTRA_NAME)

        binding.imageViewDetail.setImageURI(file?.uri)
        binding.buttonRemove.setOnClickListener {
            val intent = Intent()

            intent.putExtra(MainActivity.INTENT_EXTRA_DELETE, file?.uri)

            setResult(RESULT_OK, intent)
            finish()
        }
    }
}