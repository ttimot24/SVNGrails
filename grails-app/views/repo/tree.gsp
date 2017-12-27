<%@ page contentType="text/html;charset=UTF-8" %>

<html>
    <head>
        <meta name="layout" content="main">
        <title>SVN Tree</title>
    </head>
    <body>
        <div class="jumbotron">
            <div class="container">
                <h3 class="col-md-6">Repository: ${repository}</h3>
                <h3 class="col-md-6 text-right">Latest revision ${latestRevision}</h3>
            </div>
        </div>
        
        <div class="col-md-10 col-md-offset-1">
        <ul class="list-group">
        <g:each var="entry" in="${entries}">

            <li class="list-group-item">
              <span class="badge">${entry.getDate()}</span>
              <a href="?dir=${currentDirectory}${entry.getPath()}"><b>${entry.getName()}</b></a> <font class="text-muted" style="font-size:12px;"> | ${entry.getAuthor()}</font>
            </li>

        </g:each>
        </ul>
        </div>
        <div class="col-md-offset-1"></div>
    </body>
</html>
