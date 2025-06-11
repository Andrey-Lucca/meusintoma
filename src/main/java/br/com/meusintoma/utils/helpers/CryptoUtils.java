package br.com.meusintoma.utils.helpers;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.meusintoma.modules.email.exception.InvalidTokenException;

@Component
public class CryptoUtils {

    @Value("${app.crypto.pass}")
    String password;

    @Value("${app.crypto.salt}")
    String salt;

    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH = 128;

    public String encrypt(String stringToEncrypt) {
        try {
            SecretKey secretKey = getKeyFromPassword(password, salt);
            byte[] iv = generateIV();

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);

            byte[] encrypted = cipher.doFinal(stringToEncrypt.getBytes(StandardCharsets.UTF_8));

            byte[] encryptedWithIv = ByteBuffer
                    .allocate(iv.length + encrypted.length)
                    .put(iv)
                    .put(encrypted)
                    .array();

            return Base64.getEncoder().encodeToString(encryptedWithIv);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar", e);
        }
    }

    public String decrypt(String stringToDecrypt) {
        try {
            SecretKey secretKey = getKeyFromPassword(password, salt);
            byte[] decoded = Base64.getDecoder().decode(stringToDecrypt);

            ByteBuffer buffer = ByteBuffer.wrap(decoded);
            byte[] iv = new byte[IV_LENGTH];
            buffer.get(iv);
            byte[] encryptedText = new byte[buffer.remaining()];
            buffer.get(encryptedText);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

            byte[] decrypted = cipher.doFinal(encryptedText);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new InvalidTokenException("Token de confirmação inválido ou corrompido");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar", e);
        }
    }

    private byte[] generateIV() {
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    private SecretKey getKeyFromPassword(String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        return secret;
    }

}
