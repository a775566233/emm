package com.emm.util.check;

import com.emm.entity.template.CheckTemplate;

import java.util.regex.Pattern;

public class CheckTools {
    public static boolean check(String message, boolean isNullable, int minLength, int maxLength, Pattern pattern) {
        if (!isNullable) {
            return !(message == null || message.length() < minLength || message.length() > maxLength || !patternCheck(message, pattern));
        }
        if (message == null) {
            return true;
        } else {
            return !(message.length() < minLength || message.length() > maxLength || !patternCheck(message, pattern));
        }
    }

    public static boolean check(String message, boolean isNullable, int minLength, int maxLength) {
        if (!isNullable) {
            return !(message == null || message.length() < minLength || message.length() > maxLength);
        }
        if (message == null) {
            return true;
        } else {
            return !(message.length() < minLength || message.length() > maxLength);
        }
    }

    public static boolean check(String message, CheckTemplate template) {
        if (template == null) {
            return true;
        }
        return check(message, template.isNullable(), template.getMinLength(), template.getMaxLength(), template.getRegex());
    }

    public static boolean patternCheck(String message, Pattern pattern) {
        if (pattern == null) {
            return true;
        } else {
            return pattern.matcher(message).matches();
        }
    }

    public static boolean patternCheck(String message, String patternStr) {
        if (patternStr == null) {
            return true;
        } else {
            Pattern pattern = Pattern.compile(patternStr);
            return pattern.matcher(message).matches();
        }
    }
}
