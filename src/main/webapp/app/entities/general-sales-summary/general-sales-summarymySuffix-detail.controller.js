(function() {
    'use strict';

    angular
        .module('reportApp')
        .controller('GeneralSalesSummaryMySuffixDetailController', GeneralSalesSummaryMySuffixDetailController);

    GeneralSalesSummaryMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'GeneralSalesSummary', 'SalesTransaction'];

    function GeneralSalesSummaryMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, GeneralSalesSummary, SalesTransaction) {
        var vm = this;

        vm.generalSalesSummary = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('reportApp:generalSalesSummaryUpdate', function(event, result) {
            vm.generalSalesSummary = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
