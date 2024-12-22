package com.tarotapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TarotInterface {
    private JFrame frame;
    private JPanel cardsPanel;
    private JButton drawButton;
    private JLabel meaningLabel;

    public TarotInterface() {
        // Создание основного окна

        frame = new JFrame("Дневной расклад Таро");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Инициализация компонентов
        initComponents();

        // Устанавливаем видимость окна
        frame.setVisible(true);
    }

    private void initComponents() {

        JPanel gridPanel = new JPanel(new GridLayout(3, 1));

        // Панель для отображения карт
        cardsPanel = new JPanel();
        cardsPanel.setLayout(new FlowLayout());

        // Начальное состояние, один зеленый прямоугольник
        JButton initialCard = createCardButton(" ");
        initialCard.setBackground(Color.GREEN);
        initialCard.addActionListener(e -> showFourCards());
        cardsPanel.add(initialCard);

        gridPanel.add(cardsPanel);

        // Кнопка для начала расклада
        drawButton = new JButton("Начать расклад");
        drawButton.addActionListener(e -> showFourCards());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(drawButton);
        gridPanel.add(buttonsPanel);

        // Метка для толкования карты
        meaningLabel = new JLabel("Толкование карты", SwingConstants.CENTER);
        gridPanel.add(meaningLabel);

        frame.add(gridPanel, BorderLayout.CENTER);
    }

    private JButton createCardButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 150));
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }

    private void showFourCards() {
        // Удаляем все компоненты из панели для карт и кнопки
        cardsPanel.removeAll();

        // Создаем 4 зеленых прямоугольника
        for (int i = 0; i < 4; i++) {
            JButton card = createCardButton("Card " + (i + 1));
            card.setBackground(Color.GREEN);
            card.addActionListener(new CardActionListener(i));
            cardsPanel.add(card);
        }

        // Удаляем кнопку "Начать расклад"
        drawButton.setVisible(false);

        // Обновляем интерфейс
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private void showCardMeaning(int cardIndex) {
        // Пример для демонстрации
        String[] cardNames = {"Шут", "Маг", "Верховная жрица", "Императрица"};
        String[] cardMeanings = {
                "New beginnings, optimism, trust in life",
                "Action, the power to manifest",
                "Inaction, going within, the subconscious",
                "Fertility, femininity, beauty, nature, abundance"
        };

        meaningLabel.setText("Карта: " + cardNames[cardIndex] + " - Толкование: " + cardMeanings[cardIndex]);
    }

    private class CardActionListener implements ActionListener {
        private final int cardIndex;

        public CardActionListener(int cardIndex) {
            this.cardIndex = cardIndex;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            source.setBackground(Color.RED);
            showCardMeaning(cardIndex);
        }
    }

    public static void main(String[] args) {
        // Запуск приложения
        SwingUtilities.invokeLater(TarotInterface::new);
    }
}
