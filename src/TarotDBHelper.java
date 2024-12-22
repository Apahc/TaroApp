package com.tarotapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.tarotapp.TarotCard;

public class TarotDBHelper {

    private static final String URL = "jdbc:postgresql://localhost:5432/TarotDB";
    private static final String USER = "postgres";
    private static final String PASSWORD = "12345"; // Убедитесь, что пароль верный

    public List<TarotCard> getRandomCards(int numberOfCards) {
        List<TarotCard> cards = new ArrayList<>();
        String query = "SELECT name, upright_meaning, image_url FROM public.tarot_cards ORDER BY RANDOM() LIMIT " + numberOfCards;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String meaning = resultSet.getString("upright_meaning");
                String imageUrl = resultSet.getString("image_url");

                // Печать для отладки
                System.out.println("Card: " + name + ", Image URL: " + imageUrl);

                cards.add(new TarotCard(name, meaning, imageUrl));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }
}