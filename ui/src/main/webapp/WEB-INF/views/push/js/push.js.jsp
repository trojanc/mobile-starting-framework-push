<%--

    The MIT License
    Copyright (c) 2011 Kuali Mobility Team

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
<%@ page language="java" contentType="application/javascript; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:message code="push.title" 	var="msgCat_Title"/>




var push = angular.module("pushApp", ['ngRoute', 'ngSanitize','ui.bootstrap']);

push.controller("PushController", function($scope,$http,$window, $templateCache,$location,$sce,$log) {

    $scope.init = function() {
        $scope.labToolTitle = "<c:out value="${msgCat_Title}"/>";
        $scope.pageTitle = $scope.labToolTitle;


        var menuItems = "{\"menus\": ["+
            "{ \"url\":\"/push\" , \"label\":\"Push Notifications\" },"+ 
            "{ \"url\":\"/push/history\" , \"label\":\"Push History\" },"+
            "{ \"url\":\"/devices\" , \"label\":\"Push Devices\" },"+
            "{ \"url\":\"/pushsenders\" , \"label\":\"Push Senders\" },"+
            "{ \"url\":\"/pushprefs\" , \"label\":\"Push Preferences\" },"+            
            <%--"{ \"url\":\"/preferences\" , \"label\":\"Preferences\" }"+--%>
            "]}";
        $scope.menuItems = eval ("(" + menuItems + ")");
    }

    $scope.kmeNavLeft = function() {
		if($scope.showPushHistoryPage){
			$scope.showMainPage();
		}else{
	        window.history.back();
		}
    }

	$scope.clearPush = function(){
		$scope.inputTitle = "";
		$scope.inputMessage = "";
		$scope.pushselect = "nil";
		$scope.recipientselect = "nil";
	}

	$scope.sendPush = function (){
		var data = new Object();
		data.title = $scope.inputTitle;
		data.message = $scope.inputMessage;
		data.sender = ($scope.sender) ? $scope.sender : "";
		data.url = ($scope.inputUrl) ? $scope.inputUrl : "";
		data.emergency = ($scope.inputEmergency) ? true : false;
		data.recipients = ($scope.recipientselect) ? $scope.recipientselect : "" ;
		data.senderKey = "${senderKey}";
		var devId = $scope.deviceId;
		data.devices = new Array();
		if(devId){
			data.devices[0] = devId;
		}else{
			data.devices[0] = "";
		}

		var url = "<c:out value="${pageContext.request.contextPath}"/>/services/push/submit";		
		$http({
		    method: 'POST',
		    url: url,
		    data: data,
		    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
		}).success(function (data, status, headers, config) {
		    // TODO
			console.log("SUCCESS" );
			$scope.showHistoryPage();
		}).error(function (data, status, headers, config) {
		    // TODO
			console.log("ERROR" + status);
		});
	

	}
	
	$scope.onPushSelectChange = function(){
		console.log($scope.pushselect);
		console.log($("#pushselect option:selected").text());
		$scope.inputTitle = $("#pushselect option:selected").text();
		$scope.inputMessage = $scope.pushselect;

	}
	
	$scope.onRecipientSelectChange = function(){
		console.log($scope.recipientselect);
	}

	$scope.showMainPage = function(){
		$scope.showMainPushPage = true;
		$scope.showPushHistoryPage = false;	
	}
	
	
	
	$scope.showHistoryPage = function(){
		window.location = "<c:out value="${pageContext.request.contextPath}"/>/push/history";
	}
});