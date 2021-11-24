<%@ page session="false" trimDirectiveWhitespaces="true" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                    <%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
                        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

                        <petclinic:layout pageName="newGameForm">
                            
                            <form:form modelAttribute="newGame">
                                <tr class="form-group">
                                    <td>Nombre de la partida</td>
                                    <form:input path="name"/>
                                </tr>

                                <tr>
                                    <td>Numero maximo de jugadores</td>
                                    <td><form:radiobuttons path="maxNumberOfPlayers" items="${[2,3,4,5,6]}"/></td>
                                </tr>
                                
                                <tr>
                                    <input type="submit" value="Crear nueva partida"/>
                                </tr>
                            </form:form>

                        </petclinic:layout>