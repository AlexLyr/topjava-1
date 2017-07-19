<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meal</title>
</head>
<body>
<h3><a href="index.jsp">Home</a></h3>
<h2>Meal List</h2>

<c:if test="${!empty meal.id}">
    <jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<table border="1">
    <tr  align="center" valign="middle">
        <td width="120">Description</td>
        <td width="150">DateTime</td>
        <td width="100">Calories</td>
    </tr>

    <tr  align="center" valign="middle">
        <td>${meal.getDescription()}</td>
        <td>${meal.getFormattedDate()}</td>
        <td>${meal.getCalories()}</td>
    </tr>
</table>
</c:if>
<form method="POST" action='mealsServlet' name="addmeal">
<c:if test="${!empty meal.id}">
    Meal ID:<input type="text" readonly="readonly" name="mealId"
               value="<c:out value="${meal.id}"/>"/> <br/>
</c:if>
    Description:<input type="text" name="description"
                       value="<c:out value="${meal.description}"/>"/> <br/>
    Calories:<input type="text" name="calories" value="<c:out value="${meal.calories}"/>"/> <br/>

    <input type="submit" value="${!empty meal.id?'Edit':'Add'}"/>

</form>
</body>
</html>
