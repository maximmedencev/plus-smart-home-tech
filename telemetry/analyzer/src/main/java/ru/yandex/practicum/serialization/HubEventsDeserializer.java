package ru.yandex.practicum.serialization;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public class HubEventsDeserializer extends BaseAvroDeserializer<SensorsSnapshotAvro> {
    public HubEventsDeserializer() {
        super(HubEventAvro.getClassSchema());
    }
}