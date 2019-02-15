package apiincucumber.api;


import apiincucumber.dto.Game;
import apiincucumber.dto.User;
import io.restassured.http.Header;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class ApiMethods {

    public static final BigDecimal YEN_EXCHANGE = new BigDecimal(0.007);


    public List<User> getUsers() {
        return Arrays.asList(given().header(new Header("Accept", "application/json"))
                .header(new Header("Content-Type", "application/json"))
                .when()
                .get("http://localhost:3000/users")
                .then()
                .statusCode(200)
                .extract()
                .as(User[].class));
    }

    public List<Game> getGames() {
        return Arrays.asList(given().header(new Header("Accept", "application/json"))
                .header(new Header("Content-Type", "application/json"))
                .when()
                .get("http://localhost:3000/games")
                .then()
                .statusCode(200)
                .extract()
                .as(Game[].class));
    }

    public void updateUsers(int idToUpdate, User userToUpdate) {
        given().header(new Header("Accept", "application/json"))
                .header(new Header("Content-Type", "application/json"))
                .pathParam("idToUpdate", idToUpdate)
                .when()
                .body(userToUpdate)
                .put("http://localhost:3000/users/{idToUpdate}")
                .then()
                .statusCode(200);
    }

    public void postUser(User userToAdd) {
        given().header(new Header("Accept", "application/json"))
                .header(new Header("Content-Type", "application/json"))
                .when()
                .body(userToAdd)
                .post("http://localhost:3000/users/")
                .then()
                .statusCode(201);
    }

    public List<Integer> getIDsOfHighestRollers(List<User> usersList) {
        BigDecimal tempBalance = new BigDecimal(0);
        List<Integer> idList = new LinkedList<>();

        for (User userItem : usersList) {
            BigDecimal currentBalance = userItem.getBalance();
            if (currentBalance.compareTo(tempBalance) > 0) {
                idList.clear();
                idList.add(userItem.getId());
                tempBalance = currentBalance;
            } else if (currentBalance.compareTo(tempBalance) == 0) {
                idList.add(userItem.getId());
            }
        }
        return idList;
    }

    public List<Integer> getIdsOfNotGBPcurrency(List<User> usersList, String targetCurrency) {
        List<Integer> idList = new LinkedList<>();

        for (User userItem : usersList) {
            if (!targetCurrency.equals(userItem.getCurrency())) {
                idList.add(userItem.getId());
            }
        }
        return idList;
    }

    public User getUserById(List<User> usersList, int userId) {
        for (User userItem : usersList) {
            if (userItem.getId() == userId) {
                return userItem;
            }
        }
        return null;
    }

    public User convertUserCurrencyAndBalance(User user, String targetCurrency) {
        User newUser = User.builder()
                .id(user.getId())
                .name(user.getName())
                .balance(convertCurrency(user.getCurrency(), user.getBalance()))
                .currency(targetCurrency)
                .likes(user.getLikes())
                .highRoller(user.getHighRoller())
                .build();
        return newUser;
    }

    public User updateBalanceForUser(List<User> usersList, String userName, double balanceToAdd) {
        for (User userItem : usersList) {
            if (userName.equals(userItem.getName())) {
                User newUser = User.builder()
                        .id(userItem.getId())
                        .name(userItem.getName())
                        .balance(userItem.getBalance().add(new BigDecimal(balanceToAdd)))
                        .currency(userItem.getCurrency())
                        .likes(userItem.getLikes())
                        .highRoller(userItem.getHighRoller())
                        .build();

                return newUser;
            }
        }
        return null;
    }

    public BigDecimal getBalanceForUser(List<User> usersList, String userName) {
        for (User userItem : usersList) {
            if (userName.equals(userItem.getName())) {
                return userItem.getBalance();
            }
        }
        return new BigDecimal(0);
    }

    public User createNewUser(List<User> usersList, String userName, double balance, String currency, String likes) {
        return User.builder()
                .id(usersList.get(usersList.size() - 1).getId() + 1)
                .name(userName)
                .balance(new BigDecimal(balance))
                .currency(currency)
                .likes(likes)
                .highRoller(false)
                .build();
    }

    public void sortGamesFromMostPopular(List<Game> gamesList) {
        gamesList.sort(Comparator.comparing(Game::getStakesThisWeek).reversed());
    }

    public String getTypeForGame(List<Game> gamesList, String gameName) {
        for (Game game : gamesList) {
            if (gameName.equals(game.getName())) {
                return game.getType();
            }
        }
        return null;
    }

    public List<User> getUsersByLikes(List<User> userList, String likesPattern) {
        List<User> likesUsers = new LinkedList<>();
        likesPattern += " Games";
        for (User user : userList) {
            if (likesPattern.equals(user.getLikes())) {
                likesUsers.add(user);
            }
        }
        return likesUsers;
    }

    public String calculateSpinPossibilitiesForUser(User user, Game game) {
        BigDecimal balance = user.getBalance();
        BigDecimal[] stakes = game.getStake();

        for (BigDecimal stake : stakes) {
            BigDecimal possibleSpins = calculatePossibleSpins(balance, stake);
            return "User: " + user.getId() + " with balance: " + balance + " in game: " + game.getName() + " for stake: " + stake + " could spin: " + possibleSpins;
        }
        return null;
    }

    private BigDecimal calculatePossibleSpins(BigDecimal balance, BigDecimal stake) {
        return balance.divide(stake).setScale(0, BigDecimal.ROUND_DOWN);
    }

    private BigDecimal convertCurrency(String currency, BigDecimal balance) {
        if ("YEN".equals(currency)) {
            return balance.multiply(YEN_EXCHANGE).setScale(2, RoundingMode.CEILING);
        }
        return new BigDecimal(0);
    }
}
