import com.emm.entity.permission.PermissionModel;
import com.emm.entity.template.CheckTemplate;
import com.emm.util.check.CheckTools;
import com.emm.util.file.FileTools;
import com.emm.util.json.Deserialize;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

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
        //System.out.println(CheckTools.check("1627453256@qq.com", false, 1, 30, Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")));
        PermissionModel permissionModel;
        HashMap<String, CheckTemplate> checkTemplateMap;
        String jsonString;
//        try {
//            jsonString = FileTools.getFileContent("C:\\Users\\A7755\\IdeaProjects\\emm\\emm-account\\data\\config\\permission.json");
//            System.out.println(jsonString);
//            permissionModel = Deserialize.toObject(jsonString, PermissionModel.class);
//            System.out.println(permissionModel);
//        } catch (IOException e) {
//            System.out.println("权限配置设置出错，读取相关json文件异常：" + e);
//            throw new RuntimeException();
//        }
        try {
            jsonString = FileTools.getFileContent("C:\\Users\\A7755\\IdeaProjects\\emm\\emm-account\\data\\config\\check-template.json");
            List<CheckTemplate> checkTemplates = Deserialize.toList(jsonString, new TypeReference<List<CheckTemplate>>() {});
            System.out.println(checkTemplates);
            checkTemplateMap = new HashMap<>();
            for (CheckTemplate checkTemplate : checkTemplates) {
                checkTemplateMap.put(checkTemplate.getFieldName(), checkTemplate);
            }
            System.out.println(checkTemplateMap);
        } catch (IOException e) {
            System.out.println("输入检查模板设置出错，读取相关json文件异常：{}" + e);
            throw new RuntimeException();
        }
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
