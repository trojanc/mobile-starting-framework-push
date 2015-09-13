/**
 * Created by Charl on 2015-09-09.
 */
;(function(angular, global){
    global.kmeApp
    /**
     * Rest service client for the TypeRestService
     */
        .controller("SearchDevicesController",
        ["$scope", "DeviceRestService", function($scope, DeviceRestService) {

            DeviceRestService.devices().then(function(response){
                $scope.searchResults = response.devices;
            })


        } ])
        .controller("DeviceDetailsController",
        ["$scope", "$routeParams", "DeviceRestService", function($scope, $routeParams, DeviceRestService) {

            DeviceRestService.findDeviceById($routeParams.deviceId).then(function(response){
                $scope.device = response.device;
            })


        } ]);



})(angular, this);