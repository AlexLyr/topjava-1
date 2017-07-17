<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.jsp">Home</a></h3>
<h2>Meal List</h2>

<table border="1">
    <tr  align="center" valign="middle">
        <td width="120">Description</td>
        <td width="150">DateTime</td>
        <td width="100">Calories</td>
        <td width="100">Edit</td>
        <td width="100">Delete</td>
    </tr>
    <jsp:useBean id="meals" scope="request" type="java.util.List"/>
    <c:forEach var="meal1" items="${meals}">
        <tr style="color: ${meal1.isExceed()?'red':'green'}"  align="center" valign="middle">
            <td>${meal1.getDescription()}</td>
            <td>${meal1.getFormattedDate()}</td>
            <td>${meal1.getCalories()}</td>
            <td><a href="<c:url value='/mealsServlet?action=edit&mealId=${meal1.id}'/>">Edit</a></td>
            <td><a href="<c:url value='/mealsServlet?action=delete&mealId=${meal1.id}'/>">Remove</a></td>
        </tr>
    </c:forEach>
</table>
<a href="meal.jsp">Add User</a>
</body>
</html>
