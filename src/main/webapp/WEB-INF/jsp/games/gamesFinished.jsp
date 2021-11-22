<%@ page session="false" trimDirectiveWhitespaces="true" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                    <%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

                        <petclinic:layout pageName="games">
                            <h2>Games</h2>

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
                                    <c:forEach items="${games}" var="game">
                                        <tr>
                                            <td>
                                                <c:out value="${game.name}" />
                                                </a>
                                            </td>
                                            <td>
                                                <c:out value="${game.creator}" />
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
                                                <c:out value="${game.startTime}" />
                                            </td>
                                            <td>
                                                <c:out value="${game.endTime}" />
                                            </td>
                                            <td>
                                                <c:out value="${game.playersAmount()}" /> 
                                             </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </petclinic:layout>