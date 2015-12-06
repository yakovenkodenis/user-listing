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
<title>Resource not found</title>
<style type="text/css">
	<%@ include file="styles/main.css" %>
</style>
</head>
<body>
	<div class="container">
		<h1>404 Not Found. We're sorry</h1>
		<br>
		<a href="${pageContext.request.contextPath}/">Home Page</a>
	</div>
	
</body>
</html>