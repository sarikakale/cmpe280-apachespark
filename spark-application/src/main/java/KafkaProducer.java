import java.util.*;

import com.github.dvdme.ForecastIOLib.FIOCurrently;
import com.github.dvdme.ForecastIOLib.FIOHourly;
import com.github.dvdme.ForecastIOLib.ForecastIO;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class KafkaProducer {
	static HashMap<String,String> coordinatesmap = new HashMap<String,String>();
	static String     randomKey ="";
	static String      value ="";
	
	public static void main(String[] args) {
		//initHashMap();
		long events = Long.parseLong(args[0]);
		Random rnd = new Random();
		Properties props = new Properties();
		props.put("metadata.broker.list", "localhost:9092");
		props.put("producer.type", "sync");
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("partitioner.class", "KafkaPartitioner");
		props.put("request.required.acks", "1");
		ProducerConfig config = new ProducerConfig(props);
		Producer<String, String> producer = new Producer<String, String>(config);
		for (long nEvents = 0; nEvents < events; nEvents++) {
			System.out.println("creating event " + nEvents);
			long runtime = new Date().getTime();
			
			
			 ForecastIO fio = new ForecastIO("bac3f4678c5eefffb966cf2d767c2472");
			 
				fio.setUnits(ForecastIO.UNITS_SI); //sets the units as SI - optional
				fio.setExcludeURL("hourly,minutely"); //excluded the minutely and hourly reports from the reply
			
			/**
			 * Create random hashmap
			 */
			
			
	        coordinatesmap.put("38.7252993","-9.1500364");
			coordinatesmap.put("18.750139","73.042066");
			coordinatesmap.put("17.680464","74.018261");	
			coordinatesmap.put("44.968046","-94.420307");	
			coordinatesmap.put("44.33328","-89.132008");	
			coordinatesmap.put("33.755787","-116.359998");
			coordinatesmap.put("44.9205","-93.44786");
			coordinatesmap.put("44.240309","-91.493619");
			coordinatesmap.put("44.968041","-94.419696");
			
			/**
			 * Pick random key-value from hashmap
			 */
			
			Random       random    = new Random();
			List<String> keys      = new ArrayList<String>(coordinatesmap.keySet());
			randomKey = keys.get( random.nextInt(keys.size()) );
			value     = coordinatesmap.get(randomKey);
			System.out.println("randomKey "+randomKey);
			System.out.println("randomValue "+value);
			
			
			fio.getForecast(randomKey, value);
			System.out.println("Forecast for :" +randomKey+ " "+value);
			FIOCurrently currently = new FIOCurrently(fio);
		   
		    System.out.println("\nCurrently\n");
		   
		    String tempreture = " " +currently.get().temperature()*5;
		    String msg = runtime + " Current Tempreture is :"+tempreture;
			KeyedMessage<String, String> data = new KeyedMessage<String, String>("testdemo", tempreture, msg);
			try {
			    Thread.sleep(1000);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			producer.send(data);
		}
		producer.close();
	}
}