package cmd;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Cmd {
	File wd; // working directory
	boolean exit;
	
	public Cmd() {
		wd = new File(System.getProperty("user.dir"));
		exit = false;
	}
	
	public void main() {
		InputStreamReader isr;
		BufferedReader br;
		String cmdStr;
		String[] cmd;
		
		while(!exit) {
			try {
				System.out.print(wd.getCanonicalPath() + "> ");

				isr = new InputStreamReader(System.in);
				br = new BufferedReader(isr);
				cmdStr = br.readLine();
				cmd = cmdStr.split(" ");
				switch(cmd[0]) {
					case "exit":
						exit = true;
						break;
					case "pwd":
						pwd(cmd);
						break;
					case "cd":
						cd(cmd);
						break;
					case "ls":
						ls(cmd);
						break;
					case "mv":
						mv(cmd);
						break;
					case "cat":
						cat(cmd);
						break;
					case "wc":
						wc(cmd);
						break;
					case "mkdir":
						mkdir(cmd);
						break;
					case "cp":
						cp(cmd);
						break;
					case "head":
						head(cmd);
						break;
					case "length":
						length(cmd);
						break;
					case "tail":
						tail(cmd);
						break;
					case "grep":
						grep(cmd);
						break;
					default:
						System.out.println(cmd[0] + ": command not found");
						break;
					case "": break;
				}
			} catch(IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	protected void grep(String[] cmd) {
		
	}

	protected void tail(String[] cmd) {
		
	}

	protected void length(String[] cmd) {
		
	}

	protected void head(String[] cmd) {
		
	}
	
	// itt tartunk most
	protected void cp(String[] cmd) {
		File in, out;
		if(cmd.length == 3) {
			in = new File(wd, cmd[1]);
			out = new File(wd, cmd[2]);
			if(in.exists()) {
				
			} else {
				System.out.println("cp: " + cmd[1] + ": no such file or directory.");
			}
		} else if(cmd.length == 2) {
			System.out.println("cp: missing operand after " + cmd[1]);
		} else if(cmd.length == 1) {
			System.out.println("cp: missing operand");
		} else {
			System.out.println("cp: too many arguments");
		}
	}

	protected void mkdir(String[] cmd) {
		File newfolder;
		if(cmd.length > 1) {
			for(int i = 1; i < cmd.length; ++i) {
				newfolder = new File(wd, cmd[i]);
				if(!newfolder.exists()) {
					if(!newfolder.mkdir()) {
						System.out.println("mkdir: " + cmd[i] + ": couldn't create directory");
					}
				} else {
					System.out.println("mkdir: " + cmd[i] + ": directory already exists");
				}
			}
		} else {
			System.out.println("mkdir: missing operand");
		}
	}

	protected void wc(String[] cmd) {
		File file;
		if(cmd.length == 2) {
			int rows = 0, words = 0, chars = 0;
			file = new File(wd, cmd[1]);
			if(file.exists() && !file.isDirectory()) {
				FileReader fr;
				BufferedReader br;
				StringTokenizer st;
				try {
					fr = new FileReader(file);
					br = new BufferedReader(fr);
					while(true) {
						String line = br.readLine();
						if(line == null) break;
						rows++;
						chars += line.length();
						st = new StringTokenizer(line);
						words += st.countTokens();
					}
				} catch(IOException ex) {
					ex.printStackTrace();
				}
				System.out.println(rows + " " + words + " " + chars + " " + cmd[1]);
			} else {
				System.out.println("wc: " + cmd[1] + ": no such file or directory");
			}
		} else if(cmd.length == 1) {
			System.out.println("wc: missing operand");
		} else {
			System.out.println("wc: too many arguments");
		}
	}

	protected void cat(String[] cmd) {
		File file;
		if(cmd.length == 2) {
			file = new File(wd, cmd[1]);
			if(file.exists() && !file.isDirectory()) {
				FileReader fr;
				BufferedReader br;
				try {
					fr = new FileReader(file); 
					br = new BufferedReader(fr);
					while(true) {
						String line = br.readLine();
						if(line == null) break;
						System.out.println(line);
					}
					br.close();
					fr.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			} else {
				System.out.println("cat: " + cmd[1] + ": no such file or directory");
			}
		} else if(cmd.length == 1){
			System.out.println("cat: missing operand");
		} else {
			System.out.println("cat: too many arguments");
		}
	}

	protected void mv(String[] cmd) {
		File file;
		File target;
		switch(cmd.length) {
			case 1:
				System.out.println("mv: missing file operand");
				break;
			case 2:
				System.out.println("mv: missing destination file operand after '" + cmd[1] + "'");
				break;
			case 3:
				file = new File(wd, cmd[1]);
				target = new File(wd, cmd[2]);
				if(file.exists()) {
					file.renameTo(target);
				} else {
					System.out.println("mv: no file or directory with the name: " + cmd[1]);
				}
				break;
			default:
				System.out.println("mv: too many arguments");
				break;
		}
	}

	protected void ls(String[] cmd) {
		File[] list = wd.listFiles();
		
		if(cmd.length > 1) {
			if(cmd[1].equals("-l")) {
				System.out.println("total " + list.length);
				for(int i = 0; i < list.length; ++i) {
					if(list[i].isDirectory()) {
						System.out.format("%-5s %5s %-20s\n", "<DIR>", "", list[i].getName());
					} else {
						System.out.format("%-5s %5d %-20s\n", "", list[i].length(), list[i].getName());
					}
				}
			} else {
				System.out.println("ls: wrong arguments, only -l supported");
			}
		} else {
			for(int i = 0; i < list.length; ++i) {
				System.out.print(list[i].getName() + " ");
				if(i > 0 && i % 10 == 0) System.out.print("\n");
			}
			System.out.print("\n");
		}
	}

	protected void cd(String[] cmd) {
		if(cmd.length == 2) {
			try {
				File dir;
				if(cmd[1].equals("..")) {
					dir = new File(wd.getParentFile().getCanonicalPath());
				} else {
					dir = new File(wd, cmd[1]);
				}
				if(dir.exists()) {
					if(dir.isDirectory()) {
						wd = dir;
					} else {
						System.out.println("cd: " + cmd[1] + ": not a directory");
					}
				} else {
					System.out.println("cd: "+ cmd[1] + ": no such file or directory");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(cmd.length == 1) {
			System.out.println("cd: no arguments given.");
		} else {
			System.out.println("cd: too many arguments");
		}
	}

	protected void pwd(String[] cmd) {
		try {
			System.out.println(wd.getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}