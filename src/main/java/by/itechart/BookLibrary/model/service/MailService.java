package by.itechart.BookLibrary.model.service;

import by.itechart.BookLibrary.model.entity.BorrowRecord;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.List;

public interface MailService {
    void sendNotificationsToReaders(List<BorrowRecord> borrowRecords) throws IOException, TemplateException;
}
