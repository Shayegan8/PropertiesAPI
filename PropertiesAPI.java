import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.nio.file.attribute.PosixFilePermission;

import org.bukkit.material.MaterialData;

/**
 * @author Shayegan8
 * @apiNote this class is not threadsafe
 */
public class PropertiesAPI {

	protected static List<String> secretList;

	protected static String fileName;

	public List<String> getSecretList() {
		return secretList;
	}

	public static void setFileName(String fileName) {
		PropertiesAPI.fileName = fileName;
	}

	public static String getFileName() {
		return PropertiesAPI.fileName;
	}

	protected static List<Object> listlist;

	public List<Object> getListList() {
		return listlist;
	}

	public static int getByID(String str, String fileName) {
		int n = 0;
		while (n < readAllLines(fileName != null ? fileName : PropertiesAPI.fileName).size()) {
			if (readAllLines(fileName != null ? fileName : PropertiesAPI.fileName).get(n).equals(str)) {
				return n;
			}
			n++;
		}
		return 000;
	}

	public static List<String> readAllLines(String configFile) {
		List<String> lines = new ArrayList<>();
		try (Scanner reader = new Scanner(new File(configFile != null ? configFile : PropertiesAPI.fileName))) {
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
        
        private static String attr = "rw-r--r--";
        
        private static FileAttribute<Set<PosixFilePermission>> attribute = PosixFilePermissions.asFileAttribute
        (PosixFilePermissions.fromString(PropertiesAPI.attr));
        
        public static void setAttribute(String attr) {
            attribute = PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString(getAttributeString()));
        }
        
        public static String getAttributeString() {
            return attr;
        }
        
        public String getAttributeString(int i) {
            return attr;
        }
                
	public PropertiesAPI(String fileName) {
		PropertiesAPI.fileName = fileName;
		if (Files.notExists(Paths.get(fileName))) {
			try {
				Files.createFile(Paths.get(fileName), PropertiesAPI.attribute);
			} catch (IOException e) {
                            throw new IllegalStateException("a problem with creating properties file\n", e);
			}
		}
		try {
			secretList = readAllLines(fileName);
			listlist = new ArrayList<>();
		} catch (Exception e) {
                    throw new IllegalStateException("a problem with declaring for first time\n", e);
		}
	}
        
        static {
                try {
                    secretList = readAllLines("");
                    listlist = new ArrayList<>();
                } catch (Exception e) {
                    throw new IllegalStateException("a problem with declaring for first time\n", e);
                }
            }
        
	public PropertiesAPI() {

	}

	public static void declare() {
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
   
        private static String alphabets[] = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "k", "j", "l", "m", "n", "o", "p", "q",
			"r", "s", "t", "u", "v", "w", "x", "y", "z" };
	/**
	 * @apiNote the method with lots of big o notations
	 * @param key
	 * @param defaultValue
	 * @return the object in the file, if the object in the file was same in the
	 *         bukkit material enumeration it returns that enum, else it returns
	 *         defaultValue
	 */
	@SuppressWarnings("deprecation")
	public static Object getProperties(String key, Object defaultValue) {
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
					if (Arrays.stream(PropertiesAPI.alphabets).anyMatch(rstr::contains)) {
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
				if (Arrays.stream(PropertiesAPI.alphabets).anyMatch(rstr::contains)) {
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

	public static void setListProperties(String key, String... args) {
		int i = 0;
		try (FileWriter writer = new FileWriter(PropertiesAPI.fileName, true)) {
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

	public static List<Object> getListProperties(String key, Object... defaultValues) {
		int i = 0;
		while (i < readAllLines(PropertiesAPI.fileName).size()) {
			if (readAllLines(PropertiesAPI.fileName).get(i).equals("* " + key)) {
				int ini = getByID("* " + key, PropertiesAPI.fileName);
				int ini2 = getByID("* endif " + key, PropertiesAPI.fileName);
				while (ini < ini2) {
					ini++;
					listlist.add(readAllLines(PropertiesAPI.fileName).get(ini));
				}
			} else if (!readAllLines(PropertiesAPI.fileName).contains("* " + key)) {
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
	public static Object getProperties(String key, Object defaultValue, String fileName) {
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
							if (Arrays.stream(PropertiesAPI.alphabets).anyMatch(bdv::contains)) {
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
					if (Arrays.stream(PropertiesAPI.alphabets).anyMatch(rstr::contains)) {
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
				if (Arrays.stream(PropertiesAPI.alphabets).anyMatch(rstr::contains)) {
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

	public static void fakeFree() {
		listlist = null;
		secretList = null;
	}

	public static void setProperties(String key, String value, String fileName) {
		try (FileWriter writer = new FileWriter(fileName != null ? fileName : PropertiesAPI.fileName, true)) {
			writer.write(key + "@" + value + "\n");
			writer.flush();
		} catch (IOException e) {
			throw new IllegalStateException("a problem wtih setting properties, file not found\n" + e);
		}
	}

}
