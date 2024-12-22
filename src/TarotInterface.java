package com.tarotapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import com.tarotapp.TarotCard;
import com.tarotapp.TarotDBHelper;

public class TarotInterface {
    private JFrame frame;
    private JPanel cardsPanel;
    private JButton drawButton;
    private JButton resetButton;
    private JLabel meaningLabel;
    private List<JButton> cardButtons;

    public TarotInterface() {
        frame = new JFrame("Дневной расклад Таро");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);

        initComponents();
        frame.setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        mainPanel.add(cardsPanel);

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        drawButton = new JButton("Начать расклад");
        drawButton.addActionListener(e -> showFourCards());
        buttonsPanel.add(drawButton);

        resetButton = new JButton("Начать заново");
        resetButton.addActionListener(e -> resetGame());
        resetButton.setVisible(false);
        buttonsPanel.add(resetButton);

        mainPanel.add(buttonsPanel);

        // Используем ImagePanel для отображения фонового изображения
        JPanel meaningPanel = new ImagePanel("src/images/frame.png");
        meaningPanel.setLayout(new BorderLayout());

        meaningLabel = new JLabel("", SwingConstants.CENTER);
        meaningLabel.setVerticalAlignment(SwingConstants.TOP);
        meaningLabel.setBorder(BorderFactory.createEmptyBorder(100, 120, 100, 120)); // отступы 50 пикселей
        meaningPanel.add(meaningLabel, BorderLayout.CENTER);
        meaningPanel.setPreferredSize(new Dimension(600, 450));

        mainPanel.add(meaningPanel);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        frame.add(scrollPane);
        resetGame();
    }

    // Класс ImagePanel для отображения фонового изображения
    private static class ImagePanel extends JPanel {
        private Image backgroundImage;

        public ImagePanel(String path) {
            try {
                backgroundImage = new ImageIcon(path).getImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    private JButton createCardButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 200));
        button.setOpaque(true);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
        return button;
    }

    private void showFourCards() {
        TarotDBHelper dbHelper = new TarotDBHelper();
        List<TarotCard> randomCards = dbHelper.getRandomCards(4);

        cardsPanel.removeAll();
        ImageIcon initialIcon = createScaledIcon("src/images/back.png", 150, 200);

        cardButtons = new ArrayList<>();
        for (int i = 0; i < randomCards.size(); i++) {
            JButton cardButton = createCardButton("");
            cardButton.setIcon(initialIcon);
            cardButton.addActionListener(new CardActionListener(randomCards.get(i)));
            cardsPanel.add(cardButton);
            cardButtons.add(cardButton);
        }

        drawButton.setVisible(false);
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private ImageIcon createScaledIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }

    private void showCardMeaning(TarotCard card) {
        // Разбиваем прогноз на строки
        String[] meaningLines = card.getMeaning().split("\n");
        StringBuilder formattedMeaning = new StringBuilder("<html><div style='text-align: center;'>");

        // Устанавливаем шрифт для текста
        Font boldFont = new Font("Serif", Font.BOLD, 14);

        // Добавляем заголовок карты
        formattedMeaning.append("<b>Карта: ").append(card.getName()).append("</b><br><br>");

        // Проверяем и выделяем заголовки прогнозов
        for (String line : meaningLines) {
            if (line.startsWith("Общий прогноз на сегодня") ||
                    line.startsWith("Любовь и отношения") ||
                    line.startsWith("Финансы") ||
                    line.startsWith("Здоровье")) {
                formattedMeaning.append("<b>").append(line).append("</b><br>");
            } else {
                formattedMeaning.append(line).append("<br>");
            }
        }

        formattedMeaning.append("</div></html>");

        // Применяем форматированный текст
        meaningLabel.setText(formattedMeaning.toString());

        // Применяем жирный шрифт
        meaningLabel.setFont(boldFont);

        resetButton.setVisible(true);

        // Обновляем размеры окна приложения
        Dimension textSize = meaningLabel.getPreferredSize();
        int newHeight = textSize.height + 100; // Высота рамки с учетом минимального размера и отступов
        frame.setSize(new Dimension(frame.getWidth(), newHeight + 500)); // 500 для верхних компонентов
        frame.revalidate();
        frame.repaint();
    }

    private void resetGame() {
        cardsPanel.removeAll();

        JButton initialCard = createCardButton(" ");
        ImageIcon initialIcon = createScaledIcon("src/images/back.png", 150, 200);
        initialCard.setIcon(initialIcon);
        initialCard.addActionListener(e -> showFourCards());
        cardsPanel.add(initialCard);

        resetButton.setVisible(false);
        drawButton.setVisible(true);

        cardsPanel.revalidate();
        cardsPanel.repaint();

        // Очищаем текст в рамке
        meaningLabel.setText("");

        // Устанавливаем первоначальные размеры окна
        frame.setSize(new Dimension(800, 800)); // вернемся к исходному размеру при сбросе
        frame.revalidate();
        frame.repaint();
    }

    private class CardActionListener implements ActionListener {
        private final TarotCard card;

        public CardActionListener(TarotCard card) {
            this.card = card;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            try {
                ImageIcon revealedIcon = new ImageIcon(new java.net.URL(card.getImageUrl()));
                Image img = revealedIcon.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH);
                source.setIcon(new ImageIcon(img));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            showCardMeaning(card);

            for (JButton button : cardButtons) {
                if (button != source) {
                    button.setEnabled(false);
                    button.setOpaque(true);
                    button.setBackground(new Color(0, 0, 0, 64)); // затемняем остальные карты
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TarotInterface::new);
    }
}
