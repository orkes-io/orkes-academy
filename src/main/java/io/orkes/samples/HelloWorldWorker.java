package io.orkes.samples;

import com.netflix.conductor.client.worker.Worker;
import io.orkes.conductor.client.*;
import io.orkes.conductor.client.automator.TaskRunnerConfigurer;
import io.orkes.samples.workers.HelloWorld;

import java.util.*;
import java.util.stream.Collectors;

public class HelloWorldWorker {

    private static final String KEY = "";       //Change

    private static final String SECRET = "";    //Change

    private static final String CONDUCTOR_SERVER_URL = "https://play.orkes.io/api";

    public static void main(String[] args) {

        //Use the constructor with KEY and SECRET when running against a sever that requires authentication
        ApiClient apiClient = new ApiClient(CONDUCTOR_SERVER_URL, KEY, SECRET);
        OrkesClients orkesClients = new OrkesClients(apiClient);
        TaskClient taskClient = orkesClients.getTaskClient();

        //Add a list with Worker implementations
        List<Worker> workers = Arrays.asList(new HelloWorld());

        TaskRunnerConfigurer.Builder builder = new TaskRunnerConfigurer.Builder(taskClient, workers);

        TaskRunnerConfigurer taskRunner = builder
                .withThreadCount(workers.size())
                .build();

        //Start Polling for tasks and execute them
        System.out.printf("\n\nStarting to poll for %s\n\n", workers.stream().map(Worker::getTaskDefName).collect(Collectors.toSet()));
        taskRunner.init();

    }
}
