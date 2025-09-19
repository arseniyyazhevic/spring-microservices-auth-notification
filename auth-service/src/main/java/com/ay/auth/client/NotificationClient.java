package com.ay.auth.client;

import com.ay.auth.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(
        name = "notification-service",
        url = "${notification.service.url}"
)
public interface NotificationClient {

    @PostMapping()
    void sendNotification(@RequestBody NotificationRequest request);
}

