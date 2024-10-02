package com.blog.practiceapi.common;

public enum UrlConstants {
    replacedURL("http://localhost:8072/temp/image/"),
    gcsBucketURL("https://storage.googleapis.com/imgtest_bucket"),
    gcsPrefix("https://storage.googleapis.com/");
    private final String url;

    UrlConstants(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
