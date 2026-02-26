@cart @store
Feature: Cart Management
  As a customer
  I want to manage items in my cart and apply coupons
  So that I can purchase them with discounts

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

  @coupon @regression
  Rule: Apply Coupon Codes

  @coupon @happy_path
  Scenario: Apply best coupon off25 for 25% discount
    Given I have "Basic Blue Jeans" and "Black Over-the-shoulder Handbag" in cart with subtotal $105.00
    When I apply the coupon code "off25"
    Then the coupon "off25" should be accepted
    And the cart discount should be $26.25
    And the shipping cost should be $5.00
    And the order total should be $89.66

  @coupon
  Scenario: Apply backup coupon off15 for 15% discount
    Given I have "Basic Blue Jeans" and "Black Over-the-shoulder Handbag" in cart with subtotal $105.00
    When I apply the coupon code "off15"
    Then the coupon "off15" should be accepted
    And the cart discount should be $15.75
    And the shipping cost should be $5.00
    And the order total should be $100.94

  @coupon @shipping
  Scenario: Apply freeship coupon to get free shipping
    Given I have "Basic Blue Jeans" and "Black Over-the-shoulder Handbag" in cart with subtotal $105.00
    When I apply the coupon code "freeship"
    Then the coupon "freeship" should be accepted
    And the cart discount should be $0.00
    And the shipping cost should be $5.00
    And the order total should be $117.88

  @coupon @negative
  Scenario: Applying an invalid coupon code shows an error and no discount
    Given I have "Basic Blue Jeans" and "Black Over-the-shoulder Handbag" in cart with subtotal $105.00
    When I apply the coupon code "invalid123"
    Then the coupon should be rejected
    And the cart discount should be $0.00
    And the shipping cost should be $5.00
    And the order total should be $117.88

  @coupon @priority
  Scenario: Off25 has higher priority than freeship when both are tried
    Given I have "Basic Blue Jeans" and "Black Over-the-shoulder Handbag" in cart with subtotal $105.00
    When I apply the coupon code "freeship"
    And I remove any applied coupon
    And I apply the coupon code "off25"
    Then the coupon "off25" should be accepted
    And the cart discount should be $26.25
    And the shipping cost should be $5.00
    And the order total should be $89.66
