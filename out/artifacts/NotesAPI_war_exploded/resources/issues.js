var NotesAPI = angular.module('NotesAPI', []);
NotesAPI.controller('gitAPIController', function($scope,$http) {
    $http.get("https://api.github.com/repos/angular/angular/issues?since=2017-09-13T15:13:00+00:00")
        .success(function(data) {
            $scope.issueData = data;
        });
});