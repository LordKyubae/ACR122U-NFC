package com.kyubae.nfc;

import org.nfctools.mf.MfAccess;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.block.MfBlock;
import org.nfctools.mf.card.MfCard;
import org.nfctools.mf.classic.Key;

import javax.smartcardio.CardException;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class Mifare {

    private static final Pattern HEX_STRING_PATTERN = Pattern.compile("^([0-9A-Fa-f]{2})+$");

    private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

    public static String block(MfReaderWriter reader, MfCard card, int sectorId, int blockId, List<String> keys) {
        for (String key : keys) {
            if (isValidMifareClassic1KKey(key)) {
                byte[] keyBytes = hexStringToBytes(key);
                MfAccess access = new MfAccess(card, sectorId, blockId, Key.A, keyBytes);
                String blockData = null;
                try {
                    MfBlock block = reader.readBlock(access)[0];
                    blockData = bytesToHexString(block.getData());
                } catch (IOException e) {
                    if (e.getCause() instanceof CardException) {
                        e.printStackTrace();
                    }
                }
                if (blockData == null) {
                    access = new MfAccess(card, sectorId, blockId, Key.B, keyBytes);
                    try {
                        MfBlock block = reader.readBlock(access)[0];
                        blockData = bytesToHexString(block.getData());
                    } catch (IOException e) {
                        if (e.getCause() instanceof CardException) {
                            e.printStackTrace();
                        }
                    }
                }
                if (blockData != null) {
                    return blockData;
                }
            }
        }
        return null;
    }

    private static boolean isValidMifareClassic1KKey(String s) {
        return isHexString(s) && (s.length() == 12);
    }

    private static boolean isHexString(String s) {
        return (s != null) && HEX_STRING_PATTERN.matcher(s).matches();
    }

    private static byte[] hexStringToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    private static String bytesToHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_CHARS[v >>> 4];
            hexChars[i * 2 + 1] = HEX_CHARS[v & 0x0F];
        }
        return new String(hexChars);
    }

}