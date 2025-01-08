package ru.yandex.practicum.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class NotEnoughProductsInWarehouseException extends RuntimeException {
    private String userMessage;
    private Map<String, Integer> missingProducts;

    public NotEnoughProductsInWarehouseException(String userMessage, Map<String,
            Integer> missingProducts) {
        super(userMessage);

        StringBuilder sbUserMessage = new StringBuilder(userMessage);
        for (Map.Entry<String, Integer> entry : missingProducts.entrySet()) {
            sbUserMessage.append(" id = ")
                    .append(entry.getKey())
                    .append(" не хватило ")
                    .append(entry.getValue())
                    .append(" шт. ");
        }

        this.setUserMessage(sbUserMessage.toString());
    }
}
