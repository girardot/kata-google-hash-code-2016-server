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
        <th>Upload</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>${teamName}</td>
        <td>Simple</td>
        <td>
            <form method="post" action="upload/simple" enctype="multipart/form-data">
                <label class="btn btn-default btn-file">
                    Browse <input name="simple" type="file" style="display: none;">
                </label>
                <input id="addFile" type="submit" value="Submit" class="btn btn-success"/>
            </form>
        </td>
    </tr>
    <tr>
        <td>${teamName}</td>
        <td>Busy Day</td>
        <td>
            <form method="post" action="upload/busy_day" enctype="multipart/form-data">
                <label class="btn btn-default btn-file">
                    Browse <input name="busy_day" type="file" style="display: none;">
                </label>
                <input id="addFile" type="submit" value="Submit" class="btn btn-success"/>
            </form>
        </td>
    </tr>
    <tr>
        <td>${teamName}</td>
        <td>Redundancy</td>
        <td>
            <form method="post" action="upload/redundancy" enctype="multipart/form-data">
                <label class="btn btn-default btn-file">
                    Browse <input name="redundancy" type="file" style="display: none;">
                </label>
                <input id="addFile" type="submit" value="Submit" class="btn btn-success"/>
            </form>
        </td>
    </tr>
    <tr>
        <td>${teamName}</td>
        <td>Mother of all Warehouses</td>
        <td>
            <form method="post" action="upload/mother_of_all_warehouses" enctype="multipart/form-data">
                <label class="btn btn-default btn-file">
                    Browse <input name="mother_of_all_warehouses" type="file" style="display: none;">
                </label>
                <input id="addFile" type="submit" value="Submit" class="btn btn-success"/>
            </form>
        </td>
    </tr>
    </tbody>
</table>
</body>
</html>
