package ru.yandex.practicum.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;

@Slf4j
@GrpcService
public class HubRouterController extends HubRouterControllerGrpc.HubRouterControllerImplBase {

    @Override
    public void handleDeviceAction(DeviceActionRequest request, StreamObserver<Empty> responseObserver) {
        try {
            log.info("Получено grpc сообщение {} от analyzer",
                    request);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Во время приема grpc сообщения в методе handleDeviceAction произошла ошибка {}",
                    e.getMessage());
            responseObserver.onError(new StatusRuntimeException(Status.fromThrowable(e)));
        }
    }
}

