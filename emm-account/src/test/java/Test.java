import com.emm.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;

import static com.emm.util.generate.RandomTools.randomInt;

public class Test {

    public static byte[] getUUIDBytes() {
        UUID uuid = UUID.randomUUID();
        byte[] uuidBytes = new byte[16];
        long mostSigBits = uuid.getMostSignificantBits();
        long leastSigBits = uuid.getLeastSignificantBits();
        for (int i = 0; i < 8; i++) {
            uuidBytes[i] = (byte) (mostSigBits >>> (56 - i * 8));
            uuidBytes[8 + i] = (byte) (leastSigBits >>> (56 - i * 8));
        }
        return uuidBytes;
    }

    public static UUID getUUID(byte[] bytes) {
        if (bytes.length != 16) {
            throw new IllegalArgumentException("Array must be 16 bytes long");
        }
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return new UUID(buffer.getLong(), buffer.getLong());
    }

    public static void main(String[] args) {
        /*byte[] uuidBytes = getUUIDBytes();
        System.out.println(Arrays.toString(uuidBytes));
        System.out.println(getUUID(uuidBytes));
        System.out.println(System.currentTimeMillis());
        String content = "abcde";
        try {
            String result = encrypt(content, "SHA-256", "utf-8");
            System.out.println(result);
            System.out.println(encrypt(result, "SHA-256", "utf-8"));
            result = "sihfdshfoifdhs" + "abcde" + "wfnissn";
            System.out.println(result);
            result = encrypt(result, "SHA-256", "utf-8");
            System.out.println(result);
            System.out.println(encrypt(result, "SHA-256", "utf-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }*/
        System.out.println(randomIntString(0, 9, 6));
    }

    public static String randomIntString(int min, int max, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(randomInt(min, max));
        }
        return new String(sb);
    }

    public static String encrypt(String content, String algorithm, String charsets) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] hash = md.digest(content.getBytes(charsets));
        StringBuffer sb = new StringBuffer();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
