package configuration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;

public class AnsibleConfigurator {
	private File mainDirectory;
	private String hostIP;
	private String testingType;
	private String username;
	public AnsibleConfigurator(String username,String hostIP, String ansiblePath,String testingType) {
		this.username = username;
		mainDirectory = new File(ansiblePath);
		this.hostIP = hostIP;
		this.testingType = testingType;
		
	}

	public void startConfiguration() {
		changeHostsFile();
		changeTasksFile();
		changeTargetsFile();
	}

	private void changeHostsFile() {
		deleteFile(mainDirectory,"hosts");
		createFile(mainDirectory,"hosts", "Hosts");

	}

	private void changeTasksFile() {
		
		File dir = new File(mainDirectory.getAbsolutePath() + File.separator+"roles" +File.separator+"opc.ua"+File.separator+"tasks");
		deleteFile(dir,"main.yml");
		createFile(dir,"main.yml", "Tasks");

	}
	private void changeTargetsFile() {
		File dir =  new File(mainDirectory.getAbsolutePath() + File.separator+"group_vars");
		deleteFile(dir,"targets.yml");
		createFile(dir,"targets.yml", "Targets");
	}
	
	

	private File[] getFileFromDirectory(File directory,String fileName) {
		File[] matchingFiles = directory.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith(fileName);
			}
		});
		return matchingFiles;

	}

	private void deleteFile(File directory,String fileName) {
		File[] files = getFileFromDirectory(directory,fileName);
		for (File file : files) {
			file.delete();
		}
	}


	private void createFile(File directory, String fileName,String fileType) {
		
		
		
		String text=null;
		File file = new File(directory.getAbsolutePath() + File.separator+ fileName);
		
		switch(fileType) {
		case "Hosts":
			text = "[targets]\n" + "\n" + hostIP;
			break;
		
		case "Targets":
			text = "---\n"+"ansible_user: " +username;
			break;
		
		case "Tasks":
			text = initialTasksText()+checklibmbedtlsText()+installlibmbedtlsText()+selectiveTasksText()+buildServerText("MainServer.c")+portText()+runServerText("MainServer.c");
			break;
		}
		try {

			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(text);
			output.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String initialTasksText() {
		
		return "---\n" + "- name: Ensure /etc/opcua dir exists\n" 
				+ "  file: \n" + 
				"    path: /etc/opcua \n"
				+ "    state: directory\n" + 
				"    owner: '{{ ansible_user }}'\n" 
				+ "  become: true\n" ;
	}
	private String checklibmbedtlsText() {
		return  "\n"
				+"- name: Check that libmbedtls is installed\n"
				+"  command: dpkg-query -l libmbedtls-dev \n"
				+"  register: deb_check\n";
	}
	
	private String installlibmbedtlsText() {
		return "\n"
				+ "- name: Install libmbedtls is installed \n"
				+"  package: \n"
				+"      name: libmbedtls-dev \n"
				+"      state: present \n"
				+"  when: deb_check.stdout.find('no packages found') != -1\n";
		
		
	}
	
	
	private String selectiveTasksText() {
		return 
				 "\n"
				+ "# https://open62541.org/releases/b916bd0611.zip\n"
				+ "- name: Copy files extracted from the release https://open62541.org/releases/b916bd0611.zip \n"
				+ "  copy:\n" 
				+ "    src: '{{ item }}'\n" 
				+ "    dest: '/etc/opcua/{{ item }}'\n"
				+ "    owner: '{{ ansible_user }}'\n"
				+ "  with_items:\n" 
				+ "    - AdditionalServerClass.h\n"+
				"    - commonServerMethods.h\n"+	
				"    - DiscoveryServerClass.h\n"+
				"    - PublisherServerClass.h\n"+
				"    - ReadServerClass.h\n"+
				"    - WriteServerClass.h\n"+
				"    - server_key.der\n"+
				"    - server_cert.der\n"+
				"    - MainServer.c\n" + 
				"    - EncryptionServerClass.h\n"+
				"    - MonitoredItemsServerClass.h\n"+
				"    - NetworkingServerClass.h\n"+
				"    - open62541.c\n" + 
				"    - open62541.h\n" + 
				"\n";							
				
		
	}
	private String buildServerText(String fileName) {
		String rawName = fileName.substring(0, fileName.indexOf("."));
		return 
				"- name: Build using the command 'gcc -std=c99 open62541.c -D_POSIX_C_SOURCE=199309L " + fileName +" -lm -o "+ rawName + "'\n" + 
				"  shell: gcc -std=c99 open62541.c -lmbedtls -lmbedx509 -lmbedcrypto -D_POSIX_C_SOURCE=199309L "+fileName+ " -lm -o " +rawName+ "\n" + 
				"  args:\n" + 
				"    chdir: /etc/opcua/ \n"+
				"\n";
	}
	private String portText() {
		
		return  
				"\n- name: Check if port 4840 is used\n" 
				+ "  wait_for:\n"
				+ "    port: 4840\n" 
				+ "    state: stopped\n" 
				+ "    timeout: 2\n" 
				+ "  ignore_errors: yes\n"
				+ "  register: port_check\n" 
				+ "  when: opcua_state == \"running\"\n"
				+ "\n"
				+ "- name: If server is running, kill it (when opcua_state == \"restarted\" or \"stopped\")\n"
				+ "  shell: fuser -k -n tcp 4840\n" 
				+ "  ignore_errors: yes\n"
				+ "  when: opcua_state == \"restarted\" or opcua_state == \"stopped\"\n" 
				+ "\n"
				+ "- name: Wait for the server to be closed\n" 
				+ "  wait_for:\n" 
				+ "    port: 4840\n"
				+ "    state: stopped\n" 
				+ "    timeout: 10\n"
				+ "  when: opcua_state == \"restarted\" or opcua_state == \"stopped\"\n" + "\n";
			//	+testStringBuilder("MainServer.c");
				
				
	}

	private String runServerText(String fileName) {
		String rawName = fileName.substring(0, fileName.indexOf("."));
		String testType = testingType.substring(0,testingType.indexOf(" "));
		String encryptionType="None";
		if(testType.contains("Encryption"))
			encryptionType="256sha256";
		return 
		"- name: Run "+ rawName+ " with nohup. output is forwarded to log.txt\n"+
		"  shell: nohup ./"+ rawName +" " +encryptionType+" "+ testType +" GB > log.txt &\n"
		+ "  args:\n" 
		+ "    chdir: /etc/opcua/\n"+
		"  when: (opcua_state == \"running\" and not port_check.failed) or\n"+
		"        (opcua_state == \"restarted\")\n"+"\n";
		
		
	}
	
//	private String testStringBuilder(String fileName) {
//		
//		
//		String rawName = fileName.substring(0, fileName.indexOf("."));
//		String testType = testingType.substring(0,testingType.indexOf(" "));
//		String encryptionType="None";
//		if(testType.contains("Encryption"))
//		{
//			encryptionType="256sha256";
//		}
//		
//		return 
//				"- name: Build using the command 'gcc -std=c99 open62541.c -D_POSIX_C_SOURCE=199309L " + fileName +" -lm -o "+ rawName + "'\n" + 
//				"  shell: gcc -std=c99 open62541.c -lmbedtls -lmbedx509 -lmbedcrypto -D_POSIX_C_SOURCE=199309L "+fileName+ " -lm -o " +rawName+ "\n" + 
//				"  args:\n" + 
//				"    chdir: /etc/opcua/ \n"+
//				"\n"+
//				"- name: Run "+ rawName+ " with nohup. output is forwarded to log.txt\n"+
//				"  shell: nohup ./"+ rawName +" " +encryptionType+" "+ testType +" GB"    +" > log.txt &\n" + "  args:\n" + "    chdir: /etc/opcua/\n"+
//				"  when: (opcua_state == \"running\" and not port_check.failed) or\n"+
//				"        (opcua_state == \"restarted\")\n"+"\n";
//	}
//	
	
	
	
	
	
	
}
