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
<%@ page language="java" contentType="application/javascript; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:message code="push.history" var="msgCat_Title"/>


var pushhistory = angular.module("pushHistoryApp", ['ngRoute', 'ngSanitize','ui.bootstrap']);

pushhistory.controller("PushHistoryController", function($scope,$http,$window, $templateCache,$location,$sce,$log) {

    $scope.init = function() {
        $scope.labToolTitle = "<c:out value="${msgCat_Title}"/>";
        $scope.pageTitle = $scope.labToolTitle;
		$scope.currentPage=1;
		$scope.totalNotifications = null;
		$scope.loadHistory();
		$scope.loadHistoryCount();
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
        window.history.back();
    }

	$scope.goTo = function(url){
		console.log("Going to the following url: " + url);
		window.location.href = url;
	}
	
	$scope.goToNextPage = function(){
		if($scope.currentPage <$scope.totalPages){
			$scope.currentPage= +$scope.currentPage+1;
			console.log("--goToNextPage--Showing page "+ $scope.currentPage);
			$scope.loadHistory();
		}
	}
	
	$scope.goToPrevPage = function(url){
		if($scope.currentPage>1)
		{
			$scope.currentPage= +$scope.currentPage-1;
			console.log("goToPrevPage---Showing page "+ $scope.currentPage);
			$scope.loadHistory();
		}
	}

	$scope.loadHistory = function(){
		console.log("Loading History...")
		var url = "<c:out value="${pageContext.request.contextPath}"/>/services/push/historyByPage/"+$scope.currentPage;
		$http({
		    method: 'GET',
		    url: url,
		}).success(function (data, status, headers, config) {
			$scope.notifications = data;
			$scope.recordsPerPage= Object.keys($scope.notifications).length;
			$scope.pageRecordStartIndex=$scope.currentPage*$scope.recordsPerPage - $scope.recordsPerPage + 1 ;
		    $scope.pageRecordEndIndex=$scope.currentPage*$scope.recordsPerPage;
			
		}).error(function (data, status, headers, config) {
		    // TODO
			console.log("ERROR" + status);
		});
	}
	
	$scope.loadHistoryCount = function(){
		console.log("Loading History...")
		var url = "<c:out value="${pageContext.request.contextPath}"/>/services/push/historyCount";
		$http({
		    method: 'GET',
		    url: url,
		}).success(function (data, status, headers, config) {
		    // TODO
			$scope.totalNotifications = data[0];
			$scope.totalPages=$scope.totalNotifications/$scope.recordsPerPage;
		}).error(function (data, status, headers, config) {
		    // TODO
			console.log("ERROR" + status);
		});
	}

});