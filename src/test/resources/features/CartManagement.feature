@cart @store
Feature: Add item to the cart
  As a customer
  I want to add items to the cart
  So that I can purchase them later

  Background:
    Given I am on the AskOmDch Store page

  @smoke @addToCart
  Rule: Add From Store Page

  @smoke @positive
  Scenario Outline: Add items to cart
    When I add <items> products to the cart
    Then I should be on the cart page
    And the cart count should be <count>

    Examples:
      | items | count |
      | 2     | 2     |

  @regression @viewCart
  Scenario: View cart after adding items
    When I added a product to the cart
    Then I should see item in the cart

  @regression @removeFromCart
  Scenario Outline: Remove item from the cart
    Given I have products in the cart
      | Anchor Bracelet   |
      | Basic Blue Jeans  |
      | Blue Denim Shorts |
    And the initial cart count should be <initialCount>
    When I remove the product "<productName>"
    Then the cart count should be <finalCount>
    And I should see the product removed confirmation for "<productName>"
    And I should see the empty cart message when cart is empty

    Examples:
      | initialCount | finalCount | productName      |
      | 3            | 2          | Anchor Bracelet  |
      | 3            | 2          | Basic Blue Jeans |
