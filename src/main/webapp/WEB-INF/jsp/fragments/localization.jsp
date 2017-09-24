<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<li class="dropdown">
    <a href="#" class="dropdown-toggle" data-toggle="dropdown">${pageContext.response.locale}</a>
    <ul class="dropdown-menu">
        <li><a onclick="switchLocale('en')">English</a></li>
        <li><a onclick="switchLocale('ru')">Русский</a></li>
    </ul>
</li>

<script type="text/javascript">
    function switchLocale(lang) {
        window.location.href = window.location.href.split('?')[0] + '?lang=' + lang;
    }
</script>