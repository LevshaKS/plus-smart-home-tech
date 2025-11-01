package ru.yandex.practicum.analyzer;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.service.HubEventProcessor;
import ru.yandex.practicum.analyzer.service.SnapshotProcessor;

@Component
@AllArgsConstructor
public class AnalyzerRunner implements CommandLineRunner {
    private final HubEventProcessor hubEventProcessor;
    private final SnapshotProcessor snapshotProcessor;


    @Override
    public void run(String... args) throws Exception {

        Thread hubEventsThread = new Thread(hubEventProcessor);
        hubEventsThread.setName("HubEventHandlerThread");
        hubEventsThread.start();
        // запускаем в отдельном потоке обработчик событий
        // от пользовательских хабов

        // В текущем потоке начинаем обработку
        // снимков состояния датчиков
        snapshotProcessor.run();
    }
}
