package gov.goias;

import gov.goias.sitce.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by thiago on 09/01/17.
 */
@PropertySource("classpath:group.properties")
@Component
class Runner implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${group.partitions}")
    private Integer partitions;

    @Autowired
    private ThreadPoolTaskExecutor pool;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run(String[] args) throws Exception {

        for(int i=0; i<partitions;i++){
            pool.execute(applicationContext.getBean(Consumer.class));
        }

    }
}
