package com.serenegiant.encoder

import android.Manifest
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import androidx.annotation.RequiresPermission
import com.topdon.remoteservice.utils.Logger

/***
 * AudioRecord简单封装
 *
 */
class AudioCatcher {
    private var mAudioRecord: AudioRecord? = null
    private var mMinBufferSize = 0
    private var mCaptureThread: Thread? = null
    var isCaptureStarted = false
        private set

    @Volatile
    private var mIsLoopExit = false

    private var mAudioFrameCapturedListener: OnAudioFrameCapturedListener? = null

    interface OnAudioFrameCapturedListener {
        fun onAudioFrameCaptured(audioData: ByteArray?)
    }

    fun setOnAudioFrameCapturedListener(listener: OnAudioFrameCapturedListener?) {
        mAudioFrameCapturedListener = listener
    }

    private val AUDIO_SOURCES = intArrayOf(
        MediaRecorder.AudioSource.DEFAULT,
        MediaRecorder.AudioSource.MIC,
        MediaRecorder.AudioSource.CAMCORDER
    )

    /**
     * 开始采集
     */
    @JvmOverloads
    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    fun startCapture(
        audioSource: Int = DEFAULT_SOURCE,
        sampleRateInHz: Int = DEFAULT_SAMPLE_RATE,
        channelConfig: Int = DEFAULT_CHANNEL_CONFIG,
        audioFormat: Int = DEFAULT_AUDIO_FORMAT
    ): Boolean {
        Logger.d(TAG, "startCapture: ");
        if (isCaptureStarted) {
            Log.e(TAG, "Capture already started !")
            return false
        }
        mMinBufferSize = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat)
        if (mMinBufferSize == AudioRecord.ERROR_BAD_VALUE) {
            Log.e(TAG, "Invalid parameter !")
            return false
        }

        Log.d(TAG, "getMinBufferSize = $mMinBufferSize bytes !")
//        for (src in AUDIO_SOURCES) {
//            try {
//
//                mAudioRecord = AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, mMinBufferSize)
//                if (mAudioRecord?.state != AudioRecord.STATE_INITIALIZED) {
//                    mAudioRecord?.release()
//                    mAudioRecord = null
//                }
//            } catch (e: Exception) {
//                mAudioRecord = null
//            }
//            if (mAudioRecord != null) {
//                break
//            }
//        }
        mAudioRecord = AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, mMinBufferSize)
        if (mAudioRecord!!.state == AudioRecord.STATE_UNINITIALIZED) {
            Log.e(TAG, "AudioRecord initialize fail !")
            return false
        }

        mAudioRecord?.startRecording()
        mIsLoopExit = false
        mCaptureThread = Thread(AudioCaptureRunnable())
        mCaptureThread?.start()
        isCaptureStarted = true

        Log.d(TAG, "Start audio capture success !")
        return true
    }

    /**
     * 停止采集，释放资源
     */
    fun stopCapture() {
        Logger.d(TAG, "stopCapture: ")
        if (!isCaptureStarted) {
            return
        }
        mIsLoopExit = true
        try {
            mCaptureThread?.interrupt()
            mCaptureThread?.join(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        if (mAudioRecord?.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
            mAudioRecord?.stop()
        }
        mAudioRecord?.release()
        isCaptureStarted = false

        Log.d(TAG, "Stop audio capture success !")
    }
  fun release(){
      stopCapture()
      mAudioFrameCapturedListener = null
  }

    /**
     * 定义采集线程
     */
    private inner class AudioCaptureRunnable : Runnable {
        override fun run() {
            while (!mIsLoopExit) {
                val buffer = ByteArray(mMinBufferSize)
                val ret = mAudioRecord?.read(buffer, 0, mMinBufferSize)
                if (ret == AudioRecord.ERROR_INVALID_OPERATION) {
                    Log.e(TAG, "Error ERROR_INVALID_OPERATION")
                } else if (ret == AudioRecord.ERROR_BAD_VALUE) {
                    Log.e(TAG, "Error ERROR_BAD_VALUE")
                } else {
                    if (mAudioFrameCapturedListener != null) {
                        mAudioFrameCapturedListener!!.onAudioFrameCaptured(buffer)
                    }
                    Log.d(TAG, "OK, Captured $ret bytes !")
                }
            }
        }
    }

    /**
     * 伴生对象:用来定义初始化的一些配置参数
     */
    companion object {
        private const val TAG = "AudioCatcher"

        //设置audioSource音频采集的输入源(可选的值以常量的形式定义在 MediaRecorder.AudioSource 类中，常用的值包括：DEFAULT（默认），VOICE_RECOGNITION（用于语音识别，等同于DEFAULT），MIC（由手机麦克风输入），VOICE_COMMUNICATION（用于VoIP应用）)
        private const val DEFAULT_SOURCE = MediaRecorder.AudioSource.MIC

        //设置sampleRateInHz采样率（注意，目前44100Hz是唯一可以保证兼容所有Android手机的采样率。）
        private const val DEFAULT_SAMPLE_RATE = 44100

        //设置channelConfig通道数,可选的值以常量的形式定义在 AudioFormat 类中，常用的是 CHANNEL_IN_MONO（单通道），CHANNEL_IN_STEREO（双通道）
        private const val DEFAULT_CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO

        //设置audioFormat数据位宽,可选的值也是以常量的形式定义在 AudioFormat 类中，常用的是 ENCODING_PCM_16BIT（16bit），ENCODING_PCM_8BIT（8bit），注意，前者是可以保证兼容所有Android手机的。
        private const val DEFAULT_AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT
        //注意还有第五个最重要参数bufferSizeInBytes,它配置的是 AudioRecord 内部的音频缓冲区的大小，该缓冲区的值不能低于一帧“音频帧”（Frame）的大小，一帧音频帧的大小计算如下
        //int size = 采样率 x 位宽 x 采样时间 x 通道数(由于厂商的定制化,强烈建议通过AudioRecord类的getMinBufferSize方法确定bufferSizeInBytes的大小,getMinBufferSize方法:int getMinBufferSize(int sampleRateInHz, int channelConfig, int audioFormat);)

    }
}
