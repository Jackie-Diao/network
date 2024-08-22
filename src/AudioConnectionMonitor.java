import javax.sound.sampled.*;
public class AudioConnectionMonitor extends Thread {
    private SourceDataLine sourceDataLine;
    private boolean isRunning;
    public AudioConnectionMonitor(SourceDataLine sourceDataLine) {
        this.sourceDataLine = sourceDataLine; // 在构造函数中赋值
        this.isRunning = true;
    }
    @Override
    public void run() {
        while (isRunning) {
            if (!sourceDataLine.isActive()) {
                onConnectionLost();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
    private void onConnectionLost() {
        // 通知应用程序音频连接已断开
        System.out.println("Audio connection lost!");

        // 停止心跳检测线程,释放资源
        isRunning = false;
        sourceDataLine.close();
    }
    public void stopMonitor() {
        isRunning = false;
    }
}
/*
// 在应用程序中使用
AudioFormat audioFormat = new AudioFormat(44100, 16, 2, true, false);
SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(audioFormat);
sourceDataLine.open(audioFormat);
sourceDataLine.start();

AudioConnectionMonitor monitor = new AudioConnectionMonitor(sourceDataLine);
monitor.start();

// 当不需要监听音频连接时
monitor.stopMonitor();
 */
