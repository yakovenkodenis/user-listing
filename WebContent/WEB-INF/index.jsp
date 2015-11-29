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

	<h5><fmt:message key="index.greeting.hello" />, ${user.login}! <fmt:message key="index.greeting.your_role" /> ${user.role}</h5>
	<br>

	<c:set var="role" value="<%= session.getAttribute(\"role\") %>" scope="session" />
	
	<c:choose>
		<c:when test="${sessionScope.role == 'admin'}">
			<c:forEach items="${users}" var="user" varStatus="loop">
				<div class="user">
					<span class="user-id">${loop.index + 1}</span>
					<span class="user-name">${user.name}</span>
					<span class="user-login">${user.login}</span>
					<span class="user-role">${user.role}</span>
					<a href="/">delete</a>
				</div>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<c:forEach items="${users}" var="user" varStatus="loop">
				<div class="user">
					<span class="user-id">${loop.index}</span>
					<span class="user-name">${user.name}</span>
					<span class="user-login">${user.login}</span>
					<span class="user-role">${user.role}</span>
				</div>
			</c:forEach>
		</c:otherwise>
	</c:choose>
</body>
</html>