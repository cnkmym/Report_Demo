(function() {
    'use strict';

    angular
        .module('reportApp')
        .controller('ProductMySuffixDetailController', ProductMySuffixDetailController);

    ProductMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Product'];

    function ProductMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, Product) {
        var vm = this;

        vm.product = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('reportApp:productUpdate', function(event, result) {
            vm.product = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
