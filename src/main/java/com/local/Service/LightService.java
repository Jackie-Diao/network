package com.local.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LightService {

    private final String deviceUrl="http://device-address/api/light";

    public boolean controlLight(String action) {
        RestTemplate restTemplate=new RestTemplate();
        String url= deviceUrl + "?action=" + action;

        try {
            restTemplate.postForObject(url, null, String.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
