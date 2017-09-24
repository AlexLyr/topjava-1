<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="footer">
    <div class="container">
        <spring:message code="app.footer"/>
    </div>
</div>
<script type="text/javascript" src="webjars/jquery/3.2.1/dist/jquery.min.js"></script>
<script type="text/javascript" src="webjars/bootstrap/3.3.7-1/js/bootstrap.min.js" defer></script>
<script type="text/javascript">
    function switchLocale(lang) {
        window.location.href = window.location.href.split('?')[0] + '?lang=' + lang;
    }
</script>