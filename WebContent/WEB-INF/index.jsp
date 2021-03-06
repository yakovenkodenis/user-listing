<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="ua.nure.yakovenko.Task2.i18n.text"/>
<!DOCTYPE html>
<html lang="${language}">
<jsp:include page="/WEB-INF/head.jsp">
	<jsp:param value="User Listing" name="title"/>
</jsp:include>
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
		<div class="column-nav">
			<c:if test="${sessionScope.role == 'admin'}">
				<a class="column-nav-link" href="${pageContext.request.contextPath}/create"><fmt:message key="users.create" /></a>
			</c:if>
			<a class="column-nav-link" href="${pageContext.request.contextPath}/edit"><fmt:message key="users.edit_own" /></a>
		</div>
	</nav>

	<!-- <h5><fmt:message key="index.greeting.hello" />, ${user.login}! <fmt:message key="index.greeting.your_role" /> ${user.role}</h5>
	<br> -->

	<c:set var="role" value="<%= session.getAttribute(\"role\") %>" scope="session" />
	<c:set var="currentUserEmail" value="<%= session.getAttribute(\"email\")%>" scope="session"/>
	
	<br><br><br>
	<div class="users-list">
	<c:choose>
		<c:when test="${sessionScope.role == 'admin'}">
			<c:forEach items="${users}" var="user" varStatus="loop">
				<div class="user">
					<span class="user-id">${loop.index + 1}.</span>
					<span class="user-name">${user.name}</span>
					<span class="user-login">${user.login}</span>
					<span class="user-role">${user.role}</span>
					<c:choose>
						<c:when test="${sessionScope.currentUserEmail != user.email}">
							<a href="${pageContext.request.contextPath}/edit/${user.id}"><fmt:message key="users.edit" /></a>
							<a href="${pageContext.request.contextPath}/delete/${user.id}"><fmt:message key="users.delete" /></a>
						</c:when>
						<c:otherwise>
							<span class="current-user-indication">(<fmt:message key="current_user.this_is_you" />)</span>
						</c:otherwise>
					</c:choose>
				</div>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<c:forEach items="${users}" var="user" varStatus="loop">
				<div class="user">
					<span class="user-id">${loop.index + 1}.</span>
					<span class="user-name">${user.name}</span>
					<span class="user-login">${user.login}</span>
					<span class="user-role">${user.role}</span>
					<c:if test="${sessionScope.currentUserEmail == user.email}">
						<span class="current-user-indication">(<fmt:message key="current_user.this_is_you" />)</span>
					</c:if>
				</div>
			</c:forEach>
		</c:otherwise>
	</c:choose>
	</div>
</body>
</html>