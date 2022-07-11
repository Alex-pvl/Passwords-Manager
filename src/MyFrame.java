import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;


public class MyFrame extends JFrame {
	private static final File file = new File("C:\\Users\\sasha\\passwords\\passwords.txt");

	public static final String CONNECTION_URL = "jdbc:postgresql://localhost:5432/encoder";
	private final Connection connection =
			DriverManager.getConnection(CONNECTION_URL, "postgres", "sa3862930ha");

	JPasswordField passwordField;
	JTextField sourceField;
	JButton encodeBtn;

	public void encode() {
		String password = Arrays.toString(passwordField.getPassword());
		Encoder encoder = new Encoder(password);

		String source = sourceField.getText();
		String original = encoder.getOriginal();
		String result = encoder.getResult();

		try {
			PreparedStatement statement = connection.prepareStatement(
					"INSERT INTO passwords VALUES (?, ?, ?)");

			statement.setString(1, source);
			statement.setString(2, original);
			statement.setString(3, result);

			statement.execute();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		try (OutputStream os = new FileOutputStream(file, true)) {
			os.write(sourceField.getText().getBytes());
			os.write(": ".getBytes());
			os.write(result.getBytes());
			os.write("\n".getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.exit(0);
	}


	public MyFrame() throws SQLException {
		super("My Encoder");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(new ImageIcon("C:\\Users\\sasha\\dev\\Java\\Projects\\Encoder\\logo.png").getImage());
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dimension = toolkit.getScreenSize();
		setBounds(dimension.width / 2 - 250, dimension.height / 2 - 100,
				500, 200);
		setLayout(new BorderLayout());
		setResizable(false);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(219, 219, 219));
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				requestFocusInWindow();
			}
		});

		sourceField = new JTextField();
		sourceField.setPreferredSize(new Dimension(350, 30));

		passwordField = new JPasswordField();
		passwordField.setPreferredSize(new Dimension(350, 30));

		encodeBtn = new JButton("Encode!");
		encodeBtn.setBorderPainted(false);
		encodeBtn.setForeground(Color.WHITE);
		encodeBtn.setBackground(new Color(78, 166, 64));
		encodeBtn.setPreferredSize(new Dimension(138, 30));
		encodeBtn.setFont(new Font("Arial", Font.BOLD, 16));
		encodeBtn.addActionListener(e -> encode());
		encodeBtn.setFocusable(false);
		encodeBtn.setContentAreaFilled(true);

		panel.add(sourceField);
		panel.add(passwordField);
		panel.add(encodeBtn);

		add(panel);

		setVisible(true);
	}
}
