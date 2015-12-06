<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="ua.nure.yakovenko.Task2.i18n.text"/>
<!DOCTYPE html>
<html lang="${language}">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="UTF-8">
<title>User Listing</title>
<style type="text/css">
	<%@include file="styles/main.css" %>
</style>
<c:set var="deleteUser" value="<%= session.getAttribute(\"deleteUser\")%>" scope="page" />
<title>Delete user ${deleteUser.login}?</title>
</head>
<body>

	<h1 class="text-center"><a href="${pageContext.request.contextPath}/" class="disable-link">User listing</a></h1>
	
	<nav id="top-nav">
	
		<form method="post" action="${pageContext.request.contextPath}/" class="logout">
			<fmt:message key="authentication.logout" var="logout" />
			<input type="submit" name="logout" value="${logout}" />
		</form>
		
		<form class="language-select">
			<select id="language" name="language" onchange="submit()">
				<option value="ru" ${language == 'ru' ? 'selected' : ''}><fmt:message key="lang.russian" /></option>
				<option value="en" ${language == 'en' ? 'selected' : ''}><fmt:message key="lang.english" /></option>
			</select>
		</form>
	</nav>

	<h4><fmt:message key="users.delete.question" /> ${deleteUser.login}?</h4>
	<div class="user-info">
		<p><span><fmt:message key="users.delete.name" />: ${deleteUser.name}</span></p>
		<p><span><fmt:message key="users.delete.login" />: ${deleteUser.login}</span></p>
		<p><span><fmt:message key="users.delete.email" />: ${deleteUser.email}</span></p>
	</div>
	<form action="${pageContext.request.contextPath}/delete/${deleteUser.id}" method="post">
		<fmt:message key="users.delete.sure" var="confirm" />
		<input type="submit" name="delete" value="${confirm}" />
	</form>
</body>
</html>