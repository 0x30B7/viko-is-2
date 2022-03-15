package dev.mantas.is.antra;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;

public class EncryptionHelper {

    private Cipher cipher;
    private IvParameterSpec iv;
    private Key key;

    public void init(String key, String mode, String iv) throws Exception {
        this.cipher = Cipher.getInstance("DES/" + mode + "/NoPadding");
        this.key = new SecretKeySpec(Hex.decodeHex(key), "DES");

        if (!"ECB".equals(mode)) {
            this.iv = new IvParameterSpec(Hex.decodeHex(iv));
        }
    }

    public String encrypt(String content) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] encrypted = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(encrypted);
    }

    public String decrypt(String encrypted) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] data = cipher.doFinal(Base64.decodeBase64(encrypted));
        return new String(data);
    }

}
