<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <a href="meals" class="navbar-brand"><spring:message code="app.title"/></a>

        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <form:form class="navbar-form navbar-right" action="logout" method="post">
                        <sec:authorize access="isAuthenticated()">
                            <sec:authorize access="hasRole('ROLE_ADMIN')">
                                <a class="btn btn-info" href="users"><spring:message code="user.title"/></a>
                            </sec:authorize>
                            <a class="btn btn-info" href="profile">${userTo.name} <spring:message
                                    code="app.profile"/></a>
                            <button class="btn btn-primary" type="submit">
                                <span class="glyphicon glyphicon-log-out" aria-hidden="true"></span>
                            </button>
                        </sec:authorize>
                    </form:form>
                </li>
                <%-- <jsp:include page="WEB-INF/jsp/fragments/localization.jsp"/> dont see this page, i dont know why!!--%>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown"> <b>${pageContext.response.locale}</b>
                        <span class="glyphicon glyphicon-arrow-down"></span></a>
                    <ul class="dropdown-menu">
                        <li><a onclick="switchLocale('en')">English</a></li>
                        <li><a onclick="switchLocale('ru')">Русский</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</div>

