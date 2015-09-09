/**
 * Created by Charl on 2015-09-09.
 */
;(function(angular){
	angular.module('kme-device', ['ngRoute'])
		.config(['$routeProvider',
			function($routeProvider) {
				$routeProvider
					.when('/', 			{templateUrl: contextPath + 'html/device/menu.html'})
					.when('/search',	{templateUrl: contextPath + 'html/device/search.html'})
					.when('/purge',		{templateUrl: contextPath + 'html/device/purge.html'})
					.otherwise({ redirectTo: '/' });
			}]);
})(angular);