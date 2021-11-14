package by.itechart.BookLibrary.model.service.impl;

import by.itechart.BookLibrary.controller.Controller;
import by.itechart.BookLibrary.model.entity.BorrowRecord;
import by.itechart.BookLibrary.model.service.MailService;
import by.itechart.BookLibrary.util.mail.MailSender;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MailServiceImpl implements MailService {
    private static final Configuration configuration;
    private static final MailSender mailSender;

    private static final String MAIL_SUBJECT = "Library notification";

    static {
        configuration = new Configuration(Configuration.getVersion());
        ClassTemplateLoader loader = new ClassTemplateLoader(Controller.class, "/mail-templates");
        configuration.setTemplateLoader(loader);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        mailSender = MailSender.getInstance();
    }

    @Override
    public void sendNotificationsToReaders(List<BorrowRecord> borrowRecords) throws IOException, TemplateException {
        Template upcomingDueDateReminder = configuration.getTemplate("upcoming-due-date.ftl");
        Template deadlineExpiredNotification = configuration.getTemplate("deadline-expired.ftl");

        try (StringWriter stringWriter = new StringWriter()) {
            Map<String, Object> map = new HashMap<>();

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate now = LocalDate.now();

            for (BorrowRecord borrowRecord : borrowRecords) {
                LocalDate dueDate = LocalDate.parse(borrowRecord.getDueDate().split(" ")[0], dateTimeFormatter);
                long daysRemain = ChronoUnit.DAYS.between(now, dueDate);

                if(daysRemain == 7 || daysRemain == 1){
                    map.put("borrowRecord", borrowRecord);
                    map.put("daysRemain", daysRemain);
                    upcomingDueDateReminder.process(map, stringWriter);

                    send(borrowRecord.getReader().getEmail(), stringWriter.getBuffer().toString());
                } else if(daysRemain == -1) {
                    map.put("borrowRecord", borrowRecord);
                    deadlineExpiredNotification.process(map, stringWriter);

                    send(borrowRecord.getReader().getEmail(), stringWriter.getBuffer().toString());
                }

                if(!map.isEmpty()){
                    map.clear();
                    stringWriter.getBuffer().setLength(0);
                }
            }
        }
    }

    private void send(String email, String content){
        mailSender.setupLetter(email, MAIL_SUBJECT, content);
        mailSender.send();
    }
}
