package javacore2017;

import java.util.ArrayList;
import java.util.Arrays;

public class ParserString {
    public String[] getLineToken(String line) {
        String[] token = line.split(" ");
        if(token.length >= 3) {
            if (token[0].equals("setformula")) {
                String formula = "";
                for(int i = 2; i < token.length; ++i) {
                    formula += token[i] + " ";
                }
                token[2] = formula;
            }
        }
        return token;
    }

    public Pair getVariableInfo(String variable) throws Exception {
        Pair pair = new Pair();
        if(variable.length() < 2) {
            throw new Exception("Некорректный идентификатор!!!");
        }

        Character ch = variable.charAt(0);

        if(((int)ch < (int)'A') || ((int)ch > (int)'Z')) {
            throw new Exception("Некорректный идентификатор!!!");
        }

        String newStr = variable.substring(1, variable.length());

        Integer num;
        try {
            num = Integer.parseInt(newStr);
        } catch (NumberFormatException  ex) {
            throw new Exception("Некорректный идентификатор!!!");
        }

        pair.first = ch;
        pair.second = num;
        return pair;
    }


}
