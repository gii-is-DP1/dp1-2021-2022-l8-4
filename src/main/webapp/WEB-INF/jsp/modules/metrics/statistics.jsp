<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->

        <petclinic:layout pageName="statistics">
            <div class="container">
                 <div class="row">
                    <h1 style="display: flex;flex-direction: column;justify-content: center;text-align: center; margin-bottom: 0px;
                                margin-bottom: 14px;">
                    ESTADISTICAS</h1>
                </div>
                <div class="row">
                    <div class="col col-sm-6" style="align-items: center;justify-content: center;display: flex;">
                        <h4>
                            Ranking usuarios
                        </h4>
                    </div>
                    <div class="col col-sm-6" style="align-items: center;justify-content: center;display: flex;">
                        <h4>
                            Estadisticas del juego
                        </h4>
                    </div>
                </div>
                <div class="row" >
                    <div class="col col-sm-6">
                        <hr style="margin-right: 17%;margin-left: 18%;">
                    </div>
                    <div class="col col-sm-6">
                        <hr style="margin-left: 17%;margin-right: 13%;">
                    </div>
                </div>
                <div class="row">
                    <div class="col col-sm-3" style="align-items: center;justify-content: center;display: grid;">
                        <h4>
                            Mas partidas ganadas
                        </h4>

                        <table class="table">
                            <tbody>
                                <tr>
                                    <th>1</th>
                                    <th>User1</th>
                                    <th>10</th>
                                </tr>
                                <tr>
                                    <th>1</th>
                                    <th>User1</th>
                                    <th>10</th>
                                </tr>
                                <tr>
                                    <th>1</th>
                                    <th>User1</th>
                                    <th>10</th>
                                </tr>
                                <tr>
                                    <th>1</th>
                                    <th>User1</th>
                                    <th>10</th>
                                </tr>
                            </tbody>
                        </table>

                    </div>
                    <div class="col col-sm-3" style="align-items: center;justify-content: center;display: grid;">
                        <h4>
                            Mas puntuacion de logros
                        </h4>
                        <table class="table">
                            <tbody>
                                <tr>
                                    <th>1</th>
                                    <th>User1</th>
                                    <th>10</th>
                                </tr>
                                <tr>
                                    <th>1</th>
                                    <th>User1</th>
                                    <th>10</th>
                                </tr>
                                <tr>
                                    <th>1</th>
                                    <th>User1</th>
                                    <th>10</th>
                                </tr>
                                <tr>
                                    <th>1</th>
                                    <th>User1</th>
                                    <th>10</th>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="col col-sm-3" style="align-items: center;justify-content: center;display: grid;">
                        <div class="row">
                            <h4>    
                                Numero de partidas totales que se han jugado
                                <spring:url value="/resources/images/errorK.png" htmlEscape="true" var="principal" />
                                <img class="img-responsive" src="${principal}" style="width: 25%;"/>
                            </h4>
                        </div>
                        <div class="row">
                            <h4>    
                                Criatura elegida con mas frecuencia
                                <spring:url value="/resources/images/errorK.png" htmlEscape="true" var="principal" />
                                <img class="img-responsive" src="${principal}" style="width: 25%;"/>
                            </h4>
                        </div>
                        <div class="row">
                            <h4>    
                                Criatura elegida con menos frecuencia
                                <spring:url value="/resources/images/errorK.png" htmlEscape="true" var="principal" />
                                <img class="img-responsive" src="${principal}" style="width: 25%;"/>
                            </h4>
                        </div>
                    </div>
                    <div class="col col-sm-3" style="align-items: center;justify-content: center;display: grid;">
                        <div class="row">
                            <h4>    
                                Tiempo medio por partida
                                <spring:url value="/resources/images/errorK.png" htmlEscape="true" var="principal" />
                                <img class="img-responsive" src="${principal}" style="width: 25%;"/>
                            </h4>
                        </div>
                        <div class="row">
                            <h4>    
                                Jugadores que mas tiempo han estado dentro de tokyo
                                <table class="table">
                                    <tbody>
                                        <tr>
                                            <th>1</th>
                                            <th>User1</th>
                                            <th>10</th>
                                        </tr>
                                        <tr>
                                            <th>1</th>
                                            <th>User1</th>
                                            <th>10</th>
                                        </tr>
                                        <tr>
                                            <th>1</th>
                                            <th>User1</th>
                                            <th>10</th>
                                        </tr>
                                        <tr>
                                            <th>1</th>
                                            <th>User1</th>
                                            <th>10</th>
                                        </tr>
                                    </tbody>
                                </table>
                            </h4>
                        </div>
                    </div>
                    

                </div>
                
            </div>
           
        </petclinic:layout>