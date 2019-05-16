/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.presentation.utils;

import com.google.common.hash.Hashing;
import com.google.common.primitives.Chars;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Oliver
 */
public class StringUtils {

    /**
     * The offset on the Unicode table to convert a lowercase character to a
     * Math Sans-Serif Bold character
     */
    private static final int BOLD_UNICODE_OFFSET_LOWERCASE = 120205;

    /**
     * The offset on the Unicode table to convert a uppercase character to a
     * Math Sans-Serif Bold character
     */
    private static final int BOLD_UNICODE_OFFSET_UPPERCASE = 120211;

    /**
     * The offset on the Unicode table to convert a number character to a Math
     * Sans-Serif Bold digit
     */
    private static final int BOLD_UNICODE_OFFSET_NUMBER = 120764;

    /**
     * Converts a string into a string written with bold characters
     *
     * @param s The String input
     * @return The input string in Math Bold Latin characters instead of normal
     */
    public static String getBoldString(String s) {
        //Check if the system is capable os the bold characters used in this method
        String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
            return s;
        } else if (OS.indexOf("win") >= 0) {
            //Continue
        } else if (OS.indexOf("nux") >= 0) {
            //detectedOS = OSType.Linux;
        } else {
            return s;
        }
        List<Character> input = Chars.asList(s.toCharArray());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.size(); i++) {
            if (Character.isLowerCase(input.get(i))) {
                sb.append(Character.toChars(BOLD_UNICODE_OFFSET_LOWERCASE + input.get(i)));
            } else if (Character.isUpperCase(input.get(i))) {
                sb.append(Character.toChars(BOLD_UNICODE_OFFSET_UPPERCASE + input.get(i)));
            } else if (Character.isDigit(input.get(i))) {
                sb.append(Character.toChars(BOLD_UNICODE_OFFSET_NUMBER + input.get(i)));
            } else {
                sb.append(input.get(i));
            }
        }
        return sb.toString();
    }

    /**
     *
     * @param input
     * @return input hashed in sha256
     */
    public static String hash(String input) {
        return Hashing.sha256().hashString(input, Charset.forName("UTF-8")).toString();
    }
}
