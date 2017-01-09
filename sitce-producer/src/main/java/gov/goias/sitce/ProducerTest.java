package gov.goias.sitce;

import com.google.common.io.Resources;
import gov.goias.Sitce;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

/**
 * Created by thiago-rs on 12/22/16.
 */
public class ProducerTest {

    public static void main(String args[]) throws IOException {

        KafkaProducer<String, byte[]> producer;

        try (InputStream props = Resources.getResource("producer.properties").openStream()) {
            final Properties properties = new Properties();
            properties.load(props);
            producer = new KafkaProducer<>(properties);
        }

        final Random rdn = new Random();

        IntStream.range(1, 10).forEach(a -> {

            final int id = rdn.nextInt(Integer.SIZE - 1);
            System.out.println(id);

            Sitce.DomainUpdate du = Sitce.DomainUpdate
                    .newBuilder()
                    .setDesc(UUID.randomUUID().toString())
                    .setId(id)
                    .build();

            final Future<RecordMetadata> send = producer.send(new ProducerRecord<String, byte[]>("test", du.toByteArray()),
                (r, e) -> {
                    System.out.println(r);

                    if(e != null){
                        e.printStackTrace();
                    }
                });

        });

        producer.close();

    }
}
