package production;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;

public class Producer {
	private static int width = 800;

	public static void main(String[] args) throws IOException {
		if(args.length == 0) {
			System.out.println("batch all files");
			produce();
		} else {
			for (String arg : args) {
				if(arg.contains("w")) {
					width = Integer.parseInt(arg.replace("w", ""));
					if(args.length == 1) {
						produce();
						return;
					}
				} 
			}
			for (String arg : args) {
				if(!arg.contains("w")) {
					if(arg.contains("-")) {
						String low = "";
						String high = "";
						String[] split = arg.split("-");
						if(split.length == 2) {
							low = formatPageNum(split[0]);
							high = formatPageNum(split[1]);
							DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(""));
							for(Path p : stream) {
								String fileName = p.getFileName().toString();
								if(fileName.compareTo(fileName.replaceFirst("\\d+\\.kra$", low + ".kra")) >= 0
										&& fileName.compareTo(fileName.replaceFirst("\\d+\\.kra$", high + ".kra")) <= 0) {
									produce(width, p);
								}
							}
						}
					} else {
						String pageNum = formatPageNum(arg);
						DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(""));
						for(Path p : stream) {
							String fileName = p.getFileName().toString();
							if(fileName.matches(".*" + pageNum + "\\.kra$")) {
								produce(width, p);
							}
						}
					}
				}
			}
		}
	}

	private static String formatPageNum(String arg) {
		return String.format("%02d", Integer.parseInt(arg));
	}

	private static void produce() throws IOException {
		DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(""));
		for(Path entry : stream) {
			produce(width, entry);
		}
	}

	private static void produce(int paramWidth, Path entry) throws IOException {
		if(entry.getFileName().toString().endsWith(".kra")) {
			Thread decompresseur = new Thread(new Runnable() {

				@Override
				public void run() {
					ZipInputStream zip;
					ZipFile zipped;
					try {
						File file = entry.toFile();
						System.out.println(file.getName());
						zipped = new ZipFile(file);
						zip = new ZipInputStream(new FileInputStream(file));
						ZipEntry zipFile = null;
						while((zipFile = zip.getNextEntry()) != null) {
							if(zipFile.getName().equalsIgnoreCase("mergedimage.png")) {
								InputStream zippedTarget = zipped.getInputStream(zipFile);
								Path target = Paths.get("../png/" + file.getName().replaceAll("kra$", "png"));
								System.out.println(target.getFileName().toAbsolutePath());
								Files.copy(zippedTarget, target, StandardCopyOption.REPLACE_EXISTING);
								BufferedImage input = ImageIO.read(target.toFile());
								int height = (int) (input.getHeight()*((double)paramWidth/input.getWidth()));
								BufferedImage output = new BufferedImage(paramWidth, height, BufferedImage.TYPE_INT_RGB);
								Graphics2D g = output.createGraphics();
								g.drawImage(input, 0, 0, paramWidth, height, null);
								g.dispose();
								ImageIO.write(output, "jpg", new File("../jpg/" + file.getName().replaceAll("kra$", "jpg")));
								break;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			decompresseur.start();
		}
	}
}
