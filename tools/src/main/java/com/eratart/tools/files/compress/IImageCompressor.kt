package com.eratart.tools.files.compress

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface IImageCompressor {
    fun compressByUri(uri: Uri): Flow<Uri>
}