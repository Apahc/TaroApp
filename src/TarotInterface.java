package com.tarotapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TarotInterface {
    private JFrame frame;
    private JPanel cardsPanel;
    private JButton drawButton;
    private JButton resetButton;
    private JLabel meaningLabel;
    private List<JButton> cardButtons;

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
        JPanel gridPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        // Панель для отображения карт
        cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.4;
        gridPanel.add(cardsPanel, gbc);

        ImageIcon initialIcon = createScaledIcon("src/images/back.png", 150, 200);
        JButton card = createCardButton("");
        card.setIcon(initialIcon);
        cardsPanel.add(card);

        // Панель кнопок
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        drawButton = new JButton("Начать расклад");
        drawButton.addActionListener(e -> showFourCards());
        buttonsPanel.add(drawButton);

        resetButton = new JButton("Начать заново");
        resetButton.addActionListener(e -> resetGame());
        resetButton.setVisible(false);
        buttonsPanel.add(resetButton);

        gbc.gridy = 1;
        gbc.weighty = 0;
        gridPanel.add(buttonsPanel, gbc);

        // Панель для отображения результата в рамке с картинкой и отступами
        JPanel meaningPanel = new ImagePanel("src/images/frame.png");
        meaningPanel.setLayout(new BorderLayout());
        meaningLabel = new JLabel("", SwingConstants.CENTER);
        meaningLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // отступы со всех сторон
        meaningPanel.add(meaningLabel, BorderLayout.CENTER);
        meaningPanel.setPreferredSize(new Dimension(600, 450));

        gbc.gridy = 2;
        gbc.weighty = 0.6;
        gbc.fill = GridBagConstraints.BOTH;
        gridPanel.add(meaningPanel, gbc);

        // Обертываем gridPanel в JScrollPane для добавления прокрутки
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        frame.add(scrollPane, BorderLayout.CENTER);
    }

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
        cardsPanel.removeAll();
        ImageIcon initialIcon = createScaledIcon("src/images/back.png", 150, 200);

        cardButtons = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            JButton card = createCardButton("");

            card.setIcon(initialIcon);

            card.addActionListener(new CardActionListener(i));
            cardsPanel.add(card);
            cardButtons.add(card);
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

    private void showCardMeaning(int cardIndex) {
        String[] cardNames = {"The Fool", "The Magician", "The High Priestess", "The Empress"};
        String[] cardMeanings = {
                "Общий прогноз на сегодня: Сегодня ваша жизнь будет зависеть от текущих действий. Важно фокусироваться на обучении и развитии интеллектуальных способностей, чтобы раскрыть свой потенциал для достижения целей. Имейте в виду: возможны коварные интриги и нехватка важной информации.\n" +
                        "\n" +
                        "Любовь и отношения: Остерегайтесь обмана и лицемерия со стороны партнера. Возможны разочарование и ревность. Постарайтесь сохранить внимательность и уверенность в себе. Эмоциональная близость может быть под угрозой, старайтесь укрепить ваши отношения.\n" +
                        "\n" +
                        "Финансы: На работе предстоит внедрение инновационного подхода. Возможен хаос и срыв планов; важно не спешить с принятием решений. Вы обладаете независимостью от материальных вещей и можете сосредоточиться на творческом развитии, отказавшись от денежных вознаграждений.\n" +
                        "\n" +
                        "Здоровье: Обратите внимание на возможные заболевания или их обострение. Вероятен неточный диагноз, поэтому воздержитесь от посещения врачей и перепроверьте медицинские заключения. Высок риск врачебной ошибки, поэтому будьте внимательны и осторожны.",
                "Action, the power to manifest",
                "Inaction, going within, the subconscious",
                "Fertility, femininity, beauty, nature, abundance"
        };

        meaningLabel.setText("Карта: " + cardNames[cardIndex] + " - Толкование: " + cardMeanings[cardIndex]);

        for (int i = 0; i < cardButtons.size(); i++) {
            if (i != cardIndex) {
                JButton button = cardButtons.get(i);
                button.setEnabled(false);
            }
        }
        resetButton.setVisible(true);
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

            String imagePath = "src/images/image" + (cardIndex + 1) + ".png";
            ImageIcon revealedIcon = createScaledIcon(imagePath, 150, 200);
            source.setIcon(revealedIcon);

            showCardMeaning(cardIndex);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TarotInterface::new);
    }
}
