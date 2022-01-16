<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="kingoftokyo" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->

        <kingoftokyo:layout pageName="rules">
            
            <div class="row">
                <h1 style="display: flex;flex-direction: column;justify-content: center;text-align: center; margin-bottom: 0px;">
                Conoce las reglas del juego</h1>
                <spring:url value="/resources/images/reglas.png" htmlEscape="true" var="reglas" />
                <img class="img-responsive" src="${reglas}" style="width: 100%;"/>
            </div>
            <div class="row">
                <h4 style="display: flex;flex-direction: column;justify-content: center;text-align: center; margin-bottom: 10px;">
                    Si quieres saber como se ve el juego en fisico, te dejamos este video</h4>
            </div>
            <div class="row" style="display: flex;justify-content: center;text-align: center;">
                <iframe width="560" height="315" src="https://www.youtube.com/embed/ohXMYBgUpqY" 
                title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; 
                encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
            </div>
            
        </kingoftokyo:layout>