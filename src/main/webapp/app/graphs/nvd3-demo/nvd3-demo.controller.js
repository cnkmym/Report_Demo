(function() {
    'use strict';

    angular
        .module('reportApp')
        .controller('Nvd3DemoController', Nvd3DemoController);

    Nvd3DemoController.$inject = ['$scope', '$state', 'Nvd3Demo', 'ParseLinks', 'AlertService'];

    function Nvd3DemoController ($scope, $state, Nvd3Demo, ParseLinks, AlertService) {
        var vm = this;

        loadAll();

        function defaultGraphOption (){
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
                    return d3.format(',.4f')(d);
                },
                transitionDuration: 500,
                xAxis: {
                    axisLabel: 'X Axis'
                },
                yAxis: {
                    axisLabel: 'Y Axis',
                    axisLabelDistance: 30
                }
              }
            };
          }

        function defaultGraphData(){
          return [{
              key: "Cumulative Return",
              values: [
                  { "label" : "A" , "value" : -29.765957771107 },
                  { "label" : "B" , "value" : 0 },
                  { "label" : "C" , "value" : 32.807804682612 },
                  { "label" : "D" , "value" : 196.45946739256 },
                  { "label" : "E" , "value" : 0.19434030906893 },
                  { "label" : "F" , "value" : -98.079782601442 },
                  { "label" : "G" , "value" : -13.925743130903 },
                  { "label" : "H" , "value" : -5.1387322875705 }
              ]
          }];
        }

        function loadAll () {
            /**
            Nvd3Demo.query({
                fromYear: 2016,
                fromMonth: 10,
                toYear:2017,
                toMonth:3
            }, onSuccess, onError);
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.productSalesSummaries = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
            */
            vm.graphOptions = defaultGraphOption();
            vm.graphData = defaultGraphData();
        }

    }
})();
