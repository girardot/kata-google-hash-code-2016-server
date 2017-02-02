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
    <tr>
        <td>${scoreByTeam.name}</td>
        <td>SUM</td>
        <td>${scoreByTeam.teamScore}</td>
    </tr>
    </#list>
    </tbody>
</table>