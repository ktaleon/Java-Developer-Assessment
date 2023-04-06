import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;

public class GUIForm extends JFrame implements ItemListener {
    // names
    private JLabel firstNameLabel;
    private JTextField firstNameField;
    private JLabel lastNameLabel;
    private JTextField lastNameField;

    // email
    private JLabel emailLabel;
    private JTextField emailField;

    // birthday
    private JLabel birthDateLabel;
    private JTextField yearField;
    private JComboBox<String> monthComboBox;
    private JComboBox<String> daysComboBox;

    // sex
    private JLabel sexLabel;

    // button
    private JButton submitButton;

    // panel
    private JPanel panel;
    JFrame displayFrame = new JFrame("User Information");
    JTextArea textArea = new JTextArea(20, 40);

    List<UserInfo> userList = new ArrayList<UserInfo>();

    public GUIForm() {
        super("Form Submission App");
        String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
                "October", "November", "December" };
        String[] days = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
                "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" };
        String[] sex = { "Male", "Female" };

        monthComboBox = new JComboBox<>(months);
        monthComboBox.addItemListener(this);
        daysComboBox = new JComboBox<>(days);
        JComboBox<String> sexComboBox = new JComboBox<>(sex);

        JFrame frame = new JFrame("Error Message");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        firstNameLabel = new JLabel("First Name: ");
        firstNameField = new JTextField(20);
        lastNameLabel = new JLabel("Last Name: ");
        lastNameField = new JTextField(20);
        emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);
        birthDateLabel = new JLabel("Birthday:");
        yearField = new JTextField(4);
        sexLabel = new JLabel("Sex:");
        submitButton = new JButton("Submit");
        panel = new JPanel();

        // formats yearField to only accept numbers and limit to 4 characters
        DecimalFormat noSeparatorFormat = new DecimalFormat("#");
        NumberFormatter formatter = new NumberFormatter(noSeparatorFormat);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(9999);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        JFormattedTextField yearField = new JFormattedTextField(formatter);
        yearField.setColumns(4);

        // Layout
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        GridBagConstraints c = new GridBagConstraints();

        // first name label and text field
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_END;
        panel.add(firstNameLabel, c);

        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        panel.add(firstNameField, c);

        // last name label and text field
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_END;
        panel.add(lastNameLabel, c);

        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_START;
        panel.add(lastNameField, c);

        // email label and text field
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LINE_END;
        panel.add(emailLabel, c);

        c.gridx = 1;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LINE_START;
        panel.add(emailField, c);

        // birth date label and combo boxes
        c.gridx = 0;
        c.gridy = 3;
        c.anchor = GridBagConstraints.LINE_END;
        panel.add(birthDateLabel, c);

        c.gridx = 1;
        c.gridy = 3;
        c.anchor = GridBagConstraints.LINE_START;
        panel.add(monthComboBox, c);

        c.gridx = 1;
        c.gridy = 4;
        c.anchor = GridBagConstraints.LINE_START;
        panel.add(daysComboBox, c);

        c.gridx = 1;
        c.gridy = 5;
        c.anchor = GridBagConstraints.LINE_START;
        panel.add(yearField, c);

        // sex label and combo box
        c.gridx = 0;
        c.gridy = 6;
        c.anchor = GridBagConstraints.LINE_END;
        panel.add(sexLabel, c);

        c.gridx = 1;
        c.gridy = 6;
        c.anchor = GridBagConstraints.LINE_START;
        panel.add(sexComboBox, c);

        // submit button
        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        panel.add(submitButton, c);

        // Sumbit Button Action
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = capitalizeName(firstNameField.getText());
                String lastName = capitalizeName(lastNameField.getText());
                String email = emailField.getText();
                String birthYear = yearField.getText();
                String birthMonth = (String) monthComboBox.getSelectedItem();
                String birthDay = (String) daysComboBox.getSelectedItem();
                String sex = (String) sexComboBox.getSelectedItem();
                String birthDate = birthMonth + " " + birthDay + ", " + birthYear;

                // checks if all fields are filled out
                if (firstName.isEmpty() || email.isEmpty() || lastName.isEmpty() || birthYear.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Please Fill Out All The Empty Fields",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);

                } else {
                    if (!isValidEmail(email)) {
                        JOptionPane.showMessageDialog(frame,
                                "Not a Valid Email",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        submitAction(firstName, lastName, email, birthDate, sex);

                        // resets all the fields
                        firstNameField.setText("");
                        lastNameField.setText("");
                        emailField.setText("");
                        yearField.setText("0");
                        monthComboBox.setSelectedIndex(0);
                        daysComboBox.setSelectedIndex(0);
                        sexComboBox.setSelectedIndex(0);
                    }
                }
            }
        });

        getContentPane().add(panel);

        setSize(500, 300);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);
    }

    // User object
    public class UserInfo {
        private String firstName;
        private String lastName;
        private String email;
        private String birthday;
        private String sex;

        public UserInfo(String firstName, String lastName, String email, String birthday, String sex) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.birthday = birthday;
            this.sex = sex;
        }

        public String getInfo() {
            return firstName + " " + lastName + " " + "(" + sex + ")\n" + "Born on " + birthday + "\n" + email;
        }
    }

    // Code after pressing the submit button
    private void submitAction(String fname, String lname, String mail, String bday, String sx) {
        UserInfo userInfo = new UserInfo(fname, lname, mail, bday, sx);
        userList.add(userInfo);

        if (displayFrame != null && displayFrame.isVisible()) {
            textArea.setText("");
            for (UserInfo userInfos : userList) {
                textArea.append(userInfos.getInfo() + "\n\n");
            }
        } else {
            displayFrame = new JFrame("User Information");
            textArea = new JTextArea(20, 40);
            textArea.setEditable(false);

            for (UserInfo userInfos : userList) {
                textArea.append(userInfos.getInfo() + "\n\n");
            }

            JScrollPane scrollPane = new JScrollPane(textArea);
            displayFrame.getContentPane().add(scrollPane);

            displayFrame.pack();
            displayFrame.setLocationRelativeTo(null);
            displayFrame.setVisible(true);
        }
    }

    // Changes the number of days on the days combobox depending on the selected
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == monthComboBox) {
            String month = (String) monthComboBox.getSelectedItem();
            if (month.equals("January") || month.equals("March") || month.equals("May") || month.equals("July")
                    || month.equals("August") || month.equals("October") || month.equals("December")) {
                String[] days = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16",
                        "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" };
                daysComboBox.removeAllItems();
                for (String day : days) {
                    daysComboBox.addItem(day);
                }
            } else if (month.equals("April") || month.equals("June") || month.equals("September")
                    || month.equals("November")) {
                String[] days = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16",
                        "17",
                        "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" };
                daysComboBox.removeAllItems();
                for (String day : days) {
                    daysComboBox.addItem(day);
                }
            } else if (month.equals("February")) {
                String[] days = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16",
                        "17",
                        "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29" };
                daysComboBox.removeAllItems();
                for (String day : days) {
                    daysComboBox.addItem(day);
                }
            }
        }
    }

    // checks for valid email
    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Capitalizes the first letter of each name
    public static String capitalizeName(String input) {
        StringBuilder output = new StringBuilder();
        boolean capitalizeNextChar = true;
        for (char c : input.toCharArray()) {
            if (Character.isWhitespace(c)) {
                capitalizeNextChar = true;
                output.append(c);
            } else {
                if (capitalizeNextChar) {
                    output.append(Character.toUpperCase(c));
                    capitalizeNextChar = false;
                } else {
                    output.append(Character.toLowerCase(c));
                }
            }
        }
        return output.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUIForm());
    }
}
