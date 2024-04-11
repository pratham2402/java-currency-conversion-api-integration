import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class CurrencyConverter extends JFrame {
    private JComboBox<String> sourceCurrencyComboBox;
    private JComboBox<String> targetCurrencyComboBox;
    private JTextField amountField;
    private JLabel resultLabel;
    private final String apiKey = "YOUR_API_KEY"; // Replace "YOUR_API_KEY" with your actual API key

    public CurrencyConverter() {
        setTitle("Currency Converter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create source currency dropdown
        String[] currencies = {"AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG", "AZN", "BAM", "BBD", "BDT", "BGN", "BHD", "BIF", "BMD", "BND", "BOB", "BRL", "BSD", "BTN", "BWP", "BYN", "BZD", "CAD", "CDF", "CHF", "CLP", "CNY", "COP", "CRC", "CUP", "CVE", "CZK", "DJF", "DKK", "DOP", "DZD", "EGP", "ERN", "ETB", "EUR", "FJD", "FKP", "FOK", "GBP", "GEL", "GGP", "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD", "HNL", "HRK", "HTG", "HUF", "IDR", "ILS", "IMP", "INR", "IQD", "IRR", "ISK", "JEP", "JMD", "JOD", "JPY", "KES", "KGS", "KHR", "KID", "KMF", "KRW", "KWD", "KYD", "KZT", "LAK", "LBP", "LKR", "LRD", "LSL", "LYD", "MAD", "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRU", "MUR", "MVR", "MWK", "MXN", "MYR", "MZN", "NAD", "NGN", "NIO", "NOK", "NPR", "NZD", "OMR", "PAB", "PEN", "PGK", "PHP", "PKR", "PLN", "PYG", "QAR", "RON", "RSD", "RUB", "RWF", "SAR", "SBD", "SCR", "SDG", "SEK", "SGD", "SHP", "SLE", "SOS", "SRD", "SSP", "STN", "SYP", "SZL", "THB", "TJS", "TMT", "TND", "TOP", "TRY", "TTD", "TVD", "TWD", "TZS", "UAH", "UGX", "USD", "UYU", "UZS", "VES", "VND", "VUV", "WST", "XAF", "XCD", "XDR", "XOF", "XPF", "YER", "ZAR", "ZMW", "ZWL"};
        sourceCurrencyComboBox = new JComboBox<>(currencies);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Source Currency:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(sourceCurrencyComboBox, gbc);

        // Create target currency dropdown
        targetCurrencyComboBox = new JComboBox<>(currencies);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Target Currency:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(targetCurrencyComboBox, gbc);

        // Create amount field
        amountField = new JTextField();
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(amountField, gbc);

        // Create button to perform conversion
        JButton convertButton = new JButton("Convert");
        convertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                convertCurrency();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(convertButton, gbc);

        // Create label to display result
        resultLabel = new JLabel();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(new JLabel("Result:"), gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(resultLabel, gbc);

        pack();
        setVisible(true);
    }

    private void convertCurrency() {
        String sourceCurrency = (String) sourceCurrencyComboBox.getSelectedItem();
        String targetCurrency = (String) targetCurrencyComboBox.getSelectedItem();
        double amount = Double.parseDouble(amountField.getText());

        try {
            String urlString = "https://api.exchangerate-api.com/v4/latest/" + sourceCurrency;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", apiKey); // Add your API key as a request header
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONParser parser = new JSONParser();
                JSONObject jsonResponse = (JSONObject) parser.parse(response.toString());
                JSONObject rates = (JSONObject) jsonResponse.get("rates");
                double rate = (double) rates.get(targetCurrency);
                double result = amount * rate;
                resultLabel.setText(String.format("%.2f %s = %.2f %s", amount, sourceCurrency, result, targetCurrency));
            } else {
                resultLabel.setText("Error: HTTP " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultLabel.setText("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CurrencyConverter();
            }
        });
    }
}
