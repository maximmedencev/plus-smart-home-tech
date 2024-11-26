package ru.yandex.practicum.event.hub;

import lombok.ToString;

@ToString(callSuper = true)
public class DeviceRemovedEvent extends HubEvent {
    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
