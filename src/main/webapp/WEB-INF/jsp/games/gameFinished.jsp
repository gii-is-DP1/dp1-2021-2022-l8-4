<%@ page session="false" trimDirectiveWhitespaces="true" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                    <%@ taglib prefix="kingoftokyo" tagdir="/WEB-INF/tags" %>
                    <%@ taglib prefix="kingoftokyo" tagdir="/WEB-INF/tags" %>

                        <kingoftokyo:layout pageName="games">
                            <h2>Partida terminada :</h2>

                            <table id="gamesTable" class="table table-striped">
                                <thead>
                                    <tr>
                                        <th style="width: 150px;">Game Name</th>
                                        <th style="width: 120px;">Creator</th>
                                        <th style="width: 120px">Turn</th>
                                        <th style="width: 120px">Winner</th>
                                        <th style="width: 150px">Start time</th>
                                        <th style="width: 150px">End time</th>
                                        <th style="width: 100px;">Jugadores</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    
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
                                                <c:out value="${game.winner}" />
                                            </td>
                                            <td>
                                                <kingoftokyo:localDateTime  dateTimeValue= "${game.startTime}" />
                                            </td>
                                            <td>
                                                <kingoftokyo:localDateTime  dateTimeValue= "${game.endTime}" />
                                            </td>
                                            <td>
                                                <c:out value="${game.playersAmount()}" /> 
                                             </td>
                                        </tr>
                                    
                                </tbody>
                            </table>
                        </kingoftokyo:layout>