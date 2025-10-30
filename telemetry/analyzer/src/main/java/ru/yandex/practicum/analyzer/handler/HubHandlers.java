package ru.yandex.practicum.analyzer.handler;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Component
public class HubHandlers {

    private final Map<String, HubHandler> handlers;


    public  HubHandlers(Set<HubHandler> handlers) {
        this.handlers = handlers.stream() .collect(Collectors.toMap(HubHandler::getMessageType, Function.identity()));
    }

   }

