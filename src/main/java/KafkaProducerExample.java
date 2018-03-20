import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class KafkaProducerExample {

	private final static String TOPIC = "accounts";
	private final static String BOOTSTRAP_SERVERS = "localhost:9092";

	private static Producer<Long, String> createProducer() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return new KafkaProducer<>(props);
	}

	static void runProducer(final int sendMessageCount) throws Exception {
		final Producer<Long, String> producer = createProducer();
		long time = System.currentTimeMillis();
		ArrayList<String> data = loadData();
		try {
			for (long index = time; index < time + sendMessageCount; index++) {
				int i = (int) (index - time);
				final ProducerRecord<Long, String> record = new ProducerRecord<>(TOPIC, index, data.get(i));

				RecordMetadata metadata = producer.send(record).get();

				long elapsedTime = System.currentTimeMillis() - time;
				System.out.printf("sent record(key=%s value=%s) " + "meta(partition=%d, offset=%d) time=%d\n",
						record.key(), record.value(), metadata.partition(), metadata.offset(), elapsedTime);

			}
		} finally {
			producer.flush();
			producer.close();
		}
	}

	static ArrayList<String> loadData() {
		File f = new File("src/sampleData.txt");
		Scanner s = null;
		ArrayList<String> al = new ArrayList<String>();
		try {
			s = new Scanner(f);
			al.add("1,39225,Amber,Duke,32,M,880 Holmes Lane,Pyrami,amberduke@pyrami.com,Brogan,IL");
			al.add("6,5686,Hattie,Bond,36,M,671 Bristol Street,Netagy,hattiebond@netagy.com,Dante,TN");
			al.add("102,29712,Dena,Olson,27,F,759 Newkirk Avenue,Hinway,denaolson@hinway.com,Choctaw,NJ");
			al.add("140,26696,Cotton,Christensen,32,M,878 Schermerhorn Street,Prowaste,cottonchristensen@prowaste.com,Mayfair,LA");

			while (s.hasNext()) {
				al.add(s.nextLine());
			}

		} catch (FileNotFoundException fnfe) {
			System.out.println("error while loading data:" + fnfe.getMessage());
		} finally {
			s.close();
		}

		return al;
	}

	public static void main(String... args) throws Exception {
		if (args.length == 0) {
			runProducer(50);
		} else {
			runProducer(Integer.parseInt(args[0]));
		}
	}

}
