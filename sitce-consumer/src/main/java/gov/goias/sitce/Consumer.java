package gov.goias.sitce;

import com.google.common.io.Resources;
import com.google.protobuf.InvalidProtocolBufferException;
import gov.goias.Sitce.DomainUpdate;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by thiago on 09/01/17.
 */
public class Consumer implements Runnable {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    final KafkaConsumer<String, byte[]> consumer;

    final AtomicBoolean closed = new AtomicBoolean(false);

    public Consumer(List<String> topics, int bufferSize) throws IOException {

        try (InputStream props = Resources.getResource("consumer.properties").openStream()) {
            final Properties properties = new Properties();
            properties.load(props);
            consumer = new KafkaConsumer<>(properties);
        }

        //Kafka Consumer subscribes list of topics here.
        consumer.subscribe(topics);
    }

    @Override
    public void run() {

        LOGGER.info("Starting new consumer Thread: " +Thread.currentThread().getName());

        while (!closed.get()) {

            ConsumerRecords<String, byte[]> records = consumer.poll(100);

            for (ConsumerRecord<String, byte[]> record : records){

                final byte[] value = record.value();

                try {

                    final DomainUpdate du = DomainUpdate
                            .newBuilder()
                            .mergeFrom(value)
                            .build();

                    System.out.printf("Domain Update ID = %d, desc = %s \n", du.getId(), du.getDesc());

                    // print the offset,key and value for the consumer records.
                    System.out.printf("Record offset = %d, key = %s, array size = %s\n",
                            record.offset(), record.key(), value.length);

                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
