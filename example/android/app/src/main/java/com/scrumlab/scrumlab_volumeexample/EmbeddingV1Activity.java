package br.com.scrumlab.scrumlab_volumeexample;

import android.os.Bundle;

import br.com.scrumlab.scrumlab_volume.ScrumlabVolumePlugin;

import io.flutter.app.FlutterActivity;

public class EmbeddingV1Activity extends FlutterActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScrumlabVolumePlugin.registerWith(registrarFor("br.com.scrumlab.scrumlab_volume.ScrumlabVolumePlugin"));
    }
}