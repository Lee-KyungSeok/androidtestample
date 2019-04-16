package com.example.testsample.unit.fundamentals;

public class StringFunc {

    public String reverse(String string) {

        StringBuilder sb = new StringBuilder();
        for (int i = string.length() - 1; i >= 0; i--) {
            sb.append(string.charAt(i));
        }

        return sb.toString();
    }

    public String duplicate(String string) {
        return string + string;
    }
}
