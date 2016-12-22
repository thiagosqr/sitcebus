import com.google.common.io.Resources;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by thiago-rs on 12/22/16.
 */
public class ProducerTest {

    public static void main(String args[]) throws IOException {

        KafkaProducer<String, String> producer;

        try (InputStream props = Resources.getResource("producer.properties").openStream()) {
            final Properties properties = new Properties();
            properties.load(props);
            producer = new KafkaProducer<>(properties);
        }

        producer.send(new ProducerRecord<String, String>("test","Teste"));
        producer.close();

    }
}
