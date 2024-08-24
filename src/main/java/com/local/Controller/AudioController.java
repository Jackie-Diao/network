package com.local.Controller;

import com.local.Service.AudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

@RestController
@RequestMapping("/api/audio")
public class AudioController {

    @Autowired
    private AudioService audioService;

    @GetMapping("/start")
    public ResponseEntity<String> startAudioStream() {
        try {
            audioService.startStreamingAudio();
            return ResponseEntity.ok("音频流已启动");
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("启动音频流失败");
        }
    }

    @GetMapping("/stop")
    public ResponseEntity<String> stopAudioStream() {
        audioService.stopStreamingAudio();
        return ResponseEntity.ok("音频流已停止");
    }
}
