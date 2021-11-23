<%@ page session="false" trimDirectiveWhitespaces="true" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                    <%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
                        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

                        <petclinic:layout pageName="lobbiesList">
                            
                            <table class="table table-striped">
                                    <thead>
                                        <tr>
                                            <th style="width: 150px;">Game Name</th>
                                            <th style="width: 120px;">Creator</th>
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
                                                <a href="/games/${game.id}/lobby" >Unirse
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                            </table>
                        </petclinic:layout>