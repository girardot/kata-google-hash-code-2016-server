<html>
<body>
<a href="">home</a><br>
<a href="upload">upload</a><br>

scores : <br>
<#list scoresByTeam as scoreByTeam>
${scoreByTeam.name} :
    <#list scoreByTeam.scores as score>
    ${score.inputType}
    ${score.value} <br>
    </#list>
</#list>
</body>
</html>