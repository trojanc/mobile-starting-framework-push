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

<spring:message code="push.senders" var="sendersToolTitle"/>
<spring:message code="push.senders.delete.sure" var="msgCat_SureDelete"/>

var senders = angular.module("sendersApp", ['ngRoute', 'ngSanitize','ui.bootstrap']);

senders.controller("SendersController", function($scope,$http,$window, $templateCache,$location,$sce,$log) {

    $scope.init = function() {
        $scope.labToolTitle = "<c:out value="${sendersToolTitle}"/>";
		$scope.showDeleteButton = false;
        $scope.pageTitle = $scope.labToolTitle;
        $scope.loadSenders();
		$scope.newKey();
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
		if($scope.showNewSenderPage){
			$scope.showMainPage();
		}else{
	        window.history.back();
		}
    }

	$scope.editSender = function(obj){
		console.log(obj);
		$scope.inputSenderKey = obj.senderKey;
		$scope.inputName = obj.name;
		$scope.inputShortName = obj.shortName;
		$scope.inputDescription = obj.description;
		$scope.inputUserBlockable = obj.hidden;
		$scope.inputId = obj.id;
		$scope.showDeleteButton = true;
		$scope.showEditPage();
	}

	$scope.newSender = function(){
		console.log("New Sender");
		$scope.inputSenderKey = "";
		$scope.inputName = "";
		$scope.inputShortName = "";
		$scope.inputDescription = "";
		$scope.inputId = "";
		$scope.newKey();
		$scope.showDeleteButton = false;
		$scope.showEditPage();
	}

	$scope.newKey = function(){
		console.log("New Key");
        $http({
            method: 'GET',
            url: '<c:out value="${pageContext.request.contextPath}"/>/services/sender/key'
        }).success(function(data, status, headers, config) {
            if( status != 200 ) {
                $scope.errors = '{"error":[{"id":'+status+',"name":"No Key Returned"}]}';
            }           
            $scope.inputSenderKey = data;
        }).error(function(data, status, headers, config) {
            $scope.errors = '{"error":[{"id":'+status+',"name":"Some Error Occurred"}]}';
        });		
	}

	$scope.cancelNewSender = function(){
		console.log("Cancel Sender");
        $scope.showMainPage();
	}

	$scope.delete = function(){
		console.log("Delete Sender");		
		$http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded";
		var data = new Object();
		data.id = $scope.inputId;
		var url = "<c:out value="${pageContext.request.contextPath}"/>/services/sender/delete";
		$http({
		    method: 'POST',
		    url: url,
		    data: data,
		    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
		}).success(function (data, status, headers, config) {
		    // TODO
			console.log("SUCCESS" );
			$scope.loadSenders();
			$scope.showMainPage();
		}).error(function (data, status, headers, config) {
		    // TODO
			console.log("ERROR" + status);
		});
        $scope.loadSenders();		
		$scope.showMainPage();
	}


	$scope.deleteSender = function(){
		if(confirm("<c:out value="${msgCat_SureDelete}"/>")){
			$scope.delete();
		}
	}
	
	$scope.saveSender = function(){
		$http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded";
		var data = new Object();
		data.name = $scope.inputName;
		data.shortName = $scope.inputShortName;
		data.description = $scope.inputDescription;
		data.senderKey = $scope.inputSenderKey;
		data.id = $scope.inputId;
		data.hidden = ($scope.inputUserBlockable) ? true : false;
				
		var url = "<c:out value="${pageContext.request.contextPath}"/>/services/sender/save";
		$http({
		    method: 'POST',
		    url: url,
		    data: data,
		    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
		}).success(function (data, status, headers, config) {
		    // TODO
			console.log("SUCCESS" );
			$scope.loadSenders();
			$scope.showMainPage();
		}).error(function (data, status, headers, config) {
		    // TODO
			console.log("ERROR" + status);
		});
	}

    $scope.loadSenders = function() {
		console.log("loadSenders");
        $http({
            method: 'GET',
            url: '<c:out value="${pageContext.request.contextPath}"/>/services/sender/all'
        }).success(function(data, status, headers, config) {
            if( status != 200 ) {
                $scope.errors = '{"error":[{"id":'+status+',"name":"'+$scope.labNoInfo+'"}]}';
            }
            for(var i = 0; i < data.senders.length; i++){
            	(data.senders[i].hidden == "false") ? data.senders[i].hidden = false : data.senders[i].hidden = true;
            }            
            $scope.senders = data.senders;
        }).error(function(data, status, headers, config) {
            $scope.errors = '{"error":[{"id":'+status+',"name":"'+$scope.labNoInfo+'"}]}';
        });
    }

	$scope.showMainPage = function(){
		$scope.showMainSenderPage = true;
		$scope.showNewSenderPage = false;	
	}
	
	$scope.showEditPage = function(){
		$scope.showMainSenderPage = false;
		$scope.showNewSenderPage = true;		
	}
});