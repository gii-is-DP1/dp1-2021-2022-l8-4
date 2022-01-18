<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="kingoftokyo" tagdir="/WEB-INF/tags" %>

<kingoftokyo:layout pageName="error">

    <spring:url value="/resources/images/dados.jpg" var="errorImage1"/>
    <h2>Oh no, hemos vuelto a perder los dados!</h2>
    <img style="max-width: 10rem;" src="${errorImage1}"/>
    <br><br>
    <h3>Error 500, vuelve a intentarlo mas tarde</h3>

    <p>${exception.message}</p>
</kingoftokyo:layout>
