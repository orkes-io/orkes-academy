package io.orkes.samples;

import com.netflix.conductor.client.worker.Worker;
import io.orkes.conductor.client.*;
import io.orkes.conductor.client.automator.TaskRunnerConfigurer;
import io.orkes.samples.workers.HelloWorld;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HelloWorldWorker {

    private static final String KEY = "10889a2d-df32-49f0-8818-55a54dcdc8aa";       //Change

    private static final String SECRET = "0NykebgQTkCNERnFaMxhtfNisKJtdTzeeBuxNyZoWpOy3Uza";    //Change

    private static final String CONDUCTOR_SERVER_URL = "https://play.orkes.io/api";

    public static void main(String[] args) {
        String userName = System.getProperty("user.name");
        System.out.println("User name set to " + userName);

        //Use the constructor with KEY and SECRET when running against a sever that requires authentication
        ApiClient apiClient = new ApiClient(CONDUCTOR_SERVER_URL, KEY, SECRET);
        OrkesClients orkesClients = new OrkesClients(apiClient);
        TaskClient taskClient = orkesClients.getTaskClient();

        //Add a list with Worker implementations
        List<Worker> workers = Arrays.asList(new HelloWorld());

        TaskRunnerConfigurer.Builder builder = new TaskRunnerConfigurer.Builder(taskClient, workers);

        Map<String, String> domainMap = new HashMap<>();
        workers.stream().forEach(worker -> domainMap.put(worker.getTaskDefName(), userName));
        TaskRunnerConfigurer taskRunner = builder
                .withThreadCount(workers.size())
                .withTaskPollTimeout(1000)
                .withTaskToDomain(domainMap)
                .build();

        //Start Polling for tasks and execute them
        System.out.printf("\n\nStarting to poll for %s\n\n", workers.stream().map(Worker::getTaskDefName).collect(Collectors.toSet()));
        taskRunner.init();

    }
}
