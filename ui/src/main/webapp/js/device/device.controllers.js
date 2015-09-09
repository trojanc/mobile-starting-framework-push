/**
 * Created by Charl on 2015-09-09.
 */
;(function(angular, global){
    global.kmeApp
    /**
     * Rest service client for the TypeRestService
     */
        .controller("SearchDevicesController", ["$scope", "DeviceRestService", function($scope, DeviceRestService) {

            DeviceRestService.devices().then(function(response){
                $scope.searchResults = response.devices;
            })


        } ]);
})(angular, this);