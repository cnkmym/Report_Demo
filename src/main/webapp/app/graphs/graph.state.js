(function() {
    'use strict';

    angular
        .module('reportApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('graph', {
            abstract: true,
            parent: 'app'
        });
    }
})();
