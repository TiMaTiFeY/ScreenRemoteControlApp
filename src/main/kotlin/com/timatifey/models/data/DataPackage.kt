package com.timatifey.models.data

data class DataPackage(
    val dataType: DataType,
    val mouse: Mouse? = null,
    val key: Key? = null,
    val image: ByteArray? = null,
    val imageSize: ImageSize? = null,
    val message: String? = null
) {
    enum class DataType {
        KEY,
        MOUSE,
        IMAGE,
        MESSAGE,
        IMAGE_SIZE
    }
}
