<%--
  Created by IntelliJ IDEA.
  User: Vredn
  Date: 26.12.2021
  Time: 23:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta charset="UTF-8">
<%--    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">--%>
<%--    <link href="./css/styles.css" rel="stylesheet">--%>
    <link href="css/style.css" rel="stylesheet">
    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>
    <title>Resume ${resume.fullName} list</title>

</head>
<body>
<section>
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit">Edit</a></h2>
    <c:forEach var="contactEntry" items="${resume.contacts}">
    <jsp:useBean id="contactEntry" type="java.util.Map.Entry<com.urise.webapp.model.ContactType, java.lang.String>"/>
    <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
    </c:forEach>
</section>

<br>
<section>
    <c:forEach var="sectionEntry" items="${resume.sections}">
        <jsp:useBean id="sectionEntry" type="java.util.Map.Entry<com.urise.webapp.model.SectionType, com.urise.webapp.model.Section>"/>
        <h2><%=sectionEntry.getKey().getTitle()%></h2>
        <ul>
            <%=sectionEntry.getValue().toHtml()%>
        </ul>
    </c:forEach>
</section>



</body>
</html>
