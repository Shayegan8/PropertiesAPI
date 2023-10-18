package api.properties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.bukkit.material.MaterialData;

import bedwars.BedWarsPlugin;

/**
 * @apiNote this class is not threadsafe
 */
public class PropertiesAPI {

	protected static List<String> secretList;

	protected String fileName;

	public List<String> getSecretList() {
		return secretList;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	protected static List<Object> listlist;

	public List<Object> getListList() {
		return listlist;
	}

	public int getByID(String str, String fileName) {
		int n = 0;
		while (n < readAllLines(fileName != null ? fileName : this.fileName).size()) {
			if (readAllLines(fileName != null ? fileName : this.fileName).get(n).equals(str)) {
				return n;
			}
			n++;
		}
		return 000;
	}

	public List<String> readAllLines(String configFile) {
		List<String> lines = new ArrayList<>();
		try (Scanner reader = new Scanner(new File(configFile != null ? configFile : this.fileName))) {
			while (reader.hasNextLine()) {
				lines.add(reader.nextLine());
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		return lines;
	}

	public static List<String> readAllLines(InputStream configFile) {
		List<String> lines = new ArrayList<>();
		try (Scanner reader = new Scanner(configFile)) {
			while (reader.hasNextLine()) {
				lines.add(reader.nextLine());
			}
		}
		return lines;
	}

	public PropertiesAPI(String fileName) {
		this.fileName = fileName;
		if (Files.notExists(Paths.get(fileName))) {
			try {
				Files.createFile(Paths.get(fileName), BedWarsPlugin.fileAttribute);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			secretList = readAllLines(fileName);
			listlist = new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PropertiesAPI() {

	}

	public void declare() {
		secretList = readAllLines(getFileName());
		listlist = new ArrayList<>();
	}

	public static int getStringId(String name) {
		int i = 0;
		while (i >= secretList.size()) {
			if (secretList.get(i).contains(name)) {
				return i;
			}
			i++;
		}
		return 1;
	}

	/**
	 * @apiNote the method with lots of big o notations
	 * @param key
	 * @param defaultValue
	 * @return the object in the file, if the object in the file was same in the
	 *         bukkit material enumeration it returns that enum, else it returns
	 *         defaultValue
	 */
	@SuppressWarnings("deprecation")
	public Object getProperties(String key, Object defaultValue) {
		int i = 0;
		String str = null;
		List<String> kos = readAllLines(getFileName());
		List<String> v1_8_R3 = readAllLines(
				PropertiesAPI.class.getClassLoader().getResourceAsStream("MaterialSourceReader"));
		while (i < kos.size()) {
			List<Character> ls = IntStream.range(0, kos.get(i).toCharArray().length).mapToObj((x) -> (char) x)
					.collect(Collectors.toList());
			str = kos.get(i).split("@")[1];
			if (kos.get(i).contains(key + "@") && !kos.get(i).equals(key + "@")) {
				if (kos.get(i).contains("&")) {
					str.replaceAll("&", "§");
					return str;

				} else if (ls != null) {
					if (!kos.get(i).equals(key + "@")) {
						str = kos.get(i).split("@")[1];
					}
					int n = 0;
					while (n < ls.size()) {
						if (IsNumber.isNumber(ls.get(n))) {
							String sexstring = null;
							if (ls.size() == 0) {
								sexstring = ls.get(0).toString();
							} else if (ls.size() == 1) {
								sexstring = ls.get(0).toString() + ls.get(1).toString();
							} else if (ls.size() == 2) {
								sexstring = ls.get(0).toString() + ls.get(1).toString() + ls.get(2);
							} else if (ls.size() == 3) {
								sexstring = ls.get(0).toString() + ls.get(1).toString() + ls.get(2).toString()
										+ ls.get(3).toString();
							}
							if (v1_8_R3.get(n).contains(sexstring)) {
								return new MaterialData(Integer.valueOf(ls.get(n))).getItemType();
							}
						}
						n++;
					}

				} else if (IsNumber.isNumber(str)) {
					return Integer.parseInt(str);
				} else if (str.contains(".")) {
					return Double.parseDouble(str);
				} else {
					return str;
				}
			}

			if (kos.get(i).equals(key + "@")) {
				if (defaultValue instanceof String) {
					String bdv = (String) defaultValue;
					if (bdv.contains("&") || !bdv.contains("&")) {
						if (defaultValue instanceof String) {
							if (Arrays.stream(alphabets).anyMatch(bdv::contains)) {
								bdv.replaceAll("&", "§");
								return bdv;
							} else {
								return bdv;
							}
						} else {
							return defaultValue;
						}
					}
				}
			} else if (!kos.contains(key + "@")) {
				if (defaultValue instanceof String) {
					String rstr = (String) defaultValue;
					if (Arrays.stream(alphabets).anyMatch(rstr::contains)) {
						rstr.replaceAll("&", "§");
						return rstr;
					}
				} else {
					return defaultValue;
				}
			}
			i++;
		}
		if (kos.size() == 0) {
			if (defaultValue instanceof String) {
				String rstr = (String) defaultValue;
				if (Arrays.stream(alphabets).anyMatch(rstr::contains)) {
					rstr.replaceAll("&", "§");
					return rstr;
				} else {
					return defaultValue;
				}
			} else {
				return defaultValue;
			}
		}
		return null;
	}

	public void setListProperties(String key, String... args) {
		int i = 0;
		try (FileWriter writer = new FileWriter(this.fileName, true)) {
			writer.write("* " + key + "\n");
			while (i < args.length) {
				writer.write(i + " - " + args[i] + "\n");
				writer.flush();
				i++;
			}
			writer.write("* endif " + key + "\n");
			writer.flush();
		} catch (IOException e) {
			throw new IllegalStateException("a problem with creating properties list, file not found\n" + e);
		}
	}

	public List<Object> getListProperties(String key, Object... defaultValues) {
		int i = 0;
		while (i < readAllLines(this.fileName).size()) {
			if (readAllLines(this.fileName).get(i).equals("* " + key)) {
				int ini = getByID("* " + key, this.fileName);
				int ini2 = getByID("* endif " + key, fileName);
				while (ini < ini2) {
					ini++;
					listlist.add(readAllLines(this.fileName).get(ini));
				}
			} else if (!readAllLines(this.fileName).contains("* " + key)) {
				List<Object> ls = new ArrayList<Object>();
				for (int n = 0; n < defaultValues.length; n++) {
					if (defaultValues[n] instanceof String) {
						String jende = ((String) defaultValues[n]).replaceAll("&", "§");
						ls.add(jende);
					} else {
						ls.add(defaultValues[n]);
					}
				}
				return ls;
			} else {
				i++;
				continue;
			}
			i++;
		}
		return listlist;
	}
        
    @SuppressWarnings("deprecation")
	public Object getProperties(String key, Object defaultValue, String fileName) {
		int i = 0;
		String str = null;
		List<String> kos = readAllLines(fileName);
		List<String> v1_8_R3 = readAllLines(
				PropertiesAPI.class.getClassLoader().getResourceAsStream("MaterialSourceReader"));
		while (i < kos.size()) {
			List<Character> ls = IntStream.range(0, kos.get(i).toCharArray().length).mapToObj((x) -> (char) x)
					.collect(Collectors.toList());
			str = kos.get(i).split("@")[1];
			if (kos.get(i).contains(key + "@") && !kos.get(i).equals(key + "@")) {
				if (kos.get(i).contains("&")) {
					str.replaceAll("&", "§");
					return str;

				} else if (ls != null) {
					if (!kos.get(i).equals(key + "@")) {
						str = kos.get(i).split("@")[1];
					}
					int n = 0;
					while (n < ls.size()) {
						if (IsNumber.isNumber(ls.get(n))) {
							String sexstring = null;
							if (ls.size() == 0) {
								sexstring = ls.get(0).toString();
							} else if (ls.size() == 1) {
								sexstring = ls.get(0).toString() + ls.get(1).toString();
							} else if (ls.size() == 2) {
								sexstring = ls.get(0).toString() + ls.get(1).toString() + ls.get(2);
							} else if (ls.size() == 3) {
								sexstring = ls.get(0).toString() + ls.get(1).toString() + ls.get(2).toString()
										+ ls.get(3).toString();
							}
							if (v1_8_R3.get(n).contains(sexstring)) {
								return new MaterialData(Integer.valueOf(ls.get(n))).getItemType();
							}
						}
						n++;
					}

				} else if (IsNumber.isNumber(str)) {
					return Integer.parseInt(str);
				} else if (str.contains(".")) {
					return Double.parseDouble(str);
				} else {
					return str;
				}
			}

			if (kos.get(i).equals(key + "@")) {
				if (defaultValue instanceof String) {
					String bdv = (String) defaultValue;
					if (bdv.contains("&") || !bdv.contains("&")) {
						if (defaultValue instanceof String) {
							if (Arrays.stream(alphabets).anyMatch(bdv::contains)) {
								bdv.replaceAll("&", "§");
								return bdv;
							} else {
								return bdv;
							}
						} else {
							return defaultValue;
						}
					}
				}
			} else if (!kos.contains(key + "@")) {
				if (defaultValue instanceof String) {
					String rstr = (String) defaultValue;
					if (Arrays.stream(alphabets).anyMatch(rstr::contains)) {
						rstr.replaceAll("&", "§");
						return rstr;
					}
				} else {
					return defaultValue;
				}
			}
			i++;
		}
		if (kos.size() == 0) {
			if (defaultValue instanceof String) {
				String rstr = (String) defaultValue;
				if (Arrays.stream(alphabets).anyMatch(rstr::contains)) {
					rstr.replaceAll("&", "§");
					return rstr;
				} else {
					return defaultValue;
				}
			} else {
				return defaultValue;
			}
		}
		return null;
	}

	public void fakeFree() {
		listlist = null;
		secretList = null;
	}

	public void setProperties(String key, String value, String fileName) {
		try (FileWriter writer = new FileWriter(fileName != null ? fileName : this.fileName, true)) {
			writer.write(key + "@" + value + "\n");
			writer.flush();
		} catch (IOException e) {
			throw new IllegalStateException("a problem wtih setting properties, file not found\n" + e);
		}
	}

}
