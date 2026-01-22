@registration @account
Feature: User Registration
  As a new user
  I want to create an account
  So that I can access my account and shop products

  Background:
    Given I am on the AskOmDch Account page

  @smoke @positive
  Scenario: Successful user registration
    When I register with valid credentials
      | username | password | email            |
      | TestUser | Test@123 | tester@gmail.com |
    Then my account should be created successfully
    And I should see a welcome message

  @regression @negative
  Scenario Outline: Registration with an already registered email
    When I register with existing email "<email>"
    Then I should see the error message "<errorMessage>"

    Examples:
      | email            | errorMessage                                                                    |
      | tester@gmail.com | Error: An account is already registered with your email address. Please log in. |

  @regression @negative
  Scenario Outline: Registration with a missing required field
    When I submit the registration form without "<field>"
    Then I should see the error message "<errorMessage>"

    Examples:
      | field    | errorMessage                                  |
      | email    | Error: Please provide a valid email address.  |
      | username | Error: Please enter a valid account username. |
      | password | Error: Please enter an account password.      |

  @regression @negative @validation
  Scenario Outline: Registration with invalid email format
    When I register with invalid email format "<email>"
    Then I should remain on the account page

    Examples:
      | email         |
      | invalid-email |
      | user@         |
      | user.com      |
