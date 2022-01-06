<%@ page import="java.util.List" %>
<%@ page import="com.urise.webapp.model.*" %>
<%@ page import="com.urise.webapp.util.DateUtil" %>
<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page import="com.urise.webapp.model.ContactType" %>

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
    <form method="post" action="resume" id="resumeForm" name="resumeForm" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Name:</dt>
            <dd><input type="text" name="fullName" size=50 value="${resume.fullName}"></dd>
        </dl>

        <h3>Contacts:</h3>
            <c:forEach var="type" items="${ContactType.values()}">
        <dl>
            <jsp:useBean id="type" type="com.urise.webapp.model.ContactType"/>
            <dt>${type.getDescription()}:</dt>
            <dd><input type="text" name=${type.name()} size=30 value="${resume.getContact(type)}"></dd>
        </dl>
            </c:forEach>

        <h3>Sections:</h3>
        <c:forEach var="secType" items="<%=SectionType.values()%>">
            <c:set var="section" value="${resume.getSection(secType)}"/>
            <jsp:useBean id="section" type="com.urise.webapp.model.Section"/>
            <h2><a>${secType.title}</a></h2>
            <c:choose>
                <c:when test="${secType=='OBJECTIVE'}">
                    <input type='text' name='${secType}' size=75 value='<%=section%>'>
                </c:when>
                <c:when test="${secType=='PERSONAL'}">
                    <textarea name='${secType}' cols=75 rows=5><%=section%></textarea>
                </c:when>
                <c:when test="${secType=='QUALIFICATIONS' || secType=='ACHIEVEMENT'}">
                    <textarea name='${secType}' cols=75
                              rows=5><%=String.join("\n", ((ListSection) section).getValues())%></textarea>
                </c:when>
                <c:when test="${secType=='EXPERIENCE' || secType=='EDUCATION'}">
                    <c:forEach var="org" items="<%=((OrganizationSection) section).getOrganizations()%>"
                               varStatus="counter">
                        <dl>
                            <dt>Название учереждения:</dt>
                            <dd><input type="text" name='${secType}' size=100 value="${org.organizationName}"></dd>
                        </dl>
                        <dl>
                            <dt>Сайт учереждения:</dt>
                            <dd><input type="text" name='${secType}url' size=100 value="${org.organizationLink.url}"></dd>
                        </dl>
                        <br>
                            <c:forEach var="exp" items="${org.experiences}">
                                <jsp:useBean id="exp" type="com.urise.webapp.model.Experience"/>
                                <dl>
                                    <dt>Начальная дата:</dt>
                                    <dd>
                                        <input type="text" name="${secType}${counter.index}startDate" size=10
                                               value="<%=DateUtil.format(exp.getStartDate())%>" placeholder="MM/yyyy"></dd>
                                </dl>
                                <dl>
                                    <dt>Конечная дата:</dt>
                                    <dd>
                                        <input type="text" name="${secType}${counter.index}endDate" size=10
                                               value="<%=DateUtil.format(exp.getEndDate())%>" placeholder="MM/yyyy"></dd>
                                </dl>
                                <dl>
                                    <dt>Должность:</dt>
                                    <dd><input type="text" name='${secType}${counter.index}title' size=75
                                               value="${exp.title}"></dd>
                                </dl>
                                <dl>
                                    <dt>Описание:</dt>
                                    <dd><textarea name="${secType}${counter.index}description" rows=5
                                                  cols=75>${exp.description}</textarea></dd>
                                </dl>
                            </c:forEach>
                    </c:forEach>
                </c:when>
            </c:choose>
        </c:forEach>
        <hr>
        <button type="submit">Save</button>
        <button onclick="window.history.back()">Cancel</button>
    </form>
</section>
</body>
</html>
