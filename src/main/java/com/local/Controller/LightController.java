package com.local.Controller;

import com.local.Service.LightService;
import com.local.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/control")
public class LightController {

    @Autowired
    private LightService lightService;

    @PostMapping("/light")
    public Result controlLight(@RequestParam String action) {
        boolean success= lightService.controlLight(action);
        if (success) {
            return Result.success("灯泡" + action);
        } else {
            return Result.error("控制灯泡失败");
        }
    }
}