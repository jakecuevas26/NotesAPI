<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.0.8/angular.min.js"></script>
<head>
  <h1>Git Issues API</h1>
</head>

<div class="text-center" ng-app="NotesAPI" ng-controller="gitAPIController" data-ng-init="init()">
  <ul>
    <li ng-repeat="x in issueData">
      <h2>{{"Issue Title: " + x.title}}</h2>
      <h3>{{"User Login: " + x.user.login}}</h3>
      <h3>{{"Assignee Login: " + x.assignee.login}}</h3>
      <br>
      {{"Issue: " + x.body }}
      <br>
      <br>
    </li>
  </ul>
</div>

<script>
    var NotesAPI = angular.module('NotesAPI', []);
    NotesAPI.controller('gitAPIController', function($scope,$http) {
        $http.get("https://api.github.com/repos/angular/angular/issues?since=2017-09-13T15:13:00+00:00")
            .success(function(data) {
                console.log(data);
                console.log("hello");
                $scope.issueData = data;
            });
    });
</script>
