package com.local.Service;

import com.local.utils.Tools;
import org.springframework.stereotype.Service;

@Service
public class LightService {

    private final String deviceIp = "127.0.0.1"; // 替换为实际设备 IP 地址

    public boolean controlLight(String action) {
        try {
            // 调用 Tools 类的方法来控制灯泡
            String result = Tools.turn_on_the_bulb(deviceIp);
            return result == null; // 如果返回值为 null，表示成功
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 捕获异常时返回失败
        }
    }
}
