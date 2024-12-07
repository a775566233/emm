package com.emm.util.uuid;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UUIDTools {
    public static byte[] getUUIDBytes() {
        return getUUIDBytes(UUID.randomUUID());
    }

    public static UUID getUUID(byte[] bytes) {
        if (bytes.length != 16) {
            throw new IllegalArgumentException("Array must be 16 bytes long");
        }
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return new UUID(buffer.getLong(), buffer.getLong());
    }

    public static byte[] getUUIDBytes(UUID uuid) {
        byte[] uuidBytes = new byte[16];
        return getUUIDBytes(uuidBytes, uuid);
    }

    public static byte[] getUUIDBytes(String uuidString) {
        byte[] uuidBytes = new byte[16];
        UUID uuid = UUID.fromString(uuidString);
        return getUUIDBytes(uuidBytes, uuid);
    }

    private static byte[] getUUIDBytes(byte[] uuidBytes, UUID uuid) {
        for (int i = 0; i < 8; i++) {
            uuidBytes[i] = (byte) (uuid.getMostSignificantBits() >>> (56 - i * 8));
            uuidBytes[8 + i] = (byte) (uuid.getLeastSignificantBits() >>> (56 - i * 8));
        }
        return uuidBytes;
    }
}
