<%@ page session="false" trimDirectiveWhitespaces="true" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                    <%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

                        <petclinic:layout pageName="dices">
                            <h2>Roll</h2>

                            <table id="dicesTable" class="table table-striped">
                                <thead>
                                    <tr>
                                        <th style="width: 150px;">Value</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${dices}" var="dice">
                                        <tr>
                                            <td>
                                                <c:out value="${dice}" />
                                                </a>
                                            </td>

                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </petclinic:layout>