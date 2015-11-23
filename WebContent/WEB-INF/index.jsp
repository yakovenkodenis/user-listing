<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="#{not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="page" />
<fmt:setLocale value="${ language }"/>
<fmt:setBundle basename="ua.nure.yakovenko.Task2.i18n.text"/>
<!DOCTYPE html>
<html lang="${ language }">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>User Listing</title>
<style type="text/css">
	<%@include file="styles/main.css" %>
</style>
</head>
<body>
	<h1 class="text-center">User listing</h1>
	<h2><fmt:message key="test.welcome"/></h2>
</body>
</html>