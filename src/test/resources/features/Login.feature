@login @account
Feature: User Login
  As a registered user
  I want to login to my account
  So that I can access my account features on website

  Background:
    Given I am on the AskOmDch Account page on login

  @smoke @positive
  Scenario Outline: Login with valid credentials
    When I login with username "<username>" and password "<password>"
    Then I should be logged in successfully
    And I should see "<welcomeText>" and "<logoutText>"

    Examples:
      | username | password | welcomeText    | logoutText |
      | TestUser | Test@123 | Hello testuser | Log out    |

  @regression @negative
  Scenario Outline: Login with multiple invalid credentials
    When I attempt to login with username "<username>" and password "<password>"
    Then I should see the login error message "<errormessage>"

    Examples:
      | username    | password      | errormessage                                                                                                                        |
      | invalidUser | Test@123      | Error: The username invalidUser is not registered on this site. If you are unsure of your username, try your email address instead. |
      | TestUser    | wrongPassword | Error: The password you entered for the username TestUser is incorrect. Lost your password?                                         |
      |             | Test@123      | Error: Username is required.                                                                                                        |
      | TestUser    |               | Error: The password field is empty.                                                                                                 |
      |             |               | Error: Username is required.                                                                                                        |