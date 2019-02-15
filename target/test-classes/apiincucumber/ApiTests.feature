Feature: Tests for my test api
  Created to test api for this new feature

  Background:
    Given clean all shares


    #Test case 1
  Scenario: Get Highest-Roller
    Given get Users data from database
    When create list of user ids who has highest balance
    Then verify only users with highest balance has marked high-roller to true
    And verify other users has marked high-roller to false

    #Test case 2
  Scenario: Convert balances and currencies to GBP
    Given get Users data from database
    When create list of user ids who has not "GBP" currency
    Then convert all users currencies and balances to "GBP"
    And update converted Users to database
    When get Users data from database
    Then check all users has "GBP" currency

    #Test case 3
  Scenario: Update balance for user
    Given get Users data from database
    When update balance about 20000 for user "Brian"
    Then update converted Users to database
    When get Users data from database
    Then verify user "Brian" balance is 20008

    #Test case 4
  Scenario: Create and add new user
    Given get Users data from database
    When create new user with name "James", balance 20, currency "GBP" and likes "Bingo"
    Then add user to database

      #Test cse 5
  Scenario: List of most popular games in week
    Given get Games data from database
    When sort games from most popular
    Then print sorted games

    #Test case 6
  Scenario: Target customers for game
    Given get Users data from database
    And get Games data from database
    When get type for game "Starburst"
    Then create list of users who likes game
    And print created list

    #Test case 7
  Scenario:  calculate spins possibilities
    Given get Users data from database
    When get Games data from database
    Then calculate possible Spins for all
    And print possible Spins for all users