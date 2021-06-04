package com.mairuis.sharp.request;

/**
 * @author Mairuis
 * @since 2021/6/3
 */
public class TestRequest implements Request {
    private final String url;
    private final byte[] content;

    public TestRequest(String url, byte[] content) {
        this.url = url;
        this.content = content;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public byte[] getContent() {
        return content;
    }
}
