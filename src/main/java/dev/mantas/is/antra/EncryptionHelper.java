package dev.mantas.is.antra;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;

/**
 * Class containing content encryption and decryption functionality
 */
public class EncryptionHelper {

    private Cipher cipher;
    private IvParameterSpec iv;
    private Key key;

    /**
     * Initializes the cryptography algorithm and its components
     *
     * @param key The key served to the algorithm
     * @param mode The cipher mode
     * @param iv The initialization vector, used when applicable
     * @throws Exception If an error occurs during initialization
     */
    public void init(String key, String mode, String iv) throws Exception {
        this.cipher = Cipher.getInstance("DES/" + mode + "/NoPadding");
        this.key = new SecretKeySpec(Hex.decodeHex(key), "DES");

        if (!"ECB".equals(mode)) {
            this.iv = new IvParameterSpec(Hex.decodeHex(iv));
        }
    }

    /**
     * Encrypts the given plaintext with the initialized cipher
     *
     * @param content The plaintext content to be encrypted
     * @return The encrypted ciphertext
     * @throws Exception If an error occurs during encryption
     */
    public String encrypt(String content) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] encrypted = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(encrypted);
    }

    /**
     * Decrypts the given ciphertext with the initialized cipher
     *
     * @param encrypted The encrypted ciphertext to be decrypted
     * @return The decrypted plaintext
     * @throws Exception If an error occurs during encryption
     */
    public String decrypt(String encrypted) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] data = cipher.doFinal(Base64.decodeBase64(encrypted));
        return new String(data);
    }

}
