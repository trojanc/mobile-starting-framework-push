<%--
  The MIT License (MIT)

  Copyright (C) 2014 by Kuali Foundation

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in

  all copies or substantial portions of the Software.
  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
--%>
<%@ page language="java" contentType="application/javascript; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:message code="push.devices.management.title" var="msgCat_Title"/>
<spring:message code="push.devices.unregistered" var="msgCat_Unreg"/>




var devices = angular.module("devicesApp", ['ngRoute', 'ngSanitize','ui.bootstrap'])

.filter('nullUsername', function() {
  return function(input) {
    return input ? input : '<c:out value="${msgCat_Unreg}"/>';
  };
});

devices.controller("DevicesController", function($scope,$http,$window, $templateCache,$location,$sce,$log) {

    $scope.init = function() {
        $scope.labToolTitle = "<c:out value="${msgCat_Title}"/>";
        $scope.pageTitle = $scope.labToolTitle;
		$scope.showSearchResults = false;
		$scope.deleteCriteria = "";
//        $scope.loadCounts();


        var menuItems = "{\"menus\": ["+
            "{ \"divider\":\"true\"},"+                        
            "{ \"url\":\"/push\" , \"label\":\"Push Notifications\" },"+ 
            "{ \"url\":\"/push/history\" , \"label\":\"Push History\" },"+
            "{ \"url\":\"/devices\" , \"label\":\"Push Devices\" },"+
            "{ \"url\":\"/pushsenders\" , \"label\":\"Push Senders\" },"+
            "{ \"url\":\"/pushprefs\" , \"label\":\"Push Preferences\" },"+   
            "{ \"divider\":\"true\"},"+                        
          <%--  "{ \"url\":\"/preferences\" , \"label\":\"Preferences\" }"+ --%>
            "]}";
        $scope.menuItems = eval ("(" + menuItems + ")");
    }

	$scope.clearSearch = function (){
		$scope.searchKeyword = "";
		$scope.onKeywordChange();
	}

	$scope.onKeywordChange = function(){		
		if($scope.searchKeyword.length > 2){
			console.log($scope.searchKeyword);
			$scope.search($scope.searchKeyword);
			$scope.showSearchResults = true;
		}else{
			$scope.showSearchResults = false;
		}
	}

	$scope.search = function(keyword){
		var url = "<c:out value="${pageContext.request.contextPath}"/>/services/device/keyword/" + keyword;
		$http({
		    method: 'GET',
		    url: url,
		}).success(function (data, status, headers, config) {
		    // TODO
			console.log("SUCCESS" );
			console.log(data.total);
			console.log(data.devices);
			$scope.searchResults = data.devices;
		}).error(function (data, status, headers, config) {
		    // TODO
			console.log("ERROR" + status);
		});
	}



	$scope.removeDevice = function(){
		console.log("Remove Device " + $scope.detailDevId);
		var data = new Object();
		data.deviceId = $scope.detailDevId;
		var url = "<c:out value="${pageContext.request.contextPath}"/>/services/device/remove";
		$http({
		    method: 'POST',
		    url: url,
		    data: data,
		    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
		}).success(function (data, status, headers, config) {
		    // TODO
			console.log("SUCCESS" );
			$scope.searchDevicesPage();
		}).error(function (data, status, headers, config) {
		    // TODO
			console.log("ERROR" + status);
		});
		
	}

	$scope.sendPush = function(){
		console.log("Send Push " + $scope.detailDevId);
		window.location = "${pageContext.request.contextPath}/push/send?id=" + $scope.detailId;
	}


    $scope.loadCounts = function() {
		console.log("loadCounts");
        $http({
            method: 'GET',
            url: '<c:out value="${pageContext.request.contextPath}"/>/services/device/counts'
        }).success(function(data, status, headers, config) {
            if( status != 200 ) {
                $scope.errors = '{"error":[{"id":'+status+',"name":"Error"}]}';
            }
            $scope.deviceCounts = data;
        }).error(function(data, status, headers, config) {
            $scope.errors = '{"error":[{"id":'+status+',"name":"Error"}]}';
        });
    }

	$scope.countDevices = function(){
	
	}

	$scope.deleteDevice = function(crit){
		var count = 0;
		var countdata = new Object();
		countdata.type = crit;
		countdata.username = $scope.inputUsername;
		var url = "<c:out value="${pageContext.request.contextPath}"/>/services/device/count?data=" + JSON.stringify(countdata);
		$http({
		    method: 'GET',
		    url: url,
		}).success(function (data, status, headers, config) {
		    // TODO
			console.log("SUCCESS" );
			if(data.status = "OK"){
				count = data.count;
				
				var message = "<spring:message code="push.device.delete.confirmation"/>" + " Total: " + count;
				bootbox.dialog({
				  message: message,
				  title: "<spring:message code="push.device.delete.confirmation.title"/>",
				  buttons: {
				    success: {
				      label: "<spring:message code="push.device.cancel"/>",
				      className: "btn-default",
				      callback: function() {
				        console.log("Cancel");
				      }
				    },
				    danger: {
				      label: "<spring:message code="push.device.delete"/>",
				      className: "btn-kme",
				      callback: function() {
						var deletedata = new Object();
						deletedata.type = crit;
						deletedata.username = $scope.inputUsername;
						var url = "<c:out value="${pageContext.request.contextPath}"/>/services/device/delete?data=" + JSON.stringify(deletedata);
						$http({
						    method: 'GET',
						    url: url,
						}).success(function (data, status, headers, config) {
						    // TODO
							console.log("SUCCESS" );
							if(data.status = "OK"){
								$scope.showDeleteResults();
							}
						}).error(function (data, status, headers, config) {
						    // TODO
							console.log("ERROR" + status);
						});
		
				        console.log("Delete: " + data);
				      }
				    }
				  }
				});
								
			}
		}).error(function (data, status, headers, config) {
		    // TODO
			console.log("ERROR" + status);
		});
	

		
		
		console.log(crit);		
	}
	
	$scope.showDeleteResults = function(){
		var message = "<spring:message code="push.devices.purge.deleted"/>";

		bootbox.dialog({
		  message: message,
		  title: "Delete Results",
		  buttons: {
		    success: {
		      label: "Ok",
		      className: "btn-kme",
		      callback: function() {

		      }
		    }
		  }
		});
	}
	
	$scope.showPurgePage = function(){
		$scope.showMainDevicePage = false;
		$scope.showSearchDevicesPage = false;
		$scope.showDeviceDetailPage = false;	
		$scope.showPurgeDevicesPage = true;	
	}
	
	$scope.showSearchPage = function(){
		$scope.showMainDevicePage = false;
		$scope.showSearchDevicesPage = true;
		$scope.showDeviceDetailPage = false;	
		$scope.showPurgeDevicesPage = false;	
	}

	$scope.showMainPage = function(){
		$scope.showMainDevicePage = true;
		$scope.showSearchDevicesPage = false;
		$scope.showDeviceDetailPage = false;	
		$scope.showPurgeDevicesPage = false;	
	}
	
	$scope.cancelDelete = function(){
		$scope.showMainPage();
	}
	
	$scope.detailDevicePage = function(obj){
		$scope.detailDeviceName = obj.deviceName;
		$scope.detailUsername = obj.username;
		$scope.detailDeviceType = obj.type;
		$scope.detailDevId = obj.deviceId;
		$scope.detailRegId = obj.regId;
		$scope.detailId = obj.Id;
		var rd = new Date(obj.registrationDate);
		rd = rd.getTime();
		$scope.detailRegDate = obj.registrationTimeStamp;

	
		$scope.showingThisDevice = false;
		$scope.showMainDevicePage = false;
		$scope.showSearchDevicesPage = false;
		$scope.showDeviceDetailPage = true;
	}

	$scope.searchDevicesPage = function(){
		console.log("Search Devices Page");
		$scope.search($scope.searchKeyword);
		$scope.showSearchPage();
	}


    $scope.kmeNavLeft = function() {
        if($scope.showSearchDevicesPage){
	        $scope.showMainPage();
        }else if($scope.showDeviceDetailPage){
	        $scope.searchDevicesPage();
        }else if($scope.showSearchDevicesPage){
	        $scope.showMainPage();
        }else if($scope.showPurgeDevicesPage){
	        $scope.showMainPage();
        }else if($scope.showMainDevicePage){
	        window.history.back();
        }
    }
});

