(function() {
    'use strict';

    angular
        .module('reportApp')
        .controller('EmployeeSalesSummaryGraphController', EmployeeSalesSummaryGraphController);

    EmployeeSalesSummaryGraphController.$inject = ['$scope', '$state', 'EmployeeSalesSummary', 'ParseLinks', 'AlertService'];

    function EmployeeSalesSummaryGraphController ($scope, $state, EmployeeSalesSummary, ParseLinks, AlertService) {
        var vm = this;
        vm.fromYear = 2016;
        vm.fromMonth = 10;
        vm.toYear = 2017;
        vm.toMonth = 3;
        vm.isEditMode = false;
        vm.isSummaryMode = true;

        vm.enterEditMode = function(){
            vm.isSummaryMode = true;
            vm.isEditMode = true;
        }

        function updatePeriod(){
          vm.isSummaryMode = true;
          vm.isEditMode = false;
          loadSummary();
        }

        vm.updatePeriod = updatePeriod;

        updatePeriod();

        function showSalesTransactionList(salesSummary){
          if (salesSummary !== null && salesSummary.id !== null){
            //update title
            vm.isSummaryMode = false;
            vm.detailYearMonth = salesSummary.label;
            vm.detailEmployeeName = salesSummary.employeeName;
            vm.detailEmployeeColor = salesSummary.color;
            //get data
            loadDetail(salesSummary.id);
            //
          }
        }

        function defaultDetailGraphOption (){
            return {
              chart: {
                type: 'discreteBarChart',
                height: 450,
                margin : {
                    top: 20,
                    right: 20,
                    bottom: 60,
                    left: 55
                },
                x: function(d){ return d.label; },
                y: function(d){ return d.value; },
                showValues: true,
                valueFormat: function(d){
                    return d3.format(',.2f')(d);
                },
                transitionDuration: 500,
                xAxis: {
                    axisLabel: 'Daily Result'
                },
                yAxis: {
                    axisLabel: 'Sales ($)',
                    axisLabelDistance: 30
                }
              }
            };
          }

          function defaultSummaryGraphOption (){
              return angular.merge({},defaultDetailGraphOption(),
                {
                chart: {
                  type:'multiBarChart',
                  xAxis: {
                      axisLabel: 'Date'
                  },
                  multibar: {
                    dispatch: {
                      elementClick: function(e) {
                        showSalesTransactionList(e.data);
                      }
                    }
                  }
                }
              });
            }

          function formatNumber(number){
            return ("0" + number).slice(-2);
          }

          vm.formatNumber = formatNumber;

          var colorMap = ["#d62728","#1f77b4","#51A351"];

          function fillEmptyMonthlySales(){
            var cache = {};
            var startY = vm.fromYear;
            var startM = vm.fromMonth;
            while ((startY<vm.toYear) || (startY == vm.toYear && startM <= vm.toMonth)){
              if (startM >= 13){
                startM = 1;
                startY ++;
                continue;
              }
              var key = startY + "/" + formatNumber(startM);
              cache[key] = {label:key, value:0};
              startM ++;
            }
            return cache;
          }

          function composeSummaryDisplayData(data){
            var ret = [];
            //compose message
            angular.forEach(data,function(item,index){
              var array = [];
              var key = item.employeeName;
              var employeeId = item.id;
              var color = colorMap[index];
              var value = fillEmptyMonthlySales();
              angular.forEach(item.value,function(item){
                var label = item.year + "/" + formatNumber(item.month);
                value[label].value = value[label].value + item.totalAmount;
                value[label].id = item.id;
                value[label].employeeName = key;
                value[label].color = color;
              });
              for (var i in value){
                array.push(value[i]);
              }
              ret.push({"key":key,"color":color, "employeeId":employeeId,"values":array});
            })
            //return
            return ret;
          }

          function loadSummary () {
              EmployeeSalesSummary.search({
                  fromYear: vm.fromYear,
                  fromMonth: vm.fromMonth,
                  toYear:vm.toYear,
                  toMonth:vm.toMonth
              }, onSuccess, onError);
              function onSuccess(data, headers) {
                  vm.graphData = composeSummaryDisplayData(data);
              }
              function onError(error) {
                  vm.graphData = [];
                  AlertService.error(error.data.message);
              }
              vm.graphOptions = defaultSummaryGraphOption();
              //vm.graphData = defaultGraphData();
          }

          function fillEmptyDailySales(year, month){
            var cache = {};
            if (month == '02'){
              for (var i = 1; i<=28; i++){
                cache[formatNumber(i)] = 0;
              }
            }else if (month == '01' ||month == '03'||month == '05'||month == '07'||month == '08'||month == '10'||month == '12'){
              for (var i = 1; i<=31; i++){
                cache[formatNumber(i)] = 0;
              }
            } else{
              for (var i = 1; i<=30; i++){
                cache[formatNumber(i)] = 0;
              }
            }
            return cache;
          }

          function composeDetailDisplayData(data){
            var year = vm.detailYearMonth.substr(0,4);
            var month = vm.detailYearMonth.substr(5,2);
            var cache = fillEmptyDailySales(year, month);
            //compose message
            angular.forEach(data,function(item){
              var label = item.transactionDate.substr(8,2);
              var value = item.transactionAmount;
              if (cache[label] === undefined){
                cache[label] = parseInt(value);
              }else{
                cache[label] = cache[label] + parseInt(value);
              }
            })
            var ret = [];
            for (var label in cache){
              ret.push({'label':label,'value':cache[label]});
            }
            //sort
            ret.sort(function(a,b){
              return a.label.localeCompare(b.label);
            });
            //return
            return ret;
          }

          function loadDetail (employeeSummaryId) {
              EmployeeSalesSummary.detail({
                  id: employeeSummaryId
              }, onSuccess, onError);
              function onSuccess(data, headers) {
                  vm.graphData = [{
                      key: "Cumulative Return",
                      values: composeDetailDisplayData(data)
                  }];
              }
              function onError(error) {
                  vm.graphData = [];
                  AlertService.error(error.data.message);
              }
              vm.graphOptions = defaultDetailGraphOption();
              //vm.graphData = defaultGraphData();
          }

    }
})();
