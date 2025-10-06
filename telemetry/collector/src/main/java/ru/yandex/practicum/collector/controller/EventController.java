package ru.yandex.practicum.collector.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.collector.model.hubEvent.HubEvent;
import ru.yandex.practicum.collector.model.sensorEvent.SensorEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.collector.service.CollectorService;

@Slf4j
@Controller
@RequestMapping(path = "/events")
public class EventController {

    private final CollectorService collectorService;

    public EventController(CollectorService collectorService) {
        this.collectorService = collectorService;
    }

    @PostMapping("/sensors")
    @ResponseStatus(HttpStatus.OK)
    public void collectSensorEvent(@Valid @RequestBody SensorEvent sensorEvent) {
        try {
            log.info("Создание обработчика событий датчиков. " + sensorEvent.toString());

            collectorService.collectSensorEvent(sensorEvent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/hubs")
    @ResponseStatus(HttpStatus.OK)
    public void collectHubEvent(@Valid @RequestBody HubEvent hubEvent) {
        try {
            log.info("Создание обработчика событий хабов. " + hubEvent.toString());
            collectorService.collectHubEvent(hubEvent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
