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

		Associate associate2 = new Associate();
		associate2.setId(2l);
		associate2.setName("Simão Leopoldo Lozano de Lima");
		associate2.setCode("615244");
		associate2.setPhone("(34) 93484-7111");
		associate2.setEmail("fontestz@gmail.com");
		associate2.setAddress("R. Guerra Junqueira, 210 - TubalinaUberlândia - MG, 38412-004");
		associate2.setCertificate(false);
		associate2.setDocumentId("991.796.490-84");
		associate2.setGender(Gender.MALE);

		LocalDate date2 = LocalDate.of(1979, 4, 15);
		DateTimeFormatter formatters2 = DateTimeFormatter.ofPattern("d/MM/uuuu");
		String text2 = date.format(formatters);
		LocalDate parsedDate2 = LocalDate.parse(text2, formatters2);

		associate2.setBirthdate(parsedDate2);
		associateService.save(associate2);

		Associate associate3 = new Associate();
		associate3.setId(3l);
		associate3.setName("Everton Ademar Garcia Jimenes de Camargo");
		associate3.setCode("277483");
		associate3.setPhone("(34) 93102-1993");
		associate3.setEmail("fontestz@gmail.com");
		associate3.setAddress("R. Guerra Junqueira, 210 - TubalinaUberlândia - MG, 38412-004");
		associate3.setCertificate(false);
		associate3.setDocumentId("605.312.090-16");
		associate3.setGender(Gender.MALE);

		LocalDate date3 = LocalDate.of(1979, 4, 15);
		DateTimeFormatter formatters3 = DateTimeFormatter.ofPattern("d/MM/uuuu");
		String text3 = date3.format(formatters);
		LocalDate parsedDate3 = LocalDate.parse(text3, formatters3);

		associate3.setBirthdate(parsedDate3);
		associateService.save(associate3);

		Associate associate4 = new Associate();
		associate4.setId(4l);
		associate4.setName("Nicole Keyla Barreto");
		associate4.setCode("490599");
		associate4.setPhone("(34) 92822-7325");
		associate4.setEmail("fontestz@gmail.com");
		associate4.setAddress("R. Guerra Junqueira, 210 - TubalinaUberlândia - MG, 38412-004");
		associate4.setCertificate(false);
		associate4.setDocumentId("498.952.440-37");
		associate4.setGender(Gender.MALE);

		LocalDate date4 = LocalDate.of(1979, 4, 15);
		DateTimeFormatter formatters4 = DateTimeFormatter.ofPattern("d/MM/uuuu");
		String text4 = date4.format(formatters4);
		LocalDate parsedDate4 = LocalDate.parse(text4, formatters4);

		associate4.setBirthdate(parsedDate4);
		associateService.save(associate4);

		Associate associate5 = new Associate();
		associate5.setId(5l);
		associate5.setName("Maitê Renata de Balestero");
		associate5.setCode("970677");
		associate5.setPhone("(34) 93399-6720");
		associate5.setEmail("fontestz@gmail.com");
		associate5.setAddress("R. Guerra Junqueira, 210 - TubalinaUberlândia - MG, 38412-004");
		associate5.setCertificate(false);
		associate5.setDocumentId("048.147.240-12");
		associate5.setGender(Gender.MALE);

		LocalDate date5 = LocalDate.of(1979, 4, 15);
		DateTimeFormatter formatters5 = DateTimeFormatter.ofPattern("d/MM/uuuu");
		String text5 = date5.format(formatters5);
		LocalDate parsedDate5 = LocalDate.parse(text5, formatters5);

		associate5.setBirthdate(parsedDate5);
		associateService.save(associate5);

		Associate associate6 = new Associate();
		associate6.setId(6l);
		associate6.setName("Elizabete Josilda Delatorre de Molina");
		associate6.setCode("701767");
		associate6.setPhone("(34) 92013-8817");
		associate6.setEmail("fontestz@gmail.com");
		associate6.setAddress("R. Guerra Junqueira, 210 - TubalinaUberlândia - MG, 38412-004");
		associate6.setCertificate(false);
		associate6.setDocumentId("882.292.110-00");
		associate6.setGender(Gender.MALE);

		LocalDate date6 = LocalDate.of(1979, 4, 15);
		DateTimeFormatter formatters6 = DateTimeFormatter.ofPattern("d/MM/uuuu");
		String text6 = date6.format(formatters6);
		LocalDate parsedDate6 = LocalDate.parse(text6, formatters6);

		associate6.setBirthdate(parsedDate6);
		associateService.save(associate6);

		EventRegistration eventRegistration = new EventRegistration();
		eventRegistration.setEvent(event2);
		eventRegistration.setAssociate(associate);
		eventRegistrationService.save(eventRegistration);

	}
}
