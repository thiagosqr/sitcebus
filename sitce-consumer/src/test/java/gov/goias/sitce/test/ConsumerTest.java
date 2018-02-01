package gov.goias.sitce.test;

import com.google.common.io.Resources;
import gov.goias.Sitce;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

/**
 * Created by thiago on 02/01/17.
 */
public class ConsumerTest {

    public static void main(String[] args) throws IOException {

        KafkaConsumer<String, byte[]> consumer;

        try (InputStream props = Resources.getResource("consumer.properties").openStream()) {
            final Properties properties = new Properties();
            properties.load(props);
            consumer = new KafkaConsumer<>(properties);
        }

        //Kafka Consumer subscribes list of topics here.
        consumer.subscribe(Arrays.asList("test"));

        while (true) {
            ConsumerRecords<String, byte[]> records = consumer.poll(100);
            for (ConsumerRecord<String, byte[]> record : records){

                byte[] value = record.value();

                Sitce.DomainUpdate du = Sitce.DomainUpdate
                        .newBuilder()
                        .mergeFrom(value)
                        .build();

                System.out.printf("Domain Update ID = %d, desc = %s \n", du.getId(), du.getDesc());

                // print the offset,key and value for the consumer records.
                System.out.printf("Record offset = %d, key = %s, value = %s\n",
                        record.offset(), record.key(), value);

            }
        }

    }
}
