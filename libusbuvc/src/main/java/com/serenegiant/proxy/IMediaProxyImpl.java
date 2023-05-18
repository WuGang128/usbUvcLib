package com.serenegiant.proxy;

import java.nio.ByteBuffer;

public interface IMediaProxyImpl {
    void onAudioResult(byte[] audioData, long timestamp);
    void onVideoResult(ByteBuffer byteBuffer, long timestamp);
}
