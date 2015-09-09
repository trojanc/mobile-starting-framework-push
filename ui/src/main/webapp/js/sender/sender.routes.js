/**
 * Created by Charl on 2015-09-09.
 */
;(function(angular){
	angular.module('kme-sender', ['ngRoute'])
		.config(['$routeProvider',
			function($routeProvider) {
				$routeProvider
					.when('/', 			{templateUrl: contextPath + '/html/sender/menu.html'})
					.when('/create',	{templateUrl: contextPath + '/html/sender/editSender.html'})
					.otherwise({ redirectTo: '/' });
			}]);
})(angular);