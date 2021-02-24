package com.paulomenezes.sharedstorage.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FileImage(
    var name: String,
    var uri: Uri,
) : Parcelable
