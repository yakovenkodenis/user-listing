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
	<%@ include file="styles/main.css" %>
</style>
</head>
<body>
	<h1 class="text-center"><a href="/Task2" class="disable-link">User listing</a></h1>
	<form>
		<select id="language" name="language" onchange="submit()">
			<option value="ru" ${language == 'ru' ? 'selected' : ''}><fmt:message key="lang.russian" /></option>
			<option value="en" ${language == 'en' ? 'selected' : ''}><fmt:message key="lang.english" /></option>
		</select>
	</form>
	<br>
	<div class="switch-block">
		<a  href="#login" class="form-switch login"><fmt:message key="auth.switch.login"/> </a>|<a href="#signup" class="form-switch signup"> <fmt:message key="auth.switch.signup"/></a>
	</div>
	<br><br>
	<div class="login-form" id="login">
		<form method="post" action="authentication">
			<c:forEach items="${errorLoginMessage}" var="message">
				<div class="error-message">${message}</div>
			</c:forEach>
			<label for="email"><fmt:message key="login.label.email"/></label>
			<input type="text" placeholder="email@example.com" name="email" value="${email}" />
			<label for="password"><fmt:message key="login.label.password"/></label>
			<input type="password" placeholder="•••••••••••" name="password" />
			<input type="text" name="language" class="hidden" value="${language}" />
			<input type="text" name="ACTION" class="hidden" value="login"/>
			<fmt:message key="login.input.submit" var="loginValue"/>
			<input type="submit" value="${loginValue}" />
		</form>
	</div>
	
	<div class="signup-form hidden" id="signup">
		<form method="post" action="authentication">
			<c:forEach items="${errorSignupMessage}" var="message">
				<div class="error-message">${message}</div>
			</c:forEach>
			<label for="name"><fmt:message key="signup.label.name"/></label>
			<input type="text" name="name" />
			<label for="login"><fmt:message key="signup.label.login"/></label>
			<input type="text" name="login" />
			<label for="email"><fmt:message key="signup.label.email"/></label>
			<input type="text" name="email" />
			<label for="password"><fmt:message key="signup.label.password"/></label>
			<input type="password" name="password" />
			<input type="text" name="language" class="hidden" value="${language}" />
			<input type="text" name="ACTION" class="hidden" value="signup"/>
			<fmt:message key="signup.input.submit" var="signupValue"/>
			<input type="submit" value="${signupValue}" />
		</form>
	</div>
	<script>
		<%@ include file="js/main.js" %>
	</script>
</body>
</html>