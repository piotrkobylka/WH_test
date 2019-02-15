package apiincucumber;

import apiincucumber.api.ApiMethods;
import apiincucumber.dto.Game;
import apiincucumber.dto.User;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Stepdefs {

    ApiMethods apiMethods = new ApiMethods();

    List<User> users = new LinkedList<>();
    List<Game> games = new LinkedList<>();
    List<User> usersToConvert = new LinkedList<>();
    List<Integer> ids = new LinkedList<>();
    List<String> stringResults = new LinkedList<>();
    String gameType;

    @When("^clean all shares$")
    public void clean_all_shares() {
        users.clear();
        games.clear();
        usersToConvert.clear();
        ids.clear();
        stringResults.clear();
        gameType = null;
    }

    @When("^get Users data from database$")
    public void get_Users_data_from_database() {
        users = apiMethods.getUsers();
    }

    @Given("^get Games data from database$")
    public void get_Games_data_from_database() {
        games = apiMethods.getGames();
    }

    @When("^update converted Users to database$")
    public void update_converted_Users_to_database() {
        for (User user : usersToConvert) {
            apiMethods.updateUsers(user.getId(), user);
        }
    }

    @Then("^add user to database$")
    public void add_user_to_database() {
        for (User user : usersToConvert) {
            apiMethods.postUser(user);
        }
    }

    @When("^create list of user ids who has highest balance$")
    public void create_list_of_user_ids_who_has_highest_balance() {
        ids.clear();
        ids = apiMethods.getIDsOfHighestRollers(users);
    }

    @When("^verify only users with highest balance has marked high-roller to true$")
    public void verify_only_users_with_highest_balance_has_marked_high_roller_to_true() {
        for (int id : ids) {
            assertTrue("User with Id: " + id + " has not marked Highest Roller as true", users.get(id - 1).getHighRoller());
        }
    }

    @When("^verify other users has marked high-roller to false$")
    public void verify_other_users_has_marked_high_roller_to_false() {
        for (User userItem : users) {
            if (!ids.contains(userItem.getId())) {
                assertFalse("User with Id: " + userItem.getId() + " has marked Highest Roler as true", users.get(userItem.getId() - 1).getHighRoller());
            }
        }
    }

    @When("^create list of user ids who has not \"(.*?)\" currency$")
    public void create_list_of_user_ids_who_has_not_GBP_currency(String currency) {
        ids.clear();
        ids = apiMethods.getIdsOfNotGBPcurrency(users, currency);
    }

    @When("^convert all users currencies and balances to \"(.*?)\"$")
    public void convert_all_users_currencies_and_balances_to_GBP(String currency) {
        usersToConvert.clear();
        for (int id : ids) {
            User user = apiMethods.convertUserCurrencyAndBalance(apiMethods.getUserById(users, id), currency);
            usersToConvert.add(user);
        }
    }

    @When("^check all users has \"(.*?)\" currency$")
    public void check_all_users_has_GBP_currency(String currency) {
        ids.clear();
        ids = apiMethods.getIdsOfNotGBPcurrency(users, currency);
        assertTrue("There are still users with not converted currency", ids.isEmpty());
    }

    @When("^update balance about (\\d+) for user \"([^\"]*)\"$")
    public void update_balance_about_for_user(Double balance, String userName) {
        usersToConvert.clear();
        User user = apiMethods.updateBalanceForUser(users, userName, balance);
        usersToConvert.add(user);
    }

    @Then("^verify user \"([^\"]*)\" balance is (\\d+)$")
    public void verify_user_balance_is(String userName, double balance) throws Exception {
        double currentBalance = apiMethods.getBalanceForUser(users, userName).doubleValue();
        assertTrue("Value for user is: " + currentBalance + " but should be: " + balance, balance == currentBalance);
    }

    @When("^create new user with name \"([^\"]*)\", balance (\\d+), currency \"([^\"]*)\" and likes \"([^\"]*)\"$")
    public void create_new_user_with_name_balance_currency_and_likes(String userName, double balance, String currency, String likes) {
        usersToConvert.clear();
        User user = apiMethods.createNewUser(users, userName, balance, currency, likes);
        usersToConvert.add(user);
    }

    @When("^sort games from most popular$")
    public void sort_games_from_most_popular() {
        apiMethods.sortGamesFromMostPopular(games);
    }

    @When("^print sorted games$")
    public void print_sorted_games() {
        System.out.println(games);
    }

    @When("^get type for game \"([^\"]*)\"$")
    public void get_type_for_game(String gameName) {
        gameType = null;
        gameType = apiMethods.getTypeForGame(games, gameName);
    }

    @When("^create list of users who likes game$")
    public void create_list_of_users_who_likes_game() {
        usersToConvert = apiMethods.getUsersByLikes(users, gameType);
    }

    @When("^print created list$")
    public void print_created_list() {
        System.out.println(usersToConvert);
    }

    @When("^calculate possible Spins for all$")
    public void calculate_possible_Spins_for_all() {
        stringResults.clear();
        for (User user : users) {
            for (Game game : games) {
                String result = apiMethods.calculateSpinPossibilitiesForUser(user, game);
                stringResults.add(result);
            }
        }
    }

    @When("^print possible Spins for all users$")
    public void print_possible_Spins_for_all_users() {
        for (String result : stringResults) {
            System.out.println(result);
        }
    }
}