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
</head>
<body>
	<h1 class="text-center"><a href="/Task2" class="disable-link">User listing</a></h1>
	
	<nav id="top-nav">
	
		<form method="post" action="/Task2/" class="logout">
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
	
	<c:choose>
		<c:when test="${user.id == editID}">
			<h3><fmt:message key="users.edit_own"/></h3>
		</c:when>
		<c:otherwise>
			<h3><fmt:message key="users.edit_user"/></h3>
		</c:otherwise>
	</c:choose>

	<form method="post" action="create" accept-charset="UTF-8" enctype="application/x-www-form-urlencoded">
		<c:forEach items="${errorEditMessage}" var="message">
			<div class="error-message">${message}</div>
		</c:forEach>
		<label for="name"><fmt:message key="users.create.name" /></label>
		<input type="text" name="name" value="${editName}" autocomplete="name" />
		<label for="login"><fmt:message key="users.create.login" /></label>
		<input type="text" name="login" value="${editLogin}" />
		<label for="email"><fmt:message key="users.create.email"/></label>
		<input type="text" name="email" value="${editEmail}" />
		<label for="password"><fmt:message key="users.create.password" /></label>
		<input type="password" name="password" />
		<input type="checkbox" name="isAdmin" class="checkbox" id="checkbox" />
		<label for="checkbox" class="checkbox-label"><fmt:message key="users.create.is_admin"/></label><br>
		<input type="text" name="language" class="hidden" value="${language}" />
		<fmt:message key="users.submit_edit" var="editValue" />
		<input type="submit" value="${editValue}" />
	</form>
</body>
</html>