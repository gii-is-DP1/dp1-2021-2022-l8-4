<%@ page session="false" trimDirectiveWhitespaces="true" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                    <%@ taglib prefix="kingoftokyo" tagdir="/WEB-INF/tags" %>
                        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

                        <kingoftokyo:layout pageName="lobbiesList">
                            
                            <table class="table table-striped">
                                    <thead>
                                        <tr>
                                            <th style="width: 150px;">Nombre de partida</th>
                                            <th style="width: 120px;">Creador</th>
                                            <th style="width: 120px;">Numero maximo de jugadores</th>
                                            <th style="width: 120px;"></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${lobbies}" var="game">
                                        <tr>
                                            <td>
                                                <c:out value="${game.name}" />
                                                </a>
                                            </td>
                                            <td>
                                                <c:out value="${game.creator.username}" />
                                                </a>
                                            </td>
                                            <td>
                                                <c:out value="${game.maxNumberOfPlayers}" />
                                                </a>
                                            </td>
                                            <td>
                                                <a href="/games/${game.id}/lobby" ><button type="button" >Unirse</button></a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                            </table>
                        </kingoftokyo:layout>