package production;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Producer {
	public static void main(String[] args) throws IOException {
		System.out.println(System.getProperty("user.dir"));
		if(args.length == 0) {
			System.out.println("batch all files");
			produce();
		} else {
			int i = 0;
			for(String arg : args) {
				System.out.println("arg " + i++ + " " + arg);
			}
		}
	}

	private static void produce() throws IOException {
		DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("."));
		int i = 0;
		for(Path entry : stream) {
			String fileName = entry.getFileName().toString();
			//			System.out.println("coucou0");
			if(fileName.matches(".*\\d+\\.kra$")) {
				final int j = i++;
				Thread decompresseur = new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						System.out.println(j + " " + entry.getFileName().toString());
						ZipInputStream zip;
						try {
							zip = new ZipInputStream(new FileInputStream(fileName));
							ZipEntry file = null;
							//				System.out.println("coucou1");
							while((file = zip.getNextEntry()) != null) {
								//					System.out.println("coucou2 " + file.getName());
								if(file.getName().equalsIgnoreCase("mergedimage.png")) {
									//						System.out.println("coucou");
									//						System.out.println(Paths.get("../png/" + fileName));
									BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("../png/" + fileName.replaceAll("kra$", "png")));	
									byte[] buffer = new byte[8192];
									int len;
									while ((len = zip.read(buffer)) != -1) {
										out.write(buffer, 0, len);
									}
									out.flush();
									out.close();
									break;
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				decompresseur.start();
			}
		}
	}
}
