package br.com.uaijug.uaijugdevapi;

import br.com.uaijug.uaijugdevapi.model.domain.*;
import br.com.uaijug.uaijugdevapi.model.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@SpringBootApplication
public class UaijugDevApiApplication implements CommandLineRunner {

	@Resource
	FilesStorageService storageService;

	@Autowired
	private EventService eventService;

	@Autowired
	private TagService tagService;

	@Autowired
	private AssociateService associateService;

	@Autowired
	private EventRegistrationService eventRegistrationService;

	@Bean
	public Java8TimeDialect java8TimeDialect() {
		return new Java8TimeDialect();
	}

	public static void main(String[] args) {
		SpringApplication.run(UaijugDevApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		storageService.init();

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

		Event event2 = new Event();
		event2.setId(3l);
		event2.setName("AWS UG - Brain");
		event2.setDate(LocalDateTime.now());
		event2.setDescription("Description");
		event2.setMeetingTime(3);
		event2.setPricipalBanner("BANNER ASA");
		event2.setLocal("Brain");
		//event1.setTags(tags1);
		event2.setOnline(false);
		eventService.save(event2);

		Associate associate = new Associate();
		associate.setId(1l);
		associate.setName("Rogério Fontes Tomaz");
		associate.setCode("143850");
		associate.setPhone("(34) 99203-1938");
		associate.setEmail("fontestz@gmail.com");
		associate.setAddress("R. Guerra Junqueira, 210 - TubalinaUberlândia - MG, 38412-004");
		associate.setCertificate(false);
		associate.setDocumentId("043.582.436-81");
		associate.setGender(Gender.MALE);

		LocalDate date = LocalDate.of(1979, 4, 15);
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d/MM/uuuu");
		String text = date.format(formatters);
		LocalDate parsedDate = LocalDate.parse(text, formatters);

		associate.setBirthdate(parsedDate);
		associateService.save(associate);

		EventRegistration eventRegistration = new EventRegistration();
		eventRegistration.setEvent(event2);
		eventRegistration.setAssociate(associate);
		eventRegistrationService.save(eventRegistration);

	}
}
