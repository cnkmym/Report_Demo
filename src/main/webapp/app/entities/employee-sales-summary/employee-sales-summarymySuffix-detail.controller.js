(function() {
    'use strict';

    angular
        .module('reportApp')
        .controller('EmployeeSalesSummaryMySuffixDetailController', EmployeeSalesSummaryMySuffixDetailController);

    EmployeeSalesSummaryMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EmployeeSalesSummary', 'SalesTransaction', 'Employee'];

    function EmployeeSalesSummaryMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, EmployeeSalesSummary, SalesTransaction, Employee) {
        var vm = this;

        vm.employeeSalesSummary = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('reportApp:employeeSalesSummaryUpdate', function(event, result) {
            vm.employeeSalesSummary = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
