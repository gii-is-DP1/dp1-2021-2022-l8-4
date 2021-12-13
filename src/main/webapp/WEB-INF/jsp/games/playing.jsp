<%@ page session="false" trimDirectiveWhitespaces="true" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
                        <%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

                            <petclinic:layout pageName="dices">
                                <c:if test="${isPlayerInGame}">
                                    <a href="/players/${actualPlayer.getId()}/surrender">
                                        <button type="button">
                                        SALIR DE LA PARTIDA
                                    </button>
                                    </a>
                                </c:if>
                                <c:if test="${!isPlayerInGame}">
                                    <h1>MODO ESPECTADOR</h1>
                                </c:if>
                                <h2>
                                    Jugadores vivos:
                                    <c:out value="${game.playersAlive().size()}" /> /
                                    <c:out value="${game.playersAmount()}" />
                                </h2>

                                <h2>
                                    Turno actual:
                                    <c:out value="${game.turn}" />
                                </h2>
                                <h2>
                                    Es el turno de:
                                    <img src="${actualPlayerTurn.monster.getIcon()}" width="40" height="40">
                                    <c:out value="${actualPlayerTurn.monster.getName()} (${actualPlayerTurn.user.username})" />
                                </h2>

                                <h1>Tirada de dados</h1>

                                <c:if test="${isPlayerTurn}">
                                    <form:form modelAttribute="roll">
                                        <span>
                                        Cantidad de tiradas realizadas: 
                                        <c:out value="${roll.rollAmount}" /> / <c:out value="${roll.maxThrows}" />
                                    </span>



                                        <c:if test="${!roll.isFinished()}">
                                            <c:if test="${roll.rollAmount != 0}">
                                                <table>
                                                    <tr>
                                                        <td>
                                                            <form:checkboxes items="${roll.values}" path="keep" />
                                                        </td>
                                                    </tr>
                                                </table>

                                            </c:if>


                                            <input type="hidden" value="${roll.rollAmount}" name="rollAmount"></input>

                                            <input type="hidden" value="False" name="newTurn"></input>
                                            <br>
                                            <input type="submit" value="REALIZAR TIRADA DE DADOS">
                                        </c:if>

                                        <input type="hidden" value="${roll.rollAmount}" name="rollAmount"></input>
                                        <input type="hidden" value="${turnList}" name="turnList"></input>

                                        <c:if test="${roll.isFinished()}">
                                            <h2>FINAL TURNO</h2>
                                            <input type="hidden" value="True" name="newTurn"></input>
                                            <input type="submit" value="FINALIZAR TURNO">
                                        </c:if>
                                    </form:form>
                                </c:if>

                                <table id="dicesTable" class="table table-striped">
                                    <thead>
                                        <tr>
                                            <th style="width: 150px;">Tirada</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <c:forEach items="${roll.values}" var="dice">

                                                <td>
                                                    <img src="${dice.getIcon()}" width="30" height="30">
                                                    <c:out value="${dice}" />
                                                </td>

                                            </c:forEach>
                                        </tr>
                                    </tbody>
                                </table>

                                <input type="hidden" value="${turnList}" name="turnList"></input>

                                <input type="hidden" value="${roll.values}" name="values"></input>





                            <div class="container-fluid">
                                <div class="row">

                                    <div class="col-1 col-sm-1  align-self-center">
                                        <h2>Turnos</h2>
                                            <c:forEach items="${orderedPlayers}" var="ordererPlayer">
                                                <div class="row" >
                                                    <img src="${ordererPlayer.monster.getIcon()}" width="50" height="50" style="margin-left: 20px;">
                                                    </a>
                                                </div>
                                            </c:forEach>
                                    </div>

                                    <div class="col-11 col-lg-11">
                                        <h2>Players</h2>

                                        <table id="playersTable" class="table table-striped">
                                            <thead>
                                                <tr>
                                                    <th style="width: 150px;">Username</th>
                                                    <th style="width: 150px;">Monster Name</th>
                                                    <th style="width: 120px;">Life Points</th>
                                                    <th style="width: 120px">Victory Points</th>
                                                    <th style="width: 120px">Energy Points</th>
                                                    <th style="width: 150px">Location</th>
                                                    <th style="width: 150px">Cards</th>
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
                                                        <td>
                                                            <c:out value="${player.lifePoints}" />
                                                            </a>
                                                        </td>
                                                        <td>
                                                            <c:out value="${player.victoryPoints}" />
                                                            </a>
                                                        </td>
                                                        <td>
                                                            <c:out value="${player.energyPoints}" />
                                                        </td>
                                                        <td>
                                                            <c:out value="${player.location}" />
                                                        </td>
                                                        <td>
                                                            <c:forEach items="${player.getAvailableCards()}" var="card">
                                                                <c:out value="${card.cardEnum.getName()}" />
                                                                <br>
                                                            </c:forEach>
                                                        </td>
                                                        <td>
                                                            <c:if test="${player.getRecentlyHurt()&&AuthenticatedPlayer==player}"> 
                                                                <a href="/games/${gameId}/exitTokyo"><button type="button" >Salir de Tokyo</button></a>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>                                

                                </div>
                            </div>



                                <h2>Card Shop</h2>

                                <table id="cardsTable" class="table table-striped">
                                    <thead>
                                        <tr>
                                            <c:if test="${isPlayerTurn && roll.isFinished()}">
                                                    <a href="/players/${actualPlayerTurn.id}/cards/discard">
                                                        <button type="button" >
                                                            Descartar cartas de la tienda por 2 
                                                            <img src="/resources/images/diceValues/energy.png" width="50" height="50"> 
                                                        </button>
                                                    </a>
                                            </c:if>
                                        </tr>
                                        <tr>
                                            <th style="width: 150px;">Card Name</th>
                                            <th style="width: 120px;">Cost</th>
                                            <th style="width: 120px">Type</th>
                                            <th style="width: 120px"></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${cards}" var="card">
                                            <tr>
                                                <td>
                                                    <c:out value="${card.cardEnum.getName()}" />
                                                    </a>
                                                </td>
                                                <td>
                                                    <c:out value="${card.cost}" />
                                                    </a>
                                                </td>
                                                <td>
                                                    <c:out value="${card.type}" />
                                                    </a>
                                                </td>
                                                <td>
                                                    <c:if test="${isPlayerTurn && roll.isFinished()}">
                                                        <a href="/players/${actualPlayerTurn.id}/cards/${card.id}/buy"><button type="button" >Comprar</button></a>
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>


                            </petclinic:layout>