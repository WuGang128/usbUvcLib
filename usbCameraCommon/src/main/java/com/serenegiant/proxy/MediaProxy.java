package com.serenegiant.proxy;

import java.nio.ByteBuffer;

public class MediaProxy {
    private static IMediaProxyImpl mediaProxyImpl;

    public static void setMediaImpl(IMediaProxyImpl mediaImpl) {
        mediaProxyImpl = mediaImpl;
    }

    public  static void onAudioResult(byte[] audioData, long timestamp) {
          if (mediaProxyImpl!=null){
              mediaProxyImpl.onAudioResult(audioData,timestamp);
          }
    }

    public  static void onVideoResult(ByteBuffer byteBuffer, long timestamp) {
        if (mediaProxyImpl!=null){
            mediaProxyImpl.onVideoResult(byteBuffer,timestamp);
        }
    }
}
