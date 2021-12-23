package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

public class ResumeServlet extends HttpServlet {
    Storage storage;// = Config.getInstance().getStorage();


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.getInstance().getStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Writer writer = response.getWriter();
        writer.write("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <link href=\"css/style.css\" rel =\"stylesheet\">\n" +
                "    <title>Resume list</title>\n" +
                "</head>\n" +
                "<body>" +
                "<table border=\"1px solid black\" border-collapse=\"collapse\">\n" +
                "<caption>Resumes</caption>\n" +
                "<tr>\n" +
                "   <th>Uuid</th>\n" +
                "   <th>Full Name</th>\n" +
                "   <th>Email</th>\n" +
                "</tr>\n");

        for (Resume resume :
                storage.getAllSorted()) {
            writer.write(
                    "<tr>\n" +
                            "<td>" + resume.getUuid() + "</td>\n" +
                            "<td>" + resume.getFullName() + "</td>\n" +
                            "<td>" + resume.getContact(ContactType.EMAIL) + "</td>\n" +
                    "</tr>");
        }
        writer.write(
                    "</table>\n" +
                        "</body>\n" +
                        "</html>");

    }
}
