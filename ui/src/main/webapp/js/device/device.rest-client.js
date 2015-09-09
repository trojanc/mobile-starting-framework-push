/**
 * Created by Charl on 2015-09-09.
 */
;(function(angular, global){
    global.kmeApp
    /**
     * Rest service client for the TypeRestService
     */
    .factory("DeviceRestService", [ "RestServiceBase", function(RestServiceBase) {
        return angular.extend({
            'devices' : function() {
                return this._callService('GET', 'device-api/devices/');

            }
        }, RestServiceBase);

    } ]);
})(angular, this);