<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="kingoftokyo" tagdir="/WEB-INF/tags" %>

<kingoftokyo:layout pageName="error">

    <spring:url value="/resources/images/Gigazaur.jpg" var="errorImage1"/>
    <spring:url value="/resources/images/cyberbunny.jpg" var="errorImage2"/>
    <div style="text-align: center;">
        <h1 style="padding-left: 2em ;">Alto ahi!</h1>
        <img src="${errorImage1}"/>
        <img src="${errorImage2}"/>
    </div>
    <br><br>
    <h2>Te has encontrado con los guardianes de Tokyo</h2>
    <h3>Parece que no puedes pasar</h3>

    <p>${exception.message}</p>
    <a style="font-weight: bold; size: 50rem;" href="/"><span class="glyphicon glyphicon-chevron-left"> </span>Volver al inicio</a>

</kingoftokyo:layout>
