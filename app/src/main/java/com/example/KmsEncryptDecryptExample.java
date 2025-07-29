package com.example;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.*;

import java.util.Base64;

public class KmsEncryptDecryptExample {
    // 여기에 본인의 KMS 키 ARN 또는 키 ID를 넣으세요
    private static final String KEY_ID = "arn:aws:kms:ap-northeast-2:077672914621:key/0bdd096d-5099-4232-8efb-c624ba617e64";

    public static void main(String[] args) {
        String plainText = "Hello, AWS KMS with Gradle!";

        KmsClient kmsClient = KmsClient.builder()
                .region(Region.AP_NORTHEAST_2) // 서울 리전
                .build();

        try {
            // 암호화
            SdkBytes encryptedBlob = encryptText(kmsClient, plainText);
            String base64Cipher = Base64.getEncoder().encodeToString(encryptedBlob.asByteArray());
            System.out.println("Encrypted (Base64): " + base64Cipher);

            // 복호화
            String decrypted = decryptText(kmsClient, encryptedBlob);
            System.out.println("Decrypted: " + decrypted);

        } catch (KmsException e) {
            System.err.println("KMS error: " + e.getMessage());
        } finally {
            kmsClient.close();
        }
    }

    private static SdkBytes encryptText(KmsClient client, String text) {
        EncryptRequest req = EncryptRequest.builder()
                .keyId(KEY_ID)
                .plaintext(SdkBytes.fromUtf8String(text))
                .build();

        return client.encrypt(req).ciphertextBlob();
    }

    private static String decryptText(KmsClient client, SdkBytes blob) {
        DecryptRequest req = DecryptRequest.builder()
                .ciphertextBlob(blob)
                .build();

        return client.decrypt(req).plaintext().asUtf8String();
    }
} 