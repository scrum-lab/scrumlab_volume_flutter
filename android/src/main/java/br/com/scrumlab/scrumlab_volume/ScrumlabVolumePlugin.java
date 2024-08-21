package br.com.scrumlab.scrumlab_volume;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * ScrumlabVolumePlugin
 */
public class ScrumlabVolumePlugin implements FlutterPlugin, ActivityAware, MethodCallHandler {

    private MethodChannel channel;
    private Activity activity;
    private AudioManager audioManager;
    private int streamType = AudioManager.STREAM_MUSIC; // Default to STREAM_MUSIC

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        channel = new MethodChannel(binding.getBinaryMessenger(), "scrumlab_volume");
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
        channel = null;
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivity() {
        activity = null;
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        activity = null;
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        switch (call.method) {
            case "controlVolume":
                Integer streamTypeArgument = call.argument("streamType");
                if (streamTypeArgument != null) {
                    streamType = streamTypeArgument;
                    controlVolume(streamTypeArgument);
                }
                result.success(null);
                break;
            case "getMaxVol":
                result.success(getMaxVol());
                break;
            case "getVol":
                result.success(getVol());
                break;
            case "setVol":
                Integer newVol = call.argument("newVol");
                Integer showUiFlag = call.argument("showVolumeUiFlag");
                if (newVol != null && showUiFlag != null) {
                    setVol(newVol, showUiFlag);
                    result.success(0);
                } else {
                    result.error("INVALID_ARGUMENTS", "Arguments for 'newVol' and 'showVolumeUiFlag' are required.", null);
                }
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    private void controlVolume(int streamType) {
        if (activity != null) {
            activity.setVolumeControlStream(streamType);
        }
    }

    private void initAudioManager() {
        if (audioManager == null && activity != null) {
            audioManager = (AudioManager) activity.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        }
    }

    private int getMaxVol() {
        initAudioManager();
        return audioManager != null ? audioManager.getStreamMaxVolume(streamType) : 0;
    }

    private int getVol() {
        initAudioManager();
        return audioManager != null ? audioManager.getStreamVolume(streamType) : 0;
    }

    private void setVol(int volume, int showVolumeUiFlag) {
        initAudioManager();
        if (audioManager != null) {
            audioManager.setStreamVolume(streamType, volume, showVolumeUiFlag);
        }
    }
}
