<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meal List</h2>

<table border="1">
    <tr  align="center" valign="middle">
        <td width="120">Description</td>
        <td width="150">DateTime</td>
        <td width="100">Calories</td>
    </tr>
    <jsp:useBean id="meals" scope="request" type="java.util.List"/>
    <c:forEach var="meal" items="${meals}">
        <tr style="color: ${meal.isExceed()?'red':'green'}"  align="center" valign="middle">
            <td>${meal.getDescription()}</td>
            <td>${meal.getFormattedDate()}</td>
            <td>${meal.getCalories()}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
