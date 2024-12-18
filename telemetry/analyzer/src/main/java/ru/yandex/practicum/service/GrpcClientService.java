package ru.yandex.practicum.service;

import com.google.protobuf.Empty;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;

@Slf4j
@Service
public class GrpcClientService {
    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    public GrpcClientService(@GrpcClient("hub-router")
                             HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient) {
        this.hubRouterClient = hubRouterClient;
    }

    public void sendData(DeviceActionRequest deviceActionRequest) {
        Empty response = hubRouterClient.handleDeviceAction(deviceActionRequest);
        if (response.isInitialized()) {
            log.info("Получил ответ от hub-router");
        } else {
            log.info("Ответ от hub-router не получен");
        }
    }

}