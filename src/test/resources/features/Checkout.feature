@checkout @cart
Feature: Checkout
  As a customer
  I want to complete my purchase
  So that I can place an order successfully

  Background:
    Given I have a product in the cart
    And I am on the checkout page

  @smoke @positive
  Scenario: Place order on checkout page
    When I complete checkout with valid billing details:
      | field          | value            |
      | First name     | Tresor           |
      | Last name      | Xavier           |
      | Company        | Gasabo           |
      | Street         | Kigali           |
      | City           | Kigali           |
      | Postcode / ZIP | 90210            |
      | Phone          | +250780000000    |
      | Email          | tresor@gmail.com |
    Then the order should be placed successfully

