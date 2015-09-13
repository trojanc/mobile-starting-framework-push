/**
 * Created by Charl on 2015-09-09.
 */
;(function(angular, global){
	global.kmeApp
		.config(['$routeProvider',
			function($routeProvider) {
				$routeProvider
					.when('/', 						{templateUrl: contextPath + '/html/device/menu.html'})
					.when('/search',				{templateUrl: contextPath + '/html/device/search.html', controller : 'SearchDevicesController'})
					.when('/details/:deviceId',		{templateUrl: contextPath + '/html/device/details.html', controller : 'DeviceDetailsController'})
					.when('/purge',					{templateUrl: contextPath + '/html/device/purge.html'})
					.otherwise({ redirectTo: '/' });
			}]);
})(angular, this);