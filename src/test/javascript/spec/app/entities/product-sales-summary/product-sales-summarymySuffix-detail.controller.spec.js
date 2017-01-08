'use strict';

describe('Controller Tests', function() {

    describe('ProductSalesSummary Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockProductSalesSummary, MockSalesTransaction, MockProduct;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockProductSalesSummary = jasmine.createSpy('MockProductSalesSummary');
            MockSalesTransaction = jasmine.createSpy('MockSalesTransaction');
            MockProduct = jasmine.createSpy('MockProduct');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ProductSalesSummary': MockProductSalesSummary,
                'SalesTransaction': MockSalesTransaction,
                'Product': MockProduct
            };
            createController = function() {
                $injector.get('$controller')("ProductSalesSummaryMySuffixDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'reportApp:productSalesSummaryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
