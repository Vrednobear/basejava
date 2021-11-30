package com.urise.webapp.storage;

import com.urise.webapp.model.*;
import org.junit.Ignore;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

public class ResumeTestData {


    public static void main(String[] args) {

        Resume resume = createResume("1","Ton");

        System.out.println(ContactType.PHONE + ": " + resume.getContact(ContactType.PHONE));
        System.out.println(ContactType.SKYPE + ": " + resume.getContact(ContactType.SKYPE));
        System.out.println(ContactType.EMAIL + ": " + resume.getContact(ContactType.EMAIL));
        System.out.println(ContactType.LINKEDIN + ": " + resume.getContact(ContactType.LINKEDIN));
        System.out.println(ContactType.GITHUB + ": " + resume.getContact(ContactType.GITHUB));
        System.out.println(ContactType.STACKOVERFLOW + ": " + resume.getContact(ContactType.STACKOVERFLOW));
        System.out.println(ContactType.HOME_PAGE + ": " + resume.getContact(ContactType.HOME_PAGE));
        System.out.println();

        System.out.println(SectionType.OBJECTIVE.getTitle() + "\n" + resume.getSection(SectionType.OBJECTIVE) + "\n");
        System.out.println(SectionType.PERSONAL.getTitle() + "\n" + resume.getSection(SectionType.PERSONAL) + "\n");
        System.out.println(SectionType.QUALIFICATIONS.getTitle() + "\n" + resume.getSection(SectionType.QUALIFICATIONS) + "\n");
        System.out.println(SectionType.ACHIEVEMENT.getTitle() + "\n" + resume.getSection(SectionType.ACHIEVEMENT) + "\n");
        System.out.println(SectionType.EXPERIENCE.getTitle() + "\n" + resume.getSection(SectionType.EXPERIENCE) + "\n");
        System.out.println(SectionType.EDUCATION.getTitle() + "\n" + resume.getSection(SectionType.EDUCATION) + "\n");

    }

    public static Resume createResume(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);

        resume.addContact(ContactType.PHONE, "89514438975");
        resume.addContact(ContactType.SKYPE, "Varantocheg");
        resume.addContact(ContactType.EMAIL, "Varganov1998@yandex.ru ");
        resume.addContact(ContactType.LINKEDIN, "link");
        resume.addContact(ContactType.GITHUB, "link");
        resume.addContact(ContactType.STACKOVERFLOW, "link");
        resume.addContact(ContactType.HOME_PAGE, "link");

        Section objectiveSection = new TextSection(
                "Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям");
        resume.addSection(SectionType.OBJECTIVE, objectiveSection);

        Section personalSection = new TextSection(
                "Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры.");
        resume.addSection(SectionType.PERSONAL, personalSection);

        resume.addSection(SectionType.OBJECTIVE,objectiveSection);
        resume.addSection(SectionType.PERSONAL,personalSection);

        List<String> list1 = new ArrayList<>();
        list1.add("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", " +
                "\"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). " +
                "Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 1000 выпускников.");
        list1.add("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. " +
                "Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.");
        list1.add("Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. " +
                "Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery. " +
                "Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.");
        list1.add("Реализация c нуля Rich Internet Application приложения на стеке технологий " +
                "JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.");
        list1.add("Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов (SOA-base архитектура, JAX-WS, JMS, AS Glassfish)." +
                " Сбор статистики сервисов и информации о состоянии через систему мониторинга Nagios. " +
                "Реализация онлайн клиента для администрирования и мониторинга системы по JMX (Jython/ Django).");
        list1.add("Реализация протоколов по приему платежей всех основных платежных системы " +
                "России (Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа.");

        Section achievementsSection = new ListSection(list1);
        resume.addSection(SectionType.ACHIEVEMENT,achievementsSection);

        String[] s = ("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2\n" +
                "Version control: Subversion, Git, Mercury, ClearCase, Perforce\n" +
                "DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle,\n" +
                "MySQL, SQLite, MS SQL, HSQLDB\n" +
                "Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy,\n" +
                "XML/XSD/XSLT, SQL, C/C++, Unix shell scripts,\n" +
                "Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, Spring (MVC, Security, Data, Clouds, Boot), JPA (Hibernate, EclipseLink), Guice, GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports, Apache Commons, Eclipse SWT, JUnit, Selenium (htmlelements).\n" +
                "Python: Django.\n" +
                "JavaScript: jQuery, ExtJS, Bootstrap.js, underscore.js\n" +
                "Scala: SBT, Play2, Specs2, Anorm, Spray, Akka\n" +
                "Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, RMI, JMS, JavaMail, JAXB, StAX, SAX, DOM, XSLT, MDB, JMX, JDBC, JPA, JNDI, JAAS, SOAP, AJAX, Commet, HTML5, ESB, CMIS, BPMN2, LDAP, OAuth1, OAuth2, JWT.\n" +
                "Инструменты: Maven + plugin development, Gradle, настройка Ngnix,\n" +
                "администрирование Hudson/Jenkins, Ant + custom task, SoapUI, JPublisher, Flyway, Nagios, iReport, OpenCmis, Bonita, pgBouncer.\n" +
                "Отличное знание и опыт применения концепций ООП, SOA, шаблонов проектрирования, архитектурных шаблонов, UML, функционального программирования\n" +
                "Родной русский, английский \"upper intermediate\"").split("\\n");

        Section qualificationSection = new ListSection(Arrays.asList(s));
        resume.addSection(SectionType.QUALIFICATIONS,qualificationSection);


        Organization organization1 = new Organization("Java Online Projects", "http://javaops.ru/");
        Experience experience1 = new Experience(LocalDate.of(2013, 10, 1), LocalDate.now(), "Автор проекта.",
                "Создание, организация и проведение Java онлайн проектов и стажировок.");
        Organization organization2 = new Organization("Wrike", "https://www.wrike.com/");
        Experience experience2 = new Experience(LocalDate.of(2014, 10, 1), LocalDate.of(2016, 1, 1),
                "Старший разработчик (backend)]", "Проектирование и разработка онлайн платформы управления проектами Wrike " +
                "(Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). " +
                "Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO.");
        Organization organization3 = new Organization("RIT Center", "rit.com");
        Experience experience3 = new Experience(LocalDate.of(2012, 4, 1), LocalDate.of(2014, 10, 1), "Java архитектор",
                "Организация процесса разработки системы ERP для разных окружений: " +
                        "релизная политика, версионирование, ведение CI (Jenkins), миграция базы (кастомизация Flyway), " +
                        "конфигурирование системы (pgBoucer, Nginx), AAA via SSO. Архитектура БД и серверной части системы. " +
                        "Разработка интергационных сервисов: CMIS, BPMN2, 1C (WebServices), " +
                        "сервисов общего назначения (почта, экспорт в pdf, doc, html). " +
                        "Интеграция Alfresco JLAN для online редактирование из браузера документов MS Office. " +
                        "Maven + plugin development, Ant, Apache Commons, Spring security, Spring MVC, Tomcat,WSO2, xcmis, OpenCmis, " +
                        "Bonita, Python scripting, Unix shell remote scripting via ssh tunnels, PL/Python");

        organization1.addExperience(experience1);
        organization2.addExperience(experience2);
        organization3.addExperience(experience3);
        Set<Organization> workOrganization = new LinkedHashSet<>();
        workOrganization.add(organization1);
        workOrganization.add(organization2);
        workOrganization.add(organization3);

        Section organizationSection = new OrganizationSection(workOrganization);
        resume.addSection(SectionType.EXPERIENCE,organizationSection);


        Organization organization4 = new Organization("JCoursera", "https://www.coursera.org/course/progfun");
        Experience experience4 = new Experience(LocalDate.of(2013, 3, 1), LocalDate.of(2013, 5, 1),
                "Functional Programming Principles in Scala\" by Martin Odersky", null);
        Organization organization5 = new Organization("Luxoft",
                "http://www.luxoft-training.ru/training/catalog/course.html?ID=22366");
        Experience experience5 = new Experience(LocalDate.of(2011, 3, 1), LocalDate.of(2011, 4, 1),
                "Курс \"Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.\"", null);
        Organization organization6 = new Organization("Siemens AG", "http://www.siemens.ru/");
        Experience experience6 = new Experience(LocalDate.of(2005, 1, 1), LocalDate.of(2005, 4, 1),
                "3 месяца обучения мобильным IN сетям (Берлин)", null);
        Organization organization7 = new Organization("ИТМО", "http://www.ifmo.ru/");
        Experience experience7 = new Experience(LocalDate.of(1993, 9, 1),
                LocalDate.of(1993, 7, 1), "\tИнженер (программист Fortran, C)", null);
        Experience experience8 = new Experience(LocalDate.of(1996, 7, 1),
                LocalDate.of(1993, 7, 1), "\tАспирантура (программист С, С++)", "null");

        organization4.addExperience(experience4);
        organization5.addExperience(experience5);
        organization6.addExperience(experience6);
        organization7.addExperience(experience7);
        organization7.addExperience(experience8);

        Set<Organization> educationOrganizations = new LinkedHashSet<>();
        educationOrganizations.add(organization4);
        educationOrganizations.add(organization5);
        educationOrganizations.add(organization6);
        educationOrganizations.add(organization7);
        Section educationSection = new OrganizationSection(educationOrganizations);
        resume.addSection(SectionType.EDUCATION,educationSection);

        return resume;
    }
}

