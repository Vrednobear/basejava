<%@ page import="com.urise.webapp.model.Resume" %>
<%@ page import="java.util.List" %>
<%@ page import="com.urise.webapp.model.ContactType" %><%--
  Created by IntelliJ IDEA.
  User: Vredn
  Date: 24.12.2021
  Time: 1:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <link href="css/style.css" rel="stylesheet">
    <title>Resume list</title>
</head>
<body>
<section>
    <a href="resume?action=add">Create new Resume</a>
    <br>
    <table border="1" cellpadding="8" cellspacing="0">
        <tr>
            <th>Full Name</th>
            <th>Email</th>
        </tr>
        <c:forEach items = "${resumes}" var="resume">
            <jsp:useBean id="resume" type="com.urise.webapp.model.Resume"/>
        <tr>
            <td><a href="resume?uuid=${resume.getUuid()}&action=view">${resume.fullName }</a></td>
            <td>${resume.getContact(ContactType.EMAIL)}</td>
            <td><a href="resume?uuid=${resume.getUuid()}&action=delete">Delete</a></td>
            <td><a href="resume?uuid=${resume.getUuid()}&action=edit">Edit</a></td>
        </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>

