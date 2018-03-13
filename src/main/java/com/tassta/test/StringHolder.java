package com.tassta.test;

public class StringHolder {
    private StringBuffer str;

    public StringHolder(String str) {
        this.str = new StringBuffer(str);
    }

    public void appendValue(int value) {
        str.append(value);
    }
    public void setValue(String value) {
        str.replace(0, str.length(), value);
    }

    public String getValue() {
        return str.toString();
    }

    @Override
    public String toString() {
        return str.toString();
    }
}
