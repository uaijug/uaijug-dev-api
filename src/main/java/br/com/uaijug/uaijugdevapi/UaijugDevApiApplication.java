package br.com.uaijug.uaijugdevapi;

import br.com.uaijug.uaijugdevapi.model.domain.Associate;
import br.com.uaijug.uaijugdevapi.model.domain.Event;
import br.com.uaijug.uaijugdevapi.model.domain.Tag;
import br.com.uaijug.uaijugdevapi.model.service.AssociateService;
import br.com.uaijug.uaijugdevapi.model.service.EventService;
import br.com.uaijug.uaijugdevapi.model.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@SpringBootApplication
public class UaijugDevApiApplication implements CommandLineRunner {

	@Autowired
	private EventService eventService;

	@Autowired
	private TagService tagService;

	@Autowired
	private AssociateService associateService;

	public static void main(String[] args) {
		SpringApplication.run(UaijugDevApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Tag tagJava = new Tag();
		tagJava.setId(1l);
		tagJava.setName("Java");
		tagService.save(tagJava);

		Tag tagAWS = new Tag();
		tagAWS.setId(2l);
		tagAWS.setName("AWS");
		tagService.save(tagAWS);

		Optional<Tag>  tagAWSesponse = tagService.findByName("AWS");
		Optional<Tag> tagJavaResponse = tagService.findByName("Java");

		Set<Tag> tags1 = new HashSet();
		tags1.add(tagJavaResponse.get());

		Set<Tag> tags2 = new HashSet();
		tags2.add(tagAWSesponse.get());

		Event event = new Event();
		event.setId(1l);
		event.setName("AWS UG - TQI");
		event.setDate(LocalDateTime.now());
		event.setDescription("Description");
		event.setMeetingTime(3);
		event.setPricipalBanner("BANNER TQI");
		event.setLocal("TQI");
		//event.setTags(tags1);
		event.setOnline(false);
		eventService.save(event);

		Event event1 = new Event();
		event1.setId(2l);
		event1.setName("AWS UG - ASA Consultoria");
		event1.setDate(LocalDateTime.now());
		event1.setDescription("Description");
		event1.setMeetingTime(3);
		event1.setPricipalBanner("BANNER ASA");
		event1.setLocal("ASA");
		//event1.setTags(tags1);
		event1.setOnline(false);
		eventService.save(event1);

		Associate associate = new Associate();
		associate.setId(1l);
		associate.setName("Rogério Fontes Tomaz");
		associate.setCode("143850");
		associate.setEmail("fontestz@gmail.com");
		associate.setAddress("naoanoa");
		associate.setCertificate(false);
		associate.setDocumentId("weew233");

		associateService.save(associate);
	}
}
