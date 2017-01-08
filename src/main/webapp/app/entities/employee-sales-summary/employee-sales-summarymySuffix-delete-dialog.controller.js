(function() {
    'use strict';

    angular
        .module('reportApp')
        .controller('EmployeeSalesSummaryMySuffixDeleteController',EmployeeSalesSummaryMySuffixDeleteController);

    EmployeeSalesSummaryMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'EmployeeSalesSummary'];

    function EmployeeSalesSummaryMySuffixDeleteController($uibModalInstance, entity, EmployeeSalesSummary) {
        var vm = this;

        vm.employeeSalesSummary = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EmployeeSalesSummary.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
