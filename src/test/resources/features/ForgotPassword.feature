@login @account @forgotPassword
Feature: Forgot Password
  As a user who forgot their password
  I want to reset my password
  So that I can regain access to my account

  Background:
    Given I am on the AskOmDch Account page on login

  @smoke @positive
  Scenario Outline: Request password reset with valid username
    When I click on "Lost your password?" link
    Then I should be on the password reset page
    When I enter username or email "<usernameOrEmail>"
    And I click Reset password button
    Then I should see the success message "<successMessage>"

    Examples:
      | usernameOrEmail  | successMessage                      |
      | TestUser         | Password reset email has been sent. |
      | tester@gmail.com | Password reset email has been sent. |

  @regression @negative
  Scenario Outline: Request password reset with invalid data
    When I click on "Lost your password?" link
    Then I should be on the password reset page
    When I enter username or email "<usernameOrEmail>"
    And I click Reset password button
    Then I should see the reset error message "<errorMessage>"

    Examples:
      | usernameOrEmail | errorMessage                       |
      |                 | Enter a username or email address. |
      | invalidUser123  | Invalid username or email.         |