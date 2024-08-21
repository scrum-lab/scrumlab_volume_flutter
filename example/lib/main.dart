import 'package:flutter/material.dart';
import 'dart:async';
import 'package:scrumlab_volume/scrumlab_volume.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  late AudioManager audioManager;
  int? maxVol, currentVol;
  ShowVolumeUI showVolumeUI = ShowVolumeUI.SHOW;

  @override
  void initState() {
    super.initState();
    audioManager = AudioManager.STREAM_SYSTEM;
    initAudioStreamType();
    updateVolumes();
  }

  Future<void> initAudioStreamType() async {
    await Volume.controlVolume(AudioManager.STREAM_SYSTEM);
  }

  updateVolumes() async {
    // get Max Volume
    maxVol = await Volume.getMaxVol;
    // get Current Volume
    currentVol = await Volume.getVol;
    setState(() {});
  }

  setVol(int i) async {
    await Volume.setVol(i, showVolumeUI: showVolumeUI);
    updateVolumes();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: <Widget>[
              DropdownButton<AudioManager>(
                value: audioManager,
                items: [
                  DropdownMenuItem(
                    child: Text("In Call Volume"),
                    value: AudioManager.STREAM_VOICE_CALL,
                  ),
                  DropdownMenuItem(
                    child: Text("System Volume"),
                    value: AudioManager.STREAM_SYSTEM,
                  ),
                  DropdownMenuItem(
                    child: Text("Ring Volume"),
                    value: AudioManager.STREAM_RING,
                  ),
                  DropdownMenuItem(
                    child: Text("Media Volume"),
                    value: AudioManager.STREAM_MUSIC,
                  ),
                  DropdownMenuItem(
                    child: Text("Alarm volume"),
                    value: AudioManager.STREAM_ALARM,
                  ),
                  DropdownMenuItem(
                    child: Text("Notifications Volume"),
                    value: AudioManager.STREAM_NOTIFICATION,
                  ),
                ],
                isDense: true,
                onChanged: (AudioManager? aM) async {
                  if (aM != null) {
                    setState(() {
                      audioManager = aM;
                    });
                    await Volume.controlVolume(aM);
                    updateVolumes();
                  }
                },
              ),
              ToggleButtons(
                borderRadius: BorderRadius.all(Radius.circular(10.0)),
                children: <Widget>[
                  Padding(
                    padding: const EdgeInsets.all(20.0),
                    child: Text(
                      "Show UI",
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.all(20.0),
                    child: Text(
                      "Hide UI",
                    ),
                  ),
                ],
                isSelected: [
                  showVolumeUI == ShowVolumeUI.SHOW,
                  showVolumeUI == ShowVolumeUI.HIDE
                ],
                onPressed: (int i) {
                  setState(() {
                    if (i == 0) {
                      showVolumeUI = ShowVolumeUI.SHOW;
                    } else if (i == 1) {
                      showVolumeUI = ShowVolumeUI.HIDE;
                    }
                  });
                },
              ),
              if (currentVol != null && maxVol != null)
                Slider(
                  value: (currentVol ?? 0).toDouble(),
                  divisions: maxVol,
                  max: (maxVol ?? 1).toDouble(),
                  min: 0,
                  onChanged: (double d) {
                    setVol(d.toInt());
                  },
                ),
            ],
          ),
        ),
      ),
    );
  }
}
