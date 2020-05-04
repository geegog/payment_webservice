package com.luminor.webservice.payment.infrastructure.http.service;

import com.luminor.webservice.payment.infrastructure.http.dto.Ip;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IpService {

    final String IP_URL = "https://api.ipify.org?format=json";

    public String ip() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(IP_URL, Ip.class).getBody().getIp();
    }

}
