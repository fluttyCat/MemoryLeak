package com.baloot.app.encryption.config

import com.baloot.app.encryption.core.enums.CipherAlgorithm
import com.baloot.app.encryption.core.enums.EncryptionMode
import com.baloot.app.encryption.core.enums.EncryptionPadding

data class EncryptionConfig(
    var cipherAlgorithm: CipherAlgorithm,
    val alias: String,
    var blockMode: EncryptionMode,
    var encryptionPadding: EncryptionPadding
)