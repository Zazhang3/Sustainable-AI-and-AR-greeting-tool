package com.tool.greeting_tool.common.utils;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyStore;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.GCMParameterSpec;

public class KeyStoreUtil {
    private static final String KEY_ALIAS = "LastLoginUser";
    private static final String TAG = "KeyStoreUtil";

    /**
     * Generate and store keywords
     */
    public static void generateKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            if (!keyStore.containsAlias(KEY_ALIAS)) {
                KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
                keyGenerator.init(
                        new KeyGenParameterSpec.Builder(KEY_ALIAS,
                                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                                .build());
                keyGenerator.generateKey();
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to generate key", e);
        }
    }

    /**
     * Encrypting Data
     *
     * @param plaintext Plain text string
     * @return Encrypted Base64 encoded string
     */
    public static String encryptData(String plaintext) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            Key key = keyStore.getKey(KEY_ALIAS, null);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] iv = cipher.getIV();
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + ciphertext.length);
            byteBuffer.put(iv);
            byteBuffer.put(ciphertext);
            byte[] encryptedBytes = byteBuffer.array();

            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            Log.e(TAG, "Failed to encrypt data", e);
            return null;
        }
    }

    /**
     * Decrypting Data
     *
     * @param encryptedData Encrypted Base64 encoded string
     * @return Decrypted plaintext string
     */
    public static String decryptData(String encryptedData) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            Key key = keyStore.getKey(KEY_ALIAS, null);

            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);

            ByteBuffer byteBuffer = ByteBuffer.wrap(encryptedBytes);
            byte[] iv = new byte[12];
            byteBuffer.get(iv);
            byte[] ciphertext = new byte[byteBuffer.remaining()];
            byteBuffer.get(ciphertext);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);

            byte[] plaintext = cipher.doFinal(ciphertext);
            return new String(plaintext, StandardCharsets.UTF_8);
        } catch (Exception e) {
            Log.e(TAG, "Failed to decrypt data", e);
            return null;
        }
    }
}
