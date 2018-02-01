package gov.goias;

import gov.goias.sitce.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.util.List;

@Configuration
@PropertySource("classpath:group.properties")
class ApplicationConfiguration {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${group.partitions}")
    private Integer partitions;

    @Value("#{'${subscribe.topics}'.split(',')}")
    private List<String> topics;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public ThreadPoolTaskExecutor threadPool() {
        final ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(0);
        pool.setMaxPoolSize(partitions);
        pool.setQueueCapacity(0);
        pool.setAwaitTerminationSeconds(10);
        return pool;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }


    @Bean
    @Scope("prototype")
    public Consumer consumer() throws IOException {
        return new Consumer(topics);
    }
}