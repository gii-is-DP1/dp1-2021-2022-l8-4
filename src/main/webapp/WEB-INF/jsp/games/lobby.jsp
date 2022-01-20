<%@ page session="false" trimDirectiveWhitespaces="true" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                    <%@ taglib prefix="kingoftokyo" tagdir="/WEB-INF/tags" %>
                        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

                        <kingoftokyo:layout pageName="gameLobby">
                            
                       <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th style="width: 150px;">Nombre de partida</th>
                                    <th style="width: 120px;">Creador</th>
                                    <th style="width: 120px;">Numero maximo de jugadores</th>
                                </tr>
                            </thead>
                            <tbody>
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
                            </tr>
                            </tbody>
                        </table>

                        <h2>Jugadores</h2>

                            <table id="playersTable" class="table table-striped">
                                <thead>
                                    <tr>
                                        <th style="width: 150px;">Usuario</th>
                                        <th style="width: 120px;">Monstruo</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${players}" var="player">
                                        <tr>
                                            <td>
                                                <c:out value="${player.user.username}" />
                                                </a>
                                            </td>
                                            <td>
                                                <img src="${player.monster.getIcon()}" width="40" height="40">
                                                <c:out value="${player.monster.getName()}" />
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>

                            <c:if test="${!currentUser.hasActivePlayer()}">
                                <form:form modelAttribute="newPlayer">
                                    <tr>
                                        <td>Selecciona tu monstruo</td>
                                        <td><form:radiobuttons path="monster" items="${availableMonsters}" itemLabel="name"/></td>
                                    </tr>

                                    <input type="hidden" value="0" name="energyPoints"></input>
                                    <input type="hidden" value="0" name="lifePoints"></input>
                                    <input type="hidden" value="0" name="victoryPoints"></input>
                                    <input type="hidden" value="FUERATOYKO" name="locationType"></input>
                                    <input type="hidden" value="FALSE" name="recentlyHurt"></input>

                                    <tr>
                                        <input type="submit" value="Unirse a partida"/>
                                    </tr>
                                </form:form>
                            </c:if>

                            <c:if test="${game.creator.id == currentUser.id}">
                                
                                <a href="/games/${game.id}/lobby/delete"><button type="submit" >Borrar partida</button></a>
                                <a href="/games/${game.id}/start"><button type="button" >Iniciar partida</button></a>
                            </c:if>


                        </kingoftokyo:layout>