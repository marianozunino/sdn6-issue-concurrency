package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableRetry
public class DemoApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(ARepository aRepository) {
        return args -> {
            var a = new A();
            a.setId("ac5cf47a-55a9-44a3-bb04-c2e88ff0a6b3");
            aRepository.save(a);

            createBs(aRepository);

            a = aRepository.findById("ac5cf47a-55a9-44a3-bb04-c2e88ff0a6b3").get();
            System.out.println(a);
        };
    }

    private void createBs(ARepository aRepository) throws InterruptedException, java.util.concurrent.ExecutionException {
        ExecutorService EXEC = Executors.newFixedThreadPool(10);
        var tasks = new ArrayList<Callable<String>>();
        for (int i = 0; i < 10; i++) {
            var c = new Callable<String>() {
                @Override
                @Retryable(
                        include = {OptimisticLockingFailureException.class},
                        maxAttempts = 5,
                        backoff = @Backoff(delay = 50, multiplier = 2, random = true)
                )
                public String call() {
                    var a = aRepository.findById("ac5cf47a-55a9-44a3-bb04-c2e88ff0a6b3").get();
                    a.getBs().add(new B());
                    aRepository.save(a);
                    return "finish";
                }
            };
            tasks.add(c);
        }

        var results = EXEC.invokeAll(tasks);
        System.out.println(results);
        for (var fr : results) {
            fr.get();
        }
    }
}
