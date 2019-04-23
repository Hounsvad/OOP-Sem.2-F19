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

/**
 *
 * @author Oliver
 */
public class StringUtils {

    private static final int BOLD_ASCII_OFFSET_LOWERCASE = 120205;
    private static final int BOLD_ASCII_OFFSET_UPPERCASE = 119743;

    /**
     * @param s String input
     * @return input string in Math Bold Latin characters instead of normal
     */
    public static String getBoldString(String s) {
        List<Character> input = Chars.asList(s.toCharArray());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.size(); i++) {
            if (Character.isLowerCase(input.get(i))) {
                sb.append(Character.toChars(BOLD_ASCII_OFFSET_LOWERCASE + input.get(i)));
            } else if (Character.isUpperCase(input.get(i))) {
                sb.append(Character.toChars(BOLD_ASCII_OFFSET_UPPERCASE + input.get(i)));
            } else {
                sb.append(input.get(i));
            }
        }
        return sb.toString();
    }
    public static String hash(String input) {
        return Hashing.sha256().hashString(input, Charset.forName("UTF-8")).toString();
    }
}
