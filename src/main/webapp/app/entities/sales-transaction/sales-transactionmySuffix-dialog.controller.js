(function() {
    'use strict';

    angular
        .module('reportApp')
        .controller('SalesTransactionMySuffixDialogController', SalesTransactionMySuffixDialogController);

    SalesTransactionMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SalesTransaction', 'Employee', 'Product'];

    function SalesTransactionMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SalesTransaction, Employee, Product) {
        var vm = this;

        vm.salesTransaction = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.employees = Employee.query();
        vm.products = Product.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.salesTransaction.id !== null) {
                SalesTransaction.update(vm.salesTransaction, onSaveSuccess, onSaveError);
            } else {
                SalesTransaction.save(vm.salesTransaction, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('reportApp:salesTransactionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.transactionDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
