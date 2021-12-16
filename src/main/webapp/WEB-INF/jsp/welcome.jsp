<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->

                <petclinic:layout pageName="Inicio">
                    <div class="row">
                        <h1 style="display: flex;flex-direction: column;justify-content: center;text-align: center;">Bienvenidos</h1>
                        <spring:url value="/resources/images/principal.png" htmlEscape="true" var="principal" />
                        <img class="img-responsive" src="${principal}" style="width: 45%; margin-left: 27%;"/>
                    </div>
                    

                    <div class="row">
                        <h2>Project ${title}</h2>
                        <p><h2> Group ${group} </h2></p>
                        <p>
                            <ul>
                                <c:forEach items="${persons}" var="person">
                                    <li>${person.firstName} ${person.lastName}</li>
                                </c:forEach>  
                            </ul>
                        </p>
                    </div>
                </petclinic:layout>