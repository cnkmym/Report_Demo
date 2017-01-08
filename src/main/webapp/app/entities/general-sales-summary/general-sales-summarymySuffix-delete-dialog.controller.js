(function() {
    'use strict';

    angular
        .module('reportApp')
        .controller('GeneralSalesSummaryMySuffixDeleteController',GeneralSalesSummaryMySuffixDeleteController);

    GeneralSalesSummaryMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'GeneralSalesSummary'];

    function GeneralSalesSummaryMySuffixDeleteController($uibModalInstance, entity, GeneralSalesSummary) {
        var vm = this;

        vm.generalSalesSummary = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            GeneralSalesSummary.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
