<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->

        <petclinic:layout pageName="rules">
            
            <div class="row">
                <h1 style="display: flex;flex-direction: column;justify-content: center;text-align: center; margin-bottom: 0px;">
                Conoce las reglas del juego</h1>
                <spring:url value="/resources/images/reglas.png" htmlEscape="true" var="reglas" />
                <img class="img-responsive" src="${reglas}" style="width: 100%;"/>
            </div>
            
        </petclinic:layout>