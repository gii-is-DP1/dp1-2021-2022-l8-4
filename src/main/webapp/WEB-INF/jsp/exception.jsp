<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="error">

    <spring:url value="/resources/images/ErrorK.png" var="errorImage"/>
    <img src="${errorImage}"/>

    <h2>Parece que King ha roto algun cable...</h2>
    <h3>Vuelve a intentarlo mas tarde</h3>

    <p>${exception.message}</p>

</petclinic:layout>
