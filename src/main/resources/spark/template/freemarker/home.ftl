<html>
<head>
    <link href="css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<#include "menu.ftl">

<table class="table">
    <thead>
    <tr>
        <th>Team</th>
        <th>Input Type</th>
        <th>Score</th>
    </tr>
    </thead>
    <tbody>
    <#list scoresByTeam as scoreByTeam>
        <#list scoreByTeam.scores as score>
        <tr>
            <td>${scoreByTeam.name}</td>
            <td>${score.inputType}</td>
            <td>${score.value}</td>
        </tr>
        </#list>
    </#list>
    </tbody>
</table>
</body>
</html>