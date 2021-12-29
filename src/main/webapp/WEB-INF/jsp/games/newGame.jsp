<%@ page session="false" trimDirectiveWhitespaces="true" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                    <%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
                        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

                       
                        <petclinic:layout pageName="newGameForm">
                            
                            <form:form modelAttribute="newGame" style="margin-bottom:20%">
                                <div class="col-lg-8" style="margin-left: 37%;">
                                    <div class="row" style="display: initial;">
                                            Nombre de la partida
                                            <form:input path="name" style="margin-left: 5%;"/>
                                    </div>
                                    
                                    <div class="row" style="display: initial;">
                                        Numero maximo de jugadores
                                        <form:radiobuttons path="maxNumberOfPlayers" items="${[ 2, 3, 4, 5, 6]}"  style="margin-left: 2%;"/>
                                    </div>

                                    <input type="hidden" value="0" name="turn"></input>
                                    <input type="hidden" value="${currentUser.id}" name="creator"></input>
                                    
                                    <div class="row" style="display: initial;">
                                        <input type="submit" value="Crear nueva partida"/>
                                    </div>
                                </div>  
                            </form:form>

                    </petclinic:layout>