/**
 * Created by Charl on 2015-09-09.
 */
;(function(angular, global){
	global.kmeApp = angular.module('kmeApp', ['ngRoute'])

		.factory("RestServiceBase", [ "$http", "$q", function($http, $q) {
			return {
				'_callService' : function(method, url, sendingData, params) {
					var restPath;
					if (contextPath === "/" || contextPath === "") {
						restPath = "/services/";
					} else {
						restPath = contextPath + "/services/";
					}
					var deferred = $q.defer();

					var token =  $("meta[name='_csrf']").attr("content");
					var header = $("meta[name='_csrf_header']").attr("content");
					var headers = {};

					if (header != null && header != "") {
						headers[header] = token;
					}

					$http({
						'method' : method,
						'url' : restPath + url,
						'data' : sendingData,
						'params' : params,
						'headers' : headers
					}).success(function(data, status, headers, config) {
						deferred.resolve(data == "" ? null : data);
					}).error(function(data, status, headers, config) {
						deferred.reject(data);
					});
					return deferred.promise;
				}
			}

		} ])
})(angular, this);