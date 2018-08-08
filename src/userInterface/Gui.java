package userInterface;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;

import controller.MainController;
import deployment.MakefileDeployer;
import javax.swing.JTabbedPane;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import java.awt.Component;
import javax.swing.JTextArea;
import java.awt.Font;

public class Gui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JTextField ipField;
	private JTextField ansiblePathField;
	private JTextField dockerPathField;
	private MainController controller;
	public static JTextArea deployableTextArea,nonDeployableTextArea;
	private static JFrame mainFrame;
	private JTextField makeFileDirectoryField;
	private JTextField additionalParameters;
	private JTextField clientFilesNonDeployablePath;
	private JTextField outputPathNonDeployable;
	private JTextField outputPathDeployable;
	private JTextField outputMergeField;
	private JTextField secondPDFField;
	private JTextField firstPDFField;
	private JTextField thirdPDFField;
	private JTextField deviceNameField;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					mainFrame = new Gui();
					mainFrame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Gui() {
		controller = new MainController();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 900, 900);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.WHITE);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel SSHdeployablePanel = new JPanel();
		SSHdeployablePanel.setBackground(Color.WHITE);
		tabbedPane.addTab("Deployable Devices", null, SSHdeployablePanel, null);
		
		JLabel label = new JLabel("Username");
		label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		usernameField = new JTextField();
		usernameField.setColumns(10);
		
		
		JLabel label_1 = new JLabel("Password");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		
		JLabel label_2 = new JLabel("IP Address of Device ");
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		ipField = new JTextField();
		ipField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		ipField.setColumns(10);
		
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnConnect.setForeground(Color.WHITE);
		btnConnect.setBackground(new Color(0, 128, 0));
		JCheckBox chckbxConnect = new JCheckBox("Status");
		chckbxConnect.setFont(new Font("Tahoma", Font.PLAIN, 14));
		chckbxConnect.setEnabled(false);
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.setConnectionAddress(ipField.getText());
				controller.setUsername(usernameField.getText());
				controller.setPassword(String.valueOf(passwordField.getPassword()));
				
				if(!controller.controlFields()) {
					JOptionPane.showMessageDialog(mainFrame, "Please Check your credentials");	
					return;
				}
				if(controller.checkConnection()) {
					chckbxConnect.setText("Connected");
					chckbxConnect.setSelected(true);
					JOptionPane.showMessageDialog(mainFrame, "Connection Establish");	
					
				}
				else
				{
					chckbxConnect.setText("Failed");
					JOptionPane.showMessageDialog(mainFrame, "Connection Failed");	
				}	
			}
		});
		
				JLabel label_3 = new JLabel("Select Directory of Ansible Files");
				label_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
				
				ansiblePathField = new JTextField();
				ansiblePathField.setColumns(10);
				JCheckBox chckbxHasAnsibleFiles = new JCheckBox("Status");
				chckbxHasAnsibleFiles.setFont(new Font("Tahoma", Font.PLAIN, 14));
				chckbxHasAnsibleFiles.setEnabled(false);
				JButton bthSelectAnsible = new JButton("Select");
				bthSelectAnsible.setFont(new Font("Tahoma", Font.PLAIN, 14));
				bthSelectAnsible.setForeground(Color.WHITE);
				bthSelectAnsible.setBackground(new Color(0, 128, 0));
				bthSelectAnsible.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
						fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						int returnValue = fileChooser.showOpenDialog(null);
						
						if (returnValue == JFileChooser.APPROVE_OPTION) {
							File selectedFile = fileChooser.getSelectedFile();		
							ansiblePathField.setText(selectedFile.getAbsolutePath());
							if(!controller.controlAnsibleFiles(selectedFile)) {
								chckbxHasAnsibleFiles.setText("Failed");
								JOptionPane.showMessageDialog(mainFrame, "Please Check Ansible file in directory exists");	
							}
							else {
								chckbxHasAnsibleFiles.setText("Confirmed");
								chckbxHasAnsibleFiles.setSelected(true);
								controller.setAnsibleFilePath(selectedFile);
							}
						}								
						
					}
					
				});
				
				
				JLabel label_4 = new JLabel("Select Directory of Docker Files");
				label_4.setFont(new Font("Tahoma", Font.PLAIN, 14));
				
				dockerPathField = new JTextField();
				dockerPathField.setColumns(10);
				JCheckBox chckbxHasDockerFile = new JCheckBox("Status");
				chckbxHasDockerFile.setFont(new Font("Tahoma", Font.PLAIN, 14));
				chckbxHasDockerFile.setEnabled(false);
				
				
				JButton btnSelectDocker = new JButton("Select");
				btnSelectDocker.setFont(new Font("Tahoma", Font.PLAIN, 14));
				btnSelectDocker.setForeground(Color.WHITE);
				btnSelectDocker.setBackground(new Color(0, 128, 0));
				
				btnSelectDocker.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
						fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						int returnValue = fileChooser.showOpenDialog(null);
						
						if (returnValue == JFileChooser.APPROVE_OPTION) {
							File selectedFile = fileChooser.getSelectedFile();		
							dockerPathField.setText(selectedFile.getAbsolutePath());
							if(!controller.controlDockerFiles(selectedFile)) {
								chckbxHasDockerFile.setText("Failed");
								JOptionPane.showMessageDialog(mainFrame, "Please Check Ansible file in directory exists");	
							}
							else {
								chckbxHasDockerFile.setText("Confirmed");
								chckbxHasDockerFile.setSelected(true);
								controller.setDockerFilePath(selectedFile);
							}
						}
					}
				});
				
				JCheckBox chckbxDeploymentOfAnsible = new JCheckBox("Configuration of Ansible Files");
				chckbxDeploymentOfAnsible.setFont(new Font("Tahoma", Font.PLAIN, 14));
				chckbxDeploymentOfAnsible.setEnabled(false);
				
				JCheckBox chckbxDeploymentOfDocker = new JCheckBox("Configuration of Client Files");
				chckbxDeploymentOfDocker.setFont(new Font("Tahoma", Font.PLAIN, 14));
				chckbxDeploymentOfDocker.setEnabled(false);
				
				JLabel label_6 = new JLabel("Select test type");
				label_6.setFont(new Font("Tahoma", Font.PLAIN, 14));
				
				JComboBox<String> testTypeBox = new JComboBox<String>();
				testTypeBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
				testTypeBox.addItem("All Tests");
				testTypeBox.addItem("Read Tests");
				testTypeBox.addItem("Write Tests");
				testTypeBox.addItem("CPU Tests");
				testTypeBox.addItem("Networking Tests");
				testTypeBox.addItem("Encryption Tests");
				testTypeBox.addItem("PubSub Tests");
				
				JButton btnStartTestingDeployable = new JButton("Start Testing");
				btnStartTestingDeployable.setForeground(Color.WHITE);
				btnStartTestingDeployable.setBackground(new Color(0, 128, 0));
				btnStartTestingDeployable.setFont(new Font("Tahoma", Font.PLAIN, 14));
				outputPathDeployable = new JTextField();
				outputPathDeployable.setColumns(10);
				btnStartTestingDeployable.addActionListener(new ActionListener() {	
					@Override
					public void actionPerformed(ActionEvent e) {
						if(outputPathDeployable.getText().isEmpty()|| outputPathDeployable.getText().equals("")) {
							JOptionPane.showMessageDialog(mainFrame, "Please Select Output Path");
						}
						else {
							controller.setTestingType(String.valueOf(testTypeBox.getSelectedItem()));
							if(controller.startConfiguration())
							{
								chckbxDeploymentOfAnsible.setSelected(true);
								chckbxDeploymentOfDocker.setSelected(true);
								
							}
							if(chckbxDeploymentOfAnsible.isSelected() && chckbxDeploymentOfDocker.isSelected()) {
								controller.setOutputOrder(false);
								JOptionPane.showMessageDialog(mainFrame, "Deployment Started");	
								controller.startDeployment(testTypeBox.getSelectedItem().toString());
							}
						}
					
						
					}
					
				});
				
				JLabel label_12 = new JLabel("Output");
				label_12.setFont(new Font("Tahoma", Font.PLAIN, 14));
				
				JScrollPane scrollPane = new JScrollPane((Component) null);
				
				JLabel lblNotePleaseEnable = new JLabel("Note: Please enable SSH Connection with your device");
				lblNotePleaseEnable.setFont(new Font("Tahoma", Font.PLAIN, 14));
				
				JLabel lblNotepleaseDownloadAnd = new JLabel("Note:Please download and install ansible on your machine");
				lblNotepleaseDownloadAnd.setFont(new Font("Tahoma", Font.PLAIN, 14));
				
				JLabel label_5 = new JLabel("Select Report Path");
				label_5.setFont(new Font("Tahoma", Font.PLAIN, 14));
				
				
				
				JButton selectReportPathBtn = new JButton("Select");
				selectReportPathBtn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
						fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						int returnValue = fileChooser.showOpenDialog(null);
						
						if (returnValue == JFileChooser.APPROVE_OPTION) {
							File selectedFile = fileChooser.getSelectedFile();		
							outputPathDeployable.setText(selectedFile.getAbsolutePath());
							controller.setOutputFilePath(selectedFile);
							}
					}
				});
				selectReportPathBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
				selectReportPathBtn.setForeground(Color.WHITE);
				selectReportPathBtn.setBackground(new Color(0, 100, 0));
				
				JLabel label_8 = new JLabel("Report Output Type");
				label_8.setFont(new Font("Tahoma", Font.PLAIN, 14));
				
				JComboBox<String> reportTypeBoxDeployable = new JComboBox<String>();
				reportTypeBoxDeployable.addItem(".pdf");
				reportTypeBoxDeployable.addItem(".csv");
				
				JButton getReportDeployable = new JButton("Get Report");
				getReportDeployable.setEnabled(false);
				getReportDeployable.setFont(new Font("Tahoma", Font.PLAIN, 14));
				getReportDeployable.setForeground(Color.WHITE);
				getReportDeployable.setBackground(new Color(0, 100, 0));
				
				getReportDeployable.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						String reportType=String.valueOf(reportTypeBoxDeployable.getSelectedIndex());
						
						controller.setReportType(reportType);
						if(deviceNameField.getText().equals("")|| deviceNameField.getText().isEmpty())
						{
							JOptionPane.showMessageDialog(mainFrame, "Please Write Device Name");
							return;
						}
						controller.setDeviceName(deviceNameField.getText().toString());
						controller.startReporting();
					}
				});
				
				JLabel lblDeviceName = new JLabel("Device Name");
				lblDeviceName.setFont(new Font("Tahoma", Font.PLAIN, 14));
				
				deviceNameField = new JTextField();
				deviceNameField.setColumns(10);
				
				
				
				
				GroupLayout gl_SSHdeployablePanel = new GroupLayout(SSHdeployablePanel);
				gl_SSHdeployablePanel.setHorizontalGroup(
					gl_SSHdeployablePanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_SSHdeployablePanel.createSequentialGroup()
							.addGroup(gl_SSHdeployablePanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_SSHdeployablePanel.createSequentialGroup()
									.addContainerGap()
									.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 849, Short.MAX_VALUE))
								.addGroup(gl_SSHdeployablePanel.createSequentialGroup()
									.addGap(32)
									.addGroup(gl_SSHdeployablePanel.createParallelGroup(Alignment.LEADING)
										.addComponent(chckbxDeploymentOfAnsible, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE)
										.addComponent(chckbxDeploymentOfDocker, GroupLayout.PREFERRED_SIZE, 230, GroupLayout.PREFERRED_SIZE)
										.addGroup(gl_SSHdeployablePanel.createSequentialGroup()
											.addGroup(gl_SSHdeployablePanel.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_SSHdeployablePanel.createSequentialGroup()
													.addGroup(gl_SSHdeployablePanel.createParallelGroup(Alignment.LEADING)
														.addGroup(gl_SSHdeployablePanel.createSequentialGroup()
															.addComponent(label, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
															.addPreferredGap(ComponentPlacement.RELATED)
															.addComponent(usernameField, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
															.addPreferredGap(ComponentPlacement.RELATED)
															.addComponent(label_1))
														.addComponent(label_3, GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
														.addComponent(label_4))
													.addPreferredGap(ComponentPlacement.RELATED)
													.addGroup(gl_SSHdeployablePanel.createParallelGroup(Alignment.LEADING)
														.addComponent(dockerPathField, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
														.addGroup(gl_SSHdeployablePanel.createParallelGroup(Alignment.TRAILING, false)
															.addComponent(passwordField, Alignment.LEADING)
															.addComponent(ansiblePathField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE))))
												.addGroup(gl_SSHdeployablePanel.createSequentialGroup()
													.addComponent(label_8, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
													.addPreferredGap(ComponentPlacement.RELATED)
													.addComponent(reportTypeBoxDeployable, 0, 227, Short.MAX_VALUE))
												.addGroup(gl_SSHdeployablePanel.createSequentialGroup()
													.addGroup(gl_SSHdeployablePanel.createParallelGroup(Alignment.LEADING)
														.addComponent(label_5)
														.addGroup(gl_SSHdeployablePanel.createParallelGroup(Alignment.TRAILING)
															.addComponent(lblDeviceName, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
															.addComponent(label_6, Alignment.LEADING)))
													.addPreferredGap(ComponentPlacement.RELATED)
													.addGroup(gl_SSHdeployablePanel.createParallelGroup(Alignment.LEADING)
														.addComponent(deviceNameField, GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
														.addComponent(testTypeBox, 0, 243, Short.MAX_VALUE)
														.addComponent(outputPathDeployable, GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE))))
											.addPreferredGap(ComponentPlacement.RELATED)
											.addGroup(gl_SSHdeployablePanel.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_SSHdeployablePanel.createSequentialGroup()
													.addGap(2)
													.addGroup(gl_SSHdeployablePanel.createParallelGroup(Alignment.LEADING)
														.addGroup(gl_SSHdeployablePanel.createSequentialGroup()
															.addComponent(btnSelectDocker)
															.addPreferredGap(ComponentPlacement.UNRELATED)
															.addComponent(chckbxHasDockerFile, GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
														.addGroup(gl_SSHdeployablePanel.createSequentialGroup()
															.addComponent(bthSelectAnsible)
															.addPreferredGap(ComponentPlacement.UNRELATED)
															.addComponent(chckbxHasAnsibleFiles, GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
														.addComponent(btnStartTestingDeployable, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
														.addComponent(selectReportPathBtn, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
														.addComponent(getReportDeployable, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)))
												.addGroup(gl_SSHdeployablePanel.createSequentialGroup()
													.addComponent(label_2)
													.addPreferredGap(ComponentPlacement.RELATED)
													.addComponent(ipField, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)))
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(btnConnect, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(chckbxConnect)
											.addGap(57)))))
							.addContainerGap())
						.addGroup(gl_SSHdeployablePanel.createSequentialGroup()
							.addContainerGap(413, Short.MAX_VALUE)
							.addGroup(gl_SSHdeployablePanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNotePleaseEnable)
								.addComponent(lblNotepleaseDownloadAnd))
							.addGap(90))
						.addGroup(gl_SSHdeployablePanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(label_12, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(809, Short.MAX_VALUE))
				);
				gl_SSHdeployablePanel.setVerticalGroup(
					gl_SSHdeployablePanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_SSHdeployablePanel.createSequentialGroup()
							.addGap(26)
							.addGroup(gl_SSHdeployablePanel.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_SSHdeployablePanel.createParallelGroup(Alignment.BASELINE)
									.addComponent(label)
									.addComponent(usernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_SSHdeployablePanel.createParallelGroup(Alignment.BASELINE)
									.addComponent(label_1)
									.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(label_2)
									.addComponent(ipField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(btnConnect)
									.addComponent(chckbxConnect)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_SSHdeployablePanel.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_SSHdeployablePanel.createParallelGroup(Alignment.BASELINE)
									.addComponent(label_3)
									.addComponent(ansiblePathField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(bthSelectAnsible))
								.addComponent(chckbxHasAnsibleFiles))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_SSHdeployablePanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_SSHdeployablePanel.createSequentialGroup()
									.addGroup(gl_SSHdeployablePanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(label_4)
										.addComponent(dockerPathField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addGap(23)
									.addComponent(chckbxDeploymentOfAnsible)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(chckbxDeploymentOfDocker)
									.addGap(43)
									.addGroup(gl_SSHdeployablePanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(label_6)
										.addComponent(btnStartTestingDeployable)
										.addComponent(testTypeBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
								.addGroup(gl_SSHdeployablePanel.createParallelGroup(Alignment.BASELINE)
									.addComponent(btnSelectDocker)
									.addComponent(chckbxHasDockerFile)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_SSHdeployablePanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblDeviceName)
								.addComponent(deviceNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_SSHdeployablePanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(label_5, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
								.addComponent(outputPathDeployable, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(selectReportPathBtn))
							.addGap(18)
							.addGroup(gl_SSHdeployablePanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(label_8, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
								.addComponent(reportTypeBoxDeployable, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(getReportDeployable))
							.addPreferredGap(ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
							.addComponent(lblNotepleaseDownloadAnd)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblNotePleaseEnable)
							.addGap(25)
							.addComponent(label_12)
							.addGap(18)
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
							.addContainerGap())
				);
				
				 deployableTextArea = new JTextArea();
				 deployableTextArea.setEditable(false);
				 deployableTextArea.setBackground(Color.LIGHT_GRAY);
				 scrollPane.setViewportView(deployableTextArea);
				 SSHdeployablePanel.setLayout(gl_SSHdeployablePanel);
		
		JPanel nonDeployablePanel = new JPanel();
		nonDeployablePanel.setBackground(Color.WHITE);
		tabbedPane.addTab("Non Deployable Devices", null, nonDeployablePanel, null);
		
		JLabel lblMakefileDirectoryPath = new JLabel("Makefile Directory Path");
		lblMakefileDirectoryPath.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		makeFileDirectoryField = new JTextField();
		makeFileDirectoryField.setColumns(10);
		
		JButton btnSelectMakefile = new JButton("Select");
		btnSelectMakefile.addActionListener(new ActionListener() {
			
				public void actionPerformed(ActionEvent arg0) {
					JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int returnValue = fileChooser.showOpenDialog(null);
					
					if (returnValue == JFileChooser.APPROVE_OPTION) {
						File selectedFile = fileChooser.getSelectedFile();		
						if(!controller.controlMakeFiles(selectedFile)) {
							
							JOptionPane.showMessageDialog(mainFrame, "Please Check CMakeLists.txt file in directory exists");	
						}
						else{
							controller.setMakeFilePath(selectedFile);
						    makeFileDirectoryField.setText(selectedFile.getAbsolutePath());
						}
							
						}
				}
		});
		btnSelectMakefile.setForeground(Color.WHITE);
		btnSelectMakefile.setBackground(Color.BLUE);
		
		JLabel lblAvailableDevices = new JLabel("Available Devices");
		lblAvailableDevices.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JComboBox<String> availableDevicesBox = new JComboBox<String>();
		getDevices(availableDevicesBox);
		
		
		
		JButton btnAddNewDevice = new JButton("Add new Device");
		btnAddNewDevice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AdditionalDeviceFrame frame = new AdditionalDeviceFrame();
				frame.showDeviceFrame();
				getDevices(availableDevicesBox);
				contentPane.revalidate();
				contentPane.repaint();
				availableDevicesBox.revalidate();
				availableDevicesBox.repaint();
				
			}
		});
		btnAddNewDevice.setForeground(Color.WHITE);
		btnAddNewDevice.setBackground(Color.BLUE);
		
		JLabel lblAdditionalParameters = new JLabel("Additional Parameters");
		lblAdditionalParameters.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblSelectClientFiles = new JLabel("Select Client Files");
		lblSelectClientFiles.setFont(new Font("Tahoma", Font.PLAIN, 14));
		JScrollPane scrollPaneNonDeployable = new JScrollPane((Component) null);
		nonDeployableTextArea = new JTextArea();
		nonDeployableTextArea.setEditable(false);
		nonDeployableTextArea.setBackground(Color.LIGHT_GRAY);
		scrollPaneNonDeployable.setViewportView(nonDeployableTextArea);
		additionalParameters = new JTextField();
		additionalParameters.setColumns(10);
		
		JButton btnDeployToDevice = new JButton("Deploy To Device");
		btnDeployToDevice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(makeFileDirectoryField.getText().equals("")||makeFileDirectoryField.getText().isEmpty())
				{
					JOptionPane.showMessageDialog(mainFrame, "Please select makefile directory first");
					return;
				}
				else {
					controller.setOutputOrder(true);
					MakefileDeployer makefileDeployer = new MakefileDeployer(makeFileDirectoryField.getText(),additionalParameters.getText(),String.valueOf(availableDevicesBox.getSelectedItem()));
					makefileDeployer.startDeployment();	
					
				}
				
			}
		});
		btnDeployToDevice.setForeground(Color.WHITE);
		btnDeployToDevice.setBackground(Color.BLUE);
		
		clientFilesNonDeployablePath = new JTextField();
		clientFilesNonDeployablePath.setColumns(10);
		
		JButton btnSelectReportPathNonDeployable = new JButton("Select");
		btnSelectReportPathNonDeployable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = fileChooser.showOpenDialog(null);
				
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();		
					outputPathDeployable.setText(selectedFile.getAbsolutePath());
						controller.setOutputFilePath(selectedFile);
					}
			}
		});
		btnSelectReportPathNonDeployable.setBackground(Color.BLUE);
		btnSelectReportPathNonDeployable.setForeground(Color.WHITE);
		
		JLabel lblSelectReportPath = new JLabel("Select Report Path");
		lblSelectReportPath.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		outputPathNonDeployable = new JTextField();
		outputPathNonDeployable.setColumns(10);
		
		JButton btnSelectClientNonDeployable = new JButton("Select");
		btnSelectClientNonDeployable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
					JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int returnValue = fileChooser.showOpenDialog(null);
					
					if (returnValue == JFileChooser.APPROVE_OPTION) {
						File selectedFile = fileChooser.getSelectedFile();		
						outputPathNonDeployable.setText(selectedFile.getAbsolutePath());
						if(!controller.controlDockerFiles(selectedFile)) {
							JOptionPane.showMessageDialog(mainFrame, "Please Check Single Client file in directory exists");	
						}
						else {
							chckbxHasDockerFile.setText("Confirmed");
							chckbxHasDockerFile.setSelected(true);
							controller.setDockerFilePath(selectedFile);
						}
					}
				
				
			}
		});
		btnSelectClientNonDeployable.setForeground(Color.WHITE);
		btnSelectClientNonDeployable.setBackground(Color.BLUE);
		
		JComboBox<String> outputTypeNonDeployable = new JComboBox<String>();
		outputTypeNonDeployable.addItem(".pdf");
		outputTypeNonDeployable.addItem(".csv");
		
		JLabel lblOutputType = new JLabel("Report Output Type");
		lblOutputType.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JButton btnGetReportNonDeployable = new JButton("Get Report");
		btnGetReportNonDeployable.setEnabled(false);
		btnGetReportNonDeployable.setForeground(Color.WHITE);
		btnGetReportNonDeployable.setBackground(Color.BLUE);
		
		JLabel label_9 = new JLabel("Select test type");
		label_9.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JComboBox<String> testTypeBoxNonDeployable = new JComboBox<String>();
		testTypeBoxNonDeployable.setFont(new Font("Tahoma", Font.PLAIN, 14));
		testTypeBoxNonDeployable.addItem("Read Tests");
		testTypeBoxNonDeployable.addItem("Write Tests");
		testTypeBoxNonDeployable.addItem("CPU Tests");
		testTypeBoxNonDeployable.addItem("Networking Tests");
		testTypeBoxNonDeployable.addItem("Encryption Tests");
		testTypeBoxNonDeployable.addItem("PubSub Tests");
		
		JButton btnStartTestingNonDeployable = new JButton("Start Testing");
		btnStartTestingNonDeployable.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(outputPathNonDeployable.getText().isEmpty()|| outputPathNonDeployable.getText().equals("")) {
					JOptionPane.showMessageDialog(mainFrame, "Please Select Output Path");
					return;
				}
				controller.setTestingType(String.valueOf(testTypeBox.getSelectedItem()));
				    controller.setOutputOrder(true);
					JOptionPane.showMessageDialog(mainFrame, "Deployment Started");	
					controller.startDeployment(testTypeBox.getSelectedItem().toString());
					
				
			}
	
		});
		btnStartTestingNonDeployable.setForeground(Color.WHITE);
		btnStartTestingNonDeployable.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnStartTestingNonDeployable.setBackground(Color.BLUE);
		
		JLabel label_10 = new JLabel("Output");
		label_10.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblExampleUsageType = new JLabel("Example Additional Parameter Type : VAR_NAME=var");
		GroupLayout gl_nonDeployablePanel = new GroupLayout(nonDeployablePanel);
		gl_nonDeployablePanel.setHorizontalGroup(
			gl_nonDeployablePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_nonDeployablePanel.createSequentialGroup()
					.addGroup(gl_nonDeployablePanel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_nonDeployablePanel.createSequentialGroup()
							.addGap(83)
							.addGroup(gl_nonDeployablePanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_nonDeployablePanel.createParallelGroup(Alignment.LEADING, false)
									.addGroup(gl_nonDeployablePanel.createSequentialGroup()
										.addComponent(lblOutputType)
										.addGap(35))
									.addComponent(lblMakefileDirectoryPath, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(lblAdditionalParameters, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addComponent(lblAvailableDevices, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblSelectClientFiles, GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
								.addComponent(lblSelectReportPath)
								.addComponent(label_9, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_nonDeployablePanel.createParallelGroup(Alignment.LEADING)
								.addComponent(availableDevicesBox, 0, 259, Short.MAX_VALUE)
								.addGroup(gl_nonDeployablePanel.createSequentialGroup()
									.addGroup(gl_nonDeployablePanel.createParallelGroup(Alignment.LEADING)
										.addComponent(outputTypeNonDeployable, 0, 155, Short.MAX_VALUE)
										.addComponent(outputPathNonDeployable, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
										.addComponent(testTypeBoxNonDeployable, 0, 155, Short.MAX_VALUE)
										.addComponent(clientFilesNonDeployablePath, Alignment.TRAILING, 155, 155, Short.MAX_VALUE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_nonDeployablePanel.createParallelGroup(Alignment.LEADING)
										.addComponent(btnStartTestingNonDeployable, GroupLayout.PREFERRED_SIZE, 98, Short.MAX_VALUE)
										.addComponent(btnSelectClientNonDeployable, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
										.addComponent(btnSelectReportPathNonDeployable, GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
										.addComponent(btnGetReportNonDeployable, GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)))
								.addComponent(additionalParameters, 259, 259, Short.MAX_VALUE)
								.addComponent(makeFileDirectoryField, GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
								.addComponent(lblExampleUsageType, GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_nonDeployablePanel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(btnDeployToDevice, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnSelectMakefile, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnAddNewDevice, GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE))
							.addGap(325))
						.addGroup(gl_nonDeployablePanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(label_10, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
						.addComponent(nonDeployableTextArea, GroupLayout.PREFERRED_SIZE, 938, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_nonDeployablePanel.setVerticalGroup(
			gl_nonDeployablePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_nonDeployablePanel.createSequentialGroup()
					.addGap(72)
					.addGroup(gl_nonDeployablePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAvailableDevices)
						.addComponent(availableDevicesBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAddNewDevice))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_nonDeployablePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMakefileDirectoryPath)
						.addComponent(makeFileDirectoryField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSelectMakefile))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_nonDeployablePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAdditionalParameters)
						.addComponent(additionalParameters, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnDeployToDevice))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblExampleUsageType)
					.addGap(102)
					.addGroup(gl_nonDeployablePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(clientFilesNonDeployablePath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSelectClientNonDeployable)
						.addComponent(lblSelectClientFiles))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_nonDeployablePanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_nonDeployablePanel.createSequentialGroup()
							.addComponent(btnStartTestingNonDeployable, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_nonDeployablePanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(outputPathNonDeployable, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSelectReportPathNonDeployable)
								.addComponent(lblSelectReportPath))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_nonDeployablePanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblOutputType)
								.addComponent(outputTypeNonDeployable, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnGetReportNonDeployable)))
						.addGroup(gl_nonDeployablePanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(testTypeBoxNonDeployable, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
							.addComponent(label_9, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(label_10, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(nonDeployableTextArea, GroupLayout.PREFERRED_SIZE, 438, GroupLayout.PREFERRED_SIZE)
					.addGap(12))
		);
		nonDeployablePanel.setLayout(gl_nonDeployablePanel);
		
		JPanel comparePanel = new JPanel();
		comparePanel.setBackground(Color.WHITE);
		tabbedPane.addTab("Merge Results", null, comparePanel, null);
		
		JLabel lblSelectOutputPath = new JLabel("Select Output Path");
		lblSelectOutputPath.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		outputMergeField = new JTextField();
		outputMergeField.setColumns(10);
		
		JButton button_2 = new JButton("Select");
		button_2.setForeground(Color.WHITE);
		button_2.setBackground(Color.RED);
		
		button_2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = fileChooser.showOpenDialog(null);
				
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();		
					outputMergeField.setText(selectedFile.getAbsolutePath());
					controller.setOutputFilePath(selectedFile);
					}
				
			}
			
			
			
		});
		
		secondPDFField = new JTextField();
		secondPDFField.setColumns(10);
		
		firstPDFField = new JTextField();
		firstPDFField.setColumns(10);
		
		JButton btnChooseFile = new JButton("Choose File");
		btnChooseFile.setForeground(Color.WHITE);
		btnChooseFile.setBackground(Color.RED);
		btnChooseFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				int returnValue = fileChooser.showOpenDialog(null);
				
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();	
					if(!controller.checkFileExtension(selectedFile, ".pdf")) {
						JOptionPane.showMessageDialog(mainFrame, "Please Select a Pdf File ");
						return;
					}
					secondPDFField.setText(selectedFile.getAbsolutePath());
					}
				
			}
			
			
		});
		
		JButton btnChooseFile_1 = new JButton("Choose File");
		btnChooseFile_1.setForeground(Color.WHITE);
		btnChooseFile_1.setBackground(Color.RED);
		btnChooseFile_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				int returnValue = fileChooser.showOpenDialog(null);
				
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();	
					if(!controller.checkFileExtension(selectedFile, ".pdf")) {
						JOptionPane.showMessageDialog(mainFrame, "Please Select a Pdf File ");
						return;
					}
					firstPDFField.setText(selectedFile.getAbsolutePath());
					}
				
			}
			
			
		});
		
		JButton button = new JButton("Choose File");
		button.setForeground(Color.WHITE);
		button.setBackground(Color.RED);
		
		thirdPDFField = new JTextField();
		thirdPDFField.setColumns(10);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				int returnValue = fileChooser.showOpenDialog(null);
				
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();	
					if(!controller.checkFileExtension(selectedFile, ".pdf")) {
						JOptionPane.showMessageDialog(mainFrame, "Please Select a Pdf File ");
						return;
					}
					thirdPDFField.setText(selectedFile.getAbsolutePath());
					}
				
			}
			
			
		});
		
		JButton btnMerge = new JButton("Merge Results!");
		btnMerge.setForeground(Color.WHITE);
		btnMerge.setBackground(Color.RED);
		btnMerge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if(     (firstPDFField.getText().equals("") && secondPDFField.getText().equals("")  ) || (firstPDFField.getText().equals("") && thirdPDFField.getText().equals("")  )  ||
						(thirdPDFField.getText().equals("") && secondPDFField.getText().equals("")  )
						)
				{
					
						JOptionPane.showMessageDialog(mainFrame, "Please Select a Pdf Files to Merge ");
						return;
					
				}
				
				
				controller.startMerging(firstPDFField.getText(),secondPDFField.getText(),thirdPDFField.getText());
			}
		});
		
		
		
		GroupLayout gl_comparePanel = new GroupLayout(comparePanel);
		gl_comparePanel.setHorizontalGroup(
			gl_comparePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_comparePanel.createSequentialGroup()
					.addGap(140)
					.addGroup(gl_comparePanel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(lblSelectOutputPath, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(button, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnChooseFile, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnChooseFile_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_comparePanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_comparePanel.createSequentialGroup()
							.addComponent(btnMerge, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(gl_comparePanel.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_comparePanel.createSequentialGroup()
								.addGroup(gl_comparePanel.createParallelGroup(Alignment.LEADING)
									.addComponent(outputMergeField, GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
									.addComponent(secondPDFField, 259, 268, Short.MAX_VALUE)
									.addComponent(firstPDFField, GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(button_2, GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
								.addGap(230))
							.addGroup(gl_comparePanel.createSequentialGroup()
								.addComponent(thirdPDFField, GroupLayout.PREFERRED_SIZE, 268, GroupLayout.PREFERRED_SIZE)
								.addContainerGap()))))
		);
		gl_comparePanel.setVerticalGroup(
			gl_comparePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_comparePanel.createSequentialGroup()
					.addGap(73)
					.addGroup(gl_comparePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSelectOutputPath)
						.addComponent(outputMergeField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(button_2))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_comparePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(firstPDFField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnChooseFile_1))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_comparePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(secondPDFField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnChooseFile))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_comparePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(button)
						.addComponent(thirdPDFField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(64)
					.addComponent(btnMerge, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(538, Short.MAX_VALUE))
		);
		comparePanel.setLayout(gl_comparePanel);
	}
	public void getDevices(JComboBox<String> box) {
		File files = new File(System.getProperty("user.dir")+File.separator+"devices");
		for(File file : files.listFiles()) {
			String deviceName = file.getName().substring(0, file.getName().length()-4).toUpperCase();
			if(((DefaultComboBoxModel<String>)box.getModel()).getIndexOf(deviceName) == -1) {
				  box.addItem(deviceName );
				}
		}
		
	}
}
