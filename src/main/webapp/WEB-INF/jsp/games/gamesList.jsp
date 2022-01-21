<%@ page session="false" trimDirectiveWhitespaces="true" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                    <%@ taglib prefix="kingoftokyo" tagdir="/WEB-INF/tags" %>

                        <kingoftokyo:layout pageName="games">
                            <h2>Partidas</h2>

                            <table id="gamesTable" class="table table-striped">
                                <thead>
                                    <tr>
                                        <th style="width: 150px;">Nombre de partida</th>
                                        <th style="width: 120px;">Creador</th>
                                        <th style="width: 120px">Turno</th>
                                        <th style="width: 150px">Fecha de inicio</th>
                                        <th style="width: 100px;">Jugadores</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${games}" var="game">
                                        <tr>
                                            <td>
                                                <c:out value="${game.name}" />
                                                </a>
                                            </td>
                                            <td>
                                                <c:out value="${game.creator.getUsername()}" />
                                                </a>
                                            </td>
                                            <td>
                                                <c:out value="${game.turn}" />
                                                </a>
                                            </td>
                                            <td>
                                                <kingoftokyo:localDateTime  dateTimeValue= "${game.startTime}" />
                                            </td>
                                            <td>
                                                <c:out value="${game.playersAlive().size()}" /> /
                                                <c:out value="${game.playersAmount()}" /> 
                                             </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </kingoftokyo:layout>