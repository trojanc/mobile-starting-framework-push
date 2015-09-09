/**
 * Created by Charl on 2015-09-09.
 */
;(function(angular){
	angular.module('kme-push', ['ngRoute'])
		.config(['$routeProvider',
			function($routeProvider) {
				$routeProvider
					.when('/', 			{templateUrl: contextPath + '/html/push/menu.html'})
					.when('/send',		{templateUrl: contextPath + '/html/push/send.html'})
					.when('/history',	{templateUrl: contextPath + '/html/push/history.html'})
					.when('/queue',		{templateUrl: contextPath + '/html/push/queue.html'})
					.otherwise({ redirectTo: '/' });
			}]);
})(angular);