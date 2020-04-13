package de.wevsvirushackathon.coronareport;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.wevsvirushackathon.coronareport.client.Client;
import de.wevsvirushackathon.coronareport.client.ClientRepository;
import de.wevsvirushackathon.coronareport.contactperson.ContactPerson;
import de.wevsvirushackathon.coronareport.contactperson.ContactPersonRepository;
import de.wevsvirushackathon.coronareport.diary.DiaryEntry;
import de.wevsvirushackathon.coronareport.diary.DiaryEntryRepository;
import de.wevsvirushackathon.coronareport.diary.TypeOfContract;
import de.wevsvirushackathon.coronareport.diary.TypeOfProtection;
import de.wevsvirushackathon.coronareport.firstReport.FirstReport;
import de.wevsvirushackathon.coronareport.firstReport.FirstReportRepository;
import de.wevsvirushackathon.coronareport.healthdepartment.HealthDepartment;
import de.wevsvirushackathon.coronareport.healthdepartment.HealthDepartmentRepository;
import de.wevsvirushackathon.coronareport.symptomes.Symptom;
import de.wevsvirushackathon.coronareport.symptomes.SymptomRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
//@Profile("!prod")
@Order(300)
@Slf4j
public class DummyDataInputBean implements ApplicationListener<ContextRefreshedEvent>, Ordered {

    final static String VALID_CLIENT_CODE_DEP1 = "738d3d1f-a9f1-4619-9896-2b5cb3a89c22";
    final static String VALID_CLIENT_CODE2_DEP1 = "4dsafc1f-a9f1-4619-9896-2b5cb3akd8e4";
    final static String VALID_CLIENT_CODE3_DEP2 = "22safg1f-a9f1-225f-9896-2b5cb3akdg88";
    
    final static String DEPARTMENT_CODE_DEP1 = "aba0ec65-6c1d-4b7b-91b4-c31ef16ad0a2";
    final static String DEPARTMENT_CODE_DEP2 = "ca3f3e9a-414a-4117-a623-59b109b269f1";
    
    @Getter private Client clientWithNoAccount;
    @Getter private Client clientWithAccountAndEntries;
    

    private final ClientRepository clientRepository;
    private final ContactPersonRepository contactPersonRepository;
    private final DiaryEntryRepository diaryEntryRepository;
    private final HealthDepartmentRepository healthDepartmentRepository;
    private final SymptomRepository symptomRepository;
    private final FirstReportRepository firstReportRepository;

    public DummyDataInputBean(ClientRepository clientRepository,
                              ContactPersonRepository contactPersonRepository,
                              DiaryEntryRepository diaryEntryRepository,
                              HealthDepartmentRepository healthDepartmentRepository,
                              SymptomRepository symptomRepository,
                              FirstReportRepository firstReportRepository) {
        this.clientRepository = clientRepository;
        this.contactPersonRepository = contactPersonRepository;
        this.diaryEntryRepository = diaryEntryRepository;
        this.healthDepartmentRepository = healthDepartmentRepository;
        this.symptomRepository = symptomRepository;
        this.firstReportRepository = firstReportRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
    	
    	// check if testclients do already exist:
    	
    	Client client = clientRepository.findByClientCode(VALID_CLIENT_CODE_DEP1);
    	if(client != null){
            log.info("Initial client data already exists, skipping dummy data generation");
    		return;
    	}

        log.info("Generating dummy data");

        final HealthDepartment hd1 = this.healthDepartmentRepository.save(HealthDepartment.builder().fullName("Testamt 1")
                .id("Testamt1").passCode(UUID.fromString(DEPARTMENT_CODE_DEP1)).build());
        final HealthDepartment hd2 = this.healthDepartmentRepository.save(HealthDepartment.builder().fullName("Testamt 2")
                .id("Testamt2").passCode(UUID.fromString(DEPARTMENT_CODE_DEP2)).build());

        List<Client> clientList = new ArrayList<>();
        
        final Client client1 = clientRepository.save(Client.builder().firstname("Fabian")
        		.surename("Bauer").infected(true).clientCode(VALID_CLIENT_CODE_DEP1)
        		.healthDepartment(hd1)
        		.phone("0175 664845454").zipCode("66845")
        		.build());
        clientList.add(client1);

        this.firstReportRepository.save(FirstReport.builder()
                .belongToLaboratoryStaff(false)
                .belongToMedicalStaff(true)
                .directContactWithLiquidsOfC19pat(true)
                .familyMember(true)
                .client(client1)
                .build());

        final Client client2 = clientRepository.save(Client.builder().firstname("Sabine")
        		.surename("Wohlfart").infected(false).clientCode(VALID_CLIENT_CODE2_DEP1)
        		.healthDepartment(hd1)
        		.phone("0172 9847845125").zipCode("68309")
        		.build());
        clientList.add(client2);
        this.clientWithNoAccount = client2;
        
        this.firstReportRepository.save(FirstReport.builder()
                .belongToLaboratoryStaff(true)
                .directContactWithLiquidsOfC19pat(true)
                .familyMember(false)
                .client(client2)
                .build());

        final Client client3 = clientRepository.save(Client.builder().firstname("Daniela")
        		.surename("Maurer").infected(true).clientCode("22safg1f-a9f1-225f-9896-2b5cb3akdg88")
        		.healthDepartment(hd2)
        		.phone("0621 884433").zipCode("68259").build());
        clientList.add(client3);
        this.firstReportRepository.save(FirstReport.builder()
                .directContactWithLiquidsOfC19pat(true)
                .familyMember(true)
                .client(client3)
                .build());
        
        
        for(Client createdClient: clientList) {
            log.info("created dummy client with id: " + createdClient.getClientId() + " and client-code: " + createdClient.getClientCode());

        }

        final ContactPerson cp1 = contactPersonRepository.save(ContactPerson.builder().client(client1).firstname("Alice").surename("Sommer")
                .typeOfContract(TypeOfContract.AE)
                .typeOfProtection(TypeOfProtection.H)
                .build());

        final ContactPerson cp2 = contactPersonRepository.save(ContactPerson.builder().client(client1).firstname("Boris").surename("Johnson")
                .typeOfContract(TypeOfContract.Aer)
                .typeOfProtection(TypeOfProtection.M1)
                .build());

        final ContactPerson cp3 = contactPersonRepository.save(ContactPerson.builder().client(client1).firstname("Roland").surename("Koch")
                .typeOfContract(TypeOfContract.O)
                .typeOfProtection(TypeOfProtection.Zero)
                .build());

        final Symptom s1 = symptomRepository.findAllByName("Angst").get(0);
        final Symptom s2 = symptomRepository.findAllByName("Kopfschmerzen").get(0);
        final Symptom s3 = symptomRepository.findAllByName("Fieber").get(0);
        final Symptom s4 = symptomRepository.findAllByName("Husten").get(0);
        final Symptom s5 = symptomRepository.findAllByName("Atemnot").get(0);

        diaryEntryRepository.save(DiaryEntry.builder()
                .client(client1)
                .dateTime(dateOf(2020, 3, 26))
                .bodyTemperature(38.1f)
                .symptoms(Collections.singletonList(s1))
                .build());
        diaryEntryRepository.save(DiaryEntry.builder()
                .client(client1)
                .dateTime(dateOf(2020, 3, 27))
                .bodyTemperature(38.4f)
                .symptoms(Arrays.asList(s1, s2, s3))
                .contactPersons(Arrays.asList(cp1, cp2))
                .build());

        diaryEntryRepository.save(DiaryEntry.builder()
                .client(client2)
                .dateTime(dateOf(2020, 3, 25))
                .bodyTemperature(37.5f)
                .build());
        diaryEntryRepository.save(DiaryEntry.builder()
                .client(client2)
                .dateTime(dateOf(2020, 3, 26))
                .bodyTemperature(38.0f)
                .symptoms(Collections.singletonList(s3))
                .build());
        diaryEntryRepository.save(DiaryEntry.builder()
                .client(client2)
                .dateTime(dateOf(2020, 3, 27))
                .bodyTemperature(39.2f)
                .symptoms(Arrays.asList(s3, s4, s5))
                .contactPersons(Collections.singletonList(cp3))
                .build());
        
        diaryEntryRepository.save(DiaryEntry.builder()
                .client(client3)
                .dateTime(dateOf(2020, 3, 20))
                .bodyTemperature(38.2f)
                .symptoms(Arrays.asList(s3, s4))
                .build());        

        diaryEntryRepository.save(DiaryEntry.builder().client(client3)
                .dateTime(dateOf(2020, 3, 19)).bodyTemperature(38.1f).build());
        diaryEntryRepository.save(DiaryEntry.builder().client(client3)
                .dateTime(dateOf(2020, 3, 19)).bodyTemperature(37.5f).build());
    }
    
   
    
    public Timestamp dateOf(int year, int month, int day) {
        return new Timestamp(Date.from(LocalDate.of(year, month, day).atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant()).getTime());
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
