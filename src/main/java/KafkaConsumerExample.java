
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaConsumerExample {
	private final static String TOPIC = "accounts";
	private final static String BOOTSTRAP_SERVERS = "localhost:9092";

	private static Consumer<Long, String> createConsumer() {
		final Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		// Create the consumer using props.
		final Consumer<Long, String> consumer = new KafkaConsumer<>(props);
		// Subscribe to the topic.
		consumer.subscribe(Collections.singletonList(TOPIC));
		return consumer;
	}

	static void runConsumer() throws InterruptedException {
		final Consumer<Long, String> consumer = createConsumer();
		final int giveUp = 100;
		int noRecordsCount = 0;
		while (true) {
			final ConsumerRecords<Long, String> consumerRecords = consumer.poll(1000);
			if (consumerRecords.count() == 0) {
				noRecordsCount++;
				if (noRecordsCount > giveUp)
					break;
				else
					continue;
			}
			consumerRecords.forEach(record -> {
				System.out.println("Consumer Record Key :" + record.key());
				System.out.println("Consumer Record Value:" + record.value());
				System.out.println("Consumer Record Partition:" + record.partition());
				System.out.println("Consumer Record Offset:" + record.offset());

				ElasticSearchClient.sendDataToElasticSearch(record.value());
			});
			consumer.commitAsync();
		}
		consumer.close();
		System.out.println("DONE");
	}

	public static void main(String... args) throws Exception {
		runConsumer();
	}
}
