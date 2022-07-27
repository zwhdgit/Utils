package com.zwh.utils.log;


public class ErrorBean {

    private TextBean text;
    private String msgtype = "text";

    public ErrorBean setText(TextBean text) {
        this.text = text;
        return this;
    }

    public static class TextBean {
        private String content;

        public TextBean setContent(String content) {
            this.content = content;
            return this;
        }
    }
}

