(function() {
    'use strict';

    angular
        .module('reportApp')
        .controller('SalesTransactionMySuffixDeleteController',SalesTransactionMySuffixDeleteController);

    SalesTransactionMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'SalesTransaction'];

    function SalesTransactionMySuffixDeleteController($uibModalInstance, entity, SalesTransaction) {
        var vm = this;

        vm.salesTransaction = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SalesTransaction.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
