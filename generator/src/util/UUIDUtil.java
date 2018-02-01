package util;

import java.util.UUID;

public class UUIDUtil {
    /**
     * 生成UUID，只包含数字和字母。
     */
    public static String generate() {
        return UUID.randomUUID().toString().replaceAll( "-", StringUtil.EMPTY );
    }
}
