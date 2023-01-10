package com.parisa.app.encryption.config

import com.parisa.app.encryption.core.enums.CipherAlgorithm
import com.parisa.app.encryption.core.enums.EncryptionMode
import com.parisa.app.encryption.core.enums.EncryptionPadding

data class EncryptionConfig(
    var cipherAlgorithm: CipherAlgorithm,
    val alias: String,
    var blockMode: EncryptionMode,
    var encryptionPadding: EncryptionPadding
)